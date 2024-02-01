/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import grammar.read.questions.GrammarEntries;
import static grammar.read.questions.ReadWriteConstants.summaryHeader;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class Statistics {

    private String frameType = null;
    private Integer numberOfGrammarRules = null;
    private Integer numberOfQuestions = null;
    private Integer bindingList = null;
    private String Success_Fail = "Success";
    private String reason = "-";

    public Statistics(String frameType, Integer numberOfGrammarRules, Integer numberOfQuestions, Integer bindingList) {
        this.frameType = frameType;
        this.numberOfGrammarRules = numberOfGrammarRules;
        this.numberOfQuestions = numberOfQuestions;
        this.bindingList = bindingList;
        if (bindingList == 0) {
            this.Success_Fail = "Failed";
            this.reason = "binding list is empty";
        }

    }

    public Integer getBindingList() {
        return bindingList;
    }

    public String getSuccess_Fail() {
        return Success_Fail;
    }

    public String getReason() {
        return reason;
    }

    public String getFrameType() {
        return frameType;
    }

    public Integer getNumberOfGrammarRules() {
        return numberOfGrammarRules;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public static void findNumberOfGrammarRules(File file) throws Exception {
        Integer index = 0;

        index = index + 1;
        ObjectMapper mapper = new ObjectMapper();
        GrammarEntries grammarEntries = mapper.readValue(file, GrammarEntries.class);
        Integer total = grammarEntries.getGrammarEntries().size();
        Integer idIndex = 0, noIndex = 0;
        //processEachGrammarUnit(grammarEntries, existingEntries, noIndex, idIndex);

    }

    public static void main(String[] args) throws Exception {
        String inputDir = "result/en/";
        String grammar_FULL_DATASET = "/grammar_FULL_DATASET_EN.json";
        //File file = new File(inputDir + grammar_FULL_DATASET);
        //Statistics.findNumberOfGrammarRules(file);
        Integer sum=13373+3078+2183+910+168;
        System.out.println(sum);
        

    }

}
