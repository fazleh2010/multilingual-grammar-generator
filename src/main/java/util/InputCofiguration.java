/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import core.element.LinkedData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.element.Language;
import java.io.File;


/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputCofiguration {

    //java -jar target/QuestionGrammarGenerator.jar DE lexicon/de output/de 10 all dataset/dbpedia.json 80.0  
    @JsonProperty("languageCode")
    private String languageCode = null;
    @JsonProperty("inputDir")
    private String inputDir = null;
    @JsonProperty("outputDir")
    private String outputDir = null;
    @JsonProperty("entityDir")

    private LinkedData linkedData = null;

    public InputCofiguration() {

    }

    public String getLanguageCode() {
        return languageCode;
    }

    public Language getLanguage() {
        if (languageCode.contains("de")) {
            return Language.DE;
        } else if (languageCode.contains("en")) {
            return Language.EN;
        } else if (languageCode.contains("es")) {
            return Language.ES;
        } else if (languageCode.contains("it")) {
            return Language.IT;
        }
        return Language.EN;
    }

    public String getInputDir() {
        return inputDir + File.separator + this.languageCode;
    }

    public String getOutputDir() {
        return outputDir + File.separator + this.languageCode;
    }


    public void setLinkedData(String fileName) throws Exception {
        this.linkedData = FileProcessUtils.getLinkedDataConf(new File(fileName));
    }

    public LinkedData getLinkedData() {
        return linkedData;
    }

   
    @Override
    public String toString() {
        return "InputCofiguration{" + "language=" + languageCode + ", inputDir=" + getInputDir() + ", outputDir=" + getOutputDir() +  '}';
    }

}
