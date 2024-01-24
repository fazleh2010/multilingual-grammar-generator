/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.io.GenericElement;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarDisplayT {

    @JsonProperty("Frame")
    private String frame = null;
    @JsonProperty("GrammarRuleTemplates")
    private List<GrammarRuleTemplate> grammarRuleTemplate = new ArrayList<GrammarRuleTemplate>();

    public GrammarDisplayT( List<GrammarEntry> grammarEntries) {

        Integer index = 0;

        for (GrammarEntry grammarEntry : grammarEntries) {
            index = index + 1;
            if (index == 1) {
                frame = grammarEntry.getFrameType().getName();
            }

            grammarRuleTemplate.add(new GrammarRuleTemplate(
                    grammarEntry.getFrameType(),
                    index.toString(),
                    grammarEntry.getSentenceTemplate(),
                    grammarEntry.getSentences())
            );

        }

    }

    public String getSyntacticFrame() {
        return frame;
    }

    public List<GrammarRuleTemplate> getRules() {
        return grammarRuleTemplate;
    }

}
