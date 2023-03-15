/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.opencsv.CSVWriter;
import static grammar.datasets.sentencetemplates.TempConstants.superlative;
import grammar.sparql.PrepareSparqlQuery;
import grammar.sparql.SparqlQuery;
import grammar.structure.component.FrameType;
import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import linkeddata.LinkedData;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryType;
import org.apache.logging.log4j.LogManager;
import util.io.*;

/**
 *
 * @author elahi
 */
public class DirectQuestionGeneration implements ReadWriteConstants {

    private static String language = "en";
    private String propertyDir = null;
    public CSVWriter csvWriterQuestions;
    public String questionAnswerFile = null;
    public String questionSummaryFile = null;
    public String classDir = null;
    private Boolean online = false;
    private LinkedData linkedData = null;

    private Integer batchNumber = 0;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ProtoToRealQuesrion.class);
    private Map<String, OffLineResult> entityLabels = new TreeMap<String, OffLineResult>();
    private InputCofiguration inputCofiguration = null;
    private String parameter = null;
    private String endpoint = null;

    public DirectQuestionGeneration(LinkedData linkedData, InputCofiguration inputCofiguration) throws Exception {
        this.inputCofiguration = inputCofiguration;
        this.language = inputCofiguration.getLanguageCode();
        this.propertyDir = inputCofiguration.getEntityDir();
        this.questionAnswerFile = inputCofiguration.getQuestionDir() + File.separator + questionsFile + "_" + language + ".csv";
        this.questionSummaryFile = inputCofiguration.getQuestionDir() + File.separator + summaryFile + "_" + language + ".csv";
        this.batchNumber = 1;
        this.parameter = inputCofiguration.getParameter();
        this.classDir = inputCofiguration.getClassDir();
        this.online = this.inputCofiguration.getOnline();
        this.linkedData = linkedData;
        this.endpoint = this.linkedData.getEndpoint();
    }

    public void offline(String dir) throws Exception {
        File[] files = new File(dir).listFiles();
        for (File file : files) {
            CsvFile csvFile = new CsvFile();
            List<String[]> rows = csvFile.getRows(new File(dir + file.getName()));
        }

    }

    public void offline(List<String[]> rows) throws Exception {

        if (rows.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        Integer rowIndex = 0, idIndex = 0;
        for (String[] row : rows) {
            String lexicalEntiryUri = row[0];
            batchNumber = batchNumber + 1;
            String property = this.findProperty(row);
            String questionAnswerFile = this.inputCofiguration.getQuestionDir() + File.separator + this.batchNumber.toString() + "~" + parameter + "~" + lexicalEntiryUri + "~" + questionsFile + ".csv";
            this.csvWriterQuestions = new CSVWriter(new FileWriter(questionAnswerFile, true));
            String syntacticFrame = findFrameType(row);
            List<String> questions = findQuestions(property);

            String returnSubjOrObj = findReturnSubjOrObj(row, syntacticFrame);
            String bindingType = findBindingType(row, syntacticFrame);
            String returnType = findReturnType(row, syntacticFrame);
            String sentenceTemplate = findSeneteneTemplate(row, syntacticFrame);
            String sparqlQuery = findSparql(property);
            if (syntacticFrame.contains(FrameType.AG.toString()) && sentenceTemplate.contains(superlative)) {
                sparqlQuery = findAdjektive(property);
            } else {
                sparqlQuery = findSparql(property);
            }

            List<UriLabel> bindingList = new ArrayList<UriLabel>();

            try {
                String propertyFile = AddQuote.getProperty(this.propertyDir, sparqlQuery);
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

            rowIndex = this.questionGeneration(lexicalEntiryUri, sparqlQuery, bindingList,
                    questions, rowIndex,
                    syntacticFrame, returnSubjOrObj, QueryType.SELECT, returnType, sentenceTemplate);
            rowIndex = rowIndex + 1;
            idIndex = idIndex + 1;
            this.csvWriterQuestions.close();
        }
    }

    private Integer questionGeneration(String lexicalEntiryUri, String sparqlQuery, List<UriLabel> bindingList,
            List<String> questions, Integer rowIndex,
            String syntacticFrame, String returnSubjOrObj, QueryType queryType, String returnType, String sentenceTemplate) throws IOException, Exception {
        Integer index = 0;

        String rdfTypeProperty = linkedData.getRdfPropertyType();
        String className = linkedData.getRdfPropertyClass(returnType);
        String template = sentenceTemplate;
        List< String[]> rows = new ArrayList<String[]>();

        if (questions.isEmpty()) {
            return rowIndex;
        }

        for (UriLabel uriLabel : bindingList) {
            String questionUri = uriLabel.getUri(), questionLabel = uriLabel.getLabel();
            String answerWiki = null, answerThumb = null, answerAbstract = null;

            try {

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

                        String id = lexicalEntiryUri + "~" + rowIndex.toString();
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
                        } else {
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

    private String getUriLabel(String uri, String property) {
        String classSparql = PrepareSparqlQuery.findClassGivenInstance(uri, property);
        SparqlQuery sparqlQueryForClass = new SparqlQuery(this.linkedData, classSparql);
        String labelProperty = this.linkedData.getRdfPropertyLabel(sparqlQueryForClass.getObject());
        String labelSparql = PrepareSparqlQuery.findClassGivenInstance(uri, labelProperty);
        sparqlQueryForClass = new SparqlQuery(this.linkedData, labelSparql);
        return sparqlQueryForClass.getObject();
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

    private String findProperty(String[] row) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String findFrameType(String[] row) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<String> findQuestions(String property) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String findReturnSubjOrObj(String[] row, String syntacticFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String findBindingType(String[] row, String syntacticFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String findReturnType(String[] row, String syntacticFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String findSeneteneTemplate(String[] row, String syntacticFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String findSparql(String property) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String findAdjektive(String property) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
