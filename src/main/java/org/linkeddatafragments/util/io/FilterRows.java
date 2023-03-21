/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.util.io;

import static org.linkeddatafragments.main.Constants.questionsFile;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class FilterRows {

    private List<String> lexialEntry = new ArrayList<String>();
    private Map<String, String[]> queGGQuestions = null;

    public FilterRows(String outputDir, String[] files, String filterFileName, Integer range) throws IOException, FileNotFoundException, CsvException {
        this.queGGQuestions = getQuestionFromFile(outputDir, files, filterFileName, range);
    }
    
    public FilterRows(String outputDir, String fileName) throws IOException, FileNotFoundException, CsvException {
        this.queGGQuestions = getQuestionFromFile(outputDir, fileName);
    }
    
     public FilterRows(File file) throws IOException, FileNotFoundException, CsvException {
        this.queGGQuestions = getQuestionFromFile(file);
    }
     
     
    private Map<String, String[]> getQuestionFromFile(File file) throws IOException, FileNotFoundException, CsvException {
        Map<String, String[]> queGGQuestions = new HashMap<String, String[]>();
        List<String[]> rows;
        CsvFile csvFile = new CsvFile();
        rows = csvFile.getRows(file);
        for (String[] row : rows) {
            String question = row[1];
            String cleanQuestion = question.toLowerCase().trim().strip().stripLeading().stripTrailing();
            queGGQuestions.put(cleanQuestion, row);
        }
        return queGGQuestions;
    }


    private Map<String, String[]> getQuestionFromFile(String outputDir, String fileName) throws IOException, FileNotFoundException, CsvException {
        Map<String, String[]> queGGQuestions = new HashMap<String, String[]>();

        List<String[]> rows;
        File file = new File(outputDir + File.separator + fileName);
        CsvFile csvFile = new CsvFile();
        rows = csvFile.getRows(file);
        for (String[] row : rows) {
            String question = row[1];
            String cleanQuestion = question.toLowerCase().trim().strip().stripLeading().stripTrailing();
            queGGQuestions.put(cleanQuestion, row);
        }

        return queGGQuestions;
    }

    private Map<String, String[]> getQuestionFromFile(String outputDir, String[] files, String filterFileName, Integer range) throws IOException, FileNotFoundException, CsvException {
        Map<String, String[]> queGGQuestions = new HashMap<String, String[]>();

        for (String fileName : files) {
            List<String[]> rows;

            Integer index = 0;
            if (fileName.contains("lock.")) {
                continue;
            }
            if (fileName.contains(questionsFile) && fileName.contains(".csv")) {
                File file = new File(outputDir + File.separator + fileName);
                CsvFile csvFile = new CsvFile(file);
                //rows = csvFile.getRowsTab(file);
                rows = csvFile.getRows(file);
                for (String[] row : rows) {
                    String question = row[1];
                    String cleanQuestion = question.toLowerCase().trim().strip().stripLeading().stripTrailing();
                    queGGQuestions.put(cleanQuestion, row);
                    index = index + 1;
                    if (fileName.contains(filterFileName)) {
                        this.lexialEntry.add(row[0]);
                    }
                    if (fileName.contains(filterFileName) && index > range) {
                        break;
                    }
                }
            }
        }

        return queGGQuestions;
    }
    public List<String> getLexialEntry() {
        return lexialEntry;
    }

    public Map<String, String[]> getQueGGQuestions() {
        return queGGQuestions;
    }

    public void setLexialEntry(List<String> lexialEntry) {
        this.lexialEntry = lexialEntry;
    }

    public void setQueGGQuestions(Map<String, String[]> queGGQuestions) {
        this.queGGQuestions = queGGQuestions;
    }

}
