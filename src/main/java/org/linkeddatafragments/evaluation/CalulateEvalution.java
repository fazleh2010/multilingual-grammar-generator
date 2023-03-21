/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.linkeddatafragments.util.io.CsvFile;
import org.linkeddatafragments.util.io.Summary;

/**
 *
 * @author elahi
 */
public class CalulateEvalution {

    private static final Logger LOG = LogManager.getLogger(EvaluateAgainstQALD.class);
    private List< String[]> result = new ArrayList<String[]>();
    private String evalutionFile = null;


    public CalulateEvalution(String evalutionFile) {
       this.evalutionFile =evalutionFile;

    }

    public List<EntryComparison> retrivedDataFromXslSheet(String resultMatchFile) throws IOException, FileNotFoundException, CsvException {
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

    public EvaluationResult doEvaluation(QALD qaldFile, List<EntryComparison> entryComparisons, String languageCode, Boolean flag, String lexialEntry) {
        EvaluationResult evaluationResult = new EvaluationResult();
        Integer numberOfElement = 0;
        float globalTp = 0, globalFp = 0, globalFn = 0, totalPreision = 0, totalRecall = 0, totalF_measure = 0;
        for (EntryComparison entryComparison : entryComparisons) {
            numberOfElement = numberOfElement + 1;
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
            if (entryComparison.getQaldEntry().getId().contains("210")) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! START  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("getQaldResults()::" + entryComparison.getQaldResults());
                System.out.println("getQueGGResults()::" + entryComparison.getQueGGResults());
                System.out.println("tp::" + entryComparison.getTp() + " fp::" + entryComparison.getFp() + " fn::" + entryComparison.getFn());
                System.out.println("preision::" + entryComparison.getPrecision() + " reall::" + entryComparison.getRecall() + " f-sore::" + entryComparison.getF_measure());
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! END  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }

            evaluationResult.getEntryComparisons().add(entryComparison);

            globalTp += fscore.getTp();
            globalFp += fscore.getFp();
            globalFn += fscore.getFn();
            FscoreCalculation fscoreInd = new FscoreCalculation(fscore.getTp(), fscore.getFp(), fscore.getFn());
            totalPreision += fscoreInd.getPrecision();
            totalRecall += fscoreInd.getRecall();
            totalF_measure += fscoreInd.getFscore();

        }

        FscoreCalculation fscore_global = new FscoreCalculation(globalTp, globalFp, globalFn);

        evaluationResult.setTp_global(globalTp);
        evaluationResult.setFp_global(globalFp);
        evaluationResult.setFn_global(globalFn);
        evaluationResult.setPrecision_global(fscore_global.getPrecision());
        evaluationResult.setRecall_global(fscore_global.getRecall());
        evaluationResult.setF_measure_global(fscore_global.getFscore());

        evaluationResult.setPrecision_average(totalPreision / numberOfElement);
        evaluationResult.setRecall_average(totalRecall / numberOfElement);
        evaluationResult.setF_measure_average(totalF_measure / numberOfElement);

        this.result.add(Summary.setKomparsionResult(evalutionFile, lexialEntry, evaluationResult));

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

    private List<String> makeList(String data) {
        List<String> list = new ArrayList<String>();

        data = data.replace("[", "").replace("]", "");
        data = data.replace("(", "").replace(")", "");
        data=data.replace(" ", "").strip().stripLeading().stripTrailing().trim().replace("\"", "");

        if (data.contains(",")) {
            List<String> newlist = new ArrayList<String>();
            list = Arrays.asList(data.split(","));

            for (String element : list) {
                element = element.strip().stripLeading().stripTrailing().trim();
                if (element.contains("http")) {
                    element = element;
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

}
