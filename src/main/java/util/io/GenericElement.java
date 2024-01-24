/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.datasets.sentencetemplates.TempConstants;
import grammar.structure.component.FrameType;
import java.util.List;
import util.exceptions.QueGGMissingFactoryClassException;
import util.io.ParamterFinder;

/**
 *
 * @author elahi
 */
public class GenericElement implements TempConstants {

    public static final String PreTerminalNounSingular = "[NounSingular]";
    public static final String PreTerminalNounPlural = "[NounPlural]";
    public static final String PreTerminalVerbParticiple = "[VerbParticiple]";
    public static final String PreTerminalVerbPresent = "[VerbPresent]";
    public static final String PreTerminalVerbPast = "[VerbPast]";
    public static final String PreTerminalVerb3rdPerson = "[Verb3rdPerson]";
    public static final String PreTerminalAdjectivePositive = "[AdjectivePositive]";
    public static final String PreTerminalAdjectiveSuperlative = "[AdjectiveSuperlative]";
    public static final String PreTerminalAdjectiveComparative = "[AdjectiveComparative]";
    //public static final String NonTerminalSparqlEntity ="SELECT  ?label WHERE {?Domain Property ?Range . ?Domain rdfs:label ?label .} OR SELECT  ?label WHERE {?Domain Property ?Range . ?Range rdfs:label ?label .}";
    //public static final String NonTerminalSparqlClass ="SELECT ?label WHERE {?Domain Property ?Range. ?Domain rdf:type ?Class. ?Class rdfs:label ?label } OR SELECT ?label WHERE {?Domain Property ?Range. ?Range rdf:type ?Class. ?Class rdfs:label ?label }";
    public static final String entityDomain = "SELECT  ?label WHERE {?Domain Property ?Range . ?Domain <http://www.w3.org/2000/01/rdf-schema#label> ?label .}";
    public static final String entityRange = "SELECT  ?label WHERE {?Domain Property ?Range . ?Range <http://www.w3.org/2000/01/rdf-schema#label> ?label .}";
    public static final String NonTerminalSparqlClassDomain = "SELECT ?label WHERE {?Domain Property ?Range. ?Domain <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?Class. ?Class rdfs:label ?label }";
    public static final String classRange = "SELECT ?label WHERE {?Domain Property ?Range. ?Range <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?Class. ?Class rdfs:label ?label }";
    public static final String entityAtrribute = "<NP_{map(SyntacticFunction), Property}>";
    public static final String classAtrribute = "<NP_{Class, <map(SyntacticFunction),Property>}>";


    public static String findMainGenericVerb(ParamterFinder paramterFinder) throws QueGGMissingFactoryClassException {
        String word = "XX";

        if (paramterFinder.getTensePair().second.contains(perfect)) {
            word = PreTerminalVerbParticiple;
        } else if (paramterFinder.getTensePair().second.contains(present)) {
            word = PreTerminalVerbPresent;
        } else if (paramterFinder.getTensePair().second.contains(past)) {
            word = PreTerminalVerbPast;
        } else if (paramterFinder.getTensePair().second.contains(infinitive)) {
            word = PreTerminalVerbPresent;
        } else if (paramterFinder.getTensePair().second.contains(present3rd)) {
            word = PreTerminalVerb3rdPerson;
        }

        return word;
    }

    public static String getGenericNonTerminalEntity() {
        return GENARIC_ENTITY;
    }

    public static String getGenericNonTerminalClass() {
        return GENARIC_Class;
    }

    public static String getGenericNounPreTerminal(String numberType) {
        String word = "XX";
        if (numberType.contains(singular)) {
            word = PreTerminalNounSingular;
        } else if (numberType.contains(plural)) {
            word = PreTerminalNounPlural;
        }
        return word;
    }

    public static String getAdjectiveReference(String reference) {
        String word = "XX";
        if (reference.contains(adjectiveBaseForm)) {
            word = PreTerminalAdjectivePositive;
        } else if (reference.contains(superlative)) {
            word = PreTerminalAdjectiveSuperlative;
        } else if (reference.contains(comperative)) {
            word = PreTerminalAdjectiveComparative;
        }
        return word;
    }

    public Boolean checkSyntacticFunction(FrameType frame, List<String> sentences) {
        for (String sentence : sentences) {
            if (sentence.contains("SyntacticFunction")) {
                return true;
            }
        }
        return false;
    }

    public Boolean checkClassFunction(FrameType frame, List<String> sentences) {
        for (String sentence : sentences) {
            if (sentence.contains("Class")) {
                 return true;
            }
        }
        return false;
    }

    public static String getGenericNounNonTerminalClass(String numberType) {
        return GENARIC_Class;
    }

    public String findGroup(FrameType frame,String group) {
        if(sentenceTemplateMapping.containsKey(group)){
            return sentenceTemplateMapping.get(group);
        }
        return null;
    }

}
