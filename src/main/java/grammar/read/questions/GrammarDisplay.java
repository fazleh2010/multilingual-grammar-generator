/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import static grammar.read.questions.ReadWriteConstants.RETURN_TYPE_OBJECT;
import static grammar.read.questions.ReadWriteConstants.RETURN_TYPE_SUBJECT;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarDisplay {

    @JsonProperty("id")
    private Integer id = null;
    @JsonProperty("Entry")
    private String Entry = null;
    @JsonProperty("syntacticFrame")
    private String syntacticFrame = null;
    //@JsonProperty("sentenceTemplates")
    //private String sentenceTemplates = null;
    @JsonProperty("rules")
    private List<GrammarRule> rules = new ArrayList<GrammarRule>();

    public GrammarDisplay(Integer id, String lex, List<GrammarEntryUnit> grammarEntries) {
        this.id = id;
        this.Entry = lex;
        Integer index = 0;

        for (GrammarEntryUnit grammarEntryUnit : grammarEntries) {
            index = index + 1;
            String bindingType = null;
            if (index == 1) {
                syntacticFrame = grammarEntryUnit.getFrameType();
            }
            if (grammarEntryUnit.getReturnVariable().contains(RETURN_TYPE_OBJECT)) {
                bindingType = RETURN_TYPE_SUBJECT;
            } else if (grammarEntryUnit.getReturnVariable().contains(RETURN_TYPE_SUBJECT)) {
                bindingType = RETURN_TYPE_OBJECT;
            }

            /*rules.add(new Rule(grammarEntryUnit.getId(), index, grammarEntryUnit.getSentences(), 
                    grammarEntryUnit.getBindingVariableName(),grammarEntryUnit.getSparqlQuery(),
                   bindingType,grammarEntryUnit.getReturnVariable(),grammarEntryUnit.getBindingType(),grammarEntryUnit.getReturnType()));*/
            /*rules.add(new Rule(index.toString(), grammarEntryUnit.getSentences(),
                    grammarEntryUnit.getSparqlQuery(),
                    bindingType, grammarEntryUnit.getReturnVariable(),
                    grammarEntryUnit.getSentenceTemplate(), grammarEntryUnit.getFrameType()));*/

        }

    }

    public GrammarDisplay(Integer id, String lex, List<GrammarEntry> grammarEntries,boolean flag) {
        this.id = id;
        this.Entry = lex;
        Integer index = 0;

        for (GrammarEntry grammarEntry : grammarEntries) {
            index = index + 1;
            String bindingType = null;
            if (index == 1) {
                syntacticFrame = grammarEntry.getFrameType().getName();
            }

            rules.add(new GrammarRule(index.toString(), grammarEntry.getSentences(),
                    grammarEntry.getBindingSparql(),grammarEntry.getSparqlQuery(),
                    grammarEntry.getSentenceTemplate(), grammarEntry.getFrameType().getName(),grammarEntry.getReturnVariable()));

        }

    }
    
    public GrammarDisplay(Boolean genericFlag,Integer id, String lex, List<GrammarEntry> grammarEntries,boolean flag) {
        this.id = id;
        this.Entry = lex;
        Integer index = 0;

        for (GrammarEntry grammarEntry : grammarEntries) {
            index = index + 1;
            String bindingType = null;
            if (index == 1) {
                syntacticFrame = grammarEntry.getFrameType().getName();
            }
            rules.add(new GrammarRule(genericFlag,grammarEntry.getFrameType(), 
                     index.toString(),grammarEntry.getSentenceTemplate(), grammarEntry.getSentences()));
        }

    }

    public String getSyntacticFrame() {
        return syntacticFrame;
    }

    public List<GrammarRule> getRules() {
        return rules;
    }

    public Integer getId() {
        return id;
    }

    public String getEntry() {
        return Entry;
    }

}
