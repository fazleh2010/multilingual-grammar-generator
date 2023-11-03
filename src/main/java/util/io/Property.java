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
public class Property {

    private static String dboPrefix = "http://dbpedia.org/ontology/";
    private static String dbo_ = "dbo:";
    private static String dbpPrefix = "http://dbpedia.org/property/";
    private static String dbp_ = "dbp:";
    private static String rdfPrefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static String rdf_ = "rdf:";
    private static String resPrefix = "http://dbpedia.org/resource/";
    private static String res_ = "res:";
    private static String yagoPrefix = "http://dbpedia.org/class/yago/";
    private static String yago_ = "yago:";
    

    public static String shortPrefix(String property) {
        property = property.replace(dboPrefix, dbo_);
        property = property.replace(dbpPrefix, dbp_);
        property = property.replace(rdfPrefix, rdf_);
        property = property.replace(resPrefix, res_);
        property = property.replace(yagoPrefix, yago_);
        property=property.replace(" ", "").strip().stripLeading().stripTrailing();
        return property;
    }
    
    public static String onlyProperty(String property) {
        if(property.contains(":")){
            return property.split(":")[1];
        }
        return property;
    }
    
     public static String withPrefix(String property) {
        property = property.replace(dbo_,dboPrefix);
        property = property.replace(dbp_,dbpPrefix);
        property = property.replace(rdf_,rdfPrefix);
        property = property.replace(res_,resPrefix);
        property = property.replace(yago_,yagoPrefix);
        property=property.replace(" ", "");
        return property;
    }


}
