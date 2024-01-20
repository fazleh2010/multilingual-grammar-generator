/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import static java.lang.System.exit;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author elahi
 */
public class FileFolderUtils {

    private static String anchors = "src/main/resources/dbpedia/anchors/";
    private static String input = "input/";
    private static String achorFileTsv = "anchors_sorted_by_frequency.tsv";

    public static void createDirectory(String location) throws IOException {
        Path location_path = Paths.get(location);
        /*if (Files.exists(location_path)) {
            Files.delete(location_path);
        }*/
        Files.createDirectories(location_path);

    }

    public static List<File> getFiles(String fileDir, String ntriple) throws Exception {
        try {
            File dir = new File(fileDir);
            FileFilter fileFilter = new WildcardFileFilter("*" + ntriple);
            File[] files = dir.listFiles(fileFilter);
            return List.of(files);
        } catch (Exception exp) {
            throw new Exception("No file is found in the directory:" + fileDir);
        }

    }

    public static List<File> getSpecificFiles(String fileDir, String category, String extension) {
        List<File> selectedFiles = new ArrayList<File>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(category) && fileName.contains(extension)) {
                    selectedFiles.add(new File(fileDir + fileName));
                }
            }

        } catch (Exception exp) {
            System.err.println("file not found!!");
            return new ArrayList<File>();
        }

        return selectedFiles;
    }

    public static List<File> getSpecificFiles(String fileDir, String extension) {
        List<File> selectedFiles = new ArrayList<File>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(extension)) {
                    selectedFiles.add(new File(fileDir + fileName));
                }
            }

        } catch (Exception exp) {
            System.err.println("file not found!!");
            return new ArrayList<File>();
        }

        return selectedFiles;
    }

    public static List<File> getSpecificFiles(String fileDir, String interestingness, String experiment, String extension) {
        List<File> selectedFiles = new ArrayList<File>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(interestingness) && fileName.contains(experiment) && fileName.contains(extension)) {
                    selectedFiles.add(new File(fileDir + fileName));
                }
            }

        } catch (Exception exp) {
            System.err.println("file not found!!" + exp.getMessage());
            return new ArrayList<File>();
        }

        return selectedFiles;
    }

    /*public static String getFile(String fileDir, String category, String extension) {
        String[] files = new File(fileDir).list();
        for (String fileName : files) {
            if (fileName.contains(category) && fileName.contains(extension)) {
                return fileName;
            }
        }
        return null;
    }*/
    public static List<String> getSelectedFile(String fileDir, String category, String extension) {
        List<String> selectedFiles = new ArrayList<String>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(category) && fileName.contains(extension)) {
                    selectedFiles.add(fileName);
                }
            }
        } catch (Exception exp) {
            System.err.println("file not found!!");
            return new ArrayList<String>();
        }
        return selectedFiles;
    }

    public static List<String> getHash(String fileName) throws FileNotFoundException, IOException {
        List<String> lists = new ArrayList<String>();
        List<String> lines = new ArrayList<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();

                if (line != null) {
                    line = line.replace("http", "\nhttp");
                    lines = IOUtils.readLines(new StringReader(line));
                    for (String value : lines) {
                        //System.out.println("test:" + value);
                        lists.add(value);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }

    public static LinkedHashMap<String, String> fileToHashOrg(String fileName,String seperator) throws FileNotFoundException, IOException {
        LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            //line = reader.readLine();
            while ((line= reader.readLine()) != null) {
                //line = reader.readLine();
                //if (line != null) {
                    if (line.contains(seperator)) {
                        String[] info = line.split(seperator);
                        String key = info[0].trim().stripLeading().stripTrailing().strip();
                        String value = info[1].trim().stripLeading().stripTrailing().strip();
                        hash.put(key, value);
                    }
                    //else
                    //   System.out.println("line::"+line);

                //}

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash;
    }
    
    public static void hashOrgToFile(LinkedHashMap<String, String> orgHash, String fileName) throws FileNotFoundException, IOException {
        BufferedReader reader;
        String str = "";

        for (String key : orgHash.keySet()) {
            String value = orgHash.get(key);
            String line = key + "=" + value+ "\n" ;
            str += line;
        }
        stringToFiles(str, fileName);

    }

    public static Set<String> fileToSet(String fileName) throws FileNotFoundException, IOException {
        Set<String> set = new TreeSet<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                if (line != null) {
                    line = line.strip().trim();
                    set.add(line);
                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }
   

    public static List<String> getList(String fileName) throws FileNotFoundException, IOException {
        List<String> entities = new ArrayList<String>();

        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    String url = line.trim();
                    entities.add(url);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public static LinkedHashMap<String, String> getListString(String fileName) {
        LinkedHashMap<String, String> selectedWords = new LinkedHashMap<String, String>();

        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                //System.out.println(line);
                if (line != null) {
                    String url = line.trim();
                    String[] info = url.split(" ");
                    selectedWords.put(info[1], info[2]);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("the file " + fileName + " does not exist" + e.getMessage());
            e.printStackTrace();
        }
        return selectedWords;
    }

    public static List<String> getSortedList(String fileName, Integer thresold, Integer listSize) throws FileNotFoundException, IOException {
        List<String> words = new ArrayList<String>();
        List<String> finalWords = new ArrayList<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    if (line.contains(" ")) {
                        //System.out.println(line);
                        String[] info = line.split(" ");
                        Integer count = Integer.parseInt(info[0].trim());
                        String word = info[1].trim();

                        if (count > thresold) {
                            //System.out.println(line);
                            words.add(word);
                        }
                    }

                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer size = 0;
        for (String word : words) {

            if (size == listSize) {
                break;
            } else {
                finalWords.add(word);
            }
            size = size + 1;
        }
        //System.out.println("finalWords:" + finalWords.size());
        //System.out.println("finalWords:" + finalWords);

        return finalWords;
    }

    public static void listToFiles(List<String> list, String fileName) {
        String str = "";
        Integer number = -1, index = 0;
        for (String element : list) {
            if (element.contains("http")) {
                index++;
                String line = element + "\n";
                str += line;
                if (index == number) {
                    break;
                }
            }

        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            //System.out.println(str);
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void stringToFiles(String str, String fileName) {       
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeToTextFile(String str, String filename) {
        if (str != null) {
            stringToFiles(str, filename);
        } else {
            return;
        }
    }

    private static Map<String, TreeMap<String, String>> getAlphabetic(String fileName, Set<String> alphabetSets) {
        Map<String, TreeMap<String, String>> alphabeticAnchors = new TreeMap<String, TreeMap<String, String>>();

        BufferedReader reader;
        String line = "";
        String firstLetter = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            Integer index = 0;
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    if (line.contains("\t")) {
                        String[] info = line.split("\t");
                        String anchor = info[1];
                        anchor = anchor.replace("\"", "");

                        String kb = info[0];
                        Character ch = null;
                        String str = null;
                        String alphabetFileName = null;
                        if (anchor.length() >= 1) {
                            ch = anchor.charAt(0);
                            str = String.valueOf(ch).toLowerCase().trim();

                            /*if(!str.endsWith("a"))
                                continue;
                             */
                            if (alphabetSets.contains(str)) {
                                alphabetFileName = anchors + str + ".txt";
                            } else {
                                alphabetFileName = anchors + "other" + ".txt";
                            }

                            index = index + 1;
                            //System.out.println(index + "line= " + line);
                            //anchor=anchor.toLowerCase().replaceAll(" ", "_").strip();
                            //kb=kb.strip();
                            anchor = anchor.stripLeading();
                            line = anchor + " = " + kb;
                            appendToFile(alphabetFileName, line);
                        }
                    }
                }
            }
            //System.out.println("total= " + index);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alphabeticAnchors;
    }

    public static void writeInterestingEntityToJsonFile(Map<String, List<String>> interestingEntitities, String filename) throws IOException, Exception {
        if (interestingEntitities.isEmpty()) {
            throw new Exception("no data found to write in the file!!");
        }
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get(filename).toFile(), interestingEntitities);
    }

    public static void writeInterestingEntityEachToJsonFile(Map<String, List<String>> interestingEntitities, String location) throws IOException, Exception {
        if (interestingEntitities.isEmpty()) {
            throw new Exception("no data found to write in the file!!");
        }
        location = location.replace(".json", "");
        String str = "";
        FileFolderUtils.createDirectory(location);
        for (String word : interestingEntitities.keySet()) {
            String finalFileName = location + word + ".json";
            //System.out.println("finalFileName:" + finalFileName);
            List<String> entityList = interestingEntitities.get(word);
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(Paths.get(finalFileName).toFile(), entityList);
            String line = word + " " + word + ".json" + "\n";
            str += line;
        }
        FileFolderUtils.stringToFiles(str, location + "AAClass.txt");

    }

    public static List<String> readInterestingEntityEachToJsonFile(File file) throws IOException, Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<String> entityList = mapper.readValue(file, new TypeReference<List<String>>() {
        });
        return entityList;
    }

 
    public static void writeDictionaryToJsonFile(Map<String, Map<Integer, String>> units, String fileName) throws Exception {
        if (units.isEmpty()) {
            throw new Exception("no data found to write in the json file!!");
        }
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get(fileName).toFile(), units);
    }

    

    public static String urlUnicodeToString(String url) throws Exception {
        URI uri = new URI(url);
        String urlStr = uri.getQuery();
        return urlStr;
    }

    public static String stringToUrlUnicode(String string) throws UnsupportedEncodingException {
        String encodedString = URLEncoder.encode(string, "UTF-8");
        return encodedString;
    }

    public static String readHtmlFile() {

        return null;
    }

    public static void appendToFile(String fileName, String line) {
        File file = new File(fileName);
        boolean b;
        if (file.exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.append(System.getProperty("line.separator"));
                bw.append(line);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            stringToFiles(line, fileName);
        }
    }

    public static String getClassDir(String dbo_Politician) {
        return dbo_Politician.split(":")[1];
    }

   
    public static String getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) + " mb";
    }

    public static String getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024 + "  kb";
    }

    public static String getFileSizeBytes(File file) {
        return file.length() + " bytes";
    }

    public static Boolean isFileBig(File file, Double limit) {
        String fileSize = getFileSizeMegaBytes(file);

        String fileType = null;
        if (fileSize.contains(" ")) {
            String info[] = fileSize.split(" ");
            fileType = info[1];
            if (fileType.contains("mb")) {
                Double fileSizeNumber = Double.parseDouble(info[0]);
                if (fileSizeNumber > limit) {
                    //System.out.println("fileSize:::::::" + fileSize);
                    return true;
                }
            } else {
                return false;
            }
        }

        return false;
    }
    
    public static void deleteFiles(String inputDir, String extension) {
        File f = new File(inputDir);
        String[] pathnames = f.list();
        try{
        for (String pathname : pathnames) {
            String[] files = new File(inputDir + File.separator + pathname).list();
            for (String fileName : files) {
                File file = new File(inputDir + File.separator + pathname + File.separator + fileName);
                if (file.getName().contains(extension)) {
                    if (file.delete()) {
                        //System.out.println(fileName + " deleted successfully");
                    } else {
                        System.out.println("Failed to delete the file");
                    }
                }

            }

        }
        }catch(Exception ex){
            return;
        }

    }

    
    
    public static void main(String[] args) throws IOException {
        String dir = "/home/elahi/AHack/italian/question-grammar-generator/src/main/resources/it/base/";
        String genderFileName = "gender.txt";
        String labeledGenderFileName = "genderLabeled.txt";
        String allFileName = "all.txt";
        String genderNotLabeled = "genderNotLabeled.txt";
        String seperator = "=";
        LinkedHashMap<String, String> map = FileFolderUtils.fileToHashOrg(dir + genderFileName, seperator);
        FileFolderUtils.hashOrgToFile(map, dir+labeledGenderFileName);
        //System.out.println(map);
        List<String> list = getList(dir + allFileName);
        Set<String> set = new HashSet<String>(list);
        System.out.println(set.size());

        //Set<String> common=Sets.intersection(map.keySet(), set);
        //System.out.println(common.size());

        set.removeAll(map.keySet());
         List<String> resultList = new ArrayList<String>(set);
        listToFiles(resultList,dir+genderNotLabeled);
        
        

    }

    public static void setToFile(Set<String> sets, String fileName) {
        String str="";
        for(String string:sets){
            str+=string+"\n";
        }
        stringToFiles(str,fileName);
    }
    
   

}
