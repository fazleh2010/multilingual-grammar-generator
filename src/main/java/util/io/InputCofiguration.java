/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import linkeddata.LinkedData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.generator.GrammarRuleGeneratorRoot;
import grammar.sparql.SPARQLRequest;
import grammar.structure.component.Language;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import turtle.TutleConverter;

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
    @JsonProperty("parameterDir")
    private String parameterDir = null;
    @JsonProperty("entityDir")
    private String entityDir = null;
    @JsonProperty("questionDir")
    private String questionDir = null;
    @JsonProperty("classDir")
    private String classDir = null;
    @JsonProperty("wikiFile")
    private String wikiFile = null;
    @JsonProperty("abstractFile")
    private String abstractFile = null;
    private String qaldDir = null;
    @JsonProperty("numberOfEntities")
    private Integer numberOfEntities;
    @JsonProperty("similarityThresold")
    private Double similarityThresold;
    @JsonProperty("csvToTurtle")
    private Boolean csvToTurtle = false;
    @JsonProperty("turtleToProtoType")
    private Boolean turtleToProtoType = false;
    @JsonProperty("protoTypeToQuestion")
    private Boolean protoTypeToQuestion = false;
    @JsonProperty("evalution")
    private Boolean evalution;
    @JsonProperty("composite")
    private Boolean compositeFlag;
    @JsonProperty("single")
    private Boolean singleFlag;
    @JsonProperty("online")
    private Boolean online;
    @JsonProperty("externalEntittyList")
    private Boolean externalEntittyList;
    @JsonProperty("evalutionQuestion")
    private Boolean evalutionQuestion = false;
    @JsonProperty("offlineQuestion")
    private Boolean offlineQuestion = false;
    @JsonProperty("evalutionBindingFile")
    private String evalutionBindingFile;
    @JsonProperty("batchFile")
    private String batchFile;

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

    public Integer getNumberOfEntities() {
        return numberOfEntities;
    }

    public Boolean isCsvToTurtle() {
        return csvToTurtle;
    }

    public Boolean getTurtleToProtoType() {
        return turtleToProtoType;
    }

    public Boolean isProtoTypeToQuestion() {
        return protoTypeToQuestion;
    }

    public Boolean isEvalution() {
        return evalution;
    }

    public Double getSimilarityThresold() {
        return similarityThresold;
    }

    public Boolean getOfflineQuestion() {
        return offlineQuestion;
    }

    public void setLinkedData(String fileName) throws Exception {
        this.linkedData = FileProcessUtils.getLinkedDataConf(new File(fileName));
    }

    public LinkedData getLinkedData() {
        return linkedData;
    }

    public String getQaldDir() {
        return qaldDir;
    }

    public Boolean getCsvToTurtle() {
        return csvToTurtle;
    }

    public Boolean getProtoTypeToQuestion() {
        return protoTypeToQuestion;
    }

    public Boolean getEvalution() {
        return evalution;
    }

    public Boolean getCompositeFlag() {
        return compositeFlag;
    }

    public Boolean getSingleFlag() {
        return singleFlag;
    }

    public String getEntityDir() {
        return entityDir;
    }

    public String getQuestionDir() {
        return questionDir;
    }

    public String getClassDir() {
        return classDir;
    }

    public Boolean getOnline() {
        return online;
    }

    public Boolean getExternalEntittyList() {
        return externalEntittyList;
    }

    public String getWikiFile() {
        return wikiFile;
    }

    public String getAbstractFile() {
        return abstractFile;
    }

    public String getEvalutionBindingFile() {
        return evalutionBindingFile;
    }

    public Boolean getEvalutionQuestion() {
        return evalutionQuestion;
    }

    public String getBatchFile() {
        return batchFile;
    }

    public String getParameterDir() {
        return parameterDir;
    }

    @Override
    public String toString() {
        return "InputCofiguration{" + "language=" + languageCode + ", inputDir=" + getInputDir() + ", outputDir=" + getOutputDir() + ", numberOfEntities=" + numberOfEntities + ", similarityThresold=" + similarityThresold + ", csvToTurtle=" + csvToTurtle + ", turtleToProtoType=" + turtleToProtoType + ", protoTypeToQuestion=" + protoTypeToQuestion + ", evalution=" + evalution + '}';
    }

}
