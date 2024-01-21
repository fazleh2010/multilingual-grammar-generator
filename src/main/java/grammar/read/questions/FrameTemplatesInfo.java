/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import grammar.datasets.sentencetemplates.SentenceTemplateFactoryDE;
import grammar.datasets.sentencetemplates.SentenceTemplateFactoryEN;
import grammar.datasets.sentencetemplates.SentenceTemplateFactoryES;
import grammar.datasets.sentencetemplates.SentenceTemplateFactoryIT;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.sentencetemplates.TempConstants;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class FrameTemplatesInfo implements TempConstants {

    private SentenceTemplateFactoryEN sentenceTemplateFactoryEN = null;
    private SentenceTemplateFactoryDE sentenceTemplateFactoryDE = null;
    private SentenceTemplateFactoryIT sentenceTemplateFactoryIT = null;
    private SentenceTemplateFactoryES sentenceTemplateFactoryES = null;
    private SentenceTemplateRepository sentenceTempRep = null;

    //private static List<FrameType> frames = List.of(FrameType.NPP, FrameType.VP, FrameType.IPP,FrameType.AA,FrameType.AG);
    private static List<FrameType> frames = List.of(FrameType.NPP, FrameType.VP, FrameType.IPP);

    public FrameTemplatesInfo(Language language) {
        //english
        if (language.equals(Language.EN)) {
            sentenceTemplateFactoryEN = new SentenceTemplateFactoryEN();
            sentenceTemplateFactoryEN.init();
            sentenceTempRep = sentenceTemplateFactoryEN.getSentenceTemplateRepository();
        } else if (language.equals(Language.DE)) {
            // german
            sentenceTemplateFactoryDE = new SentenceTemplateFactoryDE();
            sentenceTemplateFactoryDE.init();
            sentenceTempRep = sentenceTemplateFactoryDE.getSentenceTemplateRepository();
        } else if (language.equals(Language.IT)) {
            // italian
            sentenceTemplateFactoryIT = new SentenceTemplateFactoryIT();
            sentenceTemplateFactoryIT.init();
            sentenceTempRep = sentenceTemplateFactoryIT.getSentenceTemplateRepository();
        } else if (language.equals(Language.ES)) {
            // spanish
            sentenceTemplateFactoryES = new SentenceTemplateFactoryES();
            sentenceTemplateFactoryES.init();
            sentenceTempRep = sentenceTemplateFactoryES.getSentenceTemplateRepository();
        }

    }

    public Map<String, String> getGroups(String frame, Language language) {

        if (language.equals(Language.EN)) {
            return SentenceTemplateFactoryEN.getGroupsEN(frame);
        }
        if (language.equals(Language.DE)) {
            return SentenceTemplateFactoryDE.getGroupsDE(frame);
        }
        if (language.equals(Language.IT)) {
            return SentenceTemplateFactoryIT.getGroupsIT(frame);
        }
        if (language.equals(Language.ES)) {
            return SentenceTemplateFactoryES.getGroupsES(frame);
        }
        return null;
    }

    public SentenceTemplateFactoryDE getSentenceTemplateFactoryDE() {
        return sentenceTemplateFactoryDE;
    }

    public SentenceTemplateFactoryIT getSentenceTemplateFactoryIT() {
        return sentenceTemplateFactoryIT;
    }

    public SentenceTemplateFactoryES getSentenceTemplateFactoryES() {
        return sentenceTemplateFactoryES;
    }

    public SentenceTemplateRepository getSentenceTempRep() {
        return sentenceTempRep;
    }

    public List<FrameType> getFrames() {
        return frames;
    }

    public SentenceTemplateFactoryEN getSentenceTemplateFactoryEN() {
        return sentenceTemplateFactoryEN;
    }

    public SentenceTemplateRepository getSentenceTempRepEN() {
        return sentenceTempRep;
    }

}
