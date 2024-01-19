/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SentenceTemplate {
    @JsonProperty("templateNo")
    private String templateNo = null;
    @JsonProperty("group")
    private String group = null;
    @JsonProperty("sentences")
    private List<String> sentences = new ArrayList<String>();
    

    public SentenceTemplate() {

    }

    public SentenceTemplate(String sentenceTemplateNo, String syntacticType,List<String> sentencesT) {
        this.templateNo = sentenceTemplateNo;
        this.group = syntacticType;
        this.sentences = sentencesT;
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

    @Override
    public String toString() {
        return "SentenceTemplate{" + "templateNo=" + templateNo + ", group=" + group + ", sentences=" + sentences + '}';
    }

   
   
}

