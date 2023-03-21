/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.util.io;

import org.linkeddatafragments.evaluation.EvaluationResult;
import java.io.File;

/**
 *
 * @author elahi
 */
public class Summary {

    public static String[] setStart() {
        String[] row = new String[10];
        row[0] = "fileName";
        row[1] = "global Tp";
        row[2] = "global Fp";
        row[3] = "global Fn";
        row[4] = "global precision";
        row[5] = "global recall";
        row[6] = "global f-measure";
        row[7] = "average precision";
        row[8] = "average recall";
        row[9] = "average f-measure";
        return row;
    }

    public static String[] setKomparsionResult(String fileName, String lexialEntry,EvaluationResult evaluationResult) {
        String[] row = new String[11];
        row[0] = new File(fileName).getName();
        row[1] = lexialEntry.replace("http://localhost:8080#", "");
        row[2] = String.format("%.02f", evaluationResult.getTp_global());
        row[3] = String.format("%.02f", evaluationResult.getFp_global());
        row[4] = String.format("%.02f", evaluationResult.getFn_global());
        row[5] = String.format("%.02f", evaluationResult.getPrecision_global());
        row[6] = String.format("%.02f", evaluationResult.getRecall_global());
        row[7] = String.format("%.02f", evaluationResult.getF_measure_global());
        row[8] = String.format("%.02f", evaluationResult.getPrecision_average());
        row[9] = String.format("%.02f", evaluationResult.getRecall_average());
        row[10] = String.format("%.02f", evaluationResult.getF_measure_average());
        return row;
    }

}
