package grammar.generator;

import grammar.datasets.sentencetemplates.TempConstants;
import grammar.sparql.PrepareSparqlQuery;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

public class AdjGradableGrammarRuleGenerator extends GrammarRuleGeneratorRoot implements TempConstants {

    private String template = null;

    public AdjGradableGrammarRuleGenerator(Language language) {
        super(FrameType.AG, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
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
            generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            System.out.println(this.frameType + " is not working");
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("getLanguage:::"+getLanguage());
        return generatedSentences;
    }

    public List<String> generateSentencesAdjectiveBaseForm(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();

        String bindingVar = getBindingVariable();
        SentenceBuilderAllFrame sentenceBuilder = null;

        try {
            sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    lexicalEntryUtil,
                    adjectiveBaseForm);

            generatedSentences = sentenceBuilder.generateFullSentencesForward(bindingVar, lexicalEntryUtil);
            generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            System.out.println(this.frameType + " is not working");
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("generatedSentences:::"+generatedSentences);
        //exit(1);
        return generatedSentences;
    }

    public List<String> generateSentencesComperativeForm(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();

        String bindingVar = getBindingVariable();
        SentenceBuilderAllFrame sentenceBuilder = null;

        try {
            sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    lexicalEntryUtil,
                    comperative);

            generatedSentences = sentenceBuilder.generateFullSentencesBackward(bindingVar, lexicalEntryUtil);
            generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            System.out.println(this.frameType + " is not working");
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("generatedSentences:::"+generatedSentences);
        //exit(1);
        return generatedSentences;
    }

    protected List<String> generateOppositeSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String bindingVar = getBindingVariable();
        String sentenceTemplate = null;
        try {
            SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    lexicalEntryUtil,
                    this.template);
            generatedSentences = sentenceBuilder.generateFullSentencesBackward(bindingVar,  lexicalEntryUtil);
            //generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedSentences;
    }

    public List<GrammarEntry> generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries = new ArrayList<GrammarEntry>();
        grammarEntry.setSentenceTemplate(this.template);

        GrammarEntry baseGrammarEntry = getBasFormGrammarEntry(grammarEntry, lexicalEntryUtil);
        grammarEntries.add(baseGrammarEntry);
        GrammarEntry comperativeGrammarEntry = getComperativeGrammarEntry(grammarEntry, lexicalEntryUtil);
        grammarEntries.add(comperativeGrammarEntry);

        /*GrammarEntry baseFormGrammarEntry = getSuperlativeGrammarEntry(grammarEntry, lexicalEntryUtil);
        grammarEntries.add(oppositeGrammarEntry);*/
        return grammarEntries;
    }

    private GrammarEntry getBasFormGrammarEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        fragmentEntry.setReturnVariable(grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceTemplate(grammarEntry.getSentenceTemplate());
        fragmentEntry.setFrameType(FrameType.AG);

        /*Map<String, String> sentenceToSparqlParameterMapping = new HashMap<String, String>();
        sentenceToSparqlParameterMapping.put(grammarEntry.getSentenceBindings().getBindingVariableName(),
                grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceToSparqlParameterMapping(sentenceToSparqlParameterMapping);*/
        // sentences
        List<String> generatedSentences = generateSentencesAdjectiveBaseForm(lexicalEntryUtil);
        this.template = TempConstants.adjectiveBaseForm;
        fragmentEntry.setSentenceTemplate(this.template);
        fragmentEntry.setSentences(generatedSentences);
        //fragmentEntry.setSparqlQuery(PrepareSparqlQuery.getRealSparql(this.template, lexicalEntryUtil.get));
        return fragmentEntry;
    }

    private GrammarEntry getComperativeGrammarEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        fragmentEntry.setReturnVariable(grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceTemplate(grammarEntry.getSentenceTemplate());
        fragmentEntry.setFrameType(FrameType.AG);

        /*Map<String, String> sentenceToSparqlParameterMapping = new HashMap<String, String>();
        sentenceToSparqlParameterMapping.put(grammarEntry.getSentenceBindings().getBindingVariableName(),
                grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceToSparqlParameterMapping(sentenceToSparqlParameterMapping);*/
        // sentences
        List<String> generatedSentences = generateSentencesComperativeForm(lexicalEntryUtil);
        this.template = TempConstants.comperative;
        fragmentEntry.setSentenceTemplate(this.template);
        fragmentEntry.setSentences(generatedSentences);
        //fragmentEntry.setSparqlQuery(PrepareSparqlQuery.getRealSparql(this.template, lexicalEntryUtil.get));
        return fragmentEntry;
    }

    /*private GrammarEntry getAdjectiveBaseForm(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        fragmentEntry.setReturnVariable(grammarEntry.getBindingVariable());
        fragmentEntry.setSentenceTemplate(grammarEntry.getSentenceTemplate());
        fragmentEntry.setFrameType(FrameType.IPP);

        Map<String, String> sentenceToSparqlParameterMapping = new HashMap<String, String>();
        sentenceToSparqlParameterMapping.put(grammarEntry.getSentenceBindings().getBindingVariableName(),
                grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceToSparqlParameterMapping(sentenceToSparqlParameterMapping);
        // sentences
        List<String> generatedSentences = generateOppositeSentences(lexicalEntryUtil);
        fragmentEntry.setSentenceTemplate(this.template);
        fragmentEntry.setSentences(generatedSentences);
        return fragmentEntry;
    }*/
}
