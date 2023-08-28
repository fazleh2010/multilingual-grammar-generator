/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class DomainRangeDictionary {

    private Map<String, List<String>> domainOrRange = new TreeMap<String, List<String>>();
    private String domainOrRangeInputFile = "RawDomainOrRange.csv";
    private String domainOrRangeOutputFile = "DomainOrRange.csv";

    public DomainRangeDictionary(String inputDir, String[] pathnames) {
        CsvFile inputCsvFile = new CsvFile();
        Map<String, List<String>> domainOrRange = new TreeMap<String, List<String>>();

        /*for (String pathname : pathnames) {
            String[] files = new File(inputDir + File.separatorChar + pathname).list();
            for (String fileName : files) {
                if (fileName.contains(domainOrRangeInputFile)) {
                    fileName = inputDir + File.separatorChar + pathname + File.separatorChar + fileName;
                    domainOrRange = findDomainorRangeEnglish(new File(fileName));
                    this.domainOrRangeOutputFile = inputDir + File.separatorChar + pathname + File.separatorChar + domainOrRangeOutputFile;
                }

            }
        }

        List<String[]> csvData = new ArrayList<String[]>();
        for (String key : domainOrRange.keySet()) {
            List<String> rows = domainOrRange.get(key);
            String[] values = {key, rows.get(0), rows.get(1)};
            csvData.add(values);

        }

        inputCsvFile.writeToCSV(new File(this.domainOrRangeOutputFile), csvData);*/
        this.domainOrRange = findDomainorRangeEnglish(new File(inputDir + File.separatorChar +this.domainOrRangeOutputFile));

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
  
    public static void main(String []args) throws IOException{
        String[] head=new String[]{"lemon","partOfSpeech","writtenForm (singular)","writtenForm (plural)",
                                   "preposition","SyntacticFrame","copulativeArg","prepositionalAdjunct",
                                   "sense","reference","domain","range"};
        File domainAndRangeFileName=new File("lexicon/en/DomainOrRange.csv");
        File outputFileName=new File("lexicon/en/nounPPframeClass.csv");
         String classFile="lexicon/en/classes.txt";
         Set<String> classes=FileFolderUtils.fileToSet(classFile);
        CsvFile csvFile = new CsvFile(outputFileName);
        Map<String, List<String>> domainOrRange=findDomainorRangeEnglish(domainAndRangeFileName);
        for(String key:classes){
            if(domainOrRange.containsKey(key))
               continue; 
            List<String> rowT = new ArrayList<String>();
            String property=key.replace("dbo:","");
            rowT.add(property);
            rowT.add(property+"s");
            //System.out.println(property+" "+property+property+"s");
            domainOrRange.put(key, rowT);
        }
        System.out.println("size:"+domainOrRange.size());
        
        List<String[]> newRows=new ArrayList<String[]>();
        newRows.add(head);
        
        for(String key:domainOrRange.keySet()){
            String klass=key.replace("dbo:","");
            List<String> rowT = domainOrRange.get(key);
            if(key.contains("xsd:")){
               continue;  
            }
            if(rowT.get(0).contains("-")){
               continue;
            }
            String lexiId=rowT.get(0).toLowerCase()+"_"+"of";
            String singular=rowT.get(0);
            String plural=rowT.get(1);
            String[] row=new String[]{lexiId,"noun",singular,plural,"of","NounPPFrame","range","domain",
                                          "1","rdf:type",key,key};
            //System.out.println(rowT.get(0)+"_"+"of"+"noun"+rowT.get(0)+rowT.get(1)+"of"+"NounPPFrame"+"range"+"domain"+
            //                              "1"+"rdf:type"+key+key);
            newRows.add(row);
        }
        csvFile.writeToCSV(newRows);
        
         
        
        
    }
}
