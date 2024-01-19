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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class FrameInfo implements TempConstants {

    private SentenceTemplateFactoryEN sentenceTemplateFactoryEN = null;
    private SentenceTemplateRepository sentenceTempRepEN = null;
    private Map<String, String> nounGroups = new LinkedHashMap<String, String>();
    private SentenceTemplateFactoryDE sentenceTemplateFactoryDE = null;
    private SentenceTemplateRepository sentenceTempRepDE = null;

    public FrameInfo(String language) {
        //english
        if (language.contains("en")) {
            sentenceTemplateFactoryEN = new SentenceTemplateFactoryEN();
            sentenceTemplateFactoryEN.init();
            sentenceTempRepEN = sentenceTemplateFactoryEN.getSentenceTemplateRepository();
            nounGroups = new LinkedHashMap<String, String>();
            nounGroups.put(Prepositional_Adjuct, subject);
            nounGroups.put(Copulative_Subject, object);
            nounGroups.put(HOW_MANY_THING, amount);
            nounGroups.put(booleanQuestionDomainRange, ask);
            nounGroups.put(nounPhrase, nounPhrase);
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

    public Map<String, String> getNounGroups() {
        return nounGroups;
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

}
