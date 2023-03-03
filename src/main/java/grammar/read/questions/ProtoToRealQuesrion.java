package grammar.read.questions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import grammar.sparql.SparqlQuery;
import util.io.FileProcessUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVWriter;
import static grammar.datasets.sentencetemplates.TempConstants.SYNTACTIC_FRAME;
import static grammar.datasets.sentencetemplates.TempConstants.superlative;
import grammar.sparql.PrepareSparqlQuery;
import static grammar.sparql.SparqlQuery.RETURN_TYPE_SUBJECT;
import grammar.structure.component.FrameType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.System.exit;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.jena.query.QueryType;
import org.apache.logging.log4j.LogManager;
import util.io.CsvFile;
import linkeddata.LinkedData;
import util.io.AddQuote;
import static util.io.BashScript.FIND_ABSTRACT;
import static util.io.BashScript.FIND_IMAGE_LINK;
import static util.io.BashScript.FIND_WIKI_LINK;
import util.io.FileFolderUtils;
import util.io.InputCofiguration;
import util.io.JsonWriter;
import util.io.OffLineResult;
import util.io.Statistics;
import util.io.StringMatcher;
import util.io.ReferenceManagement;

/**
 *
 * @author elahi
 */
public class ProtoToRealQuesrion implements ReadWriteConstants {

    private static String language = "en";
    private String propertyDir = null;
    public CSVWriter csvWriterQuestions;
    public CSVWriter csvWriterSummary;
    public String questionAnswerFile = null;
    public String questionSummaryFile = null;
    private Map<String, Statistics> summary = new TreeMap<String, Statistics>();
    //private Map<String, String> lexEntryParameter = new TreeMap<String, String>();

    private Integer maxNumberOfEntities = 100;
    private Integer batchNumber = 0;
    private String endpoint = null;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ProtoToRealQuesrion.class);
    private LinkedData linkedData = null;
    private Map<String, OffLineResult> entityLabels = new TreeMap<String, OffLineResult>();
    private InputCofiguration inputCofiguration = null;
    private Boolean online = false;
    private List<String> menus = Stream.of(FIND_WIKI_LINK, FIND_ABSTRACT, FIND_IMAGE_LINK).collect(Collectors.toCollection(ArrayList::new));
    private String parameter=null;
    public ProtoToRealQuesrion(LinkedData linkedData, InputCofiguration inputCofiguration) throws Exception {
        this.maxNumberOfEntities = maxNumberOfEntities;
        this.linkedData = linkedData;
        this.inputCofiguration = inputCofiguration;
        this.online = this.inputCofiguration.getOnline();
        this.language = this.inputCofiguration.getLanguageCode();
        this.propertyDir = this.inputCofiguration.getEntityDir();
        this.endpoint = this.linkedData.getEndpoint();
        this.questionAnswerFile = this.inputCofiguration.getQuestionDir() + File.separator + questionsFile + "_" + language + ".csv";
        this.questionSummaryFile = this.inputCofiguration.getQuestionDir() + File.separator + summaryFile + "_" + language + ".csv";
        //this.batchNumber=this.getBatchNumner(inputCofiguration);
        this.batchNumber=1;
        this.parameter=inputCofiguration.getParameter();
        //String parameterFileName = "/media/elahi/Elements/A-project/resources/ldk/parameter/parameter.txt";
        //this.lexEntryParameter=this.findParameter(parameterFileName);
        //System.out.println(lexEntryParameter.keySet());
        //exit(1);

    }

    public void onlineQaGeneration(List<File> protoSimpleQFiles) throws Exception {
        Integer index = 0;

        if (protoSimpleQFiles.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        this.csvWriterQuestions = new CSVWriter(new FileWriter(this.questionAnswerFile, true));
        this.csvWriterSummary = new CSVWriter(new FileWriter(questionSummaryFile, true));
        this.writeInCSV(summaryHeader);

        Set<String> existingEntries = this.getExistingLexicalEntries(questionSummaryFile);

        for (File file : protoSimpleQFiles) {
            index = index + 1;
            ObjectMapper mapper = new ObjectMapper();
            GrammarEntries grammarEntries = mapper.readValue(file, GrammarEntries.class);
            Integer total = grammarEntries.getGrammarEntries().size();
            Integer idIndex = 0, noIndex = 0;
            processEachGrammarUnit(grammarEntries, existingEntries, noIndex, idIndex);
        }

        this.csvWriterQuestions.close();
        this.writeSummary(this.summary);
        this.csvWriterSummary.close();

    }
    
  
    
     private Integer questionGeneration(String uri, String sparqlQuery, List<UriLabel> uriLabels, List<String> questions, Integer rowIndex, String lexicalEntry, GrammarEntryUnit grammarEntryUnit, Map<String, OffLineResult> entityLabels) throws IOException {
        Integer index = 0;
        String returnSubjOrObj = grammarEntryUnit.getReturnVariable();
        String syntacticFrame = grammarEntryUnit.getFrameType();
        QueryType queryType = grammarEntryUnit.getQueryType();
        String rdfTypeProperty = linkedData.getRdfPropertyType();
        String className = linkedData.getRdfPropertyClass(grammarEntryUnit.getReturnType());
        String template = grammarEntryUnit.getSentenceTemplate();
        List< String[]> rows = new ArrayList<String[]>();

        if (questions.isEmpty()) {
            return rowIndex;
        }

        for (UriLabel uriLabel : uriLabels) {
            String questionUri = uriLabel.getUri(), questionLabel = uriLabel.getLabel();
            String answerWiki = null, answerThumb = null, answerAbstract = null;

            if (questionUri != null && !questionLabel.isEmpty()) {
                questionUri = questionUri.trim().strip().stripLeading().stripTrailing();
                questionLabel = uriLabel.getLabel().replaceAll("\'", "").replaceAll("\"", "");
                //bashScript=  new BashScript(menus, inputCofiguration.getWikiFile(), inputCofiguration.getAbstractFile(),inputCofiguration.getWikiFile(),  questionUri);

            } else if (questionLabel.isEmpty()) {
                //find labels for the specific database
                if (this.online) {
                    String classType = this.linkedData.getClassProperty();
                    questionLabel = getUriLabel(questionUri, classType);
                }
            }

            if (!AddQuote.isKbValid(uriLabel)) {
                continue;
            }
            String questionForShow = questions.iterator().next();

            if (questionForShow.contains("Where is $x located?")) {
                continue;
            }
            String[] wikipediaAnswer = new String[3];
            String sparql = null, answerUri = null, answerLabel = null;
            wikipediaAnswer = this.getAnswerFromWikipedia(template, rdfTypeProperty, className, questionUri, sparqlQuery, null, returnSubjOrObj, endpoint, queryType);
            sparql = wikipediaAnswer[0];

            if (this.online) {
                answerUri = wikipediaAnswer[1];
                answerLabel = wikipediaAnswer[2];
            } else {
                answerUri = uriLabel.getAnswerUri();
                answerLabel = uriLabel.getAnswerLabel();
                /*answerWiki =bashScript.getWikiLink();
                answerThumb =bashScript.getImageLink();
                answerAbstract = bashScript.getAbstractText();*/
                //System.out.println(lexicalEntry + " questionForShow:" + questionForShow + " questionUri::" + questionUri + " questionLabel:" + questionLabel + " answerUri:" + answerUri + " answerLabel" + answerLabel);
            }

            index = index + 1;

            sparql = AddQuote.modifySparql(sparql);

            try {
                /*if (answer.isEmpty() || answer.contains("no answer found")) {
                    continue;
                } else*/ {
                    /*if (index >= this.maxNumberOfEntities) {
                        break;
                    }*/

                    if (this.online) {
                        /*if (answerUri.isEmpty() || answerUri.length() < 2) {
                            continue;
                        }*/
                    }

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
                        //System.out.println("answerWiki::" + answerWiki + " answerThumb::" + answerThumb+" answerAbstract::"+answerAbstract);

                        if (this.online) {
                            if (answerUri != null) {
                                this.csvWriterQuestions.writeNext(newRecord);
                                System.out.println("index::" + index + " questionT::" + questionT + " sparql::" + sparql + " answerUri::" + answerUri + " answerLabel::" + answerLabel + " syntacticFrame:" + syntacticFrame);
                                rowIndex = rowIndex + 1;
                            } else {
                                continue;
                            }
                        }
                        else {
                            System.out.println("index::" + index + " questionT::" + questionT + " answerUri:" + answerUri + " answerLabel:" + answerLabel + " sparql:" + sparql);
                            this.csvWriterQuestions.writeNext(newRecord);
                            rowIndex = rowIndex + 1;
                        }

                        //if(rowIndex>100)
                        //  return rowIndex;
                        //}
                    }
                }

            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " " + sparql + " " + answerLabel);
            }
        }

        return rowIndex;
    }

   

    private void processEachGrammarUnit(GrammarEntries grammarEntries, Set<String> existingEntries, Integer noIndex, Integer idIndex) throws IOException, Exception {
        List<String> questions;
        for (GrammarEntryUnit grammarEntryUnit : grammarEntries.getGrammarEntries()) {
            String uri = null, className;

            if (grammarEntryUnit.getLexicalEntryUri() != null) {
                uri = grammarEntryUnit.getLexicalEntryUri().toString();
                if (existingEntries.isEmpty() && existingEntries.contains(uri)) {
                    continue;
                }
            } else {
                continue;
            }
            questions = this.validQuestion(grammarEntryUnit);

            if (questions.isEmpty()) {
                continue;
            }

            List<UriLabel> bindingList = this.getBindingList(grammarEntryUnit);
            String sparql = this.getSparql(grammarEntryUnit);

            if (grammarEntryUnit.getQueryType().equals(QueryType.SELECT)) {
                noIndex = this.questionGeneration(uri, sparql, bindingList, questions, noIndex, "", grammarEntryUnit, entityLabels);
                noIndex = noIndex + 1;
                idIndex = idIndex + 1;
            }

            if (grammarEntryUnit.getLexicalEntryUri() != null) {
                uri = grammarEntryUnit.getLexicalEntryUri().toString();

                if (this.summary.containsKey(uri)) {
                    Statistics summary = this.summary.get(uri);
                    this.summary.put(uri, new Statistics(grammarEntryUnit.getFrameType(), summary.getNumberOfGrammarRules() + 1, noIndex, bindingList.size()));
                } else {
                    Statistics summary = new Statistics(grammarEntryUnit.getFrameType(), 1, noIndex, bindingList.size());
                    this.summary.put(uri, summary);
                }
            }
        }
    }

   

    public String[] getAnswerFromWikipedia(String template, String rdfPropertyType, String className, String domainEntityUri,
            String sparql, String rangeEntityUri, String returnSubjOrObj, String endpoint,
            QueryType queryType) throws IOException {
        String answerLabel = null, answerUri = null;
        SparqlQuery sparqlQuery = new SparqlQuery(template, rdfPropertyType, className, domainEntityUri, sparql, rangeEntityUri, SparqlQuery.FIND_ANY_ANSWER, returnSubjOrObj, language, endpoint, online, queryType, this.linkedData);
        answerUri = sparqlQuery.getObject();

        if (answerUri != null) {
            if (queryType.equals(QueryType.ASK)) {
                answerLabel = answerUri;
            } else if (queryType.equals(QueryType.SELECT)) {
                if (answerUri.contains("http:")) {
                    SparqlQuery sparqlQueryLabel = new SparqlQuery(template, rdfPropertyType, className, answerUri, sparql, rangeEntityUri, SparqlQuery.FIND_LABEL, null, language, endpoint, online, queryType, this.linkedData);
                    answerLabel = sparqlQueryLabel.getObject();
                    answerLabel = StringMatcher.modifyLabels(answerLabel);
                }
            }
        }

        return new String[]{sparqlQuery.getSparqlQuery(), answerUri, answerLabel};

    }

   

    private void writeSummary(Map<String, Statistics> summary) {
        if (summary.isEmpty()) {
            return;
        }
        for (String key : summary.keySet()) {
            Statistics element = summary.get(key);
            String[] record = {key, element.getNumberOfGrammarRules().toString(), element.getNumberOfQuestions().toString(), element.getFrameType(), element.getSuccess_Fail(), element.getReason()};
            this.csvWriterSummary.writeNext(record);

        }
    }

    private Set<String> getExistingLexicalEntries(String questionSummaryFile) {
        Integer index = 0;
        CsvFile csvFile = new CsvFile();
        Set<String> lexicalEntries = new HashSet<String>();
        List<String[]> rows = csvFile.getRows(new File(questionSummaryFile));
        for (String[] row : rows) {
            String column = row[0];
            column = column.trim().strip().stripLeading().stripTrailing();
            lexicalEntries.add(column);
        }
        return lexicalEntries;
    }

  

    private void writeInCSV(String []summaryHeader) {
        //this.csvWriterQuestions.writeNext(questionHeader);
        this.csvWriterSummary.writeNext(summaryHeader);
    }

    private List<UriLabel> getBIndingList(Map<String, OffLineResult> entityLabels) {
        List<UriLabel> urlLabels = new ArrayList<UriLabel>();
        for (String key : entityLabels.keySet()) {
            OffLineResult offLineResult = entityLabels.get(key);
            UriLabel uriLabel = new UriLabel(offLineResult.getSubjectUri(), offLineResult.getSubjectLabel(), offLineResult.getObjectUri(), offLineResult.getObjectLabel(), null, null, null);
            urlLabels.add(uriLabel);
        }
        return urlLabels;
    }

    private List<UriLabel> getBindingList(GrammarEntryUnit grammarEntryUnit) {
        List<UriLabel> bindingList = grammarEntryUnit.getBindingList();

        if (grammarEntryUnit.getQueryType().equals(QueryType.ASK)) {
            return new ArrayList<UriLabel>();
        }

        //If it is an external binding list
        if (this.inputCofiguration.getExternalEntittyList()) {
            bindingList = this.getBIndingList(this.entityLabels);
        } else {
            bindingList = grammarEntryUnit.getBindingList();

        }

        if (grammarEntryUnit.getBindingType().contains("date")) {
            bindingList = grammarEntryUnit.getBindingList();
        }
        return bindingList;
    }

    private String getSparql(GrammarEntryUnit grammarEntryUnit) throws Exception {
        String sparql = grammarEntryUnit.getSparqlQuery();

        try {
            if (grammarEntryUnit.getFrameType().contains(FrameType.AG.toString()) && grammarEntryUnit.getSentenceTemplate().contains(superlative)) {
                sparql = grammarEntryUnit.getExecutable();
            } else {
                sparql = grammarEntryUnit.getSparqlQuery();

            }
        } catch (Exception ex) {
            throw new Exception("no sparlq query!!!");
        }
        return sparql;
    }

    private List<String> validQuestion(GrammarEntryUnit grammarEntryUnit) {
        if (!grammarEntryUnit.getSentences().contains("Where is $x located?")) {
            return grammarEntryUnit.getSentences();
        }
        return new ArrayList<String>();
    }

    private String getUriLabel(String uri, String property) {
        String classSparql = PrepareSparqlQuery.findClassGivenInstance(uri, property);
        SparqlQuery sparqlQueryForClass = new SparqlQuery(this.linkedData, classSparql);
        String labelProperty = this.linkedData.getRdfPropertyLabel(sparqlQueryForClass.getObject());
        String labelSparql = PrepareSparqlQuery.findClassGivenInstance(uri, labelProperty);
        sparqlQueryForClass = new SparqlQuery(this.linkedData, labelSparql);
        return sparqlQueryForClass.getObject();
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

   

    private void findCoverage(String propertyDir, Map<String, List<GrammarEntryUnit>> lexicalEntiryUris,String fileName) {
        String[] files = new File(propertyDir).list();
        Set<String> properties = new TreeSet<String>();
        Set<String> generatedProperties = new TreeSet<String>();
        for (String property : files) {
            if (property.contains(".txt")) {
                property = property.replace(".txt", "");
                property = property.replace("dbo_", "dbo:").trim();
                property = property.replace("dbp_", "dbp:").trim();
                if(property.contains("dbo:")||property.contains("dbp:"))
                   properties.add(property);
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
                property=property.strip().stripLeading().stripTrailing().trim();
                if (!properties.contains(property)) {
                    generatedProperties.add(grammarEntryUnit.getLexicalEntryUri()+"="+property+"="+grammarEntryUnit.getFrameType());
                }
            }
        }
        FileFolderUtils.setToFile(generatedProperties, fileName);

    }

    
    /*public static void main(String[] args) {
        Map<String, List<GrammarEntryUnit>> lexicalEntiryUris = GrammarEntryUnit.getLexicalEntries(protoSimpleQFiles);
        GrammarEntriesLex grammarEntriesLex = new GrammarEntriesLex(lexicalEntiryUris);
        JsonWriter.writeClassToJson(grammarEntriesLex, propertyDir + "NounPPFrame-Grammar.json");
        this.findCoverage(this.propertyDir, lexicalEntiryUris, propertyDir + "missedProperty.txt");
        exit(1);

    }*/

    private void writeGrammarRules(List<File> protoSimpleQFiles) {
         for (String frameType : SYNTACTIC_FRAME.keySet()) {
             String syntaktikFrame=SYNTACTIC_FRAME.get(frameType);
             Map<String, List<GrammarEntryUnit>> lexicalEntiryUris = GrammarEntryUnit.getLexicalEntries(protoSimpleQFiles,frameType);
             GrammarEntriesLex grammarEntriesLex = new GrammarEntriesLex(lexicalEntiryUris);
             //NounPPFrame 
             JsonWriter.writeClassToJson(grammarEntriesLex, propertyDir + syntaktikFrame+".json");

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



}
