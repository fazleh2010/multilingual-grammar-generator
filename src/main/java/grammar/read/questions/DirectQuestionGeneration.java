/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.opencsv.CSVWriter;
import grammar.datasets.sentencetemplates.SentenceTemplateFactoryEN_1;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import static grammar.datasets.sentencetemplates.TempConstants.AdjectiveAttributiveFrame;
import static grammar.datasets.sentencetemplates.TempConstants.AdjectiveSuperlativeFrame;
import static grammar.datasets.sentencetemplates.TempConstants.HOW_MANY_THING;
import static grammar.datasets.sentencetemplates.TempConstants.IntransitivePPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.NounPPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.Prepositional_Adjuct;
import static grammar.datasets.sentencetemplates.TempConstants.TransitiveFrame;
import static grammar.datasets.sentencetemplates.TempConstants.domain;
import static grammar.datasets.sentencetemplates.TempConstants.nounPhrase;
import static grammar.datasets.sentencetemplates.TempConstants.superlative;
import grammar.sparql.PrepareSparqlQuery;
import grammar.sparql.SparqlQuery;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import linkeddata.LinkedData;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryType;
import org.apache.logging.log4j.LogManager;
import turtle.EnglishCsv;
import turtle.EnglishCsv.NounPPFrameCsv;
import turtle.EnglishCsv.TransitFrameCsv;
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
    private Map<String, List<String>> domainOrRange = new TreeMap<String, List<String>>();

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
        DomainRangeDictionary domainRangeDictionary = new DomainRangeDictionary(inputCofiguration.getDomainAndRangeDir());
        this.domainOrRange = domainRangeDictionary.getDomainOrRange();
    }

    public void offline(String dir) throws Exception {
        File[] files = new File(dir).listFiles();
        for (File file : files) {
            CsvFile csvFile = new CsvFile();
            List<String[]> rows = csvFile.getRows(new File(dir + file.getName()));
            offline(file.getName(),rows);
        }

    }

    public void offline(String fileName,List<String[]> rows) throws Exception {

        if (rows.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        Integer rowIndex = 0, idIndex = 0;
        String questionAnswerFile = this.inputCofiguration.getQuestionDir() + File.separator + this.batchNumber.toString()+ "~" +  fileName + "~" + questionsFile + ".csv";
        this.csvWriterQuestions = new CSVWriter(new FileWriter(questionAnswerFile, true));
        
        for (String[] row : rows) {
            if (idIndex == 0) {
                idIndex = idIndex + 1;
                continue;
            }
            String lexicalEntiryUri = row[0];
            batchNumber = batchNumber + 1;
            String syntacticFrame = this.findSyntacticFrame(row);
            String property = this.findProperty(row, syntacticFrame);
            

            String returnSubjOrObj = findReturnSubjOrObj(row, syntacticFrame);
            String bindingType = findBindingType(syntacticFrame, returnSubjOrObj, row);
            String returnType = findReturnType(syntacticFrame, returnSubjOrObj, row);
            Map<String, String[]> templateQuestions = findQuestions(syntacticFrame, bindingType, returnType, row);
            String sparqlQuery = findSparql(property, returnSubjOrObj);

            /*if (syntacticFrame.contains(FrameType.AG.toString()) && sentenceTemplate.contains(superlative)) {
                sparqlQuery = findAdjektive(property);
            } else {
                sparqlQuery = findSparql(property, returnSubjOrObj);
            }*/
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
            for (String template : templateQuestions.keySet()) {
                String[] questions = templateQuestions.get(template);
                rowIndex = this.questionGeneration(lexicalEntiryUri, sparqlQuery, bindingList,
                        questions, rowIndex,
                        syntacticFrame, returnSubjOrObj, QueryType.SELECT, returnType, template);
                rowIndex = rowIndex + 1;
                idIndex = idIndex + 1;
            }
        }
         this.csvWriterQuestions.close();
    }

    private Integer questionGeneration(String lexicalEntiryUri, String sparqlQuery, List<UriLabel> bindingList,
            String[] questions, Integer rowIndex,
            String syntacticFrame, String returnSubjOrObj, QueryType queryType, String returnType, String sentenceTemplate) throws IOException, Exception {
        Integer index = 0;

        String rdfTypeProperty = linkedData.getRdfPropertyType();
        String className = linkedData.getRdfPropertyClass(returnType);
        String template = sentenceTemplate;
        List< String[]> rows = new ArrayList<String[]>();

        if (questions.length == 0) {
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
            String questionForShow = questions[0];

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

                        if (question.contains("XX")) {
                            continue;
                        }
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

    public String findSyntacticFrame(String[] row) throws Exception {

        Integer nounPPIndex = 5;
        Integer transitiveIndex = 6;
        Integer InTransitiveIndex = 7;
        Integer adjectiveFrameIndex = 3;
        Integer gradableAdjectiveFrameIndex = 5;
        String nounPPFrame = row[nounPPIndex];

        /*Integer index=0;
        for (String string:row) {
          System.out.println(index+" "+string);
          index=index+1;
        }
        System.out.println(transitiveIndex+" "+row[transitiveIndex]);
         System.out.println(InTransitiveIndex+" "+row[InTransitiveIndex]);
         */
        try {
            if (nounPPFrame.contains(NounPPFrame)) {
                return NounPPFrame;
            } else if (row[transitiveIndex].contains(TransitiveFrame) || row[InTransitiveIndex].contains("InTransitivePPFrame")) {
                return TransitiveFrame;
            } else if (row[InTransitiveIndex].contains(IntransitivePPFrame) || row[InTransitiveIndex].contains("InTransitivePPFrame")) {
                return IntransitivePPFrame;
            } else if (row[adjectiveFrameIndex].contains(AdjectiveAttributiveFrame)) {
                return AdjectiveAttributiveFrame;
            } else if (row[gradableAdjectiveFrameIndex].contains(AdjectiveSuperlativeFrame)) {
                return AdjectiveSuperlativeFrame;
            } else {
                throw new Exception("No grammar entry is found!!!!");
            }
        } catch (Exception ex) {
            throw new Exception("lexial entry:" + row[0] + " invalid entry in XSL sheet:" + ex.getMessage().toString()); //To change body of generated methods, choose Tools | Templates.   
        }
    }

    /*
       public static Integer lemonEntryIndex = 0;
        public static Integer partOfSpeechIndex = 1;
        public static Integer writtenFormInfinitive = 2;
        public static Integer writtenFormPluralIndex = 3;
        public static Integer prepositionIndex = 4;
        public static Integer syntacticFrameIndex = 5;
        public static Integer copulativeArgIndex = 6;
        public static Integer prepositionalAdjunctIndex = 7;
        public static Integer senseIndex = 8;
        public static Integer referenceIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;
        public static Integer domainWrittenSingular = 12;
        public static Integer domainWrittenPlural = 13;
        public static Integer rangeWrittenSingular = 14;
        public static Integer rangeWrittenPlural = 15;
        private static String proeposition_id;
     */
    private Map<String, String[]> findQuestions(String syntacticFrame, String bindingType, String returnType, String[] row) {
        Map<String, String[]> templateQuestions = new TreeMap<String, String[]>();
        SentenceTemplateFactoryEN_1 sentenceTemplateRepository = new SentenceTemplateFactoryEN_1();
        SentenceTemplateRepository sentenceTemplateRepositoryT = sentenceTemplateRepository.init();
        if (syntacticFrame.contains(NounPPFrame)) {
            Set<String> sentenceTemplate = findSeneteneTemplate(row, syntacticFrame);
            for (String template : sentenceTemplate) {
                List<String> questions = sentenceTemplateRepositoryT.findOneByEntryTypeAndLanguageAndArguments(
                        SentenceType.SENTENCE, Language.EN, new String[]{syntacticFrame, template});
                if (!questions.isEmpty()) {
                    String templateAllQuestions = questions.iterator().next();
                    String[] realQuestions = findNounPPQuestions(bindingType,returnType,templateAllQuestions,row);
                    templateQuestions.put(template, realQuestions);
                }

            }
        }
        return templateQuestions;
    }

    private String findReturnSubjOrObj(String[] row, String syntacticFrame) throws Exception {
        Integer nounPPFrameDomainIndex = 10;
        Integer transitiveFrameDomainIndex = 11;
        Integer inTransitiveFrameDomainIndex = 12;
        if (syntacticFrame.contains(NounPPFrame)) {
            if (row[nounPPFrameDomainIndex].contains(domain)) {
                return ReadWriteConstants.RETURN_TYPE_SUBJECT;
            } else {
                return ReadWriteConstants.RETURN_TYPE_OBJECT;
            }
        } else if (syntacticFrame.contains(TransitiveFrame)) {
            if (row[transitiveFrameDomainIndex].contains(domain)) {
                return ReadWriteConstants.RETURN_TYPE_OBJECT;
            } else {
                return ReadWriteConstants.RETURN_TYPE_SUBJECT;
            }
        } else if (syntacticFrame.contains(IntransitivePPFrame)) {
            if (row[inTransitiveFrameDomainIndex].contains(domain)) {
                return ReadWriteConstants.RETURN_TYPE_OBJECT;
            } else {
                return ReadWriteConstants.RETURN_TYPE_SUBJECT;
            }
        }

        throw new Exception("No return Type is found!!!");
    }

    private String findBindingType(String syntacticFrame, String returnSubjOrObj, String[] row) throws Exception {
        Integer nounPPFrameDomainIndex = 10;
        Integer transitiveFrameDomainIndex = 11;
        Integer inTransitiveFrameDomainIndex = 12;

        if (syntacticFrame.contains(NounPPFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                return row[nounPPFrameDomainIndex].split(":")[1];
            } else {
                return row[nounPPFrameDomainIndex + 1].split(":")[1];
            }

        } else if (syntacticFrame.contains(TransitiveFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                return row[transitiveFrameDomainIndex].split(":")[1];
            } else {
                return row[transitiveFrameDomainIndex + 1].split(":")[1];
            }

        } else if (syntacticFrame.contains(IntransitivePPFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                return row[inTransitiveFrameDomainIndex].split(":")[1];
            } else {
                return row[inTransitiveFrameDomainIndex + 1].split(":")[1];
            }

        }
        throw new Exception("No binding Type is found!!!");
    }

    private String findReturnType(String syntacticFrame, String returnSubjOrObj, String[] row) throws Exception {
        Integer nounPPFrameDomainIndex = 10;
        Integer transitiveFrameDomainIndex = 11;
        Integer inTransitiveFrameDomainIndex = 12;

        if (syntacticFrame.contains(NounPPFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                return row[nounPPFrameDomainIndex + 1].split(":")[1];
            } else {
                return row[nounPPFrameDomainIndex].split(":")[1];
            }

        } else if (syntacticFrame.contains(TransitiveFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                return row[transitiveFrameDomainIndex + 1].split(":")[1];
            } else {
                return row[transitiveFrameDomainIndex].split(":")[1];
            }

        } else if (syntacticFrame.contains(IntransitivePPFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                return row[inTransitiveFrameDomainIndex + 1].split(":")[1];
            } else {
                return row[inTransitiveFrameDomainIndex].split(":")[1];
            }

        }
        throw new Exception("No binding Type is found!!!");
    }

    private Set<String> findSeneteneTemplate(String[] row, String syntacticFrame) {
        if (syntacticFrame.contains(NounPPFrame)) {
            return SentenceTemplateFactoryEN_1.nounPPTemplates;
        }
        return new HashSet<String>();
    }

    private String findSparql(String property, String questionType) throws Exception {
        if (property.contains("dbo:")) {
            String[] info = property.split(":");
            property = info[1];
            return "(bgp (triple ?subjOfProp " + "<http://dbpedia.org/ontology/" + property + ">" + " ?objOfProp))";
        } else if (property.contains("dbp:")) {
            String[] info = property.split(":");
            property = info[1];
            return "(bgp (triple ?subjOfProp " + "<http://dbpedia.org/property/" + property + ">" + " ?objOfProp))";
        }
        throw new Exception("No sparql query is found!!!");
    }

    private String findProperty(String[] row, String syntacticFrame) {
        Integer nounPPIndex = 9;
        Integer transitiveIndex = 10;
        Integer InTransitiveIndex = 11;

        if (syntacticFrame.contains(NounPPFrame)) {
            return row[nounPPIndex];
        } else if (syntacticFrame.contains(TransitiveFrame)) {
            return row[transitiveIndex];
        } else if (syntacticFrame.contains(IntransitivePPFrame) || syntacticFrame.contains("InTransitivePPFrame")) {
            return row[InTransitiveIndex];
        }
        return null;
    }


    private String makeVairable(String bindingType) {
        return "($x | " + bindingType + "_NP)";
    }

    public List<String> findSingularPlural(String domainOrRangeStr) {
        if (domainOrRange.containsKey(domainOrRangeStr)) {
            return domainOrRange.get(domainOrRangeStr);

        }
        return new ArrayList<String>();
    }

    private String[] findNounPPQuestions(String bindingType,String returnType, String templateAllQuestions,String []row) {
        bindingType = this.makeVairable(bindingType);
        List<String> singularPlural = findSingularPlural(returnType);
        String rangeSingular = "XX";
        String rangePlural = "XX";
        if (!singularPlural.isEmpty()) {
            rangeSingular = singularPlural.get(0);
            rangePlural = singularPlural.get(1);
        }

        String refereneSingular = row[EnglishCsv.NounPPFrameCsv.writtenFormInfinitive];
        String referenePlural = row[EnglishCsv.NounPPFrameCsv.writtenFormInfinitive];
        String preposition = row[EnglishCsv.NounPPFrameCsv.prepositionIndex];

        templateAllQuestions = templateAllQuestions.replace("Variable", bindingType);
        templateAllQuestions = templateAllQuestions.replace("(reference:singular)", refereneSingular);
        templateAllQuestions = templateAllQuestions.replace("(reference:plural)", referenePlural);
        templateAllQuestions = templateAllQuestions.replace("(range:singular)", rangeSingular);
        templateAllQuestions = templateAllQuestions.replace("(range:plural)", rangePlural);
        templateAllQuestions = templateAllQuestions.replace("preposition", preposition);
        String[] realQuestions = templateAllQuestions.split("\n");
        return realQuestions;
    }

}
