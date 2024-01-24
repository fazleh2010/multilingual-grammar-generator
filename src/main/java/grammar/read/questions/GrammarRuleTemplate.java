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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.io.GenericElement;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarRuleTemplate {
    
    @JsonProperty("groupNo")
    private String groupNo = null;
    @JsonProperty("group")
    private String group = null;
    @JsonProperty("grammarRuleTemplates")
    private List<String> grammarRuleTemplates = new ArrayList<String>();
    @JsonProperty("nonTerminals")
    private Map<String,String> nonTerminalSparql = new HashMap<String,String>();
    @JsonProperty("Sparql")
    private String questionSparql = null;
    
    public GrammarRuleTemplate() {

    }

    public GrammarRuleTemplate(SentenceTemplate st, List<String> modSentences) {
        this.group = st.getGroup();
        this.grammarRuleTemplates = modSentences;
        //this.nonTerminalSparql = ENTITY_SPARQL;
        //this.nonTerminalClassSparql =CLASS_SPARQL;
        //this.questionSparql = ENTITY_SPARQL;
    }
    
    public GrammarRuleTemplate(FrameType frameType, String templateNo, String group, List<String> sentences) {
        GenericElement genericElement = new GenericElement();
        this.group = genericElement.findGroup(frameType,group);
        this.groupNo=templateNo;
        this.grammarRuleTemplates = sentences;
        if (genericElement.checkSyntacticFunction(frameType, sentences)) {
            this.nonTerminalSparql.put(GenericElement.entityAtrribute,
                    GenericElement.entityDomain);
            this.questionSparql = GenericElement.entityRange;
        }
        if (genericElement.checkClassFunction(frameType, sentences)) {
            this.nonTerminalSparql.put(GenericElement.classAtrribute,
                    GenericElement.classRange);
        }
    }


    public String getGroup() {
        return group;
    }

    public List<String> getGrammarRuleTemplates() {
        return grammarRuleTemplates;
    }

    public Map<String, String> getNonTerminalSparql() {
        return nonTerminalSparql;
    }

    private boolean isEmpty(String[] array) {
        return array.length == 0;
    }

    public String getQuestionSparql() {
        return questionSparql;
    }

    
    
}
