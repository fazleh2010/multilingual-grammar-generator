package grammar.generator;

import grammar.datasets.sentencetemplates.TempConstants;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import lexicon.LexicalEntryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NPPGrammarRuleGenerator extends GrammarRuleGeneratorRoot implements TempConstants {

    private String senTemplate = null;

    public NPPGrammarRuleGenerator(Language language) {
        super(FrameType.NPP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }
    
    public List<GrammarEntry> generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries = new ArrayList<GrammarEntry>();
        List<String> sentences = new ArrayList<String>();

        //temporary close of boolean questions
        sentences = generateBooleanSentences(lexicalEntryUtil, booleanQuestionDomainRange);
        if (!sentences.isEmpty()) {
            GrammarEntry booleanGrammarEntryDomainRange = this.generateGrammarEntryforNP(grammarEntry, lexicalEntryUtil, SentenceType.SENTENCE, sentences,booleanQuestionDomainRange);
            booleanGrammarEntryDomainRange.setSentenceTemplate(booleanQuestionDomainRange);
            grammarEntries.add(booleanGrammarEntryDomainRange);
        }
        /*sentences = generateBooleanSentences(lexicalEntryUtil, booleanQuestionDomain);
        if (!sentences.isEmpty()) {
            GrammarEntry booleanGrammarEntryDomain = this.generateGrammarEntryforNP(grammarEntry, lexicalEntryUtil, SentenceType.SENTENCE, sentences,booleanQuestionDomain);
            grammarEntries.add(booleanGrammarEntryDomain);
        }*/
        sentences = generateOppositeSentences(lexicalEntryUtil, backward);
        if (!sentences.isEmpty()) {
            GrammarEntry fragmentEntry = this.generateGrammarEntryforNP(grammarEntry, lexicalEntryUtil, SentenceType.SENTENCE, sentences, backward);
            grammarEntries.add(fragmentEntry);
        }
        sentences = generateForwardAmount(lexicalEntryUtil, HOW_MANY_THING);
        if (!sentences.isEmpty()) {
            GrammarEntry fragmentEntry = this.generateGrammarEntryforNP(grammarEntry, lexicalEntryUtil, SentenceType.SENTENCE, sentences, HOW_MANY_THING);
            fragmentEntry.setSentenceTemplate(HOW_MANY_THING);
            grammarEntries.add(fragmentEntry);
        }
        sentences = generateNounPhrase(lexicalEntryUtil, nounPhrase);
        if (!sentences.isEmpty()) {
            GrammarEntry fragmentEntry = this.generateGrammarEntryforNP(grammarEntry, lexicalEntryUtil, SentenceType.NP, sentences, nounPhrase);
            fragmentEntry.setSentenceTemplate(nounPhrase);
            grammarEntries.add(fragmentEntry);
        }
        return grammarEntries;
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
        generatedSentences.sort(String::compareToIgnoreCase);
        this.senTemplate = sentenceBuilder.getTemplate();

        return generatedSentences;
    }

    protected List<String> generateOppositeSentences(LexicalEntryUtil lexicalEntryUtil, String type) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                lexicalEntryUtil
        );
        generatedSentences = new ArrayList<String>();
        generatedSentences.addAll(sentenceBuilder.generateFullSentencesBackward(getBindingVariable(), new String[]{}, lexicalEntryUtil));
        generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
        generatedSentences.sort(String::compareToIgnoreCase);
        this.senTemplate = sentenceBuilder.getTemplate();
        return generatedSentences;
    }

    protected List<String> generateForwardAmount(LexicalEntryUtil lexicalEntryUtil, String type) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                lexicalEntryUtil
        );
        generatedSentences = new ArrayList<String>();
        generatedSentences.addAll(sentenceBuilder.generateForwardAmount(getBindingVariable(), new String[]{}, lexicalEntryUtil));
        generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
        generatedSentences.sort(String::compareToIgnoreCase);
        this.senTemplate = sentenceBuilder.getTemplate();
        return generatedSentences;
    }

    protected List<String> generateNounPhrase(LexicalEntryUtil lexicalEntryUtil, String type) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                lexicalEntryUtil
        );
        if (type.equals(nounPhrase)) {
            generatedSentences = new ArrayList<String>();
            generatedSentences.addAll(sentenceBuilder.generateNounPhrase(getBindingVariable(), new String[]{}, lexicalEntryUtil));
            this.senTemplate = sentenceBuilder.getTemplate();
        }
        generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
        generatedSentences.sort(String::compareToIgnoreCase);
        return generatedSentences;
    }
    
    protected List<String> generateBooleanSentences(LexicalEntryUtil lexicalEntryUtil, String type) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                lexicalEntryUtil
        );
        generatedSentences = new ArrayList<String>();
        List<String> sentences=sentenceBuilder.generateBooleanSentences(getBindingVariable(), lexicalEntryUtil);
        generatedSentences.addAll(sentences);
        generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
        generatedSentences.sort(String::compareToIgnoreCase);
        this.senTemplate = sentenceBuilder.getTemplate();
        return generatedSentences;
    }

    /**
     * Generate an entry with sentence structure: Which _noun_ does $x _verb_
     * _preposition_?
     */
    

    private GrammarEntry generateGrammarEntryforNP(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil, SentenceType sentenceType, List<String> generatedSentences, String type) throws QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(sentenceType);
        List<DomainOrRangeType> bindingTypes=new ArrayList<DomainOrRangeType>();
        bindingTypes.addAll(grammarEntry.getBindingType());
        bindingTypes.addAll(grammarEntry.getReturnType());

        if (type.contains(booleanQuestionDomainRange)) {
            fragmentEntry.setBindingType(bindingTypes);
            fragmentEntry.setReturnType(Arrays.asList(DomainOrRangeType.BooleanFlag));
        }
        else if (type.contains(backward)) {
            fragmentEntry.setReturnType(grammarEntry.getBindingType());
            fragmentEntry.setBindingType(grammarEntry.getReturnType());
        } else {
            fragmentEntry.setReturnType(grammarEntry.getReturnType());
            fragmentEntry.setBindingType(grammarEntry.getBindingType());
           
        }
        fragmentEntry.setReturnVariable(grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceTemplate(this.senTemplate);
        fragmentEntry.setSentences(generatedSentences);

        return fragmentEntry;
    }

    //sparql ="ASK WHERE "+fragmentEntry.getSparqlQuery().replace("?", "");
    //sparql =fragmentEntry.getSparqlQuery().replace("?", "");
    //fragmentEntry.setSparqlQuery(sparql);
    //reference =LexicalEntryUtil.getReference(lexicalEntryUtil);
    //sparql = "ASK WHERE (bgp (triple "+"subjOfProp"+" <" + reference + "> objOfProp))";
}
