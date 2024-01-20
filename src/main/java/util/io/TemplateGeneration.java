/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.datasets.sentencetemplates.SentenceTemplateFactoryEN;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.sentencetemplates.TempConstants;
import grammar.read.questions.FrameInfo;
import grammar.read.questions.GrammarRuleTemplate;
import grammar.read.questions.GrammarRuleTemplateAll;
import grammar.read.questions.GrammarRuleTemplateFrame;
import grammar.read.questions.SentenceTemplate;
import grammar.read.questions.SentenceTemplateAll;
import grammar.read.questions.SentenceTemplatesFrame;
import grammar.structure.component.FrameType;
import grammar.structure.component.SentenceType;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class TemplateGeneration implements TempConstants {

    public static void main(String[] args) {

        List<String> languages = List.of("en");

        for (String language : languages) {
            FrameInfo frameInfo = new FrameInfo(language);
            String sentenceFileName = "output/" + language + "/sentenceTemplate.json";
            String grammarRuleTemplateFileName = "output/" + language + "/grammarRuleTemplate.json";
            String dictFileName = "src/main/resources/" + language + "_dictionary.txt";
            Map<String, String> dictionary = getDictionary(dictFileName);
            List<SentenceTemplatesFrame> allStFrames = new ArrayList<SentenceTemplatesFrame>();
            List<GrammarRuleTemplateFrame> allGrtFrames = new ArrayList<GrammarRuleTemplateFrame>();
            for (FrameType frameType : frameInfo.getFrames()) {
                String frame = frameType.getName();
                List<SentenceTemplatesFrame> stFrames = findFrameTemplate(frameInfo, frame);
                List<GrammarRuleTemplateFrame> grtFrames = findFrameGrammarRuleTemplates(stFrames, dictionary);
                allStFrames.addAll(stFrames);
                allGrtFrames.addAll(grtFrames);
            }
            SentenceTemplateAll stAll = new SentenceTemplateAll(allStFrames);
            JsonWriter.writeSentenceTemplateToJson(stAll, sentenceFileName);
            GrammarRuleTemplateAll grtAll = new GrammarRuleTemplateAll(allGrtFrames);
            JsonWriter.writeSentenceTemplateToJson(grtAll, grammarRuleTemplateFileName);
        }

    }

    private static List<SentenceTemplatesFrame> findFrameTemplate(FrameInfo frameInfo, String frame) {
        List<SentenceTemplatesFrame> sentenceTemplatesFrames = new ArrayList<SentenceTemplatesFrame>();
        Integer index = 0;
        List<SentenceTemplate> sentenceTemplates = findSentenceTemplate(frameInfo.getSentenceTemplateFactoryEN(), frameInfo.getSentenceTempRepEN(), SentenceType.SENTENCE, frame, frameInfo.getNounGroups(frame), index);
        SentenceTemplatesFrame sentenceTemplatesFrame = new SentenceTemplatesFrame(frame, sentenceTemplates);
        sentenceTemplatesFrames.add(sentenceTemplatesFrame);
        return sentenceTemplatesFrames;
    }

    private static List<SentenceTemplate> findSentenceTemplate(SentenceTemplateFactoryEN senTempFactoryEN,
            SentenceTemplateRepository sentenceTempRep, SentenceType sentenceType, String frame, Map<String, String> groups, Integer index) {
        List<SentenceTemplate> sentenceTemplates = new ArrayList<SentenceTemplate>();
        for (String key : groups.keySet()) {
            String groupName = groups.get(key);
            index = index + 1;
            List<String> list = new ArrayList<String>();

            list = sentenceTempRep.findOneByEntryTypeAndLanguageAndArguments(sentenceType,
                    senTempFactoryEN.getLanguage(), new String[]{frame, key});

            sentenceTemplates.add(new SentenceTemplate(index.toString(), groupName, list));
        }
        return sentenceTemplates;
    }

    private static List<GrammarRuleTemplateFrame> findFrameGrammarRuleTemplates(List<SentenceTemplatesFrame> stFrames, Map<String, String> dict) {
        List<GrammarRuleTemplateFrame> grtFrames = new ArrayList<GrammarRuleTemplateFrame>();

        for (SentenceTemplatesFrame stFrame : stFrames) {
            List<GrammarRuleTemplate> grts = new ArrayList<GrammarRuleTemplate>();
            for (SentenceTemplate st : stFrame.getSentenceTemplates()) {
                List<String> modSentences = converToGrammarRuleTemplate(st.getSentences(), dict);
                GrammarRuleTemplate grt = new GrammarRuleTemplate(st, modSentences);
                grts.add(grt);
            }
            GrammarRuleTemplateFrame grtFrame = new GrammarRuleTemplateFrame(stFrame.getFrame(), grts);
            grtFrames.add(grtFrame);

        }
        return grtFrames;
    }

    private static Map<String, String> getDictionary(String fileName) {
        try {
            return FileFolderUtils.fileToHashOrg(fileName, "=");
        } catch (IOException ex) {
            Logger.getLogger(TemplateGeneration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new HashMap<String, String>();
    }


    private static List<String> converToGrammarRuleTemplate(List<String> sentences, Map<String, String> dictionary) {
        List<String> grammarRuleTemplates = new ArrayList<String>();

        for (String sentence : sentences) {
            //System.out.println("sentence::" + sentence);
            for (String key : dictionary.keySet()) {
                String value = dictionary.get(key);
                sentence = sentence.replace(key, value);
            }
            String grammarRuleTemplate = sentence;
            //System.out.println("grammarRuleTemplate::" + grammarRuleTemplate);
            grammarRuleTemplates.add(grammarRuleTemplate);
        }
        return grammarRuleTemplates;
    }

}
