/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.main;

import java.util.Set;
import java.util.TreeSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.linkeddatafragments.model.LinkedDataFragmentGraph;

/**
 *
 * @author elahi
 */
public interface Constants {

    public static final String questionsFile = "questions";
    public static final String summaryFile = "summary";
    public static final String MAKE_PROPERTY_FILE = "MAKE_PROPERTY_FILE";

    public static final String FIND_QALD_ANSWER = "FIND_QALD_ANSWER";
    public static final String FIND_NUMBER_QUESTIONS = "FIND_NUMBER_QUESTIONS";

    public static final String ANSWER_SELECTED = "ANSWER_SELECTED";
    public static final String FIND_SIMILARITY = "FIND_SIMILARITY";
    public static final String FIND_QALD_QUEGG_ANSWER = "FIND_QALD_QeeGG_ANSWER";
    public static final String FIND_NUMBER_OF_QUESTION = "FIND_NUMBER_OF_QUESTION";
    public static final String EVALUTE_QALD_QUEGG = "EVALUTE_QALD_QUEGG";
    public static final String ORIGINAL = "ORIGINAL";
    public static final String BOG = "BOG";
    public static final String PROTOTYPE_QUESTION = "PROTOTYPE_QUESTION";
    public static final String REAL_QUESTION = "REAL_QUESTION";
    public static LinkedDataFragmentGraph ldfg = new LinkedDataFragmentGraph("http://data.linkeddatafragments.org/dbpedia2014");
    public static Model model = ModelFactory.createModelForGraph(ldfg); 
    public static String configFile = "conf/inputConf.json";
    public static String singleTripeFile = "conf/oneTriple.txt";
    public static String dataSetConfFile = "conf/dbpedia.json";   
    
    /*
     public static final String questionsFile = "questions";
    public static final String summaryFile = "summary";
    public static final String FIND_QALD_ANSWER = "FIND_QALD_ANSWER";
    public static final String FIND_NUMBER_QUESTIONS = "FIND_NUMBER_QUESTIONS";

    public static final String ANSWER_SELECTED = "ANSWER_SELECTED";
    public static final String FIND_SIMILARITY = "FIND_SIMILARITY";
    public static final String FIND_QALD_QUEGG_ANSWER = "FIND_QALD_QeeGG_ANSWER";
    public static final String EVALUTE_QALD_QUEGG = "EVALUTE_QALD_QUEGG";
    public static final String ORIGINAL = "ORIGINAL";
    public static final String BOG = "BOG";
    public static final String PROTOTYPE_QUESTION = "PROTOTYPE_QUESTION";
    public static final String REAL_QUESTION = "REAL_QUESTION";
    public static LinkedDataFragmentGraph ldfg = new LinkedDataFragmentGraph("http://data.linkeddatafragments.org/dbpedia2014");
    public static Model model = ModelFactory.createModelForGraph(ldfg); 
    public static String configFile = "conf/inputConf.json";
    public static String singleTripeFile = "conf/oneTriple.txt";
    public static String dataSetConfFile = "conf/dbpedia.json";   
   
    */
   

}
