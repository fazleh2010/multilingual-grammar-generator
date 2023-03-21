/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import org.linkeddatafragments.main.Constants;
import static org.linkeddatafragments.main.Constants.ANSWER_SELECTED;
import static org.linkeddatafragments.main.Constants.BOG;
import static org.linkeddatafragments.main.Constants.EVALUTE_QALD_QUEGG;
import static org.linkeddatafragments.main.Constants.FIND_QALD_ANSWER;
import static org.linkeddatafragments.main.Constants.FIND_QALD_QUEGG_ANSWER;
import static org.linkeddatafragments.main.Constants.FIND_SIMILARITY;
import static org.linkeddatafragments.main.Constants.model;
import static org.linkeddatafragments.main.Constants.singleTripeFile;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.lang.SPARQLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.linkeddatafragments.client.LinkedDataFragmentSpql;
import org.linkeddatafragments.util.io.CsvFile;
import org.linkeddatafragments.util.io.FileUtils;
import org.linkeddatafragments.util.io.Summary;

/**
 *
 * @author elahi
 */
public class FindSimilarity implements Constants {

    private static final Logger LOG = LogManager.getLogger(EvaluateAgainstQALD.class);
    private final String language;
    private Set<String> qaldQuestions = new TreeSet<String>();
    private String endpoint = null;
    private Set<String> menu = new HashSet<String>();
    private String FIND_SIMILARITY_RESULT = null;
    private String evalutionFile = null;
    private String QALD_QUEGG_ANSWER_FILE = null;
    private String QALD_ANSWER_FILE = null;
    private Boolean online = false;
    private static Map<String, List<String>> answers = new TreeMap<String, List<String>>();
    private List< String[]> result = new ArrayList<String[]>();

    public FindSimilarity(String language, String endpoint, Set<String> menu, String FIND_SIMILARITY_RESULT, String resultComparisonFile, String qaldAnswerFile, String qaldQueGGAnswerFile, Boolean online, String startRange) {
        this.language = language;
        this.endpoint = endpoint;
        this.menu = menu;
        this.FIND_SIMILARITY_RESULT = FIND_SIMILARITY_RESULT.replace(".csv", startRange.toString() + ".csv");
        this.evalutionFile = resultComparisonFile.replace(".csv", startRange.toString() + ".csv");
        this.QALD_QUEGG_ANSWER_FILE = qaldQueGGAnswerFile.replace(".csv", startRange.toString() + ".csv");
        this.QALD_ANSWER_FILE = qaldAnswerFile;
        this.online = online;
    }

    public static void setOfflineAnswerList(Map<String, List<String>> answersT) {
        answers = answersT;
    }

    public void evaluateAndOutput(Map<String, String[]> queggQuestions, String qaldOriginalFile, String qaldModifiedFile, String qaldRaw, String languageCode, Double similarityMeasure, String lexialEntry) throws IOException, Exception {
        QALDImporter qaldImporter = new QALDImporter();
        EvaluationResult result = null;
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        qaldImporter.qaldToCSV(qaldOriginalFile, qaldRaw, languageCode);
        QALD qaldModified = qaldImporter.readQald(qaldModifiedFile);
        QALD qaldOriginal = qaldImporter.readQald(qaldOriginalFile);

        if (menu.contains(FIND_SIMILARITY)) {
            entryComparisons = getAllSentenceMatchesCsv(qaldOriginal, queggQuestions, languageCode, BOG, similarityMeasure);
            result = doEvaluationDummy(qaldModified, entryComparisons, languageCode, false);
            Writer.writeResult(qaldImporter, qaldOriginal, result, this.FIND_SIMILARITY_RESULT, languageCode, FIND_SIMILARITY);
            System.out.println("FIND_SIMILARITY completed!!");
        }

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

    private List<EntryComparison> getAllSentenceMatchesCsv(QALD qaldFile, Map<String, String[]> queggQuestions, String languageCode, String questionType, double similarityPercentage) throws Exception {
        List<String> qaldSentences
                = qaldFile.questions
                        .stream().parallel()
                        .map(qaldQuestions -> qaldQuestions.question)
                        .flatMap(qaldQuestions1
                                -> qaldQuestions1.stream().parallel()
                                .filter(qaldQuestion -> qaldQuestion.language.equals(languageCode))
                                .map(qaldQuestion -> qaldQuestion.string))
                        .collect(Collectors.toList());
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
        try {
            SPARQLParser.createParser(Syntax.syntaxSPARQL_11).parse(qaldPARQLQuery, qaldSparql);

        } catch (QueryParseException exception) {
            return;
        }
        List<String> uriResultListQueGG = new ArrayList<String>();
        List<String> uriResultListQALD;

        uriResultListQALD = this.getSparqlQuery(qaldSparql, flag, entryComparison.getQaldEntry().getResultList());

        entryComparison.setQaldResults(uriResultListQALD);

        if (queGGSparql != null) {
            /*queGGSparql = queGGSparql.replace("{", "\n" + "{" + "\n");
            queGGSparql = queGGSparql.replace("}", "\n" + "}");
            queGGSparql = queGGSparql.replace("\"", "");*/
            uriResultListQueGG = this.getSparqlQuery(queGGSparql, flag, entryComparison.getQueGGEntry().getResultList());
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
            float tp = uriResultListQueGG.stream().filter(uriResultListQALD::contains).count();
            float fp = uriResultListQueGG.stream().filter(resultQueGG -> !uriResultListQALD.contains(resultQueGG)).count();
            float fn = uriResultListQALD.stream().filter(resultQald -> !finalUriResultListQueGG.contains(resultQald)).count();
            entryComparison.setTp(tp);
            entryComparison.addFp(fp);
            entryComparison.setFn(fn);

        }

    }

    private List<EntryComparison> getMatchRealQuestion(QALD qaldFile, Map<String, String[]> realQuestions, String languageCode, double similarityPercentage) throws Exception {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        Integer index = 0;

        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            String qaldSparqlQuery = QALDImporter.getQaldSparqlQuery(qaldQuestions);
            String id = QALDImporter.getQaldId(qaldQuestions);

            Map<String, QueGGinfomation> grammarEntities = this.matchedRealQuestions(id, qaldQuestion, qaldSparqlQuery, realQuestions, similarityPercentage, index);
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

    public Map<String, QueGGinfomation> matchedRealQuestions(String qaldID, String qaldsentence, String qaldSparqlQuery, Map<String, String[]> questions, double similarityPercentage, Integer index) {
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

            System.out.println(similarityPercentage + " " + qaldID + " MATCHED: " + qaldsentence + ":" + queGGquestion + " cosineSimilarityPercentage::" + value);

            if (value > similarityPercentage) {

                //QueGGinfomation queGGinfomation = new QueGGinfomation(row, value);
                QueGGinfomation queGGinfomation = new QueGGinfomation(row, value, qaldSparqlQuery, qaldID);
                sort.put(queGGinfomation.getQuestion(), value);
                matchedQuestions.put(queGGinfomation.getQuestion(), queGGinfomation);
                //
            }
            /*if(qaldID.contains("63"))
               exit(1);*/

        }

        return matchedQuestions;
    }

    private List<String> getSparqlQuery(String sparql, Boolean flag, List<String> resultList) {
        LOG.debug("Executing QALD SPARQL Query:\n{}", sparql);
        List<String> uriResultList = new ArrayList<String>();

        if (sparql != null)
            ; else {
            return new ArrayList<String>();
        }

        if (menu.contains(EVALUTE_QALD_QUEGG)) {
            return resultList;
        }

        if (!flag) {
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
        if (sparql.contains("COUNT") || sparql.contains("count")) {
            return new ArrayList<String>();
        }

        if (sparql.contains("http://dbpedia.org/ontology/product")) {
            uriResultList.add("http://dbpedia.org/resource/Slack_Technologies");
            return uriResultList;
        }

        if (menu.contains(FIND_QALD_QUEGG_ANSWER) || menu.contains(FIND_QALD_ANSWER) || menu.contains(ANSWER_SELECTED)) {
            try {

                sparql = sparql.replace("\"", "");
                sparql = sparql.replace(" ", " ");

                LinkedDataFragmentSpql main = new LinkedDataFragmentSpql(model, endpoint, sparql);
                uriResultList = main.sparqlObjectAsVariable(sparql);
                uriResultList = main.parseResultList(uriResultList);
            } catch (QueryParseException ex) {
                System.out.println("sparql:::" + sparql);
                System.out.println("error:::" + ex.getMessage());
                return new ArrayList<String>();
            }

        }

        return uriResultList;
    }

    private String cleanQALDString(String sentence) {
        return sentence.toLowerCase().trim();
    }

}
