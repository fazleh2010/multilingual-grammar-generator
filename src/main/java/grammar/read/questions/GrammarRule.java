/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.sparql.PrepareSparqlQuery;
import grammar.structure.component.FrameType;
import java.util.ArrayList;
import java.util.List;
import util.io.AddQuote;
import util.io.GenericElement;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarRule {

    @JsonProperty("grammarRuleNo")
    private String ruleNo = null;
    @JsonProperty("sentTemplate")
    private String sentTemplate = null;
    @JsonProperty("sentences")
    private List<String> sentences = new ArrayList<String>();
    @JsonProperty("nonTerminal-X")
    private String nonTerminal_X = null;
    @JsonProperty("nonTerminal-Y")
    private String nonTerminal_Y = null;
    @JsonProperty("question")
    private String question = null;

    public GrammarRule() {

    }

    public GrammarRule(String ruleNo, List<String> sentencesT, String bindingSparql, String questionSparql,
            String sentTemplate, String frameType, String returnVariable) {
        this.ruleNo = ruleNo;
        this.sentTemplate = sentTemplate;
        String property = null;

        try {
            property = AddQuote.getProperty(questionSparql).replace("_", ":");
        } catch (Exception ex) {
            return;
        }

        String domainOrRange = null;

        String bindingType = PrepareSparqlQuery.findOppositeVariable(returnVariable);

        if (bindingType.contains("subjOfProp")) {
            domainOrRange = "domain";
        } else {
            domainOrRange = "range";
        }

        this.nonTerminal_X = bindingSparql;
        this.nonTerminal_Y = "([A-Za-z0-9_-]*)";
        this.question = questionSparql;

        String nonTerminal = this.generateNonTerminals(property, domainOrRange);
        this.sentences = sentencesT;
        //this.sentences = StringMatcher.modifySentencesWithNonTerminals(sentencesT, nonTerminal);
        //this.sentences = StringMatcher.modifySentencesWithRegularExpression(sentencesT, nonTerminal);

    }

  
    public GrammarRule(Boolean genericFlag,FrameType frameType,String ruleNo,String sentTemplate, List<String> sentencesT) {
        this.ruleNo = ruleNo;
        this.sentTemplate = sentTemplate;
        this.nonTerminal_X = GenericElement.getFrameEntitySparqlDomain(frameType,sentencesT);
        this.nonTerminal_Y = GenericElement.getFrameClassSparqlRange(frameType,sentencesT);
        this.question = GenericElement.getFrameEntitySparqlRange(frameType,sentencesT);;
        this.sentences = sentencesT;

    }
    
      private String generateNonTerminals(String property, String domainOrRange) {
        String nonTerminal = null;
        String nounPhrase = "NP";

        nonTerminal = "<" + nounPhrase + "{" + domainOrRange + "," + property + "}>";
        return nonTerminal;
    }

    public String getRuleNo() {
        return ruleNo;
    }

    public String getSentTemplate() {
        return sentTemplate;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public String getNonTerminal_X() {
        return nonTerminal_X;
    }

    public String getNonTerminal_Y() {
        return nonTerminal_Y;
    }

    public String getQuestion() {
        return question;
    }


}
