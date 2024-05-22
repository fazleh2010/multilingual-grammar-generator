/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

/**
 *
 * @author elahi
 */
public class Spliter {
    private String test="PREFIX res: <http://dbpedia.org/resource/> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX onto: <http://dbpedia.org/ontology/> PREFIX prop: <http://dbpedia.org/property/> SELECT DISTINCT ?uri WHERE { ?uri rdf:type onto:Film { ?uri prop:starring res:Tom_Cruise } UNION { ?uri onto:starring res:Tom_Cruise } OPTIONAL { ?uri rdfs:label ?string FILTER ( lang(?string) = \"en\" ) } }";
    public static String splitString(String value){
          if(value.contains("/"))
                return value.split("/")[0];
          return value;
    } 
}
