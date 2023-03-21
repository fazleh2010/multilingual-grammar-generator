/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import com.google.gdata.util.common.base.Pair;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.RatcliffObershelp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.linkeddatafragments.util.io.JaccardSimilarity;

/**
 *
 * @author elahi
 */
public class StringSimilarity {

  
    
    private boolean singleSparql=false;
    private boolean multipleSparql=false;
    private String singleProperty=null;
    private String singleSubject=null;
    private String singleObject=null;

    
    
    public QueGGinfomation getMostSimilarMatch(Map<String, QueGGinfomation> grammarEntities) throws Exception {
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (String question : grammarEntities.keySet()) {
            QueGGinfomation queGGinfomation = grammarEntities.get(question);
            map.put(question, queGGinfomation.getValue());
            System.out.println(question + " " + queGGinfomation);
        }
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
        sorted_map.putAll(map);
        //System.out.println("sorted_map: " + sorted_map);
        String key = sorted_map.firstKey();
        Double value = sorted_map.get(key);

        QueGGinfomation queGGinfomation = grammarEntities.get(key);
        return queGGinfomation;

    }
      public QueGGinfomation getMostSimilarMatchKB(Map<String, QueGGinfomation> grammarEntities,String qaldSparqlQuery) throws Exception {
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (String question : grammarEntities.keySet()) {
            QueGGinfomation queGGinfomation = grammarEntities.get(question);
            map.put(question, queGGinfomation.getValue());
        }
        if(this.isSparqlMatched(grammarEntities,qaldSparqlQuery)){
            
        }
        QueGGinfomation queGGinfomation = this.getBestMatchingWithOutKB(grammarEntities,map);
      
        return queGGinfomation;

    }

    

    // Example implementation of the Levenshtein Edit Distance
    // See http://r...content-available-to-author-only...e.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

   
   

    private QueGGinfomation getBestMatchingWithOutKB(Map<String, QueGGinfomation> grammarEntities, HashMap<String, Double> map) {
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
        sorted_map.putAll(map);
        String key = sorted_map.firstKey();
        Double value = sorted_map.get(key);
        QueGGinfomation queGGinfomation = grammarEntities.get(key);
        return queGGinfomation;
    }

   
    private boolean isSparqlMatched(Map<String, QueGGinfomation> grammarEntities, String qaldSparqlQuery) {
        Boolean sparqlMatchFlag=false;
        for (String question : grammarEntities.keySet()) {
             QueGGinfomation queGGinfomation = grammarEntities.get(question);
             String queGGSparql=queGGinfomation.getSparqlQuery();
             Pair<Boolean,String []> qaldSingleTriple=this.isSingle(queGGSparql);
             Pair<Boolean,String []> queGGSingleTriple=this.isSingle(qaldSparqlQuery);
             
             if(qaldSingleTriple.first&&queGGSingleTriple.first){
               for(Integer index=0;index<qaldSingleTriple.second.length;index++){
                   if(index==0){
                       
                   }
                   
               }
             }
             return true;
        }
        return true;
    }

    ////SELECT DISTINCT ?d WHERE { <http://dbpedia.org/resource/Diana,_Princess_of_Wales> <http://dbpedia.org/ontology/deathDate> ?d . }
    private Pair<Boolean, String[]> isSingle(String queGGSparql) {
        String triples = null;
        Boolean singleSparql = false;
        String singleSubject = null, singleProperty = null;
        String[] values = new String[3];

        triples = StringUtils.substringBetween(queGGSparql, "{", "}");
        Integer numOfTriple = this.characterCount(triples, '.');
        if (numOfTriple == 1) {
            singleSparql = true;
            triples = triples.trim().stripLeading().stripTrailing();
            triples = triples.replace(" ", "\n");
            String[] lines = triples.split(System.getProperty("line.separator"));
            Integer index = 0;
            for (String line : lines) {

                if (index == 0) {
                    if (line.contains("http://dbpedia.org/resource/") || line.contains("res:")) {
                       values[index] = line;
                    }
                } else if (index == 1) {
                    if ((line.contains("http://dbpedia.org/ontology/") || line.contains("http://dbpedia.org/property/") || line.contains("dbo:") || line.contains("dbp:")) && index == 1) {
                        values[index] = line;
                    }
                } else if (index == 2) {
                    if (line.contains("http://dbpedia.org/resource/") || line.contains("res:")) {
                        values[index] = line;
                    }
                }
                index=index+1;

            }

        }
      
        return new Pair<Boolean, String[]>(singleSparql, values);

    }
   
    
    public Integer characterCount(String exampleString, Character symbol) {
       
       
      
        int totalCharacters = 0;
        char temp;
        for (int i = 0; i < exampleString.length(); i++) {

            temp = exampleString.charAt(i);

            if (temp == symbol) {
                totalCharacters=totalCharacters+1;
            }
        }

        return totalCharacters;
    }

    public boolean isMultipleSparqlQuery(String qaldSparqlQuery) {
        String triples = StringUtils.substringBetween(qaldSparqlQuery, "{", "}");
         triples=this.replacePrefix(triples);
        Integer numOfTriple = this.characterCount(triples, '+');
        /*if ((numOfTriple>1) && !triples.contains("rdf:type")) {
            return true;
        } */
        
        if (triples.contains("UNION")) {
            return true;
        } else if (numOfTriple == 2 && !triples.contains("rdf:type")) {
            return true;
        } else if (triples.contains("Philippines")) {
            return true;
        }else if (numOfTriple > 2) {
            return true;
        }

        
        /*if ((numOfTriple == 2) && triples.contains("rdf:type")) {
            return false;
        } else if ((numOfTriple == 2) && !triples.contains("rdf:type")) {
            return true;
        }
        else if ((numOfTriple > 2) ) {
            return true;
        }*/
        return false;
    }
    
    
    boolean isAskSparqlQuery(String qaldSparqlQuery) {
        if(qaldSparqlQuery.contains("ASK")){
            return true;
        }
        return false;
    }

    private String replacePrefix(String exampleString) {
        exampleString = exampleString.replace("\n", " ");
        exampleString = exampleString.replace("http://dbpedia.org/resource/", "res:");
        exampleString = exampleString.replace("http://dbpedia.org/ontology/", "dbo:");
        exampleString = exampleString.replace("http://dbpedia.org/property/", "dbp:");
        exampleString = exampleString.replace("http://dbpedia.org/property/", "dbp:");

        exampleString = exampleString.replace(" . ", "+");
        return exampleString;
    }
    
  
    
     /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public static double zacrdSimilarity(String string1, String string2) {
        RatcliffObershelp ro = new RatcliffObershelp();
        return ro.similarity(string1, string2);
    }
    
    public static double cosineSimilarity(String string1, String string2) {
        Cosine cosine = new Cosine(2);
        Map<String, Integer> profile1 = cosine.getProfile(string1);
        Map<String, Integer> profile2 = cosine.getProfile(string2);
        return cosine.similarity(profile1, profile2);
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    /*public static double generalSimilarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
         }
      
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }*/



   public static void mainLast(String[] args) {
        HashMap<String, Double> map = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);

        map.put("A", 99.5);
        map.put("B", 67.4);
        map.put("C", 67.4);
        map.put("D", 67.3);

        System.out.println("unsorted map: " + map);
        sorted_map.putAll(map);
        System.out.println("results: " + sorted_map);
        
        CharSequence charSequenceLeft = new StringBuffer("baeldung");
        CharSequence charSequenceRight = new StringBuffer("baeldung");


        
    }

    public static void main(String[] args) {
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        String s1 = "What is the ingredient of a Chocolate chip cookie?";
        String s2 = "What is in a chocolate chip cookie?";
        String s3 = "Who wrote Hotel California?";
        String s4 = "Who wrote the song Hotel California?";
        String s5 = "In which time zone is Rome?";
        String s6 = "What is the time zone of Rome?";
        String s7 = "Give me the currency of China.";
        String s8 = "Give me all currencies of China.";
        String s9="What is the location of the Houses of Parliament?";
        String s10="What is the location of the Palace of Westminster?";
        String s11="What is the country of Sitecore?";
        String s12="What country is Sitecore from?";
        String s13="In which time zone is Rome?";
        String s14="what is the time zone of Rome?";
        String s15="In what city is the Heineken brewery?";
        String s16="In what city is the Heineken located?";
        String s17="What is the timezone in San Pedro de Atacama?";
        String s18="What is the time zone of San Pedro de Atacama?";
        
        String s19="Which books by Kerouac were published by Viking Press?";
        String s20="Which books of Kerouac were published by Viking Press?";
        
        String test2="List all episodes of the first season of the HBO television series The Sopranos!";
        String test3="List all episodes of the television series The Sopranos.";
        
        test3="List all episodes of the first season of the television series The Sopranos.";
        
        
        
        
        /*CharSequence left = new StringBuffer(s1);
        CharSequence right= new StringBuffer(s2);
        Double result=jaccardSimilarity.calculateJaccardSimilarity( left,  right);
        System.out.println("Hellow World!!"+result);*/
        System.out.println("s1 and s2:::" + jaccardSimilarityManual(s1, s2));
        System.out.println("s3 and s4:::" + jaccardSimilarityManual(s3, s4));
        System.out.println("s5 and s6:::" + jaccardSimilarityManual(s5, s6));
        System.out.println("s7 and s8:::" + jaccardSimilarityManual(s7, s8));
        System.out.println("s9 and s10:::" + jaccardSimilarityManual(s9, s10));
        System.out.println("s11 and s12:::" + jaccardSimilarityManual(s11, s12));
        System.out.println("s13 and s14:::" + jaccardSimilarityManual(test2, test3));
        System.out.println("s13 and s14:::" + jaccardSimilarityManual("How many companies were founded in the same year as Google?", 
                                                                      "How many companies were founded by Google?"));
        
        System.out.println("s13 and s14:::" + jaccardSimilarityManual("which persons were born in Philippines?", 
                                                                      "Which professional surfers were born on the Philippines?"));

         System.out.println("s13 and s14:::" + jaccardSimilarityManual("Who was the wife of Abraham Lincoln?", 
                                                                       "Who was the wife of U.S. president Lincoln?"));





    }

    public static double jaccardSimilarityManual(String string1, String string2) {
        string1 = process(string1).toLowerCase();
        string2 = process(string2).toLowerCase();
        String stringOne[] = string1.split(" ");
        String stringTwo[] = string2.split(" ");
        List<String> test1 = new ArrayList<String>();
        Set<String> setOne = new LinkedHashSet<String>(Arrays.asList(stringOne));
        Set<String> settwo = new LinkedHashSet<String>(Arrays.asList(stringTwo));

        Set<String> intersection = new LinkedHashSet<String>(Arrays.asList(stringOne));
        intersection.retainAll(settwo);

        setOne.addAll(settwo);
        //System.out.println(intersection + " " + setOne);
        int inter = intersection.size();
        int union = setOne.size();
        //System.out.println(inter + " " + union);
        return (double) (inter) / (double) (union);
    }

    public static String process(String string) {
        string = string.replace("?", " ?");
        return string = string.replace(".", " .");
    }

}

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;

    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
    
  
    
    
}

  
