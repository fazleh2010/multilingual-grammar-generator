/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.structure.component.GrammarEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarEntriesLexT {
    
    @JsonProperty("sparql-endpoint")
    private String sparql_endpoint = "http://localhost:19999/bigdata/sparql";

    @JsonProperty("grammarEntries")
    private List<GrammarDisplayT> grammarEntries = new ArrayList<GrammarDisplayT>();

    
    public GrammarEntriesLexT() {
        
    }
  
    public GrammarEntriesLexT(Map<String, List<GrammarEntry>> lexicalEntiryUris) {
        Integer index = 0;
        for (String lex : lexicalEntiryUris.keySet()) {
            index = index + 1;
            List<GrammarEntry> grammarEntries = lexicalEntiryUris.get(lex);
            GrammarDisplayT grammarDisplayTemplate = new GrammarDisplayT(grammarEntries);
            this.grammarEntries.add(grammarDisplayTemplate);
        }
    }

}
