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
import grammar.structure.component.Language;
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
        List<Language> languages = List.of(Language.EN,Language.DE,Language.IT,Language.ES);
        for (Language language : languages) {
            FrameInfo frameInfo = new FrameInfo(language);
            String sentenceFileName = "output/" + language + "/sentenceTemplate.json";
            String grammarRuleTemplateFileName = "output/" + language + "/grammarRuleTemplate.json";
            String dictFileName = "src/main/resources/" + language + "_dictionary.txt";
            Map<String, String> dictionary = getDictionary(dictFileName);
            //System.out.println(dictionary);
            manualToSt(language,frameInfo, sentenceFileName);
            //FileFolderUtils.setToFile(SentenceTemplate.getCategories(), dictFileName);
            stToGrt(sentenceFileName, frameInfo, dictionary, grammarRuleTemplateFileName);
        }

    }

    private static void manualToSt(Language language,FrameInfo frameInfo, String sentenceFileName) {
        List<SentenceTemplatesFrame> allStFrames = new ArrayList<SentenceTemplatesFrame>();
        for (FrameType frameType : frameInfo.getFrames()) {
            String frame = frameType.getName();
            List<SentenceTemplatesFrame> stFrames = findFrameTemplate(language,frameInfo, frame);
            allStFrames.addAll(stFrames);
        }
        SentenceTemplateAll stAll = new SentenceTemplateAll(allStFrames);
        JsonSerializer.writeSentenceTemplateToJson(stAll, sentenceFileName);
    }
     
    private static void stToGrt(String sentenceFileName, FrameInfo frameInfo, Map<String, String> dictionary, String grammarRuleTemplateFileName) {
        SentenceTemplateAll stAll =JsonSerializer.readJsonToSentenceTemplates(sentenceFileName);
        List<GrammarRuleTemplateFrame> allGrtFrames = new ArrayList<GrammarRuleTemplateFrame>();
        for (FrameType frameType : frameInfo.getFrames()) {
            String frame = frameType.getName();
            List<SentenceTemplatesFrame> stFrames = stAll.getSentenceTemplatesFrame(frame);
            List<GrammarRuleTemplateFrame> grtFrames = findFrameGrammarRuleTemplates(frame,stFrames, dictionary);
            allGrtFrames.addAll(grtFrames);
        }
        GrammarRuleTemplateAll grtAll = new GrammarRuleTemplateAll(allGrtFrames);
        JsonSerializer.writeSentenceTemplateToJson(grtAll, grammarRuleTemplateFileName);
    }

    private static List<SentenceTemplatesFrame> findFrameTemplate(Language language,FrameInfo frameInfo, String frame) {
        List<SentenceTemplatesFrame> sentenceTemplatesFrames = new ArrayList<SentenceTemplatesFrame>();
        Integer index = 0;
        List<SentenceTemplate> sentenceTemplates = SentenceTemplate.findSentenceTemplate(language, frameInfo.getSentenceTempRepEN(), SentenceType.SENTENCE, frame, frameInfo.getNounGroups(frame), index);
        SentenceTemplatesFrame sentenceTemplatesFrame = new SentenceTemplatesFrame(frame, sentenceTemplates);
        sentenceTemplatesFrames.add(sentenceTemplatesFrame);
        return sentenceTemplatesFrames;
    }

   

    private static List<GrammarRuleTemplateFrame> findFrameGrammarRuleTemplates(String frame,List<SentenceTemplatesFrame> stFrames, Map<String, String> dict) {
        List<GrammarRuleTemplateFrame> grtFrames = new ArrayList<GrammarRuleTemplateFrame>();

        for (SentenceTemplatesFrame stFrame : stFrames) {
            List<GrammarRuleTemplate> grts = new ArrayList<GrammarRuleTemplate>();
            for (SentenceTemplate st : stFrame.getSentenceTemplates()) {
                List<String> modSentences = converToGrammarRuleTemplate(st.getSentences(), dict);
                GrammarRuleTemplate grt = new GrammarRuleTemplate(st, modSentences);
                grts.add(grt);
            }
            GrammarRuleTemplateFrame grtFrame = new GrammarRuleTemplateFrame(frame, grts);
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
            //temporary solution..
            sentence=sentence.replace("give all", "list all");
            String grammarRuleTemplate = sentence;
            //System.out.println("grammarRuleTemplate::" + grammarRuleTemplate);
            grammarRuleTemplates.add(grammarRuleTemplate);
        }
        return grammarRuleTemplates;
    }

}
