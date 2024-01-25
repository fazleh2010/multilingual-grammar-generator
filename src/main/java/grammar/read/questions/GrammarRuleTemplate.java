/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.structure.component.FrameType;
import java.util.ArrayList;
import java.util.List;
import util.io.GenericElement;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarRuleTemplate {
    
    @JsonProperty("templateNo")
    private String templateNo = null;
    @JsonProperty("group")
    private String group = null;
    @JsonProperty("grammarRuleTemplates")
    private List<String> grammarRuleTemplates = new ArrayList<String>();
    @JsonProperty("<NP_{map(SyntacticFunction), Property}>")
    private String nonTerminalSparql = null;
    @JsonProperty("<NP_{Class, <map(SyntacticFunction),Property>}>")
    private String nonTerminalClassSparql =null;
    @JsonProperty("Sparql")
    private String questionSparql = null;
    
    
    //private static String ENTITY_SPARQL="SELECT  ?label WHERE {?Domain Property ?Range . ?Domain rdfs:label ?label .}"+
    //                                 " "+"OR"+" "+
    //                                "SELECT  ?label WHERE {?Domain Property ?Range . ?Range rdfs:label ?label .}";
    //private static String CLASS_SPARQL="SELECT ?label WHERE {?Domain Property ?Range. ?Domain rdf:type ?Class. ?Class rdfs:label ?label }" +
    //                                                              " "+"OR"+" "+
    //                                   "SELECT ?label WHERE {?Domain Property ?Range. ?Range rdf:type ?Class. ?Class rdfs:label ?label }" ;

    public GrammarRuleTemplate() {

    }

    public GrammarRuleTemplate(SentenceTemplate st, List<String> modSentences) {
        this.templateNo = st.getTemplateNo();
        this.group = st.getGroup();
        this.grammarRuleTemplates = modSentences;
        this.nonTerminalSparql = GenericElement.getFrameClassSparqlDomain(FrameType.AA, modSentences);
        this.nonTerminalClassSparql =GenericElement.getFrameClassSparqlRange(FrameType.AA, modSentences);
        this.questionSparql = GenericElement.getFrameEntitySparqlRange(FrameType.AA, modSentences);
    }
    
     public GrammarRuleTemplate(FrameType frameType, String templateNo, String group,List<String> modSentences) {
        this.templateNo = templateNo;
        this.group = group;
        this.grammarRuleTemplates = modSentences;
        this.nonTerminalSparql = GenericElement.getFrameClassSparqlDomain(frameType, modSentences);
        this.nonTerminalClassSparql =GenericElement.getFrameClassSparqlRange(frameType, modSentences);
        this.questionSparql = GenericElement.getFrameEntitySparqlRange(frameType, modSentences);
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public String getGroup() {
        return group;
    }

    public List<String> getGrammarRuleTemplates() {
        return grammarRuleTemplates;
    }

    public String getNonTerminalSparql() {
        return nonTerminalSparql;
    }

    public String getQuestionSparql() {
        return questionSparql;
    }

    public String getNonTerminalClassSparql() {
        return nonTerminalClassSparql;
    }
    
}
