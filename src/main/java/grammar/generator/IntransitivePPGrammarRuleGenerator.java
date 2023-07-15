package grammar.generator;

import com.google.gdata.util.common.base.Pair;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntransitivePPGrammarRuleGenerator extends GrammarRuleGeneratorRoot {
    
    private String template = null;


    public IntransitivePPGrammarRuleGenerator(Language language) {
        super(FrameType.IPP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }

    @Override
    public List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();

        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                lexicalEntryUtil
        );
        generatedSentences.addAll(sentenceBuilder.generateFullSentencesForward(getBindingVariable(), lexicalEntryUtil));
         this.template=sentenceBuilder.getTemplate();

        //generatedSentences.sort(String::compareToIgnoreCase);
        return generatedSentences;
    }

    protected Pair<String,List<String>> generateOppositeSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String sentTemplate=null;
        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                lexicalEntryUtil
        );
        generatedSentences.addAll(sentenceBuilder.generateFullSentencesBackward(getBindingVariable(), lexicalEntryUtil));
        generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
        sentTemplate=sentenceBuilder.getTemplate();
        //generatedSentences.sort(String::compareToIgnoreCase);
        return new Pair<String,List<String>>(sentTemplate,generatedSentences);
    }
    
    protected Pair<String,List<String>> generateBackwardAmount(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
         String sentTemplate=null;
        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                lexicalEntryUtil
        );
        generatedSentences.addAll(sentenceBuilder.generateBackwardAmount(getBindingVariable(), new String[]{}, lexicalEntryUtil));
        generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
         sentTemplate=sentenceBuilder.getTemplate();
        //generatedSentences.sort(String::compareToIgnoreCase);
         return new Pair<String,List<String>>(sentTemplate,generatedSentences);
    }

    /**
     * Generate an entry with sentence structure: Which _noun_ does $x _verb_
     * _preposition_?
     */
    public List<GrammarEntry> generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries=new ArrayList<GrammarEntry>();
        /*GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setSentenceTemplate(this.template);
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        fragmentEntry.setReturnVariable(grammarEntry.getBindingVariable());
        Map<String, String> sentenceToSparqlParameterMapping = new HashMap<String, String>();
        sentenceToSparqlParameterMapping.put(grammarEntry.getSentenceBindings().getBindingVariableName(),
                grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceToSparqlParameterMapping(sentenceToSparqlParameterMapping);*/

        // sentences
        GrammarEntry fragmentEntry1 =getOppositeFrame(grammarEntry);
        Pair<String,List<String>> pair=generateOppositeSentences(lexicalEntryUtil);
        fragmentEntry1.setSentenceTemplate(pair.getFirst());
        fragmentEntry1.setSentences(pair.getSecond());
        grammarEntries.add(fragmentEntry1);
        
        GrammarEntry fragmentEntry2 =getOppositeFrame(grammarEntry);
        pair=generateBackwardAmount(lexicalEntryUtil);
        fragmentEntry2.setSentenceTemplate(pair.getFirst());
        fragmentEntry2.setSentences(pair.getSecond());
        grammarEntries.add(fragmentEntry2);

        return grammarEntries;
    }
    
    private GrammarEntry getOppositeFrame(GrammarEntry grammarEntry) {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setSentenceTemplate(this.template);
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        if (grammarEntry.getReturnVariable().contains("objOfProp")) {
            fragmentEntry.setReturnVariable("?subjOfProp");
        } else {
            fragmentEntry.setReturnVariable("objOfProp");
        }
        //fragmentEntry.setReturnVariable(grammarEntry.getBindingVariable());
        Map<String, String> sentenceToSparqlParameterMapping = new HashMap<String, String>();
        //sentenceToSparqlParameterMapping.put(grammarEntry.getSentenceBindings().getBindingVariableName(),
        //        grammarEntry.getReturnVariable());
        //fragmentEntry.setSentenceToSparqlParameterMapping(sentenceToSparqlParameterMapping);
        return fragmentEntry;

    }

    
}
