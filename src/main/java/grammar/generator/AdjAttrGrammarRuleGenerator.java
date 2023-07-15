/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator;

import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.datasets.sentencetemplates.TempConstants.adjectiveBaseForm;
import static grammar.datasets.sentencetemplates.TempConstants.comperative;
import grammar.sparql.PrepareSparqlQuery;
import grammar.sparql.SPARQLRequest;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

/**
 *
 * @author elahi
 */
public class AdjAttrGrammarRuleGenerator extends GrammarRuleGeneratorRoot implements TempConstants {

    private String template = null;

    public AdjAttrGrammarRuleGenerator(Language language) {
        super(FrameType.AA, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }

    @Override
    public List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();

        String bindingVar = getBindingVariable();

        try {

            SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    lexicalEntryUtil);
            this.template = sentenceBuilder.getTemplateFinder().getSelectedTemplate();
            generatedSentences = sentenceBuilder.generateFullSentencesForward(bindingVar, lexicalEntryUtil);

        } catch (Exception ex) {
            System.out.println(this.frameType + " is not working");
            java.util.logging.Logger.getLogger(AdjAttrGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("getLanguage:::"+getLanguage());
        return generatedSentences;
    }

    public List<GrammarEntry> generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries = new ArrayList<GrammarEntry>();
        Map<String, String> classNamesMap = new HashMap<String, String>();
        classNamesMap.put("flim", "http://dbpedia.org/ontology/Flim");
        classNamesMap.put("movie", "http://dbpedia.org/ontology/Movie");
        classNamesMap.put("person", "http://dbpedia.org/ontology/Person");
        for (String className : classNamesMap.keySet()) {
            String classNameUrl=classNamesMap.get(className);
            grammarEntry.setSentenceTemplate(this.template);
            GrammarEntry baseGrammarEntry = getBasFormGrammarEntry(grammarEntry, lexicalEntryUtil, className,classNameUrl);
            grammarEntries.add(baseGrammarEntry);
        }
        return grammarEntries;
    }

    private GrammarEntry getBasFormGrammarEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil,
                                                String className,String classNameUrl) throws QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        fragmentEntry.setReturnVariable(grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceTemplate(grammarEntry.getSentenceTemplate());
        fragmentEntry.setFrameType(FrameType.AA);
        List<String> generatedSentences = generateSentences(lexicalEntryUtil);
         List<String> sentences = new ArrayList<String>();
        for(String sentence:generatedSentences){
            sentence=sentence+" "+className;
            sentences.add(sentence);
        }
        this.template = TempConstants.adjectiveBaseForm;
        fragmentEntry.setSentenceTemplate(this.template);
        fragmentEntry.setSentences(sentences);
        OWLRestriction owlRestriction = lexicalEntryUtil.getOwlRestriction();
        String property = owlRestriction.getProperty();
        String value = owlRestriction.getValue();
        String sparql = PrepareSparqlQuery.getRealSparql(this.template, property, value, classNameUrl);
        //System.out.println(sparql);
        fragmentEntry.setSparqlQuery(sparql);
        return fragmentEntry;
    }

}
