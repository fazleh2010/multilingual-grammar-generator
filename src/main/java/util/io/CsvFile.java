/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.google.gdata.data.spreadsheet.Cell;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;
import java.io.Reader;
import static java.lang.System.exit;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
 

/**
 *
 * @author elahi
 */
public class CsvFile implements CsvConstants {

    private File filename = null;
    public String[] qaldHeader = null;
    private Map<String, List<String[]>> wordRows = new TreeMap<String, List<String[]>>();
    private Map<String, Integer> interestingnessIndexes = new HashMap<String, Integer>();
    private static String  resources="src/main/resources/";
    private static String xslDir="xsl/";
    private static String nounDir="noun/";

    private List<String[]> rows = new ArrayList<String[]>();
    private static Logger LOGGER = null;

    public CsvFile(File filename, Logger LOGGER) {
        this.filename = filename;
        this.LOGGER = LOGGER;
    }

    public CsvFile(File filename) {
        this.filename = filename;

    }

    public CsvFile() {
    }

    public void writeToCSV(List<String[]> csvData) {
        if (csvData.isEmpty()) {
            System.err.println("writing csv file failed!!!");
            return;
        }
        try ( CSVWriter writer = new CSVWriter(new FileWriter(this.filename))) {
            writer.writeAll(csvData);
        } catch (IOException ex) {
            System.err.println("writing csv file failed!!!" + ex.getMessage());
        }
    }

    public void writeToCSV(File newQaldFile, List<String[]> csvData) {
        if (csvData.isEmpty()) {
            System.err.println("writing csv file failed!!!");
            return;
        }
        try ( CSVWriter writer = new CSVWriter(new FileWriter(newQaldFile))) {
            writer.writeAll(csvData);
        } catch (IOException ex) {
            System.err.println("writing csv file failed!!!" + ex.getMessage());
        }
    }

    public File getFilename() {
        return filename;
    }

    public String[] getQaldHeader() {
        return this.qaldHeader;
    }

    public Map<String, List<String[]>> getRow() {
        return wordRows;
    }

   
    public List<String[]> getManualRow(File qaldFile, Double limit, Integer lineLimit) {
        List<String[]> rows = new ArrayList<String[]>();

        Stack<String> stack = new Stack<String>();
        CSVReader reader;
        try {
            rows = generateLinebyLine(qaldFile, lineLimit);
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rows;
    }

    public List<String[]> getRows(File qaldFile) {
        List<String[]> rows = new ArrayList<String[]>();

        /*if (FileFolderUtils.isFileSizeManageable(qaldFile, 40.0)) {
            //System.out.println("..........." + qaldFile.getName());
            return rows;
        }*/
        Stack<String> stack = new Stack<String>();
        CSVReader reader;
        try {
            System.out.println(qaldFile);
            reader = new CSVReader(new FileReader(qaldFile));
            rows = reader.readAll();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (CsvException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rows;
    }
    
    public List<String[]> getRowsManual(File qaldFile) {
        return readAllDataAtOnce(qaldFile, 100000);
    }

    public List<String[]> cvsModifier(File qaldFile) throws Exception {
        List<String[]> modifyrows = new ArrayList<String[]>();
        Map<String, List<String[]>> sort = new TreeMap<String, List<String[]>>();
        List<String[]> rows = getRows(qaldFile);
        String[] header = null;
        Integer j = 0;
        for (String[] row : rows) {
            if (j == 0) {
                header = row;

                j = j + 1;
                continue;
            }

            String key = null;
            String[] newRow = new String[row.length];
            for (Integer index = 0; index < row.length; index++) {
                //String query = " \" " + row[index].replace("$", ",") + " \" ";
                String query = row[index].replace("$", ",");
                if (index == 0) {
                    key = row[index];
                    key = key.toLowerCase();
                    key = key.replace(" ", "_").strip().trim();
                    newRow[index] = query;
                }
                newRow[index] = query;

            }
            List<String[]> list = new ArrayList<String[]>();
            if (sort.containsKey(key)) {
                list = sort.get(key);
            }

            list.add(newRow);
            sort.put(key, list);
        }

        modifyrows.add(header);
        for (String key : sort.keySet()) {
            List<String[]> list = sort.get(key);
            for (String[] row : list) {
                modifyrows.add(row);
            }
        }
        return modifyrows;
    }
    
    public static List<String[]> readAllDataAtOnce(File file,Integer lineLimit) {
        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .build();
            return csvReader.readAll();

            // print Data
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<String[]>();
    }

    private List<String[]> generateLinebyLine(File pathToCsv, Integer lineLimit) throws FileNotFoundException, IOException {
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader in = null;
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(pathToCsv), "UTF-8");
        BufferedReader csvReader = new BufferedReader(inputStreamReader);

        String line = null;
        Integer index = 0;
        while ((line = csvReader.readLine()) != null) {
            try {
                line = line.replace("\"", "");
                String[] data = line.split(",");
                rows.add(data);
            } catch (Exception ex) {
                ;
            }
            index = index + 1;
            if (index > lineLimit) {
                break;
            }
        }
        csvReader.close();
        return rows;
    }

    private List<String[]> generateLinebyLineT(File pathToCsv, Integer lineLimit) throws FileNotFoundException, IOException {
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        String line = null;
        Integer index = 0;
        while ((line = csvReader.readLine()) != null) {
            try {
                line = line.replace("\"", "");

                String[] data = line.split(",");
                
                rows.add(data);

            } catch (Exception ex) {
                ;
            }
            index = index + 1;
            if (index > lineLimit) {
                break;
            }
            // do something with the data
        }
        csvReader.close();
        return rows;
    }
    
     private Map<String,String[]> generateLinebyLine(File pathToCsv, Integer lineLimit,Integer keyIndex) throws FileNotFoundException, IOException {
        Map<String,String[]> map = new TreeMap<String,String[]>();
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        String line = null;
        Integer index = 0;
        while ((line = csvReader.readLine()) != null) {
            try {
                String[] data = line.split(",");
                String key=data[keyIndex];
                map.put(key, data);

            } catch (Exception ex) {
                ;
            }
            index = index + 1;
            if (index > lineLimit) {
                break;
            }
            // do something with the data
        }
        csvReader.close();
        return map;
    }
     
    public Map<String, String[]> generateBindingMapL(Integer keyIndex, Integer classIindex, String givenClassName) throws FileNotFoundException, IOException {
        Map<String, String[]> map = new TreeMap<String, String[]>();
        String line = null;
        Integer index = 0;
        List<String[]> rows = this.getRows(this.filename);

        for (String[] data : rows) {
            String key = data[keyIndex];
            String className = data[classIindex];
            if (this.isClassMatched(className, givenClassName)) {
                map.put(key, data);
            }

        }
        return map;
    }


    

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String fileLocation = resources + xslDir + nounDir;

    }

    private boolean isClassMatched(String className, String givenClassName) {
        className = className.toLowerCase().trim().strip().stripLeading().stripTrailing().replace(" ", "_");
        givenClassName = givenClassName.toLowerCase().trim().strip().stripLeading().stripTrailing().replace(" ", "_");
        // System.out.println("givenClassName::"+givenClassName+" bindingClass::"+className);

        if (className.contains(givenClassName)) {
            return true;
        }
        return false;

    }

    
   

}
