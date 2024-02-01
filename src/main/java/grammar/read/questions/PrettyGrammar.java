/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.databind.ObjectMapper;
import grammar.generator.GrammarRuleGeneratorRoot;
import grammar.generator.GrammarRuleGeneratorRootImpl;
import static grammar.read.questions.GrammarEntryUnit.baseUri;
import grammar.sparql.PrepareSparqlQuery;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.Language;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.jena.query.QueryType;
import util.io.JsonWriter;

/**
 *
 * @author elahi
 */
public class PrettyGrammar {

    private String grammarFileName = null;

    public PrettyGrammar(String propertyDir, List<File> protoSimpleQFiles, String outputFileName) {
        Map<String, List<GrammarEntry>> lexicalEntries = getLexicalEntries(protoSimpleQFiles);
        GrammarEntriesLex grammarEntriesLex = new GrammarEntriesLex(lexicalEntries, true);
        JsonWriter.writeClassToJson(grammarEntriesLex, propertyDir + outputFileName);

    }

    public static void prettyGrammarFuntion(GrammarWrapper grammarWrapper, Language language, String outputDir) {
        GrammarRuleGeneratorRoot generatorRoot = new GrammarRuleGeneratorRootImpl(language);
        String rdfs_label = "<http://www.w3.org/2000/01/rdf-schema#label>";
        for (GrammarEntry grammarEntry : grammarWrapper.getGrammarEntries()) {
            //System.out.println(grammarEntry);
            String questionSparql = null, bindingSparql = null;
            grammarEntry.setId(String.valueOf(grammarWrapper.getGrammarEntries().indexOf(grammarEntry) + 1));
            String returnVariable = null, bindingVariable = null;
            if (grammarEntry.getReturnVariable() != null) {
                returnVariable = grammarEntry.getReturnVariable();
                bindingVariable = PrepareSparqlQuery.findOppositeVariable(returnVariable);
            } else {
                continue;
            }
            
            if (grammarEntry.getSentenceTemplate() != null) {
                if (grammarEntry.getSentenceTemplate().contains("HOW_MANY_THING")) {
                    //System.out.println(grammarEntry.getSentenceTemplate());
                }
            }

           

            if (grammarEntry.getFrameType().getName().contains(FrameType.AA.getName())) {
                ;

            } else {
                String sparql = PrepareSparqlQuery.getRealSparqlT(grammarEntry.getSentenceTemplate(), grammarEntry.getSparqlQuery());
                if (sparql.contains(QueryType.ASK.name())) {
                    questionSparql = sparql;
                    bindingSparql = PrepareSparqlQuery.findBindingListAskQuery(grammarEntry.getBindingType());
                } else {
                    if (grammarEntry.getSentenceTemplate() != null && grammarEntry.getSentenceTemplate().contains("superlative")) {
                        bindingSparql = PrepareSparqlQuery.findBindingListSuperlative(grammarEntry.getBindingType());
                        questionSparql = sparql.replace(returnVariable, "Answer");
                    } else {

                        if (grammarEntry.getSentenceTemplate() != null && grammarEntry.getSentenceTemplate().contains("HOW_MANY")) {
                            bindingSparql = sparql.replace(bindingVariable, "Answer");;
                            sparql = sparql.replace("SELECT ?Answer", "SELECT (COUNT(DISTINCT ?" + returnVariable + ") AS ?c)");
                            questionSparql = sparql.replace(returnVariable, "Answer");
                        } else {
                            questionSparql = sparql.replace(returnVariable, "Answer");
                            bindingSparql = sparql.replace(bindingVariable, "Answer");
                        }

                    }
                }
                bindingSparql = bindingSparql.replace("}", "?Answer" + " " + rdfs_label + " " + "?label" + "}");
                bindingSparql = bindingSparql.replace("SELECT ?Answer", "SELECT ?label");
                questionSparql = questionSparql.replace("}", "?Answer" + " " + rdfs_label + " " + "?label" + "}");
                questionSparql = questionSparql.replace("SELECT ?Answer", "SELECT ?label");
                grammarEntry.setSparqlQuery(questionSparql);
                grammarEntry.setBindingSparql(bindingSparql);
            }

        }
        GrammarWrapper regularEntries = new GrammarWrapper();
        regularEntries.setGrammarEntries(
                grammarWrapper.getGrammarEntries()
                        .stream()
                        //.filter(grammarEntry -> !grammarEntry.isCombination())
                        .collect(Collectors.toList())
        );

        String grammarInterFile = "temp_" + generatorRoot.getFrameType().getName() + "_" + language + ".json";

        try {
            generatorRoot.dumpToJSON(
                    Path.of(
                            outputDir,
                            grammarInterFile
                    ).toString(),
                    regularEntries
            );
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PrettyGrammar.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file = new File(outputDir + "/" + grammarInterFile);
        List<File> protoSimpleQFiles = new ArrayList<File>();
        protoSimpleQFiles.add(file);
        String outputFileName = "grammar_" + language + ".json";
        Map<String, List<GrammarEntry>> lexicalEntries = getLexicalEntries(protoSimpleQFiles);
        GrammarEntriesLex grammarEntriesLex = new GrammarEntriesLex(lexicalEntries, true);
        JsonWriter.writeClassToJson(grammarEntriesLex, outputDir + "/" + outputFileName);

    }
    
      public static void outputForParser(GrammarWrapper grammarWrapper, Language language, String outputDir) {

         GrammarRuleGeneratorRoot generatorRoot = new GrammarRuleGeneratorRootImpl(language);
        for (GrammarEntry grammarEntry : grammarWrapper.getGrammarEntries()) {
            grammarEntry.setId(String.valueOf(grammarWrapper.getGrammarEntries().indexOf(grammarEntry) + 1));
            if (grammarEntry.getFrameType().getName().contains(FrameType.AA.getName())) {
                ;
            } else {
                String sparql = PrepareSparqlQuery.getRealSparql(grammarEntry.getSentenceTemplate(), grammarEntry.getSparqlQuery());
                if (grammarEntry.getReturnVariable() != null) {
                    sparql = sparql.replace(grammarEntry.getReturnVariable(), "Answer");
                }
                grammarEntry.setSparqlQuery(sparql);
            }

        }
        GrammarWrapper regularEntries = new GrammarWrapper();
        regularEntries.setGrammarEntries(
                grammarWrapper.getGrammarEntries()
                        .stream()
                        //.filter(grammarEntry -> !grammarEntry.isCombination())
                        .collect(Collectors.toList())
        );
        
        try {
            generatorRoot.dumpToJSON(
                    Path.of(
                            outputDir,
                            "grammar_" + generatorRoot.getFrameType().getName() + "_" + language + ".json"
                    ).toString(),
                    regularEntries
            );
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PrettyGrammar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        PrettyGrammar prepareGrammarJson = new PrettyGrammar(propertyDir, protoSimpleQFiles, outputFileName);

    }

}
