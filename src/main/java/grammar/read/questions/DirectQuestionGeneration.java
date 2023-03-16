/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.google.gdata.util.common.base.Pair;
import com.opencsv.CSVWriter;
import grammar.datasets.sentencetemplates.SentenceTemplateFactoryEN_1;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.sentencetemplates.TempConstants;
import grammar.generator.sentencebuilder.TemplateFinder;
import grammar.sparql.PrepareSparqlQuery;
import grammar.sparql.SparqlQuery;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import java.io.BufferedReader;
import java.io.*;
import static java.lang.System.exit;
import java.util.*;
import linkeddata.LinkedData;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryType;
import org.apache.logging.log4j.LogManager;
import turtle.EnglishCsv;
import util.io.*;

/**
 *
 * @author elahi
 */
public class DirectQuestionGeneration implements ReadWriteConstants, TempConstants {

    private static String language = "en";
    private String propertyDir = null;
    public CSVWriter csvWriterQuestions;
    public String questionAnswerFile = null;
    public String questionSummaryFile = null;
    public String classDir = null;
    private Boolean online = false;
    private LinkedData linkedData = null;
    private TemplateFinder templateFinder = null;
    //private String logString="";

    private Integer batchNumber = 0;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ProtoToRealQuesrion.class);
    private InputCofiguration inputCofiguration = null;
    private String endpoint = null;
    private Map<String, List<String>> domainOrRange = new TreeMap<String, List<String>>();
    private Set<String> transitiveFrameEntries = new HashSet<String>();

    public DirectQuestionGeneration(LinkedData linkedData, InputCofiguration inputCofiguration) throws Exception {
        this.inputCofiguration = inputCofiguration;
        this.language = inputCofiguration.getLanguageCode();
        this.propertyDir = inputCofiguration.getEntityDir();
        this.questionAnswerFile = inputCofiguration.getQuestionDir() + File.separator + questionsFile + "_" + language + ".csv";
        this.questionSummaryFile = inputCofiguration.getQuestionDir() + File.separator + summaryFile + "_" + language + ".csv";
        this.batchNumber = 1;
        this.classDir = inputCofiguration.getClassDir();
        this.online = this.inputCofiguration.getOnline();
        this.linkedData = linkedData;
        this.endpoint = this.linkedData.getEndpoint();
        DomainRangeDictionary domainRangeDictionary = new DomainRangeDictionary(inputCofiguration.getDomainAndRangeDir());
        this.domainOrRange = domainRangeDictionary.getDomainOrRange();
        this.transitiveFrameEntries = FileFolderUtils.fileToSet("src/main/resources/TransitiveFrame.txt");
    }

    public void offline(String dir, String logFile) throws Exception {
        File[] files = new File(dir).listFiles();
        Integer numberOfFiles = 0;
        for (File file : files) {
            CsvFile csvFile = new CsvFile();
            if (file.getName().contains(".~lock.")) {
                 continue;
            }
            List<String[]> rows = csvFile.getRows(new File(dir + file.getName()));
            System.out.println("total files::" + files.length + " now::" + numberOfFiles + " file:" + file.getName() + " rows::" + rows.size() + " folder:" + dir);
            offline(file.getName(), rows);
            numberOfFiles = numberOfFiles + 1;
        }
        //FileFolderUtils.stringToFiles(logString, logFile);
    }

    public void offline(String fileName, List<String[]> rows) throws Exception {

        if (rows.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        Integer rowIndex = 0, idIndex = 0;
        String questionAnswerFile = this.inputCofiguration.getQuestionDir() + File.separator + this.batchNumber.toString() + "~" + fileName + "~" + questionsFile + ".csv";
        this.csvWriterQuestions = new CSVWriter(new FileWriter(questionAnswerFile, true));

        for (String[] row : rows) {
            String syntacticFrame = null, property = null;
            Map<String, Pair<String, GrammarInfor>> templateQuestions = new HashMap<String, Pair<String, GrammarInfor>>();
            if (idIndex == 0) {
                idIndex = idIndex + 1;
                continue;
            }
            String lexicalEntiryUri = row[0];
            batchNumber = batchNumber + 1;
            Map<String, OffLineResult>entityLabels=new HashMap<String, OffLineResult>();

            try {
                syntacticFrame = this.findSyntacticFrame(row);
                property = this.findProperty(row, syntacticFrame);
                String propertyFile = AddQuote.getProperty(this.propertyDir, property);
                 entityLabels = FileProcessUtils.getEntityLabels(propertyFile);

                templateQuestions = ProtoQuestionGeneration(syntacticFrame, property, row);

            } catch (Exception ex) {
                System.out.println(" problems in getting all information for question generation!!"+ex.getMessage());
            }

            /*if (syntacticFrame.contains(FrameType.AG.toString()) && sentenceTemplate.contains(superlative)) {
                sparqlQuery = findAdjektive(property);
            } else {
                sparqlQuery = findSparql(property, returnSubjOrObj);
            }*/
            List<UriLabel> bindingList = new ArrayList<UriLabel>();

            for (String template : templateQuestions.keySet()) {
                //logString+=template+"\n";
                Pair<String, GrammarInfor> pair = templateQuestions.get(template);
                String sparqlQuery = pair.first;
                GrammarInfor grammarInfor = pair.second;
                System.out.println("template::" + template + " GrammarInfor:" + grammarInfor);
                String sparqlQueryT = grammarInfor.getSparqlQuery();
                String bindingTypeT = grammarInfor.getBindingType();
                String returnTypeT = grammarInfor.getReturnType();
                String returnSubjOrObjT = grammarInfor.getReturnSubjOrObj();
                String[] questionsT = grammarInfor.getRealQuestions();
               

                bindingList = this.getOffLineBindingList(entityLabels, returnSubjOrObjT);

                rowIndex = this.questionGeneration(lexicalEntiryUri, sparqlQueryT, bindingList,
                        questionsT, rowIndex,
                        syntacticFrame, returnSubjOrObjT, QueryType.SELECT, returnTypeT, template);
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

                        if (this.online) {
                            if (answerUri != null) {
                                this.csvWriterQuestions.writeNext(newRecord);
                                System.out.println("index::" + index + " questionT::" + questionT + " sparql::" + sparql + " answerUri::" + answerUri + " answerLabel::" + answerLabel + " syntacticFrame:" + syntacticFrame);
                                rowIndex = rowIndex + 1;
                            } else {
                                continue;
                            }
                        } else {
                            //logString+="index::" + index + " questionT::" + questionT + " answerUri:" + answerUri + " answerLabel:" + answerLabel + " sparql:" + sparql;
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

    private Map<String, Pair<String, GrammarInfor>> ProtoQuestionGeneration(String syntacticFrame, String property, String[] row) {
        Map<String, Pair<String, GrammarInfor>> templateQuestions = new TreeMap<String, Pair<String, GrammarInfor>>();
        List<String> ways = new ArrayList<String>();
        ways.add(FORWARD);
        ways.add(BACKWARD);
        String returnSubjOrObj = null, bindingType = null, returnType = null;
        List<UriLabel> bindingList = new ArrayList<UriLabel>();

        try {
            returnSubjOrObj = findReturnSubjOrObj(row, syntacticFrame, property);
            bindingType = findBindingType(syntacticFrame, returnSubjOrObj, row);
            returnType = findReturnType(syntacticFrame, returnSubjOrObj, row);

        } catch (Exception ex) {
            List<String> wordList = Arrays.asList(row);
            System.out.println(property + wordList + " error!!!" + ex.getMessage() + " " + returnSubjOrObj);
        }

        SentenceTemplateFactoryEN_1 sentenceTemplateRepository = new SentenceTemplateFactoryEN_1();
        SentenceTemplateRepository sentenceTemplateRepositoryT = sentenceTemplateRepository.init();
        if (syntacticFrame.contains(NounPPFrame)) {
            List<String> sentenceTemplate = findSeneteneTemplate(row, returnSubjOrObj, syntacticFrame);
            for (String template : sentenceTemplate) {
                List<String> questions = sentenceTemplateRepositoryT.findOneByEntryTypeAndLanguageAndArguments(
                        SentenceType.SENTENCE, Language.EN, new String[]{syntacticFrame, template});
                if (!questions.isEmpty()) {
                    String templateAllQuestions = questions.iterator().next();
                    String[] realQuestions = findNounPPQuestions(bindingType, returnType, templateAllQuestions, row);
                    String sparqlQuery = findSparql(property, returnSubjOrObj);
                    GrammarInfor grammarInfor = new GrammarInfor(returnSubjOrObj, bindingType, returnType, template, realQuestions, sparqlQuery);
                    Pair<String, GrammarInfor> pair = new Pair<String, GrammarInfor>(template, grammarInfor);
                    templateQuestions.put(template, pair);
                }

            }
        }
        if (syntacticFrame.contains(TransitiveFrame)) {
            List<String> sentenceTemplates = findSeneteneTemplate(row, returnSubjOrObj, syntacticFrame);
            for (String genericTemplate : sentenceTemplates) {
                for (String way : ways) {
                    String senTemplate = null;
                    if (way.contains(FORWARD)) {
                        senTemplate = genericTemplate + "_" + FORWARD;
                    } else if (way.contains(BACKWARD)) {
                        senTemplate = genericTemplate + "_" + BACKWARD;
                        String[] result = this.makeReverse(returnSubjOrObj, returnType, bindingType);
                        returnSubjOrObj = result[0];
                        returnType = result[1];
                        bindingType = result[2];
                    }

                    List<String> questions = sentenceTemplateRepositoryT.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE, Language.EN, new String[]{syntacticFrame, senTemplate});
                    if (!questions.isEmpty()) {
                        String templateAllQuestions = questions.iterator().next();
                        String[] realQuestions = findVerbQuestions(syntacticFrame, bindingType, returnType, templateAllQuestions, row);
                        String sparqlQuery = findSparql(property, returnSubjOrObj);
                        GrammarInfor grammarInfor = new GrammarInfor(returnSubjOrObj, bindingType, returnType, genericTemplate, realQuestions, sparqlQuery);

                        Pair<String, GrammarInfor> pair = new Pair<String, GrammarInfor>(genericTemplate, grammarInfor);
                        templateQuestions.put(senTemplate, pair);
                    }

                }

            }
        }
        return templateQuestions;
    }

    private String findReturnSubjOrObj(String[] row, String syntacticFrame, String property) {
        String returnSubjOrObj = null;
        if (syntacticFrame.contains(NounPPFrame)) {
            if (row[6].contains(range)) {
                returnSubjOrObj = RETURN_TYPE_OBJECT;
            } else {
                returnSubjOrObj = RETURN_TYPE_SUBJECT;
            }
        } else if (syntacticFrame.contains(TransitiveFrame)) {
            if (row[7].contains(range)) {
                returnSubjOrObj = RETURN_TYPE_OBJECT;
            } else {
                returnSubjOrObj = RETURN_TYPE_SUBJECT;
            }
            System.out.println(property + " " + row[7] + " " + syntacticFrame + " " + returnSubjOrObj);

        } else if (syntacticFrame.contains(IntransitivePPFrame)) {
            if (row[8].contains(range)) {
                returnSubjOrObj = ReadWriteConstants.RETURN_TYPE_OBJECT;
            } else {
                returnSubjOrObj = ReadWriteConstants.RETURN_TYPE_SUBJECT;
            }
        }
        return returnSubjOrObj;
    }

    private String findBindingType(String syntacticFrame, String returnSubjOrObj, String[] row) {
        Integer nounPPFrameDomainIndex = 10;
        Integer transitiveFrameDomainIndex = 11;
        Integer inTransitiveFrameDomainIndex = 12;
        String bindingType = null;

        if (syntacticFrame.contains(NounPPFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                bindingType = row[nounPPFrameDomainIndex].split(":")[1];
            } else {
                bindingType = row[nounPPFrameDomainIndex + 1].split(":")[1];
            }

        } else if (syntacticFrame.contains(TransitiveFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                bindingType = row[transitiveFrameDomainIndex].split(":")[1];
            } else {
                bindingType = row[transitiveFrameDomainIndex + 1].split(":")[1];
            }

        } else if (syntacticFrame.contains(IntransitivePPFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                bindingType = row[inTransitiveFrameDomainIndex].split(":")[1];
            } else {
                bindingType = row[inTransitiveFrameDomainIndex + 1].split(":")[1];
            }

        }
        return bindingType;
    }

    private String findReturnType(String syntacticFrame, String returnSubjOrObj, String[] row) {
        Integer nounPPFrameDomainIndex = 10;
        Integer transitiveFrameDomainIndex = 11;
        Integer inTransitiveFrameDomainIndex = 12;
        String returnType = null;

        if (syntacticFrame.contains(NounPPFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                returnType = row[nounPPFrameDomainIndex + 1].split(":")[1];
            } else {
                returnType = row[nounPPFrameDomainIndex].split(":")[1];
            }

        } else if (syntacticFrame.contains(TransitiveFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                returnType = row[transitiveFrameDomainIndex + 1].split(":")[1];
            } else {
                returnType = row[transitiveFrameDomainIndex].split(":")[1];
            }

        } else if (syntacticFrame.contains(IntransitivePPFrame)) {
            if (returnSubjOrObj.contains(RETURN_TYPE_OBJECT)) {
                returnType = row[inTransitiveFrameDomainIndex + 1].split(":")[1];
            } else {
                returnType = row[inTransitiveFrameDomainIndex].split(":")[1];
            }

        }
        System.out.println("returnTYpe::" + returnType + " " + row[11] + " " + row[12]);
        return returnType;
    }

    private String[] makeReverse(String returnSubjOrObjT, String returnTypeT, String bindingTypeT) {
        String returnSubjOrObj = null, returnType, bindingType;
        if (returnSubjOrObjT.contains(RETURN_TYPE_OBJECT)) {
            returnSubjOrObj = RETURN_TYPE_SUBJECT;
        } else if (returnSubjOrObjT.contains(RETURN_TYPE_SUBJECT)) {
            returnSubjOrObj = RETURN_TYPE_OBJECT;
        }
        returnType = bindingTypeT;
        bindingType = returnTypeT;
        return new String[]{returnSubjOrObj, returnType, bindingType};
    }

    /*
     public Integer lemonEntryIndex = 0;
        public Integer partOfSpeechIndex = 1;
        public Integer writtenFormInfinitive = 2;
        public Integer writtenForm3rdPerson = 3;
        public Integer writtenFormPast = 4;
        public Integer writtenFormPerfect = 5;
        public Integer syntacticFrameIndex = 6;
        public Integer subjectIndex = 7;
        public Integer directObjectIndex = 8;
        public Integer senseIndex = 9;
        public Integer referenceIndex = 10;
        public Integer domainIndex = 11;
        public Integer rangeIndex = 12;
        public Integer passivePrepositionIndex = 13;
     */
    private List<String> findSeneteneTemplate(String[] row, String returnSubjOrObj, String syntacticFrame) {
        List<String> templates = new ArrayList<String>();
        if (syntacticFrame.contains(NounPPFrame)) {
            templates = SentenceTemplateFactoryEN_1.nounPPTemplates;
        } else if (syntacticFrame.contains(TransitiveFrame)) {
            String referUri = resourceUri(row[10]);
            String subjUri = resourceUri(row[11]);
            String objUri = resourceUri(row[12]);
            this.templateFinder = new TemplateFinder(FrameType.VP, referUri, subjUri, objUri);
            String template = this.templateFinder.getSelectedTemplate();
            templates.add(PERSON_CAUSE);

            /*if (returnSubjOrObj.contains(RETURN_TYPE_SUBJECT) && this.templateFinder.getSelectedTemplate().contains(PERSON_CAUSE)) {
                this.templateFinder.setSelectedTemplate(PERSON_CAUSE_SUBJECT);
            }*/
        }
        return templates;
    }

    private String findSparql(String property, String questionType) {
        String sparqlQuery = null;
        if (property.contains("dbo:")) {
            String[] info = property.split(":");
            property = info[1];
            sparqlQuery = "(bgp (triple ?subjOfProp " + "<http://dbpedia.org/ontology/" + property + ">" + " ?objOfProp))";
        } else if (property.contains("dbp:")) {
            String[] info = property.split(":");
            property = info[1];
            sparqlQuery = "(bgp (triple ?subjOfProp " + "<http://dbpedia.org/property/" + property + ">" + " ?objOfProp))";
        }
        return sparqlQuery;
    }

    private String findProperty(String[] row, String syntacticFrame) throws Exception {
        Integer nounPPIndex = 9;
        Integer transitiveIndex = 10;
        Integer InTransitiveIndex = 11;

        try {
            if (syntacticFrame.contains(NounPPFrame)) {
                return row[nounPPIndex];
            } else if (syntacticFrame.contains(TransitiveFrame)) {
                return row[transitiveIndex];
            } else if (syntacticFrame.contains(IntransitivePPFrame) || syntacticFrame.contains("InTransitivePPFrame")) {
                return row[InTransitiveIndex];
            } else {
                throw new Exception("No syntactic frame found!!");
            }
        } catch (Exception ex) {
            System.out.println("No syntactic frame found!!" + ex.getMessage());
        }
        throw new Exception("No syntactic frame found!!");
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

    private String[] findNounPPQuestions(String bindingType, String returnType, String templateAllQuestions, String[] row) {
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

    private String[] findVerbQuestions(String syntacticFrame, String bindingType, String returnType, String templateAllQuestions, String[] row) {
        bindingType = this.makeVairable(bindingType);
        List<String> singularPlural = findSingularPlural(returnType);
        String rangeSingular = "XX";
        String rangePlural = "XX";
        if (!singularPlural.isEmpty()) {
            rangeSingular = singularPlural.get(0);
            rangePlural = singularPlural.get(1);
        }

        String mainVerbPresent = row[2];
        String mainVerbPresentThird = row[3];
        String mainVerbPast = row[4];
        String mainVerbPerfekt = row[5];
        String preposition = "";
        if (syntacticFrame.contains(TransitiveFrame)) {
            preposition = "by";
        }
        if (syntacticFrame.contains(IntransitivePPFrame)) {
            preposition = row[6];
        }

        templateAllQuestions = templateAllQuestions.replace("Variable", bindingType);
        templateAllQuestions = templateAllQuestions.replace("(infinitive)", mainVerbPresent);
        templateAllQuestions = templateAllQuestions.replace("(3rd-present)", mainVerbPresentThird);
        templateAllQuestions = templateAllQuestions.replace("(past)", mainVerbPast);
        templateAllQuestions = templateAllQuestions.replace("(perfect)", mainVerbPerfekt);
        templateAllQuestions = templateAllQuestions.replace("preposition", preposition);
        templateAllQuestions = templateAllQuestions.replace("(range:singular)", rangeSingular);
        templateAllQuestions = templateAllQuestions.replace("(range:plural)", rangePlural);
        templateAllQuestions = templateAllQuestions.replace("(domain:singular)", rangeSingular);
        templateAllQuestions = templateAllQuestions.replace("(domain:plural)", rangePlural);

        String[] realQuestions = templateAllQuestions.split("\n");
        return realQuestions;
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

    private String resourceUri(String resource) {
        if (resource.contains("http:")) {
            return resource;
        } else if (resource.contains("dbo:")) {
            return resource.replace("dbo:", "http://dbpedia.org/ontology/");
        } else if (resource.contains("dbp:")) {
            return resource.replace("dbp:", "http://dbpedia.org/property/");
        }
        return resource;
    }

    /*
        nounPPFrame 
    
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
 /*
      transitive frame
      public Integer lemonEntryIndex = 0;
        public Integer partOfSpeechIndex = 1;
        public Integer writtenFormInfinitive = 2;
        public Integer writtenForm3rdPerson = 3;
        public Integer writtenFormPast = 4;
        public Integer writtenFormPerfect = 5;
        public Integer syntacticFrameIndex = 6;
        public Integer subjectIndex = 7;
        public Integer directObjectIndex = 8;
        public Integer senseIndex = 9;
        public Integer referenceIndex = 10;
        public Integer domainIndex = 11;
        public Integer rangeIndex = 12;
        public Integer passivePrepositionIndex = 13;
     */
 /*
     private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormInfinitive = 2;
        private Integer writtenForm3rdPerson = 3;
        private Integer writtenFormPast = 4;
        private Integer writtenFormPerfect = 5;
        private Integer preposition = 6;
        private Integer syntacticFrameIndex = 7;
        private Integer subject = 8;
        private Integer prepositionalAdjunct = 9;
        private Integer senseIndex = 10;
        private Integer referenceIndex = 11;
        private Integer domainIndex = 12;
        private Integer rangeIndex = 13;
        private Integer domainWrittenSingularFormIndex = rangeIndex + 1;
        private Integer domainWrittenPluralFormIndex = domainWrittenSingularFormIndex + 1;
        private Integer rangeWrittenSingularFormIndex = domainWrittenPluralFormIndex + 1;
        private Integer rangeWrittenPluralFormIndex = rangeWrittenSingularFormIndex + 1;
        private static String preposition_id;
     */
}
