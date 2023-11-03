/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.structure.component.GrammarEntry;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarEntriesNew {
     @JsonProperty("grammarEntries")
    private List<GrammarEntry> grammarEntries;

    public List<GrammarEntry> getGrammarEntries() {
        return grammarEntries;
    }

    @Override
    public String toString() {
        return "GrammarEntriesNew{" + "grammarEntries=" + grammarEntries + '}';
    }
    
    
}
