/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import org.linkeddatafragments.main.Constants;
import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.linkeddatafragments.util.io.CsvFile;

/**
 *
 * @author elahi
 */
public class Writer implements Constants{

    private static final Logger LOG = LogManager.getLogger(Writer.class);

    public static void writeResult(QALDImporter qaldImporter, QALD qaldOriginal, EvaluationResult result, String resultFileName, String languageCode, String type) throws IOException {
        ZonedDateTime before = ZonedDateTime.now();
        ZonedDateTime after = ZonedDateTime.now();
        long duration = Duration.between(before, after).toSeconds();
        List<String[]> dataLines = new ArrayList<String[]>();
        if (type.contains(Constants.FIND_SIMILARITY)) {
            dataLines = qaldQueGGMatch(result, qaldOriginal,languageCode);
        } else if (type.contains(Constants.FIND_QALD_ANSWER)) {
            dataLines = qaldAnswer(result, qaldOriginal, languageCode);
        } else if (type.contains(Constants.FIND_QALD_QUEGG_ANSWER)) {
            dataLines = resultToPrintableList(result, qaldOriginal, languageCode);
        } else if (type.contains(Constants.EVALUTE_QALD_QUEGG)) {
            dataLines = resultToPrintableList(result, qaldOriginal, languageCode);
        }
        qaldImporter.writeToCSV(dataLines, resultFileName);
        LOG.info(String.format("Evaluation was completed in %dmin %ds", duration / 60, duration % 60));
        LOG.info("Results are available here: " + resultFileName);
    }
    
  
    public static List<String[]> resultToPrintableList(EvaluationResult result, QALD qaldOriginal, String languageCode) {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{
            "QALD id",
            "match status",
            "QALD question",
            "QueGG question",
            "QALD SPARQL query",
            "QueGG SPARQL query",
            "TP",
            "FP",
            "FN",
            "Precision",
            "Recall",
            "F-Measure"
        });
        int numberOfQueGGMatches = 0;
        for (EntryComparison entryComparison : result.getEntryComparisons()) {
            QALD.QALDQuestions QALDQuestions = Matcher.getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison);
            String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
            String qaldSparql = entryComparison.getQaldEntry().getSparql();

            list.add(
                    new String[]{
                        entryComparison.getQaldEntry().getId(),
                        entryComparison.getSimilarityCsore().toString(),
                        qaldQuestion,
                        entryComparison.getQueGGEntry().getQuestions(),
                        qaldSparql,
                        !isNull(entryComparison.getQueGGEntry())
                        ? entryComparison.getQueGGEntry().getSparql()
                        : "", // entryComparison.getQueGGEntries().stream().filter(entry -> entry.).getSentences().stream().reduce((x, y) -> x + "\n" + y).orElse(""),
                        String.valueOf(entryComparison.getTp()),
                        String.valueOf(entryComparison.getFp()),
                        String.valueOf(entryComparison.getFn()),
                        String.valueOf(entryComparison.getPrecision()),
                        String.valueOf(entryComparison.getRecall()),
                        String.valueOf(entryComparison.getF_measure())
                    }
            );

            if (!isNull(entryComparison.getQueGGEntry())
                    && !isNull(entryComparison.getQueGGEntry().getSparql())
                    && !entryComparison.getQueGGEntry().getSparql().equals("")) {
                numberOfQueGGMatches++;
            }
        }
        list.add(new String[]{
            "", // "QALD id",
            "", // "match status",
            "", // "QALD original question",
            "", // "QALD original SPARQL query",
            "", // "QALD reformulated question",
            "", // "QueGG SPARQL query",
            String.valueOf(result.getTp_global()), // "TP",
            String.valueOf(result.getFp_global()), // "FP",
            String.valueOf(result.getFn_global()), // "FN",
            String.valueOf(result.getPrecision_global()), // "Precision",
            String.valueOf(result.getRecall_global()), // "Recall",
            String.valueOf(result.getF_measure_global())
        });
        LOG.info(String.format("Total matches: %d", numberOfQueGGMatches));
        LOG.info(String.format(
                "QALD coverage: %.2f%%",
                (float) (numberOfQueGGMatches) / (float) qaldOriginal.questions.size() * 100
        ));

        return list;
    }
    
    /*public static void qaldQueGGMatch(EvaluationResult result, QALD qaldOriginal, String fileName) throws IOException {
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[]{"QALD id", "match status", "QALD question", "QueGG question", "QALD SPARQL query", "QueGG SPARQL query"});
        int numberOfQueGGMatches = 0;
        for (EntryComparison entryComparison : result.getEntryComparisons()) {
            QALD.QALDQuestions QALDQuestions = Matcher.getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison);
            String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
            String qaldSparql = entryComparison.getQaldEntry().getSparql().replace("\n", "");
            String id = entryComparison.getQaldEntry().getId();
            String value = entryComparison.getSimilarityCsore().toString();
            String queGGQuestion = "", queGGSparqlQuery = "";

            if (!isNull(entryComparison.getQueGGEntry().getQuestions())) {
                queGGQuestion = entryComparison.getQueGGEntry().getQuestions().replace("\n", "");
            }
            if (!isNull(entryComparison.getQueGGEntry().getSparql())) {
                queGGSparqlQuery = entryComparison.getQueGGEntry().getSparql().replace("\n", "");
            }
            
            dataLines.add(new String[]{id, value, qaldQuestion, queGGQuestion, qaldSparql, queGGSparqlQuery});

          
        }
        CsvFile csvFile=new CsvFile(new File(fileName));
        csvFile.writeToCSV(dataLines);
    }*/

    
        public static List<String[]> qaldQueGGMatch(EvaluationResult result, QALD qaldOriginal, String languageCode) {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{
            "QALD id",
            "match status",
            "QALD question",
            "QueGG question",
            "QALD SPARQL query",
            "QueGG SPARQL query"
        });
        int numberOfQueGGMatches = 0;
        for (EntryComparison entryComparison : result.getEntryComparisons()) {
            QALD.QALDQuestions QALDQuestions = Matcher.getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison);
            String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
            String qaldSparql = entryComparison.getQaldEntry().getSparql();

            
            list.add(
                    new String[]{
                        entryComparison.getQaldEntry().getId(),
                        entryComparison.getSimilarityCsore().toString(),
                        qaldQuestion,
                        entryComparison.getQueGGEntry().getQuestions(),
                        qaldSparql.replace("\n",""),
                        !isNull(entryComparison.getQueGGEntry())
                        ? entryComparison.getQueGGEntry().getSparql()
                        : "" // entryComparison.getQueGGEntries().stream().filter(entry -> entry.).getSentences().stream().reduce((x, y) -> x + "\n" + y).orElse(""),
                    }
            );

            if (!isNull(entryComparison.getQueGGEntry())
                    && !isNull(entryComparison.getQueGGEntry().getSparql())
                    && !entryComparison.getQueGGEntry().getSparql().equals("")) {
                numberOfQueGGMatches++;
            }
        }
        list.add(new String[]{
            "", // "QALD id",
            "", // "match status",
            "", // "QALD original question",
            "", // "QALD original SPARQL query",
            "", // "QALD reformulated question",
            "", // "QueGG SPARQL query",
            String.valueOf(result.getTp_global()), // "TP",
            String.valueOf(result.getFp_global()), // "FP",
            String.valueOf(result.getFn_global()), // "FN",
            String.valueOf(result.getPrecision_global()), // "Precision",
            String.valueOf(result.getRecall_global()), // "Recall",
            String.valueOf(result.getF_measure_global())
        });
        LOG.info(String.format("Total matches: %d", numberOfQueGGMatches));
        LOG.info(String.format(
                "QALD coverage: %.2f%%",
                (float) (numberOfQueGGMatches) / (float) qaldOriginal.questions.size() * 100
        ));
        
        return list;
    }

    public static List<String[]> qaldAnswer(EvaluationResult result, QALD qaldOriginal, String languageCode) {
        List<String[]> list = new ArrayList<String[]>();
        list.add(new String[]{
            "id",
            "question",
            "sparql",
            "answer"
        });
        int numberOfQueGGMatches = 0;
        for (EntryComparison entryComparison : result.getEntryComparisons()) {
            QALD.QALDQuestions QALDQuestions = Matcher.getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison);

            
            list.add(
                    new String[]{
                        entryComparison.getQaldEntry().getId(),
                        entryComparison.getQaldEntry().getQuestions(),
                        entryComparison.getQaldEntry().getSparql(),
                        entryComparison.getQaldEntry().getResultList().toString()
                    }
            );

            if (!isNull(entryComparison.getQueGGEntry())
                    && !isNull(entryComparison.getQueGGEntry().getSparql())
                    && !entryComparison.getQueGGEntry().getSparql().equals("")) {
                numberOfQueGGMatches++;
            }
        }
        list.add(new String[]{
            "", // "QALD id",
            "", // "match status",
            "", // "QALD original question",
            "", // "QALD original SPARQL query",
            "", // "QALD reformulated question",
            "", // "QueGG SPARQL query",
            String.valueOf(result.getTp_global()), // "TP",
            String.valueOf(result.getFp_global()), // "FP",
            String.valueOf(result.getFn_global()), // "FN",
            String.valueOf(result.getPrecision_global()), // "Precision",
            String.valueOf(result.getRecall_global()), // "Recall",
            String.valueOf(result.getF_measure_global())
        });
        LOG.info(String.format("Total matches: %d", numberOfQueGGMatches));
        LOG.info(String.format(
                "QALD coverage: %.2f%%",
                (float) (numberOfQueGGMatches) / (float) qaldOriginal.questions.size() * 100
        ));

        return list;
    }

}
