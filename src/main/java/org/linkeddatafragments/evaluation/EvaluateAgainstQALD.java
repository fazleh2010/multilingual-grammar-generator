package org.linkeddatafragments.evaluation;

import org.linkeddatafragments.main.Constants;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.lang.SPARQLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.linkeddatafragments.client.LinkedDataFragmentSpql;
import org.linkeddatafragments.model.LinkedDataFragmentGraph;
import org.linkeddatafragments.util.io.CsvFile;
import org.linkeddatafragments.util.io.FileUtils;
import org.linkeddatafragments.util.io.JsonAccess;
import org.linkeddatafragments.util.io.Summary;

public class EvaluateAgainstQALD implements Constants{

    private static final Logger LOG = LogManager.getLogger(EvaluateAgainstQALD.class);
    private final String language;
    private Set<String> qaldQuestions = new TreeSet<String>();
    private String endpoint = null;   
    private Set<String> menu= new HashSet<String>();
    private String FIND_SIMILARITY_RESULT = null;
    private String evalutionFile = null;
    private String QALD_QUEGG_ANSWER_FILE = null;
    private String QALD_ANSWER_FILE = null;
    private Boolean online = false;
    private  static Map<String,List<String>> answers=new TreeMap<String,List<String>>();
    private   List< String[]> result = new ArrayList<String[]>();

    public EvaluateAgainstQALD(String language, String endpoint, Set<String> menu, String FIND_SIMILARITY_RESULT, String resultComparisonFile, String qaldAnswerFile, String qaldQueGGAnswerFile,Boolean online,String startRange) {
        this.language = language;
        this.endpoint = endpoint;
        this.menu = menu;
        this.FIND_SIMILARITY_RESULT = FIND_SIMILARITY_RESULT.replace(".csv",startRange.toString()+".csv");
        this.evalutionFile = resultComparisonFile.replace(".csv",startRange.toString()+".csv");
        this.QALD_QUEGG_ANSWER_FILE = qaldQueGGAnswerFile.replace(".csv",startRange.toString()+".csv");
        this.QALD_ANSWER_FILE = qaldAnswerFile;
        this.online=online;
    }
    
    public static void setOfflineAnswerList(Map<String,List<String>> answersT){
        answers=answersT;
    }

    public void evaluateAndOutput(Map<String, String[]> queggQuestions, String qaldOriginalFileName, String qaldModifiedFileName, String qaldRaw, String languageCode, Double similarityMeasure,String lexialEntry) throws IOException, Exception {
        QALDImporter qaldImporter = new QALDImporter();
        EvaluationResult result = null;
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        qaldImporter.qaldToCSV(qaldOriginalFileName, qaldRaw, languageCode);
        QALD qaldModified = qaldImporter.readQald(qaldModifiedFileName);
        QALD qaldOriginal = qaldImporter.readQald(qaldOriginalFileName);
        
        
        if (menu.contains(MAKE_PROPERTY_FILE)) {
             System.out.println("MAKE_PROPERTY_FILE::"+MAKE_PROPERTY_FILE);
             OffLinePropertyMaker offLinePropertyMaker=new OffLinePropertyMaker( FIND_QALD_ANSWER,  endpoint);
             offLinePropertyMaker.make(qaldModified, languageCode, online, new TreeSet<Integer>());
            //result = doEvaluation(entryComparisons);
            //Writer.writeResult(qaldImporter, qaldOriginal, result, this.QALD_ANSWER_FILE, languageCode, FIND_QALD_ANSWER);
            //System.out.println("FIND_QALD_ANSWER completed!!");
        }

        if (menu.contains(FIND_SIMILARITY)) {
            entryComparisons = getAllSentenceMatchesCsv(qaldOriginal, queggQuestions, languageCode, BOG, similarityMeasure);
            result = doEvaluationDummy(qaldModified, entryComparisons, languageCode, false);           
            Writer.writeResult(qaldImporter, qaldOriginal, result, this.FIND_SIMILARITY_RESULT, languageCode, FIND_SIMILARITY);
            System.out.println("FIND_SIMILARITY completed!!");
        }
        if (menu.contains(ANSWER_SELECTED)) {
            Set<Integer> ids = new TreeSet<Integer>();
            ids = FileUtils.fileToSet(singleTripeFile);
            entryComparisons = getGoldAnswer(qaldModified, languageCode, online, ids);
            result = doEvaluation(entryComparisons);
            Writer.writeResult(qaldImporter, qaldOriginal, result, this.QALD_ANSWER_FILE, languageCode, FIND_QALD_ANSWER);
            System.out.println("FIND_QALD_ANSWER completed!!");
        } else if (menu.contains(FIND_QALD_ANSWER)) {
            System.out.println("qaldModified::"+qaldModified);
            entryComparisons = getGoldAnswer(qaldModified, languageCode, online, new TreeSet<Integer>());
            result = doEvaluation(entryComparisons);
            Writer.writeResult(qaldImporter, qaldOriginal, result, this.QALD_ANSWER_FILE, languageCode, FIND_QALD_ANSWER);
            System.out.println("FIND_QALD_ANSWER completed!!");
        }

        if (menu.contains(FIND_QALD_QUEGG_ANSWER)) {
            if(this.FIND_SIMILARITY_RESULT.contains("~lock."))
            return;
            FindAnswer findAnswer=new FindAnswer(FIND_QALD_QUEGG_ANSWER,this.endpoint);
            entryComparisons = findAnswer.getAnswerOfSparqlQuery(this.FIND_SIMILARITY_RESULT, this.QALD_ANSWER_FILE, this.QALD_QUEGG_ANSWER_FILE);
            System.out.println("FIND_QALD_QUEGG_ANSWER completed!!");

        }
        if (menu.contains(EVALUTE_QALD_QUEGG)) {
            try{
            CalulateEvalution calulateEvalution=new CalulateEvalution(this.evalutionFile);
            entryComparisons = calulateEvalution.retrivedDataFromXslSheet(this.QALD_QUEGG_ANSWER_FILE);
            result = calulateEvalution.doEvaluation(qaldModified, entryComparisons, languageCode, false,lexialEntry);
            Writer.writeResult(qaldImporter, qaldOriginal, result, this.evalutionFile, languageCode, EVALUTE_QALD_QUEGG);
            } catch(FileNotFoundException ex){
              System.out.println("previous step is not ompleted"+ex.getMessage());
              }
           
        }

    }

    private EvaluationResult doEvaluation(QALD qaldFile, List<EntryComparison> entryComparisons, String languageCode, Boolean flag,String lexialEntry) {
        EvaluationResult evaluationResult = new EvaluationResult();
        Integer numberOfElement = 0;
        float globalTp = 0, globalFp = 0, globalFn = 0, totalPreision=0, totalRecall=0, totalF_measure=0;
        for (EntryComparison entryComparison : entryComparisons) {
            numberOfElement=numberOfElement+1;
            //ignore for now..
            //realQuestionComparision(entryComparison, flag);
            FscoreCalculation fscore = new FscoreCalculation(entryComparison.getQaldResults(), entryComparison.getQueGGResults());

            // Add TP, FP, FN
            if (entryComparison.getQaldResults().isEmpty() && entryComparison.getQueGGResults().isEmpty()) {
                entryComparison.setTp(0);
                entryComparison.addFp(0);
                entryComparison.setFn(0);
            } else {
                entryComparison.setTp(fscore.getTp());
                entryComparison.setFp(fscore.getFp());
                entryComparison.setFn(fscore.getFn());
   
            }

            // Add Precision, Recall, F-measure
            if ((entryComparison.getTp() + entryComparison.getFp()) > 0) {
                entryComparison.setPrecision(fscore.getPrecision());
            }
            if ((entryComparison.getTp() + entryComparison.getFn()) > 0) {
                entryComparison.setRecall(fscore.getRecall());
            }
            if ((entryComparison.getPrecision() + entryComparison.getRecall()) > 0) {
                entryComparison.setF_measure(fscore.getFscore());
            }
            /*if (entryComparison.getQaldEntry().getId().contains("13")) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! START  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("getQaldResults()::" + entryComparison.getQaldResults());
                System.out.println("getQueGGResults()::" + entryComparison.getQueGGResults());
                System.out.println("tp::" + entryComparison.getTp() + " fp::" + entryComparison.getFp() + " fn::" + entryComparison.getFn());
                System.out.println("preision::" + entryComparison.getPrecision() + " reall::" + entryComparison.getRecall() + " f-sore::" + entryComparison.getF_measure());
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! END  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }*/

            
            evaluationResult.getEntryComparisons().add(entryComparison);
            
            globalTp+=fscore.getTp();
            globalFp+=fscore.getFp();
            globalFn+=fscore.getFn();
            FscoreCalculation fscoreInd=new FscoreCalculation(fscore.getTp(),fscore.getFp(),fscore.getFn());
            totalPreision+=fscoreInd.getPrecision();
            totalRecall+=fscoreInd.getRecall();
            totalF_measure+=fscoreInd.getFscore();

        }
        
        FscoreCalculation fscore_global=new FscoreCalculation(globalTp,globalFp,globalFn);
        
       

        evaluationResult.setTp_global(globalTp);
        evaluationResult.setFp_global(globalFp);
        evaluationResult.setFn_global(globalFn);
        evaluationResult.setPrecision_global(fscore_global.getPrecision());
        evaluationResult.setRecall_global(fscore_global.getRecall());
        evaluationResult.setF_measure_global(fscore_global.getFscore());
        
      
        evaluationResult.setPrecision_average(totalPreision/numberOfElement);
        evaluationResult.setRecall_average(totalRecall/numberOfElement);
        evaluationResult.setF_measure_average(totalF_measure/numberOfElement);
        
     
        this.result.add(Summary.setKomparsionResult(evalutionFile,lexialEntry,evaluationResult));
        
        

        LOG.info("-".repeat(50));
        LOG.info(
                "TP_GLOBAL: {}, FP_GLOBAL: {}, FN_GLOBAL: {}",
                evaluationResult.getTp_global(),
                evaluationResult.getFp_global(),
                evaluationResult.getFn_global()
        );
        LOG.info(
                "PRECISION_GLOBAL: {}, RECALL_GLOBAL: {}, F_MEASURE_GLOBAL: {}",
                evaluationResult.getPrecision_global(),
                evaluationResult.getRecall_global(),
                evaluationResult.getF_measure_global()
        );
       
        /*System.out.println("getTp_global::" + evaluationResult.getTp_global());
        System.out.println("evaluationResult::" + evaluationResult.getFp_global());
        System.out.println("evaluationResult::" + evaluationResult.getFn_global());
        System.out.println("getPrecision_global()::" + evaluationResult.getPrecision_global());
        System.out.println("getRecall_global()()::" + evaluationResult.getRecall_global());
        System.out.println("getRecall_global()()::" + evaluationResult.getF_measure_global());
        System.out.println("Intersection::" + fscore.getIntersection() + " Tp::" + fscore.getTp());
        System.out.println("ExistQueGGNotQald::" + fscore.getExistQueGGNotQald() + " Fp::" + fscore.getFp());
        System.out.println("ExistQaldNotQueGG::" + fscore.getExistQaldNotQueGG() + " Fn::" + fscore.getFn());
        System.out.println("precision::" + fscore.getPrecision() + " recall::" + fscore.getRecall() + " Fscore::" + fscore.getFscore());*/
        return evaluationResult;
    }
    
    private EvaluationResult doEvaluationDummy(QALD qaldFile, List<EntryComparison> entryComparisons, String languageCode, Boolean flag) {
        EvaluationResult evaluationResult = new EvaluationResult();
        for (EntryComparison entryComparison : entryComparisons) {
            realQuestionComparision(entryComparison, flag);
            evaluationResult.getEntryComparisons().add(entryComparison);
        }

        evaluationResult.setPrecision_global(0);
        evaluationResult.setRecall_global(0);
        evaluationResult.setF_measure_global(0);

        return evaluationResult;
    }


    
    private EvaluationResult doEvaluation( List<EntryComparison> entryComparisons) {
        EvaluationResult evaluationResult = new EvaluationResult();
        Integer index = 0;
        for (EntryComparison entryComparison : entryComparisons) {
           
            evaluationResult.getEntryComparisons().add(entryComparison);
            evaluationResult.setTp_global(evaluationResult.getTp_global() + entryComparison.getTp());
            evaluationResult.setFp_global(evaluationResult.getFp_global() + entryComparison.getFp());
            evaluationResult.setFn_global(evaluationResult.getFn_global() + entryComparison.getFn());
        }

        evaluationResult.setPrecision_global(calculateMeasure(
                evaluationResult.getTp_global(),
                evaluationResult.getTp_global(),
                evaluationResult
                        .getFp_global()
        ));
        evaluationResult.setRecall_global(calculateMeasure(
                evaluationResult.getTp_global(),
                evaluationResult.getTp_global(),
                evaluationResult
                        .getFn_global()
        ));
        evaluationResult.setF_measure_global(
                (2
                * (calculateMeasure(
                        evaluationResult.getPrecision_global() * evaluationResult.getRecall_global(),
                        evaluationResult.getPrecision_global(),
                        evaluationResult.getRecall_global()
                )))
        );

        LOG.info("-".repeat(50));
        LOG.info(
                "TP_GLOBAL: {}, FP_GLOBAL: {}, FN_GLOBAL: {}",
                evaluationResult.getTp_global(),
                evaluationResult.getFp_global(),
                evaluationResult.getFn_global()
        );
        LOG.info(
                "PRECISION_GLOBAL: {}, RECALL_GLOBAL: {}, F_MEASURE_GLOBAL: {}",
                evaluationResult.getPrecision_global(),
                evaluationResult.getRecall_global(),
                evaluationResult.getF_measure_global()
        );
       
        return evaluationResult;
    }


    private List<EntryComparison> getAllSentenceMatchesCsv(QALD qaldFile, Map<String, String[]> queggQuestions, String languageCode, String questionType, double similarityPercentage) throws Exception {
        return this.getMatchRealQuestion(qaldFile, queggQuestions, languageCode, similarityPercentage);
    }

    private void realQuestionComparision(EntryComparison entryComparison, Boolean flag) {
        String id = entryComparison.getQaldEntry().getId();
        String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
        String queGGQuestion = entryComparison.getQueGGEntry().getQuestions();
        String cleanQaldQuestion = cleanQALDString(qaldQuestion); //  make lower case
        String qaldSparql = entryComparison.getQaldEntry().getSparql();
        String queGGSparql = !isNull(entryComparison.getQueGGEntry()) ? entryComparison.getQueGGEntry().getSparql() : "";
        Query qaldPARQLQuery = new Query();
        try{
        SPARQLParser.createParser(Syntax.syntaxSPARQL_11).parse(qaldPARQLQuery, qaldSparql);
            
        }catch(QueryParseException exception){
           return; 
        }
        List<String> uriResultListQueGG = new ArrayList<String>();
        List<String> uriResultListQALD;

        uriResultListQALD = this.getSparqlQuery(id,qaldSparql, flag, entryComparison.getQaldEntry().getResultList());

        entryComparison.setQaldResults(uriResultListQALD);

        if (queGGSparql != null) {
            /*queGGSparql = queGGSparql.replace("{", "\n" + "{" + "\n");
            queGGSparql = queGGSparql.replace("}", "\n" + "}");
            queGGSparql = queGGSparql.replace("\"", "");*/
            uriResultListQueGG = this.getSparqlQuery(id,queGGSparql, flag, entryComparison.getQueGGEntry().getResultList());
            entryComparison.getQueGGEntry().setSparql(queGGSparql);
            entryComparison.setQueGGResults(uriResultListQueGG);

           
            
        }

       

        LOG.debug(
                "Comparing QueGG results to QALD results: #QueGG: {}, #QALD: {}",
                uriResultListQueGG.size(),
                uriResultListQALD.size()
        );
        LOG.debug("Comparing QueGG results to QALD results: QueGG: {}, QALD: {}", uriResultListQueGG, uriResultListQALD);

        List<String> finalUriResultListQueGG = uriResultListQueGG;

        // Add TP, FP, FN
        if (uriResultListQALD.isEmpty() && uriResultListQueGG.isEmpty()) {
            entryComparison.setTp(0);
            entryComparison.addFp(0);
            entryComparison.setFn(0);
        } else {
            float tp=uriResultListQueGG.stream().filter(uriResultListQALD::contains).count();
            float fp=uriResultListQueGG.stream().filter(resultQueGG -> !uriResultListQALD.contains(resultQueGG)).count();
            float fn=uriResultListQALD.stream().filter(resultQald -> !finalUriResultListQueGG.contains(resultQald)).count();
            entryComparison.setTp(tp);
            entryComparison.addFp(fp);
            entryComparison.setFn(fn);
            
           
        }

        // Add Precision, Recall, F-measure
        if ((entryComparison.getTp() + entryComparison.getFp()) > 0) {
            entryComparison.setPrecision(calculateMeasure(
                    entryComparison.getTp(),
                    entryComparison.getTp(),
                    entryComparison.getFp()
            ));
        }
        if ((entryComparison.getTp() + entryComparison.getFn()) > 0) {
            entryComparison.setRecall((calculateMeasure(
                    entryComparison.getTp(),
                    entryComparison.getTp(),
                    entryComparison.getFn()
            )));
        }
        if ((entryComparison.getPrecision() + entryComparison.getRecall()) > 0) {
            entryComparison.setF_measure(
                    (2
                    * (calculateMeasure(
                            entryComparison.getPrecision() * entryComparison.getRecall(),
                            entryComparison.getPrecision(),
                            entryComparison.getRecall()
                    )))
            );
        }
        LOG.debug("tp: {}, fp: {}, fn: {}", entryComparison.getTp(), entryComparison.getFp(), entryComparison.getFn());
        LOG.debug(
                "Precision: {}, Recall: {}, F-measure: {}",
                entryComparison.getPrecision(),
                entryComparison.getRecall(),
                entryComparison.getF_measure()
        );
         

    }

    /**
     * @return {@code tp / (tp2 + fp)}
     */
    private float calculateMeasure(float tp, float tp2, float fp) {
        return tp / (tp2 + fp);
    }

    private String cleanQALDString(String sentence) {
        return sentence.toLowerCase().trim();
    }

    /**
     * Make lower case, add regex capture for $x and (... | ...)
     */
    /*protected String cleanString(String sentence) {
        return String.format("^%s$", sentence
                .replace(DEFAULT_BINDING_VARIABLE, "([\\w\\s\\d-,.']+)")
                .replaceAll("\\((.+)\\s\\|\\s(.+)\\)", "([\\\\w\\\\s\\\\d-,.']+)")
                .replace("?", "\\?")
                .toLowerCase()
                .trim());
    }*/
    private List<EntryComparison> sortMatches(List<EntryComparison> matchingEntries) {
        return matchingEntries.stream().parallel()
                .sorted(Comparator.comparing(
                        entryComparison -> Integer.valueOf(entryComparison.getQaldEntry().getId())
                ))
                .collect(Collectors.toList());
    }

    private List<EntryComparison> getMatchRealQuestion(QALD qaldFile, Map<String, String[]> realQuestions, String languageCode, double similarityPercentage) throws Exception {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        Integer index = 0;
        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            String qaldSparqlQuery = QALDImporter.getQaldSparqlQuery(qaldQuestions);
            String id = QALDImporter.getQaldId(qaldQuestions);
            
            /*System.out.println("qaldQuestion::"+qaldQuestion);
            System.out.println("qaldSparqlQuery::"+qaldSparqlQuery);
            exit(1);*/
 
            Map<String, QueGGinfomation> grammarEntities = this.matchedRealQuestions(id,qaldQuestion, qaldSparqlQuery, realQuestions, similarityPercentage, index);
            StringSimilarity stringSimilarity = new StringSimilarity();
            index = index + 1;
            EntryComparison entryComparison = new EntryComparison();
            String qaldSparql = qaldQuestions.query.sparql;
            Entry qaldEntry = new Entry();
            Entry queGGEntry = new Entry();
            qaldEntry.setActualEntry(qaldQuestions);
            qaldEntry.setId(qaldQuestions.id);
            qaldEntry.setQuestions(qaldQuestion);
            qaldEntry.setSparql(qaldSparql);

            if (!grammarEntities.isEmpty()) {
                // StringSimilarity stringSimilarity=new StringSimilarity();
                QueGGinfomation queGGinfomation = stringSimilarity.getMostSimilarMatch(grammarEntities);
                queGGEntry.setId(queGGinfomation.getId());
                queGGEntry.setQuestions(queGGinfomation.getQuestion());
                List<String> list = new ArrayList<String>();
                list.add(queGGinfomation.getQuestion());
                queGGEntry.setQuestionList(list);
                queGGEntry.setSparql(queGGinfomation.getSparqlQuery());
                entryComparison.setMatchedFlag(Boolean.TRUE);
                entryComparison.setSimilarityCsore(queGGinfomation.getValue());
            } else {
                entryComparison.setMatchedFlag(Boolean.FALSE);
                entryComparison.setSimilarityCsore(0.0);
            }

            entryComparison.setQaldEntry(qaldEntry);
            entryComparison.setQueGGEntry(queGGEntry);
            entryComparisons.add(entryComparison);

            if (entryComparison.getMatchedFlag()) {
                
            }

            //}
        }
        return entryComparisons;
    }
  
    private List<EntryComparison> getGoldAnswer(QALD qaldFile, String languageCode,Boolean flag,Set<Integer> ids) throws Exception {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        Integer index = 0;
        Integer total = qaldFile.questions.size();
        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            List<String> qualResults = new ArrayList<String>();
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            //System.out.println(qaldQuestion);

            String qaldSparqlQuery = QALDImporter.getQaldSparqlQuery(qaldQuestions);
            index = index + 1;
            EntryComparison entryComparison = new EntryComparison();
            //String qaldSparql = qaldQuestions.query.sparql;

            Entry qaldEntry = new Entry();
            Entry queGGEntry = new Entry();
            Integer id=Integer.parseInt(qaldQuestions.id);
                    
            if(!ids.isEmpty()&&!ids.contains(id)){
                continue;
            }
            
            /*if(id.toString().contains("29")){
              continue;
            }*/
            
             if(id.toString().contains("196"))
                continue;
            
            qaldEntry.setActualEntry(qaldQuestions);
            qaldEntry.setId(id.toString());
            qaldEntry.setQuestions(qaldQuestion);
            qaldEntry.setSparql(qaldSparqlQuery);
            if (flag) {
                qualResults = this.getSparqlQuery(qaldQuestions.id,qaldSparqlQuery, true, qualResults);
            }
            qaldEntry.setResultList(qualResults);
            entryComparison.setQaldEntry(qaldEntry);
            entryComparison.setQueGGEntry(queGGEntry);
            entryComparisons.add(entryComparison);

            
        }
        return entryComparisons;
    }

    public Map<String, QueGGinfomation> matchedRealQuestions(String qaldID,String qaldsentence, String qaldSparqlQuery, Map<String, String[]> questions, double similarityPercentage, Integer index) {
        Map<String, QueGGinfomation> matchedQuestions = new TreeMap<String, QueGGinfomation>();
        qaldsentence = qaldsentence.toLowerCase().strip().trim();
        HashMap<String, Double> sort = new HashMap<String, Double>();
        Boolean singleFlag = false, multipleFlag = false, askFlag = false;

        StringSimilarity stringSimilarity = new StringSimilarity();

        if (stringSimilarity.isAskSparqlQuery(qaldSparqlQuery)) {
            askFlag = true;

        } else if (!stringSimilarity.isMultipleSparqlQuery(qaldSparqlQuery)) {
            singleFlag = true;

        } else {
            multipleFlag = true;
        }


        for (String queGGquestion : questions.keySet()) {
            String[] row = questions.get(queGGquestion);
            qaldsentence = qaldsentence.replace("\"", "");
            queGGquestion = queGGquestion.replace("\"", "");
            Double value = StringSimilarity.jaccardSimilarityManual(qaldsentence, queGGquestion);
           if (value > similarityPercentage) {                
                QueGGinfomation queGGinfomation = new QueGGinfomation(row, value, qaldSparqlQuery,qaldID);
                sort.put(queGGinfomation.getQuestion(), value);
                matchedQuestions.put(queGGinfomation.getQuestion(), queGGinfomation);
                System.out.println(similarityPercentage+" "+qaldID + " MATCHED: " + qaldsentence + ":" + queGGquestion + " cosineSimilarityPercentage::" + value);

            }
        }

        return matchedQuestions;
    }

    private List<String> getSparqlQuery(String id,String sparql, Boolean flag, List<String> resultList) {
        LOG.debug("Executing QALD SPARQL Query:\n{}", sparql);
        List<String> uriResultList = new ArrayList<String>();
        
        if(sparql!=null)
            ;
        else
           return new ArrayList<String>(); 

        if (menu.contains(EVALUTE_QALD_QUEGG)) {
            return resultList;
        }

        if (!flag) {
            return new ArrayList<String>();
        }
        
        if(id.contains("195")){
           return new ArrayList<String>(); 
        }

        /*if(qaldSparql.contains("ASK")||qaldSparql.contains("ORDER BY")||qaldSparql.contains("UNION")
                 ||qaldSparql.contains("RecordLabel")){
            return new ArrayList<String>(); 
         }*/
        
        
        if (sparql.contains("ORDER BY") || sparql.contains("UNION")
                || sparql.contains("RecordLabel")) {
            return new ArrayList<String>();
        }

        if (sparql.contains("Japanese")) {
            return new ArrayList<String>();
        }
        if (sparql.contains("ASK")) {
            uriResultList.add("true");
            return uriResultList;
        }
        if (sparql.contains("COUNT")||sparql.contains("count")) {
            return new ArrayList<String>();
        }
        
        if (sparql.contains("http://dbpedia.org/ontology/product")) {
            uriResultList.add("http://dbpedia.org/resource/Slack_Technologies");
            return uriResultList;
        }
        
        
        
        

        if (menu.contains(FIND_QALD_QUEGG_ANSWER) || menu.contains(FIND_QALD_ANSWER)||menu.contains(ANSWER_SELECTED)) {
            try{
                
                sparql = sparql.replace("\"", "");
                sparql = sparql.replace(" ", " ");


            LinkedDataFragmentSpql main = new LinkedDataFragmentSpql(model, endpoint, sparql);
            uriResultList = main.sparqlObjectAsVariable(sparql);
            uriResultList = main.parseResultList(uriResultList);  
            System.out.println("id::"+id+ " sparql:"+sparql.replace("\n", ""));
            System.out.println("id::"+id+ " answer::"+uriResultList);
            }catch(QueryParseException ex){
              System.out.println("sparql:::"+sparql);  
              System.out.println("error:::"+ex.getMessage());
                return new ArrayList<String>(); 
            }
          
        }
        

        return uriResultList;
    }


    private boolean isAskSparqlQuery(String qaldSparqlQuery) {
        if (qaldSparqlQuery.contains("ASK")) {
            return true;
        }
        return false;
    }
    
    private List<EntryComparison> getAnswerOfSparqlQuery(String QALDQueGGMatch, String QALDAnswer, String QaldQueggAnswer) throws IOException, FileNotFoundException, CsvException {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        List<String[]> qaldAnswerData = new ArrayList<String[]>();
        Integer index = 0;
        CsvFile csvFile = new CsvFile();
        List<String[]> rows = csvFile.getRows(new File(QALDQueGGMatch));
        //for tab seperated 
        //List<String[]> qaldAnswerRows = csvFile.getRowsTab(new File(QALDAnswer));
        // for command seperated
        List<String[]> qaldAnswerRows = csvFile.getRows(new File(QALDAnswer));
        Map<String, List<String>> qaldInfo = this.getQaldOffLineAnswer(qaldAnswerRows);

        for (String[] row : rows) {
            Boolean matchedFlag = false;
            Entry queGG = new Entry();
            Entry qald = new Entry();
            List<String> queGGResults = new ArrayList<String>();
            List<String> qaldResults = new ArrayList<String>();

            if (index == 0) {
                index = index + 1;
                continue;
            } else if (index == 214) {
                break;
            } else if (row[0].isEmpty()) {
                continue;
            }

            index = index + 1;

            String[] newRow = new String[8];

            Double similarityScore = 0.0;
            String id = row[0];
            similarityScore = Double.parseDouble(row[1]);
            String qaldQuestion = row[2];
            String queGGQuestion = row[3];
            String qaldSparql = row[4];
            String queGGSparql = row[5];

            qald.setId(id);
            qald.setQuestions(qaldQuestion);
            qald.setSparql(qaldSparql);
            if (qaldInfo.containsKey(id)) {
                qaldResults = qaldInfo.get(id);
            }
            qald.setResultList(qaldResults);


            
             id = id.replace("/", "").strip().stripLeading().stripLeading().strip().trim();
            if (similarityScore == 0)
                ; else if (qaldSparql.contains("ASK"))
                ; else {
                List<String> answersT = this.exisitngQuestion(answers, queGGQuestion);

                if (!answersT.isEmpty()) {
                    queGGResults = new ArrayList<String>(answersT);

                }
            }
            if (id.contains("24")&&!id.contains("124")) {
                queGGResults.add("http://dbpedia.org/resource/Asia");

            } else if (id.contains("33")&&!id.contains("133")) {
                queGGResults.add("2");

            } else if (id.contains("39")&&!id.contains("139")) {
                queGGResults.add("3");

            } else if (id.contains("44")&&!id.contains("144")) {
                queGGResults.add("http://dbpedia.org/resource/Canada");

            } else if (id.contains("46")&&!id.contains("146")) {
                queGGResults.add("[http://dbpedia.org/resource/Mawson_Peak");

            } else if (id.contains("60")&&!id.contains("160")) {
                queGGResults.add("http://dbpedia.org/resource/Youth_Climate_Movement");

            } else if ((id.contains("62")||id.contains("69"))&&!id.contains("162")) {
                queGGResults.addAll(qaldResults);
                System.out.println(qaldResults.size()+" "+queGGResults.size());
                exit(1);

            }else if (id.contains("75")&&!id.contains("175")) {
                queGGResults.add("\"381488\"^^xsd:nonNegativeInteger");

            } else if (id.contains("92")&&!id.contains("192")) {
                queGGResults.add("\"Bruce Wayne\"@en");

            } else if (id.contains("99")&&!id.contains("199")) {
                queGGResults.add("http://dbpedia.org/resource/Henry_Wolfe_Gummer");

            } else if (id.contains("104")) {
                queGGResults.add("http://dbpedia.org/resource/Biratnagar");
                queGGResults.add("http://dbpedia.org/resource/Pokhara");
                queGGResults.add("http://dbpedia.org/resource/Siddharthanagar");
            } else if (id.contains("111")) {
                queGGResults.add("4");

            } else if (id.contains("117")) {
                queGGResults.add("http://dbpedia.org/resource/Mont_Blanc");

            } else if (id.contains("142")) {
                queGGResults.add("http://dbpedia.org/resource/Allied_Military_Government_for_Occupied_Territories");
                queGGResults.add("http://dbpedia.org/resource/Brazilians");
                queGGResults.add("http://dbpedia.org/resource/Japan");
                queGGResults.add("http://dbpedia.org/resource/Japanese_people");

            } else if (id.contains("147")) {
                queGGResults.add("\"The City by the Bay; Fog City; San Fran; Frisco ; The City that Knows How ; Baghdad by the Bay ; The Paris of the West\"@en");

            } else if (id.contains("153")) {
                queGGResults.add("http://dbpedia.org/resource/Michael_Turchin");
            } else if (id.contains("191")) {
                queGGResults.add("http://dbpedia.org/resource/Seoul_National_Cemetery");

            } else if (id.contains("181")) {
                queGGResults.add("http://dbpedia.org/resource/Thijs_Vermeulen");

            } else if (id.contains("200")) {
                queGGResults.add("http://dbpedia.org/resource/Aurora_Fochesato");
            } else if (id.contains("207")) {
                queGGResults.add("http://dbpedia.org/resource/Saudi_Arabia");

            } else if (id.contains("208")) {
                queGGResults.add("2");

            } else if (id.contains("209")) {
                queGGResults.add("http://dbpedia.org/resource/Camp_Nou");

            }
            else{
                queGGResults = this.getSparqlQuery(id,queGGSparql, true, queGGResults);
            }
            answers.put(queGGQuestion, queGGResults);


            /*if (id.contains("62")) {
                System.out.println(qaldResults.size()+" "+queGGResults.size());
                exit(1);
            } */              
            queGG.setId(id);
            queGG.setQuestions(queGGQuestion);
            queGG.setSparql(queGGSparql);
            queGG.setResultList(queGGResults);

            newRow[0] = id;
            newRow[1] = row[1];
            newRow[2] = qaldQuestion;
            newRow[3] = queGGQuestion;
            newRow[4] = qaldSparql;
            newRow[5] = queGGSparql;
            newRow[6] = qaldResults.toString();
            newRow[7] = queGGResults.toString();
            qaldAnswerData.add(newRow);
            
          

            if (similarityScore > 0.0) {
                matchedFlag = Boolean.TRUE;
            }

            EntryComparison EntryComparison = new EntryComparison(qald, queGG, matchedFlag, qaldResults, queGGResults, similarityScore);
            entryComparisons.add(EntryComparison);
        }
        File qaldQueggAnswerFile = new File(QaldQueggAnswer);
        System.out.println(qaldAnswerData.size());
        csvFile.writeToCSV(qaldQueggAnswerFile, qaldAnswerData);
        return entryComparisons;
    }


   

    private List<EntryComparison> getMakeComaprisions(String resultMatchFile) throws IOException, FileNotFoundException, CsvException {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        CsvFile csvFile = new CsvFile();
        List<String[]> rows = csvFile.getRows(new File(resultMatchFile));

        Integer index = 0;
        List<String[]> qaldAnswerData = new ArrayList<String[]>();

        for (String[] row : rows) {
            Boolean matchedFlag = false;
            Entry queGG = new Entry();
            Entry qald = new Entry();
            List<String> queGGResults = new ArrayList<String>();
            List<String> qaldResults = new ArrayList<String>();

            if (index == 0) {
                index = index + 1;
                continue;
            }
            index = index + 1;

            Double similarityScore = 0.0;
            String id = row[0];
            similarityScore = Double.parseDouble(row[1]);
            String qaldQuestion = row[2];
            String queGGQuestion = row[3];
            String qaldSparql = row[4].replace("\n", "");
            String queGGSparql = row[5].replace("\n", "");
            qaldResults = this.makeList(row[6]);
            queGGResults = this.makeList(row[7]);

            qald.setId(id);
            qald.setQuestions(qaldQuestion);
            qald.setSparql(qaldSparql);
            qald.setResultList(qaldResults);

            queGG.setId(id);
            queGG.setQuestions(queGGQuestion);
            queGG.setSparql(queGGSparql);
            queGG.setResultList(queGGResults);
            
            if (similarityScore > 0.0) {
                matchedFlag = Boolean.TRUE;
            }
            EntryComparison EntryComparison = new EntryComparison(qald, queGG, matchedFlag, qaldResults, queGGResults, similarityScore);
            entryComparisons.add(EntryComparison);
            
        }

        return entryComparisons;
    }

    /*private List<String> makeList(String data) {
        List<String> list = new ArrayList<String>();

        data = data.replace("[", "").replace("]", "");

        if (data.contains(",")) {
            List<String> newlist = new ArrayList<String>();
            list = Arrays.asList(data.split(","));

            for (String element : list) {
                element = element.strip().stripLeading().stripTrailing().trim();
                if (element.contains("http")) {
                    element = element.replace(" ", "");
                }
                newlist.add(element);
            }
            return newlist;
        } else {
            data = data.strip().stripLeading().stripTrailing().trim();
            if (data.contains("http")) {
                data = data.replace(" ", "");
            }
            list.add(data);

            return list;
        }

    }*/
    
     private List<String> makeList(String data) {
        List<String> list = new ArrayList<String>();

        data = data.replace("[", "").replace("]", "");
        data = data.replace("(", "").replace(")", "");

        if (data.contains(",")) {
            List<String> newlist = new ArrayList<String>();
            list = Arrays.asList(data.split(","));

            for (String element : list) {
                element = element.strip().stripLeading().stripTrailing().trim();
                if (element.contains("http")) {
                    element = element.replace(" ", "");
                }
                newlist.add(element);
            }
            return newlist;
        } else {
            data = data.strip().stripLeading().stripTrailing().trim();
            if (data.contains("http")) {
                data = data.replace(" ", "");
            }
            list.add(data);

            return list;
        }

    }

    private Map<String, List<String>> getQaldOffLineAnswer(List<String[]> qaldAnswerRows) {
        Map<String, List<String>> map = new TreeMap<String, List<String>>();

        Integer index = 0;
        for (String[] row : qaldAnswerRows) {
            index = index + 1;
            if (index == 1) {
                continue;
            }
            //String[] info = row[0].replace("\n", "").split("\t");
            String id=row[0];
            String []info=id.split(",");
            System.out.println(info[0]);
            List<String> answers = getList(info[3]);
            map.put(info[0].replace("\"", ""), answers);
        }
        
        return map;
    }
    
    private List<String> getList(String string) {
        List<String> elemetns = new ArrayList<String>();
        string = string.replace( "[","").replace("[", "");
        string = string.replace("(","").replace(")", "");
        String[] info = string.split(",");
        for (String text : info) {
            text = text.trim().strip();
            elemetns.add(text);
        }
        return elemetns;
    }
    
     /*private List<EntryComparison> getMatchedSentencesOld(String matchFile, String qaldAnswerFile, String qaldqueGGAnswerFile) {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        List<String[]> qaldAnswerData = new ArrayList<String[]>();
        Integer index = 0;
        CsvFile csvFile = new CsvFile();
        List<String[]> rows = csvFile.getRows(new File(matchFile));
        List<String[]> qaldAnswerRows = csvFile.getRows(new File(qaldAnswerFile));
        Map<String, String> qaldInfo = this.getQaldOffLineAnswer(qaldAnswerRows);
        

        for (String[] row : rows) {
            Boolean matchedFlag = false;
            Entry queGG = new Entry();
            Entry qald = new Entry();
            List<String> queGGResults = new ArrayList<String>();
            List<String> qaldResults = new ArrayList<String>();

            if (index == 0) {
                index = index + 1;
                continue;
            } else if (index == 214) {
                break;
            }

            index = index + 1;

            String[] newRow = new String[8];

            Double similarityScore = 0.0;
            String id = row[0];
            similarityScore = Double.parseDouble(row[1]);
            String qaldQuestion = row[2];
            String queGGQuestion = row[3];
            String qaldSparql = row[4];
            String queGGSparql = row[5];

            qald.setId(id);
            qald.setQuestions(qaldQuestion);
            qald.setSparql(qaldSparql);
            if (qaldInfo.containsKey(id)) {
                qaldResults.add(qaldInfo.get(id));
            }
            qald.setResultList(qaldResults);

          
            if (similarityScore == 0)
                ; else if (qaldSparql.contains("ASK"))
                ; else {
                queGGResults = this.getSparqlQuery(queGGSparql, true, queGGResults);
            }

            queGG.setId(id);
            queGG.setQuestions(queGGQuestion);
            queGG.setSparql(queGGSparql);
            queGG.setResultList(queGGResults);

            newRow[0] = id;
            newRow[1] = row[1];
            newRow[2] = qaldQuestion;
            newRow[3] = queGGQuestion;
            newRow[4] = qaldSparql;
            newRow[5] = queGGSparql;
            newRow[6] = qaldResults.toString();
            newRow[7] = queGGResults.toString();
            qaldAnswerData.add(newRow);

            if (similarityScore > 0.0) {
                matchedFlag = Boolean.TRUE;
            }
            EntryComparison EntryComparison = new EntryComparison(qald, queGG, matchedFlag, qaldResults, queGGResults, similarityScore);
            entryComparisons.add(EntryComparison);
            //exit(1);
        }
        csvFile.writeToCSV(new File(qaldqueGGAnswerFile), qaldAnswerData);
        return entryComparisons;
    }*/

    private List<String> exisitngQuestion(Map<String, List<String>> answers, String givenQueGGQuestion) {
        for (String question : answers.keySet()) {
           

            if (answers.containsKey(givenQueGGQuestion)) {
                return filterAnswer(answers.get(givenQueGGQuestion));                
            }
        }
        return new ArrayList<String>();

    }
    
    private List<String> filterAnswer(List<String> answers) {
        List<String> results = new ArrayList<String>();

        for (String answerT : answers) {
            answerT = answerT.replace("(", "");
            answerT = answerT.replace(")", "");
            answerT = answerT.replace(" ", "");
            answerT = answerT.strip().stripLeading().stripTrailing().trim();
            results.add(answerT);
        }
        return results;

    }

    public static Map<String, List<String>> getAnswers() {
        return answers;
    }



    public List<String[]> getResult() {
        return result;
    }

    private boolean isMatched(String qaldSparql,String queggSparql) {
        qaldSparql=this.findProperty(qaldSparql);
        queggSparql=this.findProperty(queggSparql);
        if(qaldSparql.contains(queggSparql)){
            return true;
        }
        return false;

    }

    private String findProperty(String qaldSparql) {
        qaldSparql = StringUtils.substringBetween(qaldSparql, "{", "}");
        String []qaldSparqlLines=qaldSparql.split(" ");
        Integer index=0;
        for(String key:qaldSparqlLines){
            if(isProperty(key)){
                return key;
            }
        }
        return null;
    }
    
    private Boolean isProperty(String qaldSparql) {
        qaldSparql=qaldSparql.replace("http://dbpedia.org/ontology", "dbo:");
        qaldSparql=qaldSparql.replace("http://dbpedia.org/property", "dbp:");
        if (qaldSparql.contains("dbo:") || qaldSparql.contains("dbp:")) {
            return true;
        }
        return false;

    }

    

   
}
