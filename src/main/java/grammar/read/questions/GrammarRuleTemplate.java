/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

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
    @JsonProperty("nonTerminalSparql")
    private String nonTerminalSparql = null;
    @JsonProperty("questionSparql")
    private String questionSparql = null;

    public GrammarRuleTemplate() {

    }

    public GrammarRuleTemplate(SentenceTemplate st, List<String> modSentences) {
        this.templateNo = st.getTemplateNo();
        this.group = st.getGroup();
        this.grammarRuleTemplates = modSentences;
        this.nonTerminalSparql = "nonTerminalSparql";
        this.questionSparql = "questionSparql";
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
    
}
