/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import static grammar.datasets.sentencetemplates.TempConstants.SYNTACTIC_FRAME;
import static grammar.datasets.sentencetemplates.TempConstants.superlative;
import static grammar.read.questions.ReadWriteConstants.GENERATED;
import static grammar.read.questions.ReadWriteConstants.RETURN_TYPE_SUBJECT;
import static grammar.read.questions.ReadWriteConstants.questionsCsv;
import static grammar.read.questions.ReadWriteConstants.questionsFile;
import static grammar.read.questions.ReadWriteConstants.summaryFile;
import static grammar.read.questions.ReadWriteConstants.summaryHeader;
import grammar.sparql.PrepareSparqlQuery;
import grammar.sparql.SparqlQuery;
import grammar.structure.component.FrameType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import linkeddata.LinkedData;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryType;
import org.apache.logging.log4j.LogManager;
import util.io.AddQuote;
import static util.io.BashScript.FIND_ABSTRACT;
import static util.io.BashScript.FIND_IMAGE_LINK;
import static util.io.BashScript.FIND_WIKI_LINK;
import util.io.CsvFile;
import util.io.FileFolderUtils;
import util.io.FileProcessUtils;
import util.io.InputCofiguration;
import util.io.JsonWriter;
import util.io.OffLineResult;
import util.io.ReferenceManagement;
import util.io.Statistics;
import util.io.StringMatcher;

/**
 *
 * @author elahi
 */
public class OffLineQuestionGeneration {

    private static String language = "en";
    private String propertyDir = null;
    public CSVWriter csvWriterQuestions;
    public String questionAnswerFile = null;
    public String questionSummaryFile = null;
    public String classDir = null;

    private Integer batchNumber = 0;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ProtoToRealQuesrion.class);
    private Map<String, OffLineResult> entityLabels = new TreeMap<String, OffLineResult>();
    private InputCofiguration inputCofiguration = null;
    private String parameter = null;

    public OffLineQuestionGeneration(InputCofiguration inputCofiguration) throws Exception {
        this.inputCofiguration = inputCofiguration;
        this.language = inputCofiguration.getLanguageCode();
        this.propertyDir = inputCofiguration.getEntityDir();
        this.questionAnswerFile = inputCofiguration.getQuestionDir() + File.separator + questionsFile + "_" + language + ".csv";
        this.questionSummaryFile = inputCofiguration.getQuestionDir() + File.separator + summaryFile + "_" + language + ".csv";
        this.batchNumber = 1;
        this.parameter = inputCofiguration.getParameter();
        this.classDir = inputCofiguration.getClassDir();
    }

    public void offline(List<File> protoSimpleQFiles) throws Exception {

        if (protoSimpleQFiles.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        this.writeGrammarRules(protoSimpleQFiles);
        Map<String, List<GrammarEntryUnit>> lexicalEntiryUris = this.findCoverage(protoSimpleQFiles);

        Integer idIndex = 0, noIndex = 0;

        for (String lexicalEntiryUri : lexicalEntiryUris.keySet()) {
            String uri = null;
            List<GrammarEntryUnit> grammarEntryUnits = lexicalEntiryUris.get(lexicalEntiryUri);
            batchNumber = batchNumber + 1;
            String questionAnswerFile = this.inputCofiguration.getQuestionDir() + File.separator + this.batchNumber.toString() + "~" + parameter + "~" + lexicalEntiryUri + "~" + questionsFile + ".csv";
            this.csvWriterQuestions = new CSVWriter(new FileWriter(questionAnswerFile, true));
            for (GrammarEntryUnit grammarEntryUnit : grammarEntryUnits) {
                List<String> questions = grammarEntryUnit.getSentences();
                String sparql = grammarEntryUnit.getSparqlQuery();
                String returnSubjOrObj = grammarEntryUnit.getReturnVariable();
                String bindingType = grammarEntryUnit.getBindingType();
                String returnType = grammarEntryUnit.getReturnType();
                List<UriLabel> bindingList = new ArrayList<UriLabel>();
                String property = AddQuote.getProperty(grammarEntryUnit.getSparqlQuery());

                try {
                    String propertyFile = AddQuote.getProperty(this.propertyDir, grammarEntryUnit.getSparqlQuery());
                    this.entityLabels = FileProcessUtils.getEntityLabels(propertyFile, classDir, returnSubjOrObj, bindingType, returnType);
                    bindingList = this.getOffLineBindingList(entityLabels, returnSubjOrObj);
                    /*if (!bindingList.isEmpty()) {
                        System.out.println(property + "   bindingList::" + bindingList + " returnSubjOrObj::" + returnSubjOrObj);
                        System.out.println("   entityLabels::" + entityLabels);
                        System.out.println("   questions::" + questions);
                        exit(1);
                    }*/

                } catch (Exception ex) {
                    continue;
                }

                if (grammarEntryUnit.getQueryType().equals(QueryType.ASK)) {
                    continue;
                }

                try {
                    if (grammarEntryUnit.getFrameType().contains(FrameType.AG.toString()) && grammarEntryUnit.getSentenceTemplate().contains(superlative)) {
                        sparql = grammarEntryUnit.getExecutable();
                    } else {
                        sparql = grammarEntryUnit.getSparqlQuery();

                    }
                } catch (Exception ex) {
                    continue;
                }

                if (grammarEntryUnit.getQueryType().equals(QueryType.SELECT)) {
                    noIndex = this.questionGeneration(uri, sparql, bindingList, questions, noIndex, "", grammarEntryUnit, entityLabels);
                    noIndex = noIndex + 1;
                    idIndex = idIndex + 1;
                }

                if (grammarEntryUnit.getLexicalEntryUri() != null) {
                    uri = grammarEntryUnit.getLexicalEntryUri().toString();

                }

            }
            this.csvWriterQuestions.close();
        }
    }

    private Integer questionGeneration(String uri, String sparqlQuery, List<UriLabel> uriLabels, List<String> questions, Integer rowIndex, String lexicalEntry, GrammarEntryUnit grammarEntryUnit, Map<String, OffLineResult> entityLabels) throws IOException {
        Integer index = 0;
        String syntacticFrame = grammarEntryUnit.getFrameType();

        if (questions.isEmpty()) {
            return rowIndex;
        }

        for (UriLabel uriLabel : uriLabels) {
            String questionUri = null, questionLabel=null;
            try {
                questionUri = uriLabel.getUri();
                questionLabel = uriLabel.getLabel();
           
                if (questionUri != null && !questionLabel.isEmpty()) {
                    questionUri = questionUri.trim().strip().stripLeading().stripTrailing();
                    questionLabel = uriLabel.getLabel().replaceAll("\'", "").replaceAll("\"", "");

                }
            } catch (Exception ex) {
                continue;
            }

            if (!AddQuote.isKbValid(uriLabel)) {
                continue;
            }
            String questionForShow = questions.iterator().next();

            if (questionForShow.contains("Where is $x located?")) {
                continue;
            }

            String answerUri = uriLabel.getAnswerUri();
            String answerLabel = uriLabel.getAnswerLabel();
            String sparql = AddQuote.modifySparql(sparqlQuery);
            index = index + 1;
            System.out.println("questions::" + questions);

            try {
                {

                    for (String question : questions) {
                        if (question.contains("(") && question.contains(")")) {
                            String result = StringUtils.substringBetween(question, "(", ")");
                            question = question.replace(result, "X");
                        } else if (question.contains("$x")) {

                        }

                        String id = uri + "~" + rowIndex.toString();
                        //test
                        String questionT = getQuestion(question, questionLabel);

                        if (questionLabel.isEmpty()) {
                            continue;
                        }

                        String[] record = {id, questionT, sparql, answerUri, answerLabel, syntacticFrame, "single"};
                        String[] newRecord = AddQuote.doubleQuote(record);

                        System.out.println("index::" + index + " questionT::" + questionT + " answerUri:" + answerUri + " answerLabel:" + answerLabel + " sparql:" + sparql);
                        this.csvWriterQuestions.writeNext(newRecord);
                        rowIndex = rowIndex + 1;

                    }
                }

            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " " + sparql + " " + answerLabel);
            }
        }

        return rowIndex;
    }

    private List<UriLabel> getOffLineBindingList(Map<String, OffLineResult> entityLabels, String returnType) {
        List<UriLabel> uriLabels = new ArrayList<UriLabel>();

        for (String uri : entityLabels.keySet()) {
            OffLineResult offLineResult = entityLabels.get(uri);
            UriLabel uriLabel = null;
            if (returnType.contains(RETURN_TYPE_SUBJECT)) {
                uriLabel = new UriLabel(offLineResult.getObjectUri(), offLineResult.getObjectLabel(), offLineResult.getSubjectUri(), offLineResult.getSubjectLabel(), offLineResult.getSubjectWikiLink(), offLineResult.getSubjectThumbnail(), offLineResult.getSubjectAbstractText());

            } else {
                uriLabel = new UriLabel(offLineResult.getSubjectUri(), offLineResult.getSubjectLabel(), offLineResult.getObjectUri(), offLineResult.getObjectLabel(), offLineResult.getObjectWikiLink(), offLineResult.getObjectThumbnail(), offLineResult.getObjectAbstractText());
            }
            uriLabels.add(uriLabel);

        }

        return uriLabels;
    }

    private String getQuestion(String question, String questionLabel) {
        String questionT = question.replaceAll("(X)", questionLabel);
        questionT = questionT.replace("(", "");
        questionT = questionT.replace(")", "");
        questionT = questionT.replace("$x", questionLabel);
        questionT = questionT.replace(",", "");
        questionT = questionT.stripLeading().trim();
        return questionT;
    }

    private void findCoverage(String propertyDir, Map<String, List<GrammarEntryUnit>> lexicalEntiryUris, String fileName) {
        String[] files = new File(propertyDir).list();
        Set<String> properties = new TreeSet<String>();
        Set<String> generatedProperties = new TreeSet<String>();
        for (String property : files) {
            if (property.contains(".txt")) {
                property = property.replace(".txt", "");
                property = property.replace("dbo_", "dbo:").trim();
                property = property.replace("dbp_", "dbp:").trim();
                if (property.contains("dbo:") || property.contains("dbp:")) {
                    properties.add(property);
                }
            }
        }

        for (String lex : lexicalEntiryUris.keySet()) {
            List<GrammarEntryUnit> grammarEntryUnits = lexicalEntiryUris.get(lex);
            for (GrammarEntryUnit grammarEntryUnit : grammarEntryUnits) {
                /*if(grammarEntryUnit.getFrameType().contains("APP")){
                   continue; 
                }*/
                String property = StringUtils.substringBetween(grammarEntryUnit.getSparqlQuery(), "<", ">");
                property = property.replace("http://dbpedia.org/ontology/", "dbo:");
                property = property.replace("http://dbpedia.org/property/", "dbp:");
                property = property.strip().stripLeading().stripTrailing().trim();
                if (!properties.contains(property)) {
                    generatedProperties.add(grammarEntryUnit.getLexicalEntryUri() + "=" + property + "=" + grammarEntryUnit.getFrameType());
                }
            }
        }
        FileFolderUtils.setToFile(generatedProperties, fileName);

    }

    private void writeGrammarRules(List<File> protoSimpleQFiles) {
        for (String frameType : SYNTACTIC_FRAME.keySet()) {
            String syntaktikFrame = SYNTACTIC_FRAME.get(frameType);
            Map<String, List<GrammarEntryUnit>> lexicalEntiryUris = GrammarEntryUnit.getLexicalEntries(protoSimpleQFiles, frameType);
            GrammarEntriesLex grammarEntriesLex = new GrammarEntriesLex(lexicalEntiryUris);
            //NounPPFrame 
            JsonWriter.writeClassToJson(grammarEntriesLex, propertyDir + syntaktikFrame + ".json");

            this.findCoverage(this.propertyDir, lexicalEntiryUris, propertyDir + syntaktikFrame + "MissedProperty.txt");
        }
    }

    /*private String findParameterForLexEntry(String key) {
        if (this.lexEntryParameter.containsKey(key)) {
            return this.lexEntryParameter.get(key);
        }
        return null;
    }*/
    public Map<String, String> findParameter(String fileName) throws FileNotFoundException, IOException {
        BufferedReader reader;
        String line = "";
        Map<String, String> map = new TreeMap<String, String>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                if (line != null) {
                    if (line.contains("=")) {
                        String[] info = line.split("=");
                        map.put(info[1], info[0]);
                    }

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private Map<String, List<GrammarEntryUnit>> findCoverage(List<File> protoSimpleQFiles) {
        Map<String, List<GrammarEntryUnit>> lexicalEntiryUris = GrammarEntryUnit.getLexicalEntries(protoSimpleQFiles);
        GrammarEntriesLex grammarEntriesLex = new GrammarEntriesLex(lexicalEntiryUris);
        JsonWriter.writeClassToJson(grammarEntriesLex, propertyDir + "TransitiveFrame.json");
        this.findCoverage(this.propertyDir, lexicalEntiryUris, propertyDir + "TransitiveFrame" + "missedProperty.txt");
        return lexicalEntiryUris;
    }

}
