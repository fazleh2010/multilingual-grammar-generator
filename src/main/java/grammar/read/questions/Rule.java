/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.datasets.sentencetemplates.TempConstants.HOW_MANY_THING;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;
import util.io.AddQuote;
import util.io.StringMatcher;

/**
 *
 * @author elahi
 */
public class Rule implements TempConstants{

    @JsonProperty("grammarRuleNo")
    private Integer ruleNo = 0;
    @JsonProperty("sentTemplate")
    private String sentTemplate = null;
    @JsonProperty("sentences")
    private List<String> sentences = new ArrayList<String>();
    @JsonProperty("nonTerminal")
    private String nonTerminal = null;
    @JsonProperty("nonTerminalSparqlQuery")
    private String nonTerminalSparqlQuery = null;
    @JsonProperty("answerSparqlQuery")
    private String answerSparqlQuery = null;
    @JsonProperty("bindingType")
    private String bindingType = null;
    @JsonProperty("returnType")
    private String returnType = null;

    public Rule(Integer id, Integer ruleNo, List<String> sentencesT, String bindingVariable, String sparql,
            String bindingType, String returnType, String sentTemplate, String frameType) {
        this.ruleNo = ruleNo;
        this.sentTemplate=sentTemplate;
        this.bindingType=bindingType;
        this.returnType=returnType;

        String property = AddQuote.getProperty(sparql).replace("_", ":");
        String domainOrRange = null;
        
        
        


        if (bindingType.contains("subjOfProp")) {
            domainOrRange = "domain";
            this.nonTerminalSparqlQuery = "SELECT ?"+bindingType.toString()+" WHERE {?subjOfProp"+" " + property + " "+"?objOfProp.}";

            if (this.isAmountForward(sentTemplate)) {
                this.answerSparqlQuery = "SELECT Count("+"?"+returnType.toString()+") WHERE {?subjOfProp"+" " + property + " "+"?objOfProp.}";
            } 
            /*else if (this.isAmountBakward(sentTemplate)) {
                this.answerSparqlQuery = "SELECT Count(?subjOfProp) WHERE {?subjOfProp"+" " + property + " "+"?objOfProp.}";
            }*/else {
                this.answerSparqlQuery = "SELECT ?"+returnType.toString()+" WHERE {?subjOfProp"+" " + property + " "+"?objOfProp.}";
            }
            

        } else if (bindingType.contains("objOfProp")) {
            domainOrRange = "range";
            this.nonTerminalSparqlQuery = "SELECT ?objOfProp. WHERE {?subjOfProp"+" " + property + " "+"?objOfProp.}";
            
            /*if (this.isAmountForward(sentTemplate)) {
                this.answerSparqlQuery = "SELECT Count(?subjOfProp) WHERE {?subjOfProp"+" " + property + " "+"?objOfProp.}";
            } 
            else*/ 
            if (this.isAmountBakward(sentTemplate)) {
                this.answerSparqlQuery = "SELECT Count(?"+returnType.toString()+") WHERE {?subjOfProp"+" " + property + " "+"?objOfProp.}";
            } else {
                this.answerSparqlQuery = "SELECT ?subjOfProp WHERE {?subjOfProp"+" " + property + " "+"?objOfProp.}";
            }

        }

        this.nonTerminal = this.generateNonTerminals(property, domainOrRange);
        this.sentences = StringMatcher.modifySentencesWithNonTerminals(sentencesT, nonTerminal);

    }

    public Integer getRuleNo() {
        return ruleNo;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public String getNonTerminalSparqlQuery() {
        return nonTerminalSparqlQuery;
    }

    public String getAnswerSparqlQuery() {
        return answerSparqlQuery;
    }

    private String generateNonTerminals(String property, String domainOrRange) {
        String nonTerminal = null;
        String nounPhrase = "NP";

        nonTerminal = "<" + nounPhrase + "{" + domainOrRange + "," + property + "}>";
        return nonTerminal;
    }

    private boolean isAmountForward(String sentTemplate) {
        if (sentTemplate != null) {
            if (sentTemplate.contains(HOW_MANY_THING_FORWARD)) {
               return true;
            }
        }
        return false;
    }

    private boolean isAmountBakward(String sentTemplate) {
        if (sentTemplate != null) {
            if (sentTemplate.contains(HOW_MANY_THING_BACKWARD)) {
               return true;
            }
        }
        return false;
    }

}
