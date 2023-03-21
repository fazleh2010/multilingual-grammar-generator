package org.linkeddatafragments.util.io;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculation {

    private static Map<String, Integer> lexicalEntryNumberOfQuestions = new TreeMap<String, Integer>();

    
     public static void numberQuestions(InputCofiguration inputCofiguration) throws IOException {
        String inputDir = inputCofiguration.getOutputDir() + "/questions/";
        String questionFile = inputDir + "/" + "all.csv";
        String language = inputCofiguration.getLanguageCode();

        List<String[]> rows = new ArrayList<String[]>();
        Integer sum = 0;Integer totalLexEntry=0;
        rows.add(new String[]{"LexicalEntries", "NumOfQuestions"});

        try {
            totalLexEntry = CsvFile.mergeFilesAll(inputDir, questionFile);
            //System.out.println("merged files successful!!!");
            //totalLexEntry =countLineOfCsvFile(inputDir + path.getFileName());
        } catch (IOException ex) {
            Logger.getLogger(Calculation.class.getName()).log(Level.SEVERE, null, ex);
        }
        rows.add(new String[]{totalLexEntry.toString(), sum.toString()});
        String outputFile = questionFile.replace( "all.csv",  "total.csv") ;
        CsvFile csvFile = new CsvFile(new File(outputFile));
        csvFile.writeToCSV(rows);
    }
     
    /*public static void numberQuestionsIndividual(InputCofiguration inputCofiguration) throws IOException {
        String inputDir = inputCofiguration.getOutputDir() + "/questions/";
        String questionFile = inputDir + "/" + "all.csv";
        String language = inputCofiguration.getLanguageCode();

        List<String[]> rows = new ArrayList<String[]>();
        Integer sum = 0;
        rows.add(new String[]{"LexicalEntries", "NumOfQuestions"});

        try {
            Path path = CsvFile.mergeFiles(inputDir, questionFile);
            System.out.println("merged files successful!!!");
            countLineOfCsvFile(inputDir + path.getFileName());
        } catch (IOException ex) {
            Logger.getLogger(Calculation.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (String key : lexicalEntryNumberOfQuestions.keySet()) {
            String[] columns = new String[2];
            columns[0] = key;
            Integer total = lexicalEntryNumberOfQuestions.get(key);
            columns[1] = total.toString();
            rows.add(columns);
            sum += total;
            System.out.println("columns[0]: "+columns[0]+" columns[1]::"+columns[1]);

        }
        Integer totalLexEntry = lexicalEntryNumberOfQuestions.size();
        rows.add(new String[]{totalLexEntry.toString(), sum.toString()});
        String outputFile = inputDir + "/" + inputCofiguration.getLanguageCode() + "_" + "LexicalEntries"+"_"+totalLexEntry.toString() + "_" + "NumOfQuestions"+"_"+sum.toString() + "_total.csv";
        CsvFile csvFile = new CsvFile(new File(outputFile));
        csvFile.writeToCSV(rows);
    }*/

    public static void countLineOfCsvFileL(String fileName) throws IOException, FileNotFoundException, CsvException {
        File file = new File(fileName);
        CsvFile csvFile = new CsvFile(file);
        List<String[]> rows = csvFile.getRows(file);
        Integer lines = 0;
        Integer index = 0;
          System.out.println("rows.size(): "+rows.size());
        for (String[] row : rows) {
           

            index = index + 1;
            if (index == 1) {
                continue;
            }
            String id = row[0];
            id = editID(id);
            if (lexicalEntryNumberOfQuestions.containsKey(id)) {
                Integer count = lexicalEntryNumberOfQuestions.get(id) + 1;
                lexicalEntryNumberOfQuestions.put(id, count);
            } else {
                lexicalEntryNumberOfQuestions.put(id, 1);
            }
        }
    }
    
     public static Integer countLineOfCsvFile(String fileName) throws IOException, FileNotFoundException, CsvException {
        File file = new File(fileName);
        CsvFile csvFile = new CsvFile(file);
        List<String[]> rows = csvFile.getRows(file);
        Integer lines = 0;
        Integer index = 0;
          System.out.println("rows.size(): "+rows.size());
        for (String[] row : rows) {
            index = index + 1;
        }
        return index;
    }

    /* public static void removeDuplicate(String fileName) {
        CsvFile csvFile = new CsvFile();
        List<String[]> lines = csvFile.getRows(new File(fileName));

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            for (String[] line : lines) {
                String question = line[1];
                if (line != null) {
                    question = line[1];
                    allQuestions.put(question, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
    private static String editID(String id) {
        id = id.replace("http://localhost:8080#", "");
        id = id.replaceAll("\\d", "").replaceAll("__", "_");
        id = id.replaceAll("_", "-").toLowerCase();
        return id;
    }

    public static void folderDuplicateRemover(Map<String, Set<String>> selectedFiles, String questionDir, String frameType) throws IOException, FileNotFoundException, CsvException {
        Map<String, String[]> allQuestions = new TreeMap<String, String[]>();
        String note = null;
        if (frameType.contains("-IPP-")) {
            note = "InTraPP";
        } else if (frameType.contains("-VP-")) {
            note = "Tran";
        } else if (frameType.contains("-NPP-")) {
            note = "NounPP";
        }

        for (String property : selectedFiles.keySet()) {
            String outputFileName = questionDir + "questions_all_" + property + "_" + note + ".csv";
            Set<String> fileNames = selectedFiles.get(property);
            System.out.println("property::" + property);
            for (String fileName : fileNames) {
                System.out.println("fileName::" + fileName);

                CsvFile csvFile = new CsvFile();
                List<String[]> lines = csvFile.getRows(new File(fileName));
                Integer total = lines.size();
                Integer index = 0;
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    for (String[] line : lines) {
                        index = index + 1;
                        String question = line[1].trim().strip().stripLeading().stripTrailing();
                        if (line != null) {
                            question = line[1];
                            allQuestions.put(question, line);
                            System.out.println(property + " " + index + " total:" + total + " duplicate check:" + line[1] + " fileName:" + fileName);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<String[]> newRows = new ArrayList<String[]>();
            for (String id : allQuestions.keySet()) {
                String[] row = allQuestions.get(id);
                newRows.add(row);
            }
            CsvFile outputCsv = new CsvFile(new File(outputFileName));
            outputCsv.writeToCSV(newRows);
        }

    }

    private static String getFileProperty(String name) {
        String[] info = name.split("-");
        return info[3];
    }

}
