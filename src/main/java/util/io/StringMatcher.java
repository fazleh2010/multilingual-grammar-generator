/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.google.gdata.util.common.base.Pair;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class StringMatcher {
     public static String[] parseToken(String string) {
        if (string.contains("(") && string.contains(")")) {
            String reference = StringUtils.substringBetween(string, "(", ")");
            String attribute = string.replaceAll("\\((.*?)\\)", "");
            return new String[]{attribute, reference};
        }
        return new String[]{string};
    }
     
    public static List<String> modifySentences(List<String> sentences) {
        List<String> modifiedSentences=new ArrayList<String>();
        for(String sentence:sentences){
            sentence=sentence.replace("_", " ");
            modifiedSentences.add(sentence);
        }
       return modifiedSentences;
    }
       
    public static String modifyLabels(String answerLabel) {
        //if (answerLabel.contains("slack")) {
            System.out.println("Before:" + answerLabel);
        //}
        if (answerLabel != null && answerLabel.contains("(")) {
            System.out.println("Before:" + answerLabel);
            String property = StringUtils.substringBetween(answerLabel, "(", ")");
            /*answerLabel = answerLabel.replace(property, "");
            answerLabel = answerLabel.replace("(", "");
            answerLabel = answerLabel.replace(")", "");
            answerLabel = answerLabel.trim().strip().stripLeading().stripTrailing();
            System.out.println("after:" + answerLabel);
            exit(1);*/
        }

        return answerLabel;
    }
    
    public static List<String> modifySentencesWithNonTerminals(List<String> sentences, String nonTerminal) {
        List<String> modifiedSentences = new ArrayList<String>();
        for (String question : sentences) {
            Pair<String, String> pair = findVariable(question);
            if (pair.first != null) {
                question = pair.second;
                question = question.replace("(X)", nonTerminal);
                modifiedSentences.add(question);
            }

        }
        return modifiedSentences;
    }

    private static Pair<String, String> findVariable(String question) {
        if (question.contains("(") && question.contains(")")) {
            String result = StringUtils.substringBetween(question, "(", ")");
            question = question.replace(result, "X");
            Pair<String, String> pair = new Pair<String, String>("(X)", question);
            return pair;
        }
        return new Pair<String, String>(null, question);
    }


    
}
