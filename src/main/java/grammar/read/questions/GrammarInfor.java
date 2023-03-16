/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

/**
 *
 * @author elahi
 */
public class GrammarInfor {

    private String returnSubjOrObj = null;
    private String returnType = null;
    private String bindingType = null;
    private String template = null;
    private String[] realQuestions = null;
    private String sparqlQuery = null;


    public GrammarInfor(String returnSubjOrObj, String bindingType, String returnType, String template, String[] realQuestions, String sparqlQuery) {
        this.returnSubjOrObj = returnSubjOrObj;
        this.returnType = bindingType;
        this.bindingType = returnType;
        this.template = template;
        this.realQuestions = realQuestions;
        this.sparqlQuery = sparqlQuery;

    }

    public String getReturnSubjOrObj() {
        return returnSubjOrObj;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getBindingType() {
        return bindingType;
    }

    public String getTemplate() {
        return template;
    }

    public String[] getRealQuestions() {
        return realQuestions;
    }

    public String getSparqlQuery() {
        return sparqlQuery;
    }

    @Override
    public String toString() {
        return "GrammarInfor{" + "returnSubjOrObj=" + returnSubjOrObj + ", returnType=" + returnType + ", bindingType=" + bindingType + ", template=" + template + ", realQuestions=" + realQuestions + ", sparqlQuery=" + sparqlQuery + '}';
    }

}
