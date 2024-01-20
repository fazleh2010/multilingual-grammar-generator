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
public class GrammarRuleTemplateAll {

    @JsonProperty("TemplateType")
    private String templateType = "grammarRuleTemplate";
    @JsonProperty("FrameSentenceTemplates")
    private List<GrammarRuleTemplateFrame> frameSentenceTemplates = new ArrayList<GrammarRuleTemplateFrame>();

    public GrammarRuleTemplateAll() {

    }

    public GrammarRuleTemplateAll(List<GrammarRuleTemplateFrame> frameSentenceTemplates) {
        this.frameSentenceTemplates = frameSentenceTemplates;
    }

    public String getTemplateType() {
        return templateType;
    }

    public List<GrammarRuleTemplateFrame> getFrameSentenceTemplates() {
        return frameSentenceTemplates;
    }

}
