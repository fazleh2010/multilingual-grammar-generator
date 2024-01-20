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
        
        List<String> languages = List.of("en");


        for (String language : languages) {
            FrameInfo frameInfo = new FrameInfo(language);
            String fileName = "output/" + language + "/sentenceTemplate.json";
            List<SentenceTemplatesFrame> allFrames =new ArrayList<SentenceTemplatesFrame>();
            for (FrameType frameType : frameInfo.getFrames()) {
                String frame = frameType.getName();
                List<SentenceTemplatesFrame> sentenceTemplatesFrames = findFrameTemplate(frameInfo, frame);
                allFrames.addAll(sentenceTemplatesFrames);
            }
            SentenceTemplateAll sentenceTemplateAll = new SentenceTemplateAll(allFrames);
            JsonWriter.writeSentenceTemplateToJson(sentenceTemplateAll, fileName);
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

}
