/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import grammar.datasets.sentencetemplates.SentenceTemplateFactoryDE;
import grammar.datasets.sentencetemplates.SentenceTemplateFactoryEN;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.sentencetemplates.TempConstants;
import grammar.structure.component.FrameType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class FrameInfo implements TempConstants {

    private SentenceTemplateFactoryEN sentenceTemplateFactoryEN = null;
    private SentenceTemplateRepository sentenceTempRepEN = null;
    private SentenceTemplateFactoryDE sentenceTemplateFactoryDE = null;
    private SentenceTemplateRepository sentenceTempRepDE = null;
    //private static List<FrameType> frames = List.of(FrameType.NPP, FrameType.VP, FrameType.IPP,FrameType.AA,FrameType.AG);
    private static List<FrameType> frames = List.of(FrameType.NPP, FrameType.VP);

    public FrameInfo(String language) {
        //english
        if (language.contains("en")) {
            sentenceTemplateFactoryEN = new SentenceTemplateFactoryEN();
            sentenceTemplateFactoryEN.init();
            sentenceTempRepEN = sentenceTemplateFactoryEN.getSentenceTemplateRepository();
            
        }
        if (language.contains("de")) {
            // german
            sentenceTemplateFactoryDE = new SentenceTemplateFactoryDE();
            sentenceTemplateFactoryDE.init();
            sentenceTempRepDE = sentenceTemplateFactoryDE.getSentenceTemplateRepository();
        }

    }

    public SentenceTemplateFactoryEN getSentenceTemplateFactoryEN() {
        return sentenceTemplateFactoryEN;
    }

    public SentenceTemplateRepository getSentenceTempRepEN() {
        return sentenceTempRepEN;
    }

    public Map<String, String> getNounGroups(String frame) {
        Map<String, String> group = new LinkedHashMap<String, String>();
        if (frame.contains(FrameType.NPP.getName())) {
            group.put(Prepositional_Adjuct, subject);
            group.put(Copulative_Subject, object);
            group.put(HOW_MANY_THING, amount);
            group.put(booleanQuestionDomainRange, ask);
            group.put(nounPhrase, nounPhrase);
        }
        else if (frame.contains(FrameType.VP.getName())) {
            group.put(PERSON_CAUSE+activeTransitive, subject+"-"+"type"+"1");
            group.put(PERSON_CAUSE+passiveTransitive, object+"-"+"type"+"1");
            
            group.put(PERSON_CAUSE_OPPOSITITE+activeTransitive, subject+"-"+"type"+"2");
            group.put(PERSON_CAUSE_OPPOSITITE+passiveTransitive, object+"-"+"type"+"2");
            
            group.put(PERSON_CAUSE_NOUN_PHRASE+activeTransitive, subject+"-"+"type"+"3");
            group.put(PERSON_CAUSE_NOUN_PHRASE+passiveTransitive, object+"-"+"type"+"3");
            
            group.put(PERSON_CAUSE_SUBJECT+activeTransitive, subject+"-"+"type"+"4");
            group.put(PERSON_CAUSE_SUBJECT+passiveTransitive, object+"-"+"type"+"4");
            
            group.put(PERSON_CAUSE_SUBJECT_PREPOSITION+activeTransitive, subject+"-"+"type"+"5");
            group.put(PERSON_CAUSE_SUBJECT_PREPOSITION+passiveTransitive, object+"-"+"type"+"5");
            
            group.put(PERSON_PERSON+activeTransitive, subject+"-"+"type"+"6");
            group.put(PERSON_PERSON+passiveTransitive+passiveTransitive, object+"-"+"type"+"6");
            
            group.put(PERSON_ACTIVITY+activeTransitive, subject+"-"+"type"+"7");
            group.put(PERSON_ACTIVITY+passiveTransitive, object+"-"+"type"+"7");
            
            group.put(HOW_MANY_TOTAL+activeTransitive, subject+"-"+amount+"-"+"type"+"1");
            group.put(HOW_MANY_TOTAL+passiveTransitive, object+"-"+amount+"-"+"type"+"1");
            
            group.put(HOW_MANY_THING+activeTransitive, subject+"-"+amount+"-"+"type"+"2");
            group.put(HOW_MANY_THING+passiveTransitive, object+"-"+amount+"-"+"type"+"2");
            
        }
        else if (frame.contains(FrameType.IPP.getName())) {
            group.put(Prepositional_Adjuct, subject);
            group.put(Copulative_Subject, object);
            group.put(HOW_MANY_THING, amount);
            group.put(booleanQuestionDomainRange, ask);
            group.put(nounPhrase, nounPhrase);
        }else if (frame.contains(FrameType.AA.getName())) {
            group.put(Prepositional_Adjuct, subject);
            group.put(Copulative_Subject, object);
            group.put(HOW_MANY_THING, amount);
            group.put(booleanQuestionDomainRange, ask);
            group.put(nounPhrase, nounPhrase);
        }
        else if (frame.contains(FrameType.AG.getName())) {
            group.put(Prepositional_Adjuct, subject);
            group.put(Copulative_Subject, object);
            group.put(HOW_MANY_THING, amount);
            group.put(booleanQuestionDomainRange, ask);
            group.put(nounPhrase, nounPhrase);
        }

        return group;
    }

    public SentenceTemplateFactoryDE getSentenceTemplateFactoryDE() {
        return sentenceTemplateFactoryDE;
    }

    public SentenceTemplateRepository getSentenceTempRepDE() {
        return sentenceTempRepDE;
    }

    public static void main(String[] args) {
        System.out.println("test");
    }

    public List<FrameType> getFrames() {
        return frames;
    }

}
