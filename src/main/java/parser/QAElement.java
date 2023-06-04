/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author elahi
 */
public class QAElement {
    private String question = null;
    private String sparql = null;
    
    public QAElement(String question,String sparql){
       this.question = question;
       this.sparql = sparql;
    }

    public String getQuestion() {
        return question;
    }

    public String getSparql() {
        return sparql;
    }
    
}
