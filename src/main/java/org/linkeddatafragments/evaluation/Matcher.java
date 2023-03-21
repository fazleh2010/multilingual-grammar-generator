/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import static java.util.Objects.isNull;
import java.util.Set;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import static org.apache.jena.sparql.syntax.ElementWalker.walk;

/**
 *
 * @author elahi
 */
public class Matcher {
    
    
    public static String getMatchedUri(String sentencePattern, String matchedSentenceItem, Query qaldPARQLQuery) {
        String uriMatch = "";
        if (!isNull(sentencePattern)) {
            String matchedPattern = sentencePattern.toString();
            // Try to find the matching uri for the previously found item inside the QALD SPARQL query
            String lowerCaseMatchedSentenceItem = matchedSentenceItem.toLowerCase().replace(" ", "_");
            uriMatch = findMatchingNodeToQALDSentenceMatchedItem(qaldPARQLQuery, lowerCaseMatchedSentenceItem);
        }
        return uriMatch;
    }
    
    private static String findMatchingNodeToQALDSentenceMatchedItem(Query query, String lowerCasePattern) {
        final String[] match = {""};
        walk(
                query.getQueryPattern(), // ElementGroup
                new ElementVisitorBase() {
            // Go through blocks of triples
            public void visit(ElementPathBlock el) {
                // Go through all triples
                Iterator<TriplePath> triples = el.patternElts();
                while (triples.hasNext()) {
                    TriplePath triplePath = triples.next();
                    // Check for match in subject or object
                    if (triplePath.getSubject().isURI()) {
                        if (triplePath.getSubject().getURI().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getSubject().getURI();
                        }
                    } else if (triplePath.getSubject().isLiteral()) {
                        if (triplePath.getSubject().getLiteral().getValue().toString().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getSubject().getLiteral().getValue().toString();
                        }
                    }
                    if (triplePath.getObject().isURI()) {
                        if (triplePath.getObject().getURI().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getObject().getURI();
                        }
                    } else if (triplePath.getObject().isLiteral()) {
                        if (triplePath.getObject().getLiteral().getValue().toString().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getObject().getLiteral().getValue().toString();
                        }
                    }
                }
            }
        }
        );
        return match[0];
    }
    
    
    public static QALD.QALDQuestions getMatchingOriginalQaldQuestions(QALD qaldOriginal, EntryComparison entryComparison) {
        //System.out.println(qaldOriginal.questions);
        //System.out.println(entryComparison.getQaldEntry());
        
        return qaldOriginal.questions.stream()
                .filter(qaldQuestions -> qaldQuestions.id.equals(entryComparison.getQaldEntry()
                .getId()))
                .findAny()
                .orElseThrow();
    }
    
    public static String filterLexEntry2(String key) {
        key = key.replace("0", "");
        key = key.replace("1", "");
        key = key.replace("2", "");
        key = key.replace("3", "");
        key = key.replace("4", "");
        key = key.replace("5", "");
        key = key.replace("6", "");
        key = key.replace("7", "");
        key = key.replace("8", "");
        key = key.replace("9", "");
        key = key.replace("__", "_");

        if(key.contains("_")){
           key=key.substring(0, key.length());
           System.out.println("key::"+key);
        }
        return key;
    }

    
   
    
}
