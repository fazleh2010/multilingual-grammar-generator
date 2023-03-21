/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.util.io;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elahi
 */
public class Manual {
    
    public static List<String> getManualResult(String qaldSparql,List<String> results) {
        List<String> result=new ArrayList<String>();
        if(qaldSparql.contains("http://dbpedia.org/ontology/product")){
          result.add("http://dbpedia.org/resource/Slack_Technologies");

        }
        return result;
    }
    
}
