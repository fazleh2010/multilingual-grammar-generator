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
import grammar.read.questions.SentenceTemplate;
import grammar.read.questions.SentenceTemplateAll;
import grammar.read.questions.SentenceTemplatesFrame;
import grammar.structure.component.FrameType;
import grammar.structure.component.SentenceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class TemplateGeneration implements TempConstants {

    public static void main(String[] args) {
        SentenceTemplateAll sentenceTemplateAll = new SentenceTemplateAll();
        List<String> languages = new ArrayList<String>();

        for (String language : languages) {
            FrameInfo frameInfo = new FrameInfo(language);
            String fileName = "output/" + language + "/sentenceTemplate.json";
            for (FrameType frameType : frameInfo.getFrames()) {
                String frame = frameType.getName();
                sentenceTemplateAll = findNounPPFrame(frameInfo, frame, sentenceTemplateAll);
            }
            JsonWriter.writeSentenceTemplateToJson(sentenceTemplateAll, fileName);
        }

    }

    private static SentenceTemplateAll findNounPPFrame(FrameInfo frameInfo, String frame, SentenceTemplateAll sentenceTemplateAll) {
        List<SentenceTemplatesFrame> sentenceTemplatesFrames = new ArrayList<SentenceTemplatesFrame>();
        Integer index = 0;
        List<SentenceTemplate> sentenceTemplates = findGrammarRuleTemplates(frameInfo.getSentenceTemplateFactoryEN(), frameInfo.getSentenceTempRepEN(), SentenceType.SENTENCE, frame, frameInfo.getNounGroups(), index);
        SentenceTemplatesFrame sentenceTemplatesFrame = new SentenceTemplatesFrame(frame, sentenceTemplates);
        sentenceTemplatesFrames.add(sentenceTemplatesFrame);
        sentenceTemplateAll.setFrameSentenceTemplates(sentenceTemplatesFrames);
        return sentenceTemplateAll;
    }

    private static List<SentenceTemplate> findGrammarRuleTemplates(SentenceTemplateFactoryEN sentenceTemplateFactoryEN, SentenceTemplateRepository sentenceTempRep, SentenceType SentenceType, String frame, Map<String, String> groups, Integer index) {
        List<SentenceTemplate> sentenceTemplates = new ArrayList<SentenceTemplate>();
        for (String key : groups.keySet()) {
            String groupName = groups.get(key);
            index = index + 1;
            List<String> list = sentenceTempRep.findOneByEntryTypeAndLanguageAndArguments(SentenceType,
                    sentenceTemplateFactoryEN.getLanguage(), new String[]{frame, key});
            sentenceTemplates.add(new SentenceTemplate(index.toString(), groupName, list));
        }
        return sentenceTemplates;
    }

}
