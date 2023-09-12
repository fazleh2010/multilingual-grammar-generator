/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class AttriAdjCsvGeneration {

    public static void main(String args[]) throws IOException {
        String fileNameCsv = "src/main/resources/nationality.csv";
        CsvFile csvFile = new CsvFile(new File(fileNameCsv));
        String fileName = "src/main/resources/nationality.txt";
        Set<String> data = FileFolderUtils.fileToSet(fileName);
        List<String[]> rows = new ArrayList<String[]>();
        String[] head = new String[]{"LemonEntry", "partOfSpeech", "writtenForm", "SyntacticFrame",
            "copulativeSubject", "attributiveArg", "sense", "reference",
            "owl:onProperty", "owl:hasValue", "domain", "range"};
        Integer index = 1;
        String []properties=new String[]{"country","nationality"};
        for (String dataT : data) {
            for(String property:properties){
            //dataT=dataT.replace("\t", "+");
               if (dataT.contains("\t")) {
                String[] info = dataT.split("\t");
                String resource = info[0].replace(" ", "_");
                String nationality = info[1];
                String id=nationality.replace(" ", "-")+"_"+index;             
               
                index = index + 1;
                System.out.println(" id:"+id+" nationality::"+nationality);
                rows.add(new String[]{id, "adjective", nationality, "AdjectiveAttributiveFrame",
                    "PredSynArg", "AttrSynArg", "1", "owl:Restriction", "dbo:" + property,
                    "res:" + resource, "dbo:Film", "dbo:Film"});
                }
            }
            
        }
        csvFile.writeToCSV(rows);
    }

}
