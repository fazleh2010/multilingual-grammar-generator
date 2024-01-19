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
public class SentenceTemplateAll {

    @JsonProperty("TemplateType")
    private String templateType = "sentenceTemplates";
    @JsonProperty("FrameSentenceTemplates")
    private List<SentenceTemplatesFrame> frameSentenceTemplates = new ArrayList<SentenceTemplatesFrame>();

    public SentenceTemplateAll() {

    }

    public SentenceTemplateAll(List<SentenceTemplatesFrame> frameSentenceTemplates) {
        this.frameSentenceTemplates = frameSentenceTemplates;
    }

    public String getTemplateType() {
        return templateType;
    }

    public List<SentenceTemplatesFrame> getFrameSentenceTemplates() {
        return frameSentenceTemplates;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public void setFrameSentenceTemplates(List<SentenceTemplatesFrame> frameSentenceTemplates) {
        this.frameSentenceTemplates = frameSentenceTemplates;
    }

}
