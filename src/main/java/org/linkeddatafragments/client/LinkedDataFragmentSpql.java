/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.client;

import com.google.common.base.Stopwatch;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import static org.fest.assertions.Assertions.assertThat;
import org.linkeddatafragments.model.LinkedDataFragmentGraph;
import org.linkeddatafragments.util.io.FileUtils;
import org.linkeddatafragments.util.io.Manual;

/**
 *
 * @author elahi
 */
public class LinkedDataFragmentSpql {
    
    private static LinkedHashSet<String> classNames=new LinkedHashSet<String>();
    
    static{
       classNames.add("River");
       classNames.add("WineRegion");
       classNames.add("Place");
       classNames.add("Work");
       classNames.add("Ship");
       classNames.add("MilitaryUnit");
       classNames.add("ArchitecturalStructure");
       classNames.add("Mountain");
       classNames.add("MilitaryConflict");
       classNames.add("Event");
       classNames.add("Award");
       classNames.add("LaunchPad");
       classNames.add("Stadium");
    }
    

    private static LinkedDataFragmentGraph ldfg = new LinkedDataFragmentGraph("http://data.linkeddatafragments.org/dbpedia2014");
    protected static Model model = ModelFactory.createModelForGraph(ldfg);
    
    public static void main(String[] args) {
        String fileName = "/home/elahi/AHack/general/Client.Java/conf/";
        //String queryString = "select  ?s ?o    {    ?s <http://dbpedia.org/ontology/birthPlace>  ?o .    } Limit 100000";
        //String queryString = "select  ?s ?o    {    ?s <http://dbpedia.org/ontology/birthPlace>  ?o .    } Limit 100";
        for (String className : classNames) {
            String queryString = "select  ?s  ?label {     \n"
                    + "  ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>   <http://dbpedia.org/ontology/" + className + ">  .\n"
                    + "  ?s <http://www.w3.org/2000/01/rdf-schema#label> ?label .\n"
                    + "    filter langMatches(lang(?label),\"en\")"
                    + "} Limit 500000";
            fileName = fileName + "dbo_" + className + ".csv";
            LinkedDataFragmentSpql LinkedDataFragmentSpql = new LinkedDataFragmentSpql(queryString, fileName, className);
        }
                exit(1);


    }

    public LinkedDataFragmentSpql(String queryString,String fileName,String className) {
        List<String> results = this.sparqlObjectAsVariable(queryString,className);
       FileUtils.listToFiles(results, fileName);

        //List<String> parsedResults = this.parseResultList(results);
        //System.out.println("results::" + results);
        //System.out.println("parsedResults::" + parsedResults);
        //List<String> results this.saveInFile(results);

    }

    public LinkedDataFragmentSpql(Model modelT, String endpoint, String queryString) {
         model = modelT;
         this.sparqlObjectAsVariable(queryString);
    }

  
    
     public List<String> sparqlObjectAsVariable(String queryString,String className) {
        List<String> results = new ArrayList<String>();
        Query qry = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(qry, model);
        ResultSet rs = qe.execSelect();
        Integer index=0;
        while (rs.hasNext()) {
            String result = rs.nextSolution().toString();
            
            index=index+1;
            result=parseLine(result, className);
            results.add(result);
            //System.out.println(index+" "+result);
        }
        return results;
    }
     
    public List<String> sparqlObjectAsVariable(String qaldSparql) {
        List<String> results = new ArrayList<String>();

        System.out.println(qaldSparql);
        Query qry = QueryFactory.create(qaldSparql);
        QueryExecution qe = QueryExecutionFactory.create(qry, model);
        try {
            ResultSet rs = qe.execSelect();
            Integer index = 0;

            while (rs.hasNext()) {
                String result = rs.nextSolution().toString();
                index = index + 1;
                results.add(result);
                System.out.println(result);
            }
        } catch (Exception ex) {
            return  new ArrayList<String>(); 
        }
        return results;
    }
    
    public List<String> parseResultList(List<String> results) {

        List<String> parsedResult = new ArrayList<String>();
        for (String result : results) {
            result=StringUtils.substringBetween(result, "( ",  " )");
            if (result.contains(",")) {
                String[] info = result.split(",\\s*"); // split on commas
                for (String value : info) {
                    if (value.contains("=")) {
                        String[] info1 = value.split("=");
                        value = info1[1];
                        if (value.contains("<")) {
                            value = value.replace("<", "");
                            value = value.replace(">", "");
                        }
                        value = value.strip().trim().trim().stripLeading().stripTrailing();
                        parsedResult.add(value);
                    }
                }
            } else {
                String value = result;
                if (value.contains("=")) {
                    String[] info1 = value.split("=");
                    value = info1[1].strip().trim().stripLeading().stripTrailing();
                    /*if (value.contains("<")) {
                        value = value.replace("<", "");
                        value = value.replace(">", "");
                    }*/
                    parsedResult.add(value);

                }

            }
        }

        return parsedResult;
    }
    


    public List<String> parseResultListT(List<String> results) {

        List<String> parsedResult = new ArrayList<String>();
        for (String result : results) {
            if (result.contains(",")) {
                String[] info = result.split(",\\s*"); // split on commas
                for (String value : info) {
                    if (value.contains("=")) {
                        String[] info1 = value.split("=");
                        value = info1[1];
                        if (value.contains("<")) {
                            value = value.replace("<", "");
                            value = value.replace(">", "");
                        }
                        value = "(" + value.strip().trim();
                        parsedResult.add(value);
                    }
                }
            } else {
                String value = result;
                if (value.contains("=")) {
                    String[] info1 = value.split("=");
                    value = info1[1];
                    if (value.contains("<")) {
                        value = value.replace("<", "");
                        value = value.replace(">", "");
                    }
                    value = "(" + value.strip().trim();
                    parsedResult.add(value);

                }

            }
        }

        return parsedResult;
    }
    
    public static String parseLine(String line, String className) {
        //"http://dbpedia.org/resource/Heineken","Heineken","Food"
        String uri=null;
        String label=null;
        String result=null;

        if (line != null) {
            line = line.replace("?o", "\n" + "?o");
            line = line.replace("?s", "\n" + "?s");
            line = line.replace("?label", "\n" + "( ?label");
            //System.out.println("line:" + line);
            String[] lines = line.split(System.getProperty("line.separator"));

            for (String value : lines) {
                /*if (value.contains("?o")) {
                    object = StringUtils.substringBetween(value, "<", ">");
                } else */
                if (value.contains("?s")||value.contains("?o")) {
                    uri = StringUtils.substringBetween(value, "<", ">");
                }
                if (value.contains("?label")) {
                    label = StringUtils.substringBetween(value, "=","@en");
                    label=label.stripLeading().strip().stripTrailing().trim();
                    if(label.contains("@")){
                       label=label.replace("@en", "");
                       label=label.replace("\"", "");
                    }
                }
            }
             result = "\""+uri+"\""+ ","+label+ ","+ "\""+className+"\"";

        }

      
        return result;

    }

    private void saveInFile(List<String> results) {
        List answers=new ArrayList<String>();
        for (String result : results) {
            System.out.println("result::" + result);
            answers.add(result);

        }
        
        System.out.println(answers.size());
        
    }

    

  
}
