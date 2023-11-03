/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.databind.ObjectMapper;
import grammar.read.questions.GrammarDisplay;
import grammar.read.questions.GrammarEntriesLex;
import grammar.read.questions.GrammarEntriesNew;
import grammar.read.questions.GrammarEntryUnit;
import static grammar.read.questions.GrammarEntryUnit.baseUri;
import grammar.read.questions.ProtoToRealQuesrion;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.io.JsonWriter;

/**
 *
 * @author elahi
 */
public class PrepareGrammarJson {

    private String grammarFileName = null;

    public PrepareGrammarJson(String propertyDir, List<File> protoSimpleQFiles, String outputFileName) {
        Map<String, List<GrammarEntry>> lexicalEntries = getLexicalEntries(protoSimpleQFiles);
        GrammarEntriesLex grammarEntriesLex = new GrammarEntriesLex(lexicalEntries, true);
        JsonWriter.writeClassToJson(grammarEntriesLex, propertyDir + outputFileName);

    }

    public static Map<String, List<GrammarEntry>> getLexicalEntries(List<File> protoSimpleQFiles) {
        Integer index = 0;
        Map<String, List<GrammarEntry>> lexicalEntries = new TreeMap<String, List<GrammarEntry>>();

        for (File file : protoSimpleQFiles) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                GrammarEntriesNew grammarEntries = mapper.readValue(file, GrammarEntriesNew.class);
                for (GrammarEntry grammarEntry : grammarEntries.getGrammarEntries()) {

                    String lexicalEntry = null;
                    if (grammarEntry.getLexicalEntryUri() == null) {
                        lexicalEntry = baseUri + "unknown_" + index;
                        index = index + 1;
                    } else {
                        lexicalEntry = grammarEntry.getLexicalEntryUri().toString();
                    }

                    lexicalEntry = lexicalEntry.toLowerCase();
                    lexicalEntry = getLexicalEntrywithOutUri(lexicalEntry);
                    List<GrammarEntry> grammarEntryUnits = new ArrayList<GrammarEntry>();

                    if (lexicalEntries.containsKey(lexicalEntry)) {
                        grammarEntryUnits = lexicalEntries.get(lexicalEntry);
                    }
                    grammarEntryUnits.add(grammarEntry);
                    lexicalEntries.put(lexicalEntry, grammarEntryUnits);
                }
            } catch (IOException ex) {
                Logger.getLogger(ProtoToRealQuesrion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lexicalEntries;
    }

    public static String getLexicalEntrywithOutUri(String url) {
        return url.replace(baseUri, "");
    }

    public static void main(String[] args) throws IOException {
        String propertyDir = "result/es/";
        File file = new File(propertyDir + "grammar_FULL_DATASET_ES.json");
        List<File> protoSimpleQFiles = new ArrayList<File>();
        protoSimpleQFiles.add(file);
        String outputFileName = "grammar_" + Language.ES + ".json";
        PrepareGrammarJson prepareGrammarJson = new PrepareGrammarJson(propertyDir, protoSimpleQFiles, outputFileName);

    }

}
