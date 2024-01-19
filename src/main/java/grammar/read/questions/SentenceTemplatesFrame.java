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
public class SentenceTemplatesFrame {

    @JsonProperty("frame")
    private String frame = null;
    @JsonProperty("sentenceTemplates")
    private List<SentenceTemplate> sentenceTemplates = new ArrayList<SentenceTemplate>();
    
    public SentenceTemplatesFrame() {

    }

    public SentenceTemplatesFrame(String frame,List<SentenceTemplate> sentenceTemplates) {
        this.frame = frame;
        this.sentenceTemplates = sentenceTemplates;
    }

    public List<SentenceTemplate> getSentenceTemplates() {
        return sentenceTemplates;
    }

    @Override
    public String toString() {
        return "SentenceTemplatesFrame{" + "frame=" + frame + ", sentenceTemplates=" + sentenceTemplates + '}';
    }

}
