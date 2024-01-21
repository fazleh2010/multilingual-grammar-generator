/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.datasets.sentencetemplates.SentenceTemplateFactoryEN;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SentenceTemplate {

    private static Language findLanguage(String languageStr) {
        if (languageStr.contains("en")) {
            return Language.EN;
        } else if (languageStr.contains("de")) {
            return Language.DE;
        } else if (languageStr.contains("it")) {
            return Language.IT;
        } else if (languageStr.contains("es")) {
            return Language.ES;
        }
        return Language.EN;
    }

    @JsonProperty("templateNo")
    private String templateNo = null;
    @JsonProperty("group")
    private String group = null;
    @JsonProperty("sentences")
    private List<String> sentences = new ArrayList<String>();

    private static Set<String> categories = new TreeSet<String>();

    public SentenceTemplate() {

    }

    public SentenceTemplate(String sentenceTemplateNo, String syntacticType, List<String> sentencesT) {
        this.templateNo = sentenceTemplateNo;
        this.group = syntacticType;
        this.sentences = sentencesT;
    }

    public SentenceTemplate(SentenceTemplate sentenceTemplate, List<String> modifiedSentences) {
        this.templateNo = sentenceTemplate.getTemplateNo();
        this.group = sentenceTemplate.getGroup();
        this.sentences = modifiedSentences;
    }

    public static List<SentenceTemplate> findSentenceTemplate(Language language,
            SentenceTemplateRepository sentenceTempRep, SentenceType sentenceType,
            String frame, Map<String, String> groups, Integer index) {
        List<SentenceTemplate> sentenceTemplates = new ArrayList<SentenceTemplate>();
        for (String key : groups.keySet()) {
            String groupName = groups.get(key);
            
            List<String> list = new ArrayList<String>();

            list = sentenceTempRep.findOneByEntryTypeAndLanguageAndArguments(sentenceType,
                    language, new String[]{frame, key});

            if (!list.isEmpty()) {
                index = index + 1;
                addToCategoryDictionary(list);
                sentenceTemplates.add(new SentenceTemplate(index.toString(), groupName, list));
            }

        }
        return sentenceTemplates;
    }

    private static void addToCategoryDictionary(List<String> list) {
        for (String sentence : list) {
            sentence = sentence.replace("?", "");
            sentence = sentence.replace(".", "");
            String[] tokens = sentence.split(" ");
            for (String token : tokens) {
                categories.add(token);
            }

        }
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public String getGroup() {
        return group;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public static Set<String> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "SentenceTemplate{" + "templateNo=" + templateNo + ", group=" + group + ", sentences=" + sentences + '}';
    }

}
