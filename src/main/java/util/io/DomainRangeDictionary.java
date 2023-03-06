/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.io.File;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class DomainRangeDictionary {

    private Map<String, List<String>> domainOrRange = new TreeMap<String, List<String>>();
    private String domainOrRangeInputFile = "RawDomainOrRange.csv";
    private String domainOrRangeOutputFile = "DomainOrRange.csv";

    public DomainRangeDictionary(String inputDir) throws Exception {
        File f = new File(inputDir);
        String[] files = f.list();
        CsvFile inputCsvFile = new CsvFile();
        try {
            for (String fileName : files) {
                if (fileName.contains(domainOrRangeInputFile)) {
                    fileName = inputDir + File.separatorChar + fileName;
                    domainOrRange = findDomainorRangeEnglish(new File(fileName));
                    this.domainOrRangeOutputFile = inputDir + File.separatorChar + domainOrRangeOutputFile;
                }
            }

            List<String[]> csvData = new ArrayList<String[]>();
            for (String key : domainOrRange.keySet()) {
                List<String> rows = domainOrRange.get(key);
                String[] values = {key, rows.get(0), rows.get(1)};
                csvData.add(values);

            }

            inputCsvFile.writeToCSV(new File(this.domainOrRangeOutputFile), csvData);
            this.domainOrRange = findDomainorRangeEnglish(new File(this.domainOrRangeOutputFile));
        } catch (Exception ex) {
            throw new Exception("doamina and range file is not available!!!");
        }

    }

    private static Map<String, List<String>> findDomainorRangeEnglish(File fileName) {
        CsvFile csvFile = new CsvFile();
        Map<String, List<String>> domainOrRange = new TreeMap<String, List<String>>();
        List<String[]> rows = csvFile.getRows(fileName);
        for (String[] row : rows) {
            String property = row[0].replace(" ", "");
            List<String> rowT = new ArrayList<String>();
            rowT.add(row[1]);
            rowT.add(row[2]);
            domainOrRange.put(property, rowT);
        }
        return domainOrRange;
    }

    public Map<String, List<String>> getDomainOrRange() {
        return domainOrRange;
    }

}
