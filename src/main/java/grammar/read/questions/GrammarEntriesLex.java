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
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarEntriesLex {

    @JsonProperty("grammarEntries")
    private List<GrammarDisplay> grammarEntries=new ArrayList<GrammarDisplay>();

    public GrammarEntriesLex(Map<String, List<GrammarEntryUnit>> lexicalEntiryUris) {
        Integer index=0;
        for (String lex : lexicalEntiryUris.keySet()) {
            index=index+1;
            List<GrammarEntryUnit> grammarEntries = lexicalEntiryUris.get(lex);
            GrammarDisplay grammarDisplay=new GrammarDisplay(index,lex,grammarEntries);
            this.grammarEntries.add(grammarDisplay);
        }
    }

}
