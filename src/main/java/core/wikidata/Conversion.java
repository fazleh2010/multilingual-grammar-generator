/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.wikidata;

import core.element.LinkedData;
import static core.turtle.EnglishCsv.TempConstants.AdjectiveAttributiveFrame;
import static core.turtle.EnglishCsv.TempConstants.AdjectiveSuperlativeFrame;
import static core.turtle.EnglishCsv.TempConstants.InTransitivePPFrame;
import static core.turtle.EnglishCsv.TempConstants.NounPPFrame;
import static core.turtle.EnglishCsv.TempConstants.NounPredicateFrame;
import static core.turtle.EnglishCsv.TempConstants.TransitiveFrame;
import java.io.File;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import util.CsvFile;
import util.FileFolderUtils;
import util.FileProcessUtils;
import util.Property;
import util.SparqlQuery;

/**
 *
 * @author fazleh
 */
public class Conversion {

    private String syntactFrame = null;
    private Integer referenceIndex = 0;
    private Integer domainIndex = 0;
    private Integer rangeIndex = 0;
    private String sparqlEndpoint = null;
    private LinkedHashMap<String, String> prefixes = new LinkedHashMap<String, String>();
    private List<String[]> resultRows = new ArrayList<String[]>();
    private Map<String, String> domainRangeWikidata = new HashMap<String, String>();

    private Conversion(String sparqlEndpoint, LinkedHashMap<String, String> prefixes) {
        this.prefixes = prefixes;
        this.sparqlEndpoint = sparqlEndpoint;
    }

    public void getWikidata(List<String[]> rows, Boolean filterFlag) throws Exception {
        this.resultRows = findWikidataResource(rows, filterFlag);

    }

    public List<String[]> findWikidataResource(List<String[]> rows, Boolean filterFlag) {
        List<String[]> resultRows = new ArrayList<String[]>();
        Integer firstRow = 0;
        String propertyWikiData = null, domainWikidata = null, rangeWikidata = null, propertyDBpedia = null, domainDBpedia = null, rangeDBpedia = null;
        SparqlQuery sparqlQuery = null;
        Boolean flag = false;
        for (String[] row : rows) {
            if (firstRow == 0) {
                firstRow = firstRow + 1;
                resultRows.add(row);
                continue;
            }
            propertyDBpedia = row[referenceIndex];
            domainDBpedia = row[domainIndex];
            rangeDBpedia = row[rangeIndex];

            propertyWikiData = findWikiProperty(propertyDBpedia, filterFlag);
            domainWikidata = findWikiDomainRange(domainDBpedia);
            rangeWikidata = findWikiDomainRange(rangeDBpedia);

            if (!syntactFrame.contains(NounPredicateFrame) && propertyWikiData != null
                    && domainDBpedia != null && rangeWikidata != null) {
                if(domainDBpedia.length()<=1||rangeWikidata.length()<=1){
                 flag=false;   
                }
                else
                 flag = true;
            } else if (syntactFrame.contains(NounPredicateFrame) && propertyWikiData != null
                    && domainDBpedia != null) {
                
                flag = true;
            } 
            else {
                flag = false;
            }

            if (flag) {
                row[referenceIndex] = propertyWikiData;
                row[domainIndex] = domainWikidata;
                if (!syntactFrame.contains(NounPredicateFrame)) {
                    row[rangeIndex] = rangeWikidata;
                }
                String[] copiedArray = row.clone();
                System.out.println("propertyDBpedia::" + propertyDBpedia + " propertyWikiData:" + propertyWikiData);
                resultRows.add(copiedArray);
            }

        }
        return resultRows;
    }

    private String findWikiProperty(String propertyDBpedia, Boolean filterFlag) {
        String propertyWikiData = null;

        if (propertyDBpedia.contains("dbo:") || propertyDBpedia.contains("dbp:")) {
            propertyDBpedia = Property.withPrefix(propertyDBpedia);
            SparqlQuery sparqlQuery = new SparqlQuery(sparqlEndpoint, propertyDBpedia, filterFlag);
            propertyWikiData = sparqlQuery.getWikidataPropertyFirst();
            if (propertyWikiData != null) {
                propertyWikiData = this.prefixFilter(propertyWikiData);
            }
            /*else {
                propertyWikiData =  Property.shortPrefix(propertyDBpedia);
            }*/

        }
        /*else {
            propertyWikiData = Property.shortPrefix(propertyDBpedia);
        }*/
        return propertyWikiData;

    }

    private String findWikiDomainRange(String domainDBpedia) {
        String domainWikidata = null;
        //System.out.println(this.domainRangeWikidata);

        //if (domainDBpedia.contains("dbo:") || domainDBpedia.contains("dbp:")) {
        if (this.domainRangeWikidata.containsKey(domainDBpedia)) {
            domainWikidata = this.domainRangeWikidata.get(domainDBpedia);
        } /*else {
            domainWikidata = Property.shortPrefix(domainDBpedia);
        }*/
        return domainWikidata;

    }

    private String prefixFilter(String propertyWikiData) {
        for (String key : this.prefixes.keySet()) {
            String shortForm = this.prefixes.get(key);
            //System.out.println(propertyWikiData);
            if (propertyWikiData.contains(key)) {
                propertyWikiData = propertyWikiData.replace(key, shortForm + ":");
                break;
            }
        }
        return propertyWikiData;
    }

    public void findSyntacticFrame(List<String[]> rows) throws Exception {
        String syntactType = null;

        Integer i = 0, index = 0;
        for (String[] row : rows) {
            if (i == 0) {
                i = i + 1;
                continue;

            } else {
                String nounPPFrameIndex = row[5];
                String nounPredicateFrameIndex = row[4];
                String tranPPFrameIndex = row[6];
                String inTranPPFrameIndex = row[7];
                String adjectiveAttributiveFrame = row[3];
                if (nounPPFrameIndex.equals(NounPPFrame) || nounPPFrameIndex.equals(AdjectiveSuperlativeFrame)) {
                    syntactFrame = nounPPFrameIndex;
                    referenceIndex = 9;
                    domainIndex = 10;
                    rangeIndex = 11;
                }
                if (nounPredicateFrameIndex.equals(NounPredicateFrame)) {
                    syntactFrame = nounPredicateFrameIndex;
                    referenceIndex = 8;
                    domainIndex = 9;
                    rangeIndex = 10;
                } else if (tranPPFrameIndex.equals(TransitiveFrame)) {
                    syntactFrame = tranPPFrameIndex;
                    referenceIndex = 10;
                    domainIndex = 11;
                    rangeIndex = 12;
                } else if (inTranPPFrameIndex.equals(InTransitivePPFrame)) {
                    syntactFrame = inTranPPFrameIndex;
                    referenceIndex = 11;
                    domainIndex = 12;
                    rangeIndex = 13;
                } else if (adjectiveAttributiveFrame.equals(AdjectiveAttributiveFrame)) {
                    syntactFrame = adjectiveAttributiveFrame;
                    referenceIndex = 8;
                    domainIndex = 9;
                    rangeIndex = 10;
                } else {
                    System.out.println("no syntactic frame is found!!");
                    //syntacticFrame = row[GoogleXslSheet.TransitFrameSyntacticFrameIndex];
                }
                if (index != 0) {
                    break;
                }
            }

        }

    }

    public List<String[]> getResultRows() {
        return resultRows;
    }

    public List<String[]> findWikidataClass(String dir, Conversion conversion) throws Exception {
        File[] classFiles = new File(dir + "z-range-domain/").listFiles();
        Set<String> classes = new HashSet<String>();

        for (File classFile : classFiles) {
            if (classFile.getName().contains("-output")) {
                continue;
            }
            CsvFile csvFile = new CsvFile();
            List<String[]> rows = csvFile.getRows(classFile);
            for (String[] row : rows) {
                String domain = row[0];
                String range = row[1];
                classes.add(domain);
                classes.add(range);
            }

        }

        List<String[]> resultRows = new ArrayList<String[]>();
        Integer index = 0;
        for (String key : classes) {
            String wikidataKey = conversion.findWikiDomainRange(key);
            resultRows.add(new String[]{key, wikidataKey});
            index = index + 1;
            System.out.println("index::" + index + " total::" + classes.size() + " " + key + " " + " " + wikidataKey);
        }
        return resultRows;
    }

    public void getWikidataClasses(String dir) throws Exception {
        CsvFile csvFile = new CsvFile();
        File[] classFiles = new File(dir + "z-range-domain/").listFiles();
        List<String[]> resultRows = new ArrayList<String[]>();
        for (File classFile : classFiles) {
            if (classFile.getName().contains("-output")) {
                resultRows = csvFile.getRows(classFile);
                break;
            }
        }

        for (String[] row : resultRows) {
            String key = row[0];
            String value = Property.shortPrefix(row[1]);
            if (value.contains("dbo:") || value.contains("dbp:")) {
                //System.out.println(key + "::" + value);
                continue;
            }

            this.domainRangeWikidata.put(row[0], row[1]);
        }

    }

    public static void main(String[] args) throws Exception {
        String sparqlEndpoint = "https://dbpedia.org/sparql";
        Boolean classFlag = false;
        String partsOfSpeech[] = new String[]{"nouns/", "verbs/", "adjective/"};
        String dir = "lexicon-wikidata/en/";
        Boolean filterFlag = true;
        CsvFile csvFile = new CsvFile();
        File fileDataSet = new File("dataset/wikidata.json");
        LinkedData linkedData = FileProcessUtils.getLinkedDataConf(fileDataSet);
        linkedData.reversePrefixes();
        Conversion conversion = new Conversion(sparqlEndpoint, linkedData.getReversePrefixes());

        if (classFlag) {
            List<String[]> resultRows = conversion.findWikidataClass(dir, conversion);
            csvFile.writeToCSV(new File(dir + "z-range-domain/wikidata-output.csv"), resultRows);
        }

        conversion.getWikidataClasses(dir);

        for (String partDir : partsOfSpeech) {
            String inputDir = dir + partDir;
            String[] files = new File(inputDir).list();
            for (String fileName : files) {
                if (fileName.contains(".csv") && !fileName.contains("-output")) {
                    File inputFile = new File(inputDir + fileName);
                    csvFile = new CsvFile();
                    List<String[]> rows = csvFile.getRows(inputFile);
                    conversion.findSyntacticFrame(rows);
                    conversion.getWikidata(rows, filterFlag);
                    File outputFile = new File(inputDir + fileName.replace(".csv", "-output.csv"));
                    csvFile.writeToCSV(outputFile, conversion.getResultRows());
                }
            }
        }
        System.out.println("Hellow World!!");

    }

}
