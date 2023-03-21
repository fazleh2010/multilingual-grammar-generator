/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class QueGGinfomation {

    private String id = null;
    private String question = null;
    private String queGGSparqlQuery = null;
    private String qaldSparqlQuery = null;
    /*private String answerUri = null;
    private String answerLabel = null;*/
    private Boolean duplicate = true;
    //private String syntacticFrame = null;
    private Double similarityScore = null;
    public  static Set<String > duplicateUris=new TreeSet<String>();
    
    static
    {
     duplicateUris.add("25");
     duplicateUris.add("35");
     duplicateUris.add("136");
     duplicateUris.add("171");
     duplicateUris.add("173");

    }


    public QueGGinfomation(String[] row, Double value, String qaldSparqlQuery,String qaldId) {
        this.id = row[0];

        this.question = row[1];
        this.queGGSparqlQuery = checkSparqlQuery(qaldId, qaldSparqlQuery, row[2]);

        //this.getSparqlQuery(id,qaldSparqlQuery,row[2]);
        //this.answerUri = row[3];
        //this.answerLabel = row[4];
        //this.syntacticFrame = row[5];        
        this.similarityScore = value;
        this.qaldSparqlQuery = qaldSparqlQuery;
    }
    
   
   /* public QueGGinfomation(String id, String question, String queGGSparqlQuery, String qaldSparqlQuery,Double value) {
        this.id = id;
        this.question = question;
        //this.queGGSparqlQuery = row[2];
        this.getSparqlQuery(id,qaldSparqlQuery,queGGSparqlQuery);
        //this.answerUri = row[3];
        //this.answerLabel = row[4];
        //this.syntacticFrame = row[5];        
        this.similarityScore = value;
        this.qaldSparqlQuery = qaldSparqlQuery;
    }*/
   
    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getSparqlQuery() {
        return queGGSparqlQuery;
    }

    /*public String getAnswerUri() {
        return answerUri;
    }

    public String getAnswerLabel() {
        return answerLabel;
    }

    public String getSyntacticFrame() {
        return syntacticFrame;
    }*/

    public Double getValue() {
        return similarityScore;
    }

    public String getQueGGSparqlQuery() {
        return queGGSparqlQuery;
    }

    public String getQaldSparqlQuery() {
        return qaldSparqlQuery;
    }

    public Boolean getDuplicate() {
        return duplicate;
    }

    public Double getSimilarityScore() {
        return similarityScore;
    }

    @Override
    public String toString() {
        return "QueGGinfomation{" + "id=" + id + ", question=" + question + ", queGGSparqlQuery=" + queGGSparqlQuery + ", qaldSparqlQuery=" + qaldSparqlQuery + ", answerUri=" + ", similarityScore=" + similarityScore + '}';
    }

    static  {
       
        
        /*properties = new TreeSet<String>();
        properties.add("dbo:date");
        properties.add("dbp:date");
        duplicateUris.put("1", properties);*/

        /*if (duplicateUris.containsKey(this.question)) {
                properties = duplicateUris.get(this.question);
                properties.add(this.queGGSparqlQuery);
                 duplicateUris.put(this.question, properties);
            } else {               
                properties.add(this.queGGSparqlQuery);
                duplicateUris.put(this.question, properties);
            }*/
    }
    
     private static boolean isMatched(String qaldSparql,String queggSparql) {
        qaldSparql=findProperty(qaldSparql);
        queggSparql=findProperty(queggSparql);
        if(qaldSparql.contains(queggSparql)){
            return true;
        }
        return false;

    }

    public static String checkSparqlQuery(String id, String qaldQ, String queggQ) {
        //id = id.replace("http://localhost:8080#", "");
        //id = id.split("_")[0].replace("\"", "");
        Boolean flag = false;
        for (String key : duplicateUris) {
            if (id.contains(key)) {
                flag = true;
            }
        }

        //duplicateUris.add("35");   
        //id = id.replace("http://localhost:8080#", "");
        //id = id.split("_")[0].replace("\"", "");
        if (flag) {
            String valueQueGG = findProperty(qaldQ);
            String tagQald = findProperty(queggQ);
                    /*System.out.println("qaldQ::" + qaldQ + " queggQ::" + queggQ);

                    System.out.println("id::"+id+"  tagQald::" + tagQald + " valueQueGG::" + valueQueGG);*/
           
            if (tagQald!=null&&(!tagQald.contains(valueQueGG))) {
                //System.out.println(id+" tagQald::" + tagQald + " valueQueGG::" + valueQueGG);
                //System.out.println("qaldQ::" + qaldQ + " queggQ::" + queggQ);
                queggQ = queggQ.replace(tagQald, fullproperty(valueQueGG));
                //System.out.println("queggQ::" + queggQ);
                return queggQ;
            }
        }

        return queggQ;
    }

    private static String findProperty(String qaldSparql) {
        qaldSparql = qaldSparql.replace("\n", "").stripLeading().stripLeading().strip().trim();
        qaldSparql = StringUtils.substringBetween(qaldSparql, "{", "}");
        qaldSparql = qaldSparql.stripLeading().stripLeading().strip().trim();
        String[] qaldSparqlLines = qaldSparql.split(" ");
        Integer index = 0;
        for (String key : qaldSparqlLines) {

            if (isProperty(key, index)) {
                return key;
            }
            index = index + 1;
        }
        return null;
    }
    
    private static Boolean isProperty(String qaldSparql, Integer index) {
        if (index != 1) {
            return false;
        }
        qaldSparql = qaldSparql.replace("http://dbpedia.org/ontology/", "dbo:");
        qaldSparql = qaldSparql.replace("http://dbpedia.org/property/", "dbp:");
        if (qaldSparql.contains("dbo:") || qaldSparql.contains("dbp:")) {
            return true;
        }
        return false;

    }
    private static String fullproperty(String property) {
        property = property.replace("dbo:", "http://dbpedia.org/ontology/");
        property = property.replace("dbp:", "http://dbpedia.org/property/");
        return "<"+property+">";

    }


}
