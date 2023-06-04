package parser;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi a grammar rule Each grammar rule is essentially a string with
 * one variable $Arg that is passed by a constructor, in addition to a SPARQL
 * Query where the position of the URI to be inserted is marked with $Arg as
 * well. In the constructor we also pass a list of all entities and their URIs
 * that can fill $Arg. 
 */
public class GrammarRule {
    //  question = "Who is the president of "+argument+" s?";
    //    sparql = "SELECT X X president Sub";

    private QAElement qaElement = null;
    private Map<String, String> entityMap = new TreeMap<String, String>();

    public GrammarRule(String rule, String sparql, Map<String, String> entityMap) {
        this.qaElement = new QAElement(rule, sparql);

    }

    public Map<String, String> getEntityMap() {
        return entityMap;
    }

    String getQuestion() {
        return this.qaElement.getQuestion();
    }

    String getSparql() {
        return this.qaElement.getSparql();
    }

}
