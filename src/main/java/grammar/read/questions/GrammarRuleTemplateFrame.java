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
public class GrammarRuleTemplateFrame {

    @JsonProperty("frame")
    private String frame = null;
    @JsonProperty("grammarRuleTemplates")
    private List<GrammarRuleTemplate> grammarRuleTemplates = new ArrayList<GrammarRuleTemplate>();
    
    public GrammarRuleTemplateFrame() {

    }

    public GrammarRuleTemplateFrame(String frame,List<GrammarRuleTemplate> grammarRuleTemplates) {
        this.frame = frame;
        this.grammarRuleTemplates = grammarRuleTemplates;
    }

    public List<GrammarRuleTemplate> getSentenceTemplates() {
        return grammarRuleTemplates;
    }

    public String getFrame() {
        return frame;
    }

    @Override
    public String toString() {
        return "SentenceTemplatesFrame{" + "frame=" + frame + ", grammarRuleTemplates=" + grammarRuleTemplates + '}';
    }

}

