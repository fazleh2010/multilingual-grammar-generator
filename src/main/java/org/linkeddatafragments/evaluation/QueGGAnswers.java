/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueGGAnswers {

    @JsonProperty("answers")
    private Map<String, List<String>> answers = new TreeMap<String, List<String>>();

    public QueGGAnswers() {

    }

    public QueGGAnswers(Map<String, List<String>> answersT) {
       this.answers=answersT;
    }

    public Map<String, List<String>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, List<String>> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QueGGAnswers{" + "answers=" + answers + '}';
    }

}
