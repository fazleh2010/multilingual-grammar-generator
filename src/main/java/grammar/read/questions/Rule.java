/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonProperty;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elahi
 */
public class Rule {
    @JsonProperty("grammarRuleNo")
    private Integer ruleNo = 0;
    @JsonProperty("grammarRules")
    private List<String> sentences = new ArrayList<String>();
    @JsonProperty("bindingVariable")
    private String bindingVariable = null;
    @JsonProperty("sparql")
    private String sparql = null;
    @JsonProperty("bindingType")
    private String bindingType = null;
    //@JsonProperty("bindingClass")
    //private String bindingClass = null;
    @JsonProperty("returnType")
    private String returnType = null;
    //@JsonProperty("returnClass")
    //private String returnClass = null;

    public Rule(Integer id, Integer ruleNo, List<String> sentences, String bindingVariable,String sparql,String bindingType,String returnType) {
        this.ruleNo = ruleNo;
        this.sentences = sentences;
        this.sparql = sparql;
        this.bindingVariable=bindingVariable;
        this.bindingType=bindingType;
        this.returnType=returnType;
        //this.bindingClass=bindingClass;
        //this.returnClass=returnClass;
    }

    public Integer getRuleNo() {
        return ruleNo;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public String getSparql() {
        return sparql;
    }

}
