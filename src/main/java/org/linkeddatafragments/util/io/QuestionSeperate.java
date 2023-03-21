/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.util.io;

import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class QuestionSeperate {

    private static Map<String, List<String[]>> lexcialEntries = new TreeMap<String, List<String[]>>();
    
    public static void main(String[] args) throws IOException, FileNotFoundException, CsvException {
        System.out.println("Hello World!!");
        String filename = "output/de/";
        File[] files = new File(filename).listFiles();
        
        for (File file : files) {
            if (file.getName().contains("questions") && file.getName().contains(".csv")) {
                CsvFile csvFile = new CsvFile();
                List<String[]> rows = csvFile.getRows(file);
                for (String[] row : rows) {
                    String lexcialEntry = row[0].trim().strip().stripLeading().stripLeading();
                    List<String[]> lexRows = new ArrayList<String[]>();
                    if (lexcialEntries.containsKey(lexcialEntry)) {
                        lexRows = lexcialEntries.get(lexcialEntry);
                        lexcialEntries.put(lexcialEntry, lexRows);
                    } else {
                        lexRows.add(row);
                        lexcialEntries.put(lexcialEntry, lexRows);
                    }
                    
                }
            }
        }
        for (String key : lexcialEntries.keySet()) {
            System.out.println(key + "  " + lexcialEntries.get(key).size());
            
        }
    }
}
