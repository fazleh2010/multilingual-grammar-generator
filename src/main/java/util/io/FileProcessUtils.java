/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import linkeddata.LinkedData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gdata.util.common.base.Pair;
import evaluation.Matcher;
import grammar.read.questions.UriLabel;
import static grammar.sparql.SparqlQuery.RETURN_TYPE_SUBJECT;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import static java.lang.System.exit;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import static org.apache.jena.atlas.test.Gen.rand;
import static org.apache.jena.atlas.test.Gen.rand;
import org.linkeddatafragments.util.io.FileUtils;
import static util.io.FileFolderUtils.getHash;
import static util.io.FileFolderUtils.getList;
import static util.io.FileFolderUtils.listToFiles;

/**
 *
 * @author elahi
 */
public class FileProcessUtils {
    

    public static void stringToFile(String content, String fileName)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(content);
        writer.close();

    }
    
    

    public static String fileToString(String fileName) {
        InputStream is;
        String fileAsString = null;
        try {
            is = new FileInputStream(fileName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            fileAsString = sb.toString();
            //System.out.println("Contents : " + fileAsString);
        } catch (Exception ex) {
            Logger.getLogger(FileProcessUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fileAsString;
    }

    public static List<File> getFiles(String fileDir, String category, String extension) {
        String[] files = new File(fileDir).list();
        List<File> selectedFiles = new ArrayList<File>();
        System.out.println(fileDir);
        for (String fileName : files) {
            if (fileName.contains(category) && fileName.contains(extension)) {
                selectedFiles.add(new File(fileDir + fileName));
            }

        }

        return selectedFiles;

    }

    public static Map<String, String> getUriLabelsJson(File classFile) {
        Map<String, String> map = new TreeMap<String, String>();
        Set<String> set = new TreeSet<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(classFile));
            //line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    line = line.strip().trim();
                    if (line.contains("=")) {
                        String uri = line.split("=")[0];
                        String label = line.split("=")[1];
                        map.put(uri, label);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, String[]> getDataFromFile(File file, Integer keyIndex, Integer classIndex, String className) throws IOException {
        Map<String, String[]> map = new TreeMap<String, String[]>();
        String fileType = file.getName();
        if (fileType.contains(".csv")) {
            CsvFile csvFile = new CsvFile(file);
            return csvFile.generateBindingMapL(keyIndex, classIndex, className);
        }
        return new TreeMap<String, String[]>();
    }

    /*public static void deleteFiles(String inputDir, String extension) {
        File f = new File(inputDir);
        String[] pathnames = f.list();
        for (String pathname : pathnames) {
            //System.out.println("pathname::"+inputDir + File.separatorChar + pathname);
            String[] files = new File(inputDir + pathname).list();
            for (String file : files) {
                //System.out.println("file::"+file);
               

            }

        }

    }*/
    public static LinkedData getLinkedDataConf(File file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, LinkedData.class);
    }

    public static InputCofiguration getInputConfig(File file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, InputCofiguration.class);
    }

    public static Map<String, String> tripleFileToHash(String fileName,Integer limit) {
        Map<String, String> results = new TreeMap<String, String>();
        BufferedReader reader;
        String line = "";
        File file = new File(fileName);
        Integer number=0;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                number=number+1;
                String subject = null;
                String object = null, property = null;
                if (line != null) {
                    line = line.replace("<", "\n" + "<");
                    line = line.replace(">", ">" + "\n");
                    line = line.replace("\"", "\n" + "\"");
                    String[] lines = line.split(System.getProperty("line.separator"));

                    Integer index = 0;
                    for (String value : lines) {
                        index = index + 1;
                        if (index == 2) {
                            subject = clean(value);
                        } else if (index == 6) {
                            object = clean(value);
                        }
                    }
                    if (limit == -1)
                        ; else if (number > limit)
                        break;
                    
                    System.out.println("subject:" + subject + " " + "object:" + object);
                    results.put(subject, object);

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;

    }
 
    /*public static void matchLabelsWithEntities(String propertyFile, String labelFileName,Integer numberOfTriples) {
        BufferedReader reader;
        String line = "";
        File file = new File(labelFileName);
        Map<String, String> propertySubObjEntities = tripleFileToHash(propertyFile,-1);
        Map<String, String> propertyObjSubjEntities=reverseHash(propertySubObjEntities);
        Map<String, String> objectLabelHash = new  TreeMap<String, String> ();
       

        List<OffLineResult> offLineResults=new ArrayList<OffLineResult>();
        String content = "";
        Integer lineNumber=0;
        try {
            reader = new BufferedReader(new FileReader(labelFileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                String uri=null,label=null,subjectUri = null;
                String subjectLabel = null, objectUri = null,objectLabel=null;
                if (line != null) {
                    line = line.replace("<", "\n" + "<");
                    line = line.replace(">", ">" + "\n");
                    line = line.replace("\"", "\n" + "\"");
                    String[] lines = line.split(System.getProperty("line.separator"));

                    Integer index = 0;
                    Boolean flag = false;
                    for (String value : lines) {
                        index = index + 1;
                        if (index == 2) {
                            uri = clean(value);
                        } else if (index == 6) {
                            label = clean(value);
                        }
                    }
                    
                    if (!propertySubObjEntities.containsKey(uri)) {
                        subjectUri = null;
                    } else {
                        subjectUri = uri;
                        subjectLabel=label;
                        objectUri = propertySubObjEntities.get(subjectUri);
                    }
                    if (propertyObjSubjEntities.containsKey(uri)) {
                       objectLabelHash.put(uri, label);
                    } 
                    
                    if (subjectUri != null) {
                        lineNumber=lineNumber+1;
                        OffLineResult offLineResult = new OffLineResult(subjectUri, subjectLabel, objectUri, objectLabel,null,null,null,null,null,null);
                       
                        offLineResults.add(offLineResult);
                        
                        if(numberOfTriples==-1)
                            ;
                        else if (lineNumber>=numberOfTriples)
                            break;
                        
                        System.out.println(lineNumber+" subject:" + offLineResult.getSubjectUri() + " subjectLabel:");
       
                    }

                }
            }
           Integer index=0;
            for (OffLineResult offLineResult : offLineResults) {
                String key = offLineResult.getObjectUri().trim().strip().stripLeading().stripTrailing();
                String objectLabel = null;
                //if (objectLabelHash.containsKey(key)) {
                    index=index+1;
                    objectLabel = objectLabelHash.get(key);
                    String fileLine = offLineResult.getSubjectUri() + "=" + offLineResult.getSubjectLabel()
                            + "=" + offLineResult.getObjectUri() + "=" + objectLabel;
                    fileLine = fileLine + "\n";
                    content += fileLine;
                   
                    System.out.println(index+" subject:" + offLineResult.getSubjectUri() + " subjectLabel:" + offLineResult.getSubjectLabel()
                                + " " + " objectUri:" + offLineResult.getObjectUri() + " " + " objectLabel:" + offLineResult.getObjectUri());
       
                //}

            }
            stringToFile(content,propertyFile.replace(".ttl", ".txt") );
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
    
    public static Map<String, OffLineResult> getEntityLabels(String propertyFile) throws Exception {
        Map<String, OffLineResult> entityLabels = new TreeMap<String, OffLineResult>();
        BufferedReader reader;
        String line = "";
        File file = new File(propertyFile);
        String content = "";
        Integer lineNumber = 0;

        /*Set<String> subjectClass=new TreeSet<String>();
        Set<String> objectClass=new TreeSet<String>();

        if (returnSubjOrObj.contains(RETURN_TYPE_SUBJECT)) {
            subjectClass = FileUtils.getClassHash(getClass(classDir, returnType, ".txt"));
            objectClass = FileUtils.getClassHash(getClass(classDir, bindingType, ".txt"));
        } else {
            objectClass = FileUtils.getClassHash(getClass(classDir, returnType, ".txt"));
            subjectClass = FileUtils.getClassHash(getClass(classDir, bindingType, ".txt"));
        }*/
        try {
            reader = new BufferedReader(new FileReader(propertyFile));
            while ((line = reader.readLine()) != null) {
                //line = reader.readLine();
                String subjectUri = null,subjectLabel = null, objectUri = null, objectLabel = null;
                String subjectWiki=null,subjectThum=null,subjectAbstract=null,objectWiki=null,objectThum=null,objectAbstract=null;

                if (line != null) {
                    String[] lines = line.split("=");

                    Integer index = 0;
                    Boolean flag = false;
                    for (String value : lines) {
                        index = index + 1;
                        value = value.replace("<", "");
                        value = value.replace(">", "");
                        value = value.replace("\"", "");
                        if (index == 1) {
                            subjectUri = clean(value);
                        } else if (index == 2) {
                            subjectLabel = cleanLabels(value);
                        }  else if (index == 3) {
                            objectUri = clean(value);
                        } else if (index == 4) {
                            objectLabel = cleanLabels(value);
                        }

                    }

                    lineNumber = lineNumber + 1;
                    
                    //System.out.println(lineNumber+" subject:" + subjectUri + " subjectLabel:" + subjectLabel);
                    //        + " " + " objectUri:" + objectUri + " " + " objectLabel:" + objectLabel);

                    //if (match(subjectClass,subjectUri) && match(objectClass,objectUri)) {
                    
                    /*if (wikilink.containsKey(subjectUri)) {
                        subjectWiki = wikilink.get(subjectUri);
                    } else if (thumbnail.containsKey(subjectUri)) {
                        subjectThum = thumbnail.get(subjectUri);
                    } else if (thumbnail.containsKey(subjectUri)) {
                        subjectAbstract = thumbnail.get(subjectUri);
                    }

                    if (wikilink.containsKey(objectUri)) {
                        objectWiki = wikilink.get(objectUri);
                    } else if (thumbnail.containsKey(objectUri)) {
                         objectThum = thumbnail.get(objectUri);
                    } else if (thumbnail.containsKey(objectUri)) {
                         objectAbstract = thumbnail.get(objectUri);
                    }*/
                    
                        
                    OffLineResult offLineResult = new OffLineResult(subjectUri, subjectLabel, objectUri, objectLabel, subjectWiki, subjectThum, subjectAbstract, objectWiki, objectThum, objectAbstract);
                    /*System.out.println(lineNumber+" subject:" + subjectUri + " subjectLabel:" + subjectLabel
                                + " " + " objectUri:" + objectUri + " " + " objectLabel:" + objectLabel);*/
                    entityLabels.put(line, offLineResult);

                    //}
                    //System.out.println(lineNumber+" subject:" + subjectUri + " subjectLabel:" + subjectLabel
                    //        + " " + " objectUri:" + objectUri + " " + " objectLabel:" + objectLabel);
                }
            }
            reader.close();
        } catch (Exception ex) {
            //ex.printStackTrace();
            throw new Exception("the property not found!! "+ex.getMessage());
        }
        return entityLabels;
    }

    private static String getClass(String classDir, String entity, String extension) {
        return classDir + "dbo_" + entity + extension;
    }



    /*public static Map<String, OffLineResult> matchLabelsWithEntities( String classFileName,String labelFileName) {
        Map<String, OffLineResult> results = new TreeMap<String, OffLineResult>();
        BufferedReader reader;
        String line = "";
        File file = new File(labelFileName);
        Map<String, String> classEntities = tripleFileToHash(classFileName);
        try {
            reader = new BufferedReader(new FileReader(labelFileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                String subject = null;
                String label = null, answer = null;
                if (line != null) {
                    line = line.replace("<", "\n" + "<");
                    line = line.replace(">", ">" + "\n");
                    line = line.replace("\"", "\n" + "\"");
                    String[] lines = line.split(System.getProperty("line.separator"));

                    Integer index = 0;
                    Boolean flag = false;
                    for (String value : lines) {
                        index = index + 1;
                        if (index == 2) {
                            subject = clean(value);
                            if (!classEntities.containsKey(subject)) {
                                subject = null;
                            } else {
                                answer = classEntities.get(subject);
                            }

                        } else if (index == 6) {
                            label = clean(value);
                        }
                    }
                   
                    if (subject != null) {
                        OffLineResult offLineResult=new OffLineResult(subject,label,answer,answer);
                        System.out.println("getSubjectUri():" + offLineResult.getSubjectUri() + " getSubjectLabel:" + offLineResult.getSubjectLabel()
                                + " " + " getObjectUri:" + offLineResult.getObjectUri()+" " + " getObjectLabel:" + offLineResult.getObjectLabel());

                        results.put(subject, offLineResult);
                    }

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }*/

    /*public static Map<String, String> tripleFileToHash(String labelFileName, String classFileName) {
        Map<String, String> results = new TreeMap<String, String>();

        Map<String, String> classEntities = tripleFileToHash(classFileName);
        Map<String, String[]> EntityLabels = findLabels(labelFileName, classEntities);

        System.out.println("EntityLabels::" + EntityLabels.size());
        System.out.println("classEntities::" + classEntities.size());

        return results;

    }*/

    private static String clean(String value) {
        value = value.replace("<", "");
        value = value.replace(">", "");
        //value = value.replace("http://dbpedia.org/resource/", "");
        //value = value.replace("http://dbpedia.org/ontology/", "");
        //value = value.replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");
        //value = value.replace("\"", "");
        value = value.trim().strip().stripLeading().stripTrailing();
        return value;
    }
    
     private static String cleanLabels(String value) {
        value = value.replace("<", "");
        value = value.replace(">", "");
        if(value.contains("^")){
            value=value.split("^")[0];
            
        }
        if(value.contains("@")){
            value=value.split("@")[0];
            
        }
        value=value.replace("\"", "");
        value = value.trim().strip().stripLeading().stripTrailing();
        return value;
    }


    /*public static Map<String, String> tripleFileToHash(String fileName) {
        Map<String, String> results = new TreeMap<String, String>();
        BufferedReader reader;
        String line = "";
        File file = new File(fileName);

        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
                String subject = null;
                String object = null, property = null;
                if (line != null) {
                    String[] lines = line.split(System.getProperty("line.separator"));

                    Integer index = 0;
                    for (String value : lines) {
                        index = index + 1;
                        if (index == 1) {
                            subject = clean(value);
                        } else if (index == 3) {
                            object = clean(value);
                        }
                    }

                    results.put(subject, object);

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;

    }
    
     public static Map<String, String> tripleFileToHash(String labelFileName,String classFileName) {
        Map<String, String> results = new TreeMap<String, String>();
        
        Map<String, String> labels = tripleFileToHash(labelFileName);
        Map<String, String> classEntities = tripleFileToHash(classFileName);
        
        
        System.out.println("labels::"+labels.size());       
        System.out.println("classEntities::"+classEntities.size());

        

        return results;

    }
    
     private static String clean(String value) {
        value=value.trim().strip().stripLeading().stripTrailing();
        return value;
    }
     */
    public static Map<String, List<String>> FileToHashList(String fileName) {
        Map<String, List<String>> results = new TreeMap<String, List<String>>();
        BufferedReader reader;
        String line = "";
        File file = new File(fileName);

        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                System.out.println("line:" + line);
                String subject = null;
                String object = null;
                if (line != null) {
                    line = line.replace("?o", "\n" + "?o");
                    line = line.replace("?s", "\n" + "?s");
                    System.out.println("line:" + line);
                    String[] lines = line.split(System.getProperty("line.separator"));

                    for (String value : lines) {
                        if (value.contains("?o")) {
                            object = StringUtils.substringBetween(value, "<", ">");
                        } else if (value.contains("?s")) {
                            subject = StringUtils.substringBetween(value, "<", ">");
                        }
                    }
                    String property = file.getName().replace(".txt", "");
                    if (property.contains("dbo")) {
                        property = "http://dbpedia.org/ontology/" + property;
                    } else if (property.contains("dbp")) {
                        property = "http://dbpedia.org/property/" + property;
                    }
                    results = parseLine(property, subject, object, results);

                    /*String property=file.getName().replace(".txt", "");
                    String id1=property+"_"+subject+"_"+"?o";
                    List<String> listo=new ArrayList<String>();
                    listo.add(object);
                    String id2=file.getName()+"_"+object+"_"+"?s";
                    List<String> lists=new ArrayList<String>();
                    lists.add(subject);
                    System.out.println("id1:" + id1);
                    System.out.println("id2:" + id2);
                    results.put(id1, listo);
                    results.put(id2, lists);*/
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;

    }

    public static Map<String, List<String>> parseLine(String property, String subject, String object, Map<String, List<String>> results) {
        String id1 = property + "_" + subject + "_" + "?o";
        List<String> listo = new ArrayList<String>();
        listo.add(object);
        String id2 = property + "_" + object + "_" + "?s";
        List<String> lists = new ArrayList<String>();
        lists.add(subject);
        System.out.println("id1:" + id1);
        System.out.println("id2:" + id2);
        results.put(id1, listo);
        results.put(id2, lists);
        return results;

    }

    private static Map<String, String> reverseHash(Map<String, String> propertySubObjEntities) {
        Map<String, String> reverseHash=new TreeMap<String, String>();
        for(String key:propertySubObjEntities.keySet()){
            String value=propertySubObjEntities.get(key);
            reverseHash.put(value, key);
        }
        return reverseHash;
    }

    public static Set<String> getClassHash(String fileName) {
        Set<String> results = new TreeSet<String>();
        String line = "";
        BufferedReader reader = null;
        File file = new File(fileName);
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                line = Matcher.cleanLine(line);
                results.add(line);
            }
            reader.close();
        } catch (Exception ex) {

        }
        return results;

    }

    private static boolean match(Set<String> subjectClass, String subjectUri) throws MalformedURLException {
        subjectUri=Matcher.cleanLine(Matcher.cleanUrl(subjectUri));
        if(subjectClass.contains(subjectUri)){
            return true;
        }
        return false;
    }

    public static Set<String> filetoSet(String fileName) {
          Set<String> results = new TreeSet<String>();
        String line = "";
        BufferedReader reader = null;
        File file = new File(fileName);
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                line = Matcher.cleanLine(line);
                results.add(line);
            }
            reader.close();
        } catch (Exception ex) {

        }
        return results;
    }

  
    public static Set<String> getSetFromFile(String propertyFile) {
        Set<String> results = new TreeSet<String>();

        Path path = Paths.get(propertyFile);

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.contains("=")) {
                    String[] info = line.split("=");
                    String key = Matcher.cleanPrefix(info[0]);
                    results.add(key);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
    
    public static <T> Set<T> findCommonElements(Set<T> common, Set<T> second) {
        common.retainAll(second);
        return common;
    }

    private static Map<String, String> getWikiLinks(String wikilinkFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static Map<String, String> getAbstract(String abstractFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static Pair<Boolean,String[]> findPushEntry(String givenLex, String givenProperty,Integer index) throws IOException {
        System.out.println("hellow world!!");
        File qaldFile = new File("src/main/resources/PushResult.csv");
        CsvFile csvFile = new CsvFile();
        List<String[]> rows = csvFile.getRows(qaldFile);
        for (String[] row : rows) {
            String lex = row[0];
            String frame = row[1];
            String property = row[2];
            String questionT = row[3];
            String sparql = row[4].replace("\n", "");
            sparql = sparql.replace("SELECT", "select");
            sparql = sparql.replace("DISTINCT", "distinct");
            sparql = sparql.replace("WHERE", "where");
            if (lex.contains(givenLex) && property.contains(givenProperty)) {
                System.out.println(lex + " " + property + " " + questionT + " " + sparql);
                String[] record = {lex+"_"+index, questionT, sparql, "answerUri", "answerLabel", "", "single"};
                 return new  Pair<Boolean,String[]>(Boolean.TRUE,record);
            }
        }
       return new  Pair<Boolean,String[]>(Boolean.FALSE,null);
    }
    
    /*public static void main(String[] args) throws IOException {
        System.out.println("hellow world!!");
        File qaldFile = new File("src/main/resources/PushResult.csv");
        CsvFile csvFile = new CsvFile();
        List<String[]> rows = csvFile.getRows(qaldFile);
        String givenLex = "die", givenProperty = "dbo:deathPlace";
        Integer index = 0;
        index = index + 1;
        Pair<Boolean, String[]> pair = findPushEntry(givenLex, givenProperty, index);
        if (pair.first) {
            String[] row = pair.getSecond();
            List<String> rowList=Arrays.asList(row);
            System.out.println(rowList);
        }
    }*/
    
    public static void main(String[] args) throws IOException {
        System.out.println("hellow world!!");
        File qaldFile = new File("src/main/resources/PushResultNew.csv");
        String sortFile = "/media/elahi/Elements/A-project/LDK2023/resources/ldk/sort_data_0.1_03_2023/";
        CsvFile csvFile = new CsvFile();
        List<String[]> rows = csvFile.getRows(qaldFile);
        File[]files=new File(sortFile).listFiles();
        Set<String> properties=new HashSet<String>();
        Set<String> parameters=new HashSet<String>();
        for (File file : files) {
            String[] info = file.getName().split("-raw-");
            //System.out.println(file.getName());
            try {
                if(!file.getName().contains("Anew-")&&!file.getName().contains(".sh")){
                  properties.add(info[0]);
                }
                parameters.add(file.getName());
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println(ex.getMessage());
            }
        }
        String str="";
        String header="#!/bin/sh"+"\n";
        Map<String,List<String>>map=new HashMap<String,List<String>>();
        Integer index=0,lexNumber=1,total=0;
        /*for (String row[] : rows) {
            String lex = row[0].strip().stripLeading().stripTrailing();
            String lexMod=lex.replace(" ", "_");
            String property = row[1].strip().stripLeading().stripTrailing();
            List<String> fileNames=findPropertyFile(property,parameters);            
            for (String fileName : fileNames) {
                   String line = "grep " + "'" + lex + "' " +fileName+ ">>" + "Anew-" + lexMod + "-" + fileName+"\n";
                   str+=line;
                    //System.out.println(grep1);
                    //greps.add(grep1);  
                
            }
            //map.put(lexNumber+"-"+lex, greps);
            //lexNumber=lexNumber+1;
        }
          System.out.println(header + str);
        FileFolderUtils.stringToFiles(header + str, sortFile + "lexicalEntry" + ".sh");
        */
        
        //find qald7 properties....
        Set<String> qald7Properties=findQALD7Properties();  
        // seperate property files...
        makePropertySeperationFile(qald7Properties);
      
       
        
        //System.out.println(sr_2);
        /*index=0;
      
        for (String lex : map.keySet()) {
            List<String> grepsT=map.get(lex);
            String str="";
            for(String line:grepsT){
               str+=line+"\n" ;
            }
           //System.out.println(str);
            FileFolderUtils.stringToFiles(header+str, baseDir+lex+".sh");
        }*/
        
        
            }

    private static List<String> findPropertyFile(String property, Set<String> parameters) {
        List<String> fileNames=new ArrayList<String>();
        for(String paramter: parameters){
            if(paramter.contains(property)){
                fileNames.add(paramter);
            }
        }
        return fileNames;
    }

    private static Set<String> findQALD7Properties() throws IOException {
        String qald7PropertyFile = "src/main/resources/qald7-done-properties.txt";
        String qald7PropertyResult = "src/main/resources/qald7-result.txt";
        String finalOutput="src/main/resources/qald7-properties-output.txt";
        String fileString = FileUtils.fileToString(qald7PropertyFile);
        fileString = fileString.replace("http://:", "\n" + "http://");
        fileString = fileString.replace("dbo:", "\n" + "http://dbpedia.org/ontoloy/");
        fileString = fileString.replace("dbp:", "\n" + "http://dbpedia.org/property/");
        fileString = fileString.replace(" ", "+");
        fileString = fileString.replace("+", "\n");
        fileString = fileString.replace("<", "");
        fileString = fileString.replace(">", "");
        String[] lines = fileString.split("\n");
        Set<String> qald7Properties = new HashSet<String>();
        String sr_2 = "";
        
        for (String line : lines) {
            if (line.contains("http://") || line.contains("http:")) {
                if (!(line.contains("http://dbpedia.org/resource/") || line.contains("http://www.w3.org/"))) {
                    line = line.strip().stripLeading().stripTrailing().trim() + "\n";
                    sr_2 += line;
                    qald7Properties.add(line);
                }

            }
        }
        

        FileFolderUtils.setToFile(qald7Properties, qald7PropertyResult);
        //mannuall filter the file and save it to finalOutput
        qald7Properties = new HashSet<String>();
        qald7Properties = FileFolderUtils.fileToSet(finalOutput);
        return qald7Properties;
    }

    private static String shortForm(String property) {
       property=property.replace("http://dbpedia.org/ontology/", "dbo:");
       property=property.replace("http://dbpedia.org/property/", "dbp:");
       return property;
    }
    private static String largeForm(String property) {
       property=property.replace( "dbo:","http://dbpedia.org/ontoloy/");
       property=property.replace( "dbp:","http://dbpedia.org/property/");
       return property;
    }

    private static void makePropertySeperationFile(Set<String> qald7Properties) {
        String str = "";
        String rawFile = "/media/elahi/Elements/A-project/LDK2023/resources/ldk/raw/z-seperate-property.sh";
        String output = "/media/elahi/Elements/A-project/LDK2023/resources/ldk/property/";
        Set<String> alreadyDone = new HashSet<String>();
        String qald7AlreadyDone = "src/main/resources/qald7-done-properties.txt";
        alreadyDone = findDoneProperties(qald7AlreadyDone);
        System.out.println(alreadyDone.toString());

        Integer index = 0;
        for (String property : qald7Properties) {
                String line = "grep " + "'" + largeForm(property) + "' " + "*.csv" + ">>" + output + "Aproperty-" + shortForm(property) + ".csv" + "\n";
                str += line;
                index = index + 1;
            
        }
        System.out.println(str + "\n" + qald7Properties.size() + " " + alreadyDone.size());
        FileFolderUtils.stringToFiles(str, rawFile);

    }

     public static Set<String> findDoneProperties(String fileName) {
        BufferedReader reader;
        String line = "";
        Set<String> set = new TreeSet<String>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                line = line.replace(" ", "").strip().stripLeading().stripTrailing().trim();
                line=largeForm(line);
                set.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

}
