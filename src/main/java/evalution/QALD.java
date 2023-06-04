package evalution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import parser.GrammarRule;
import parser.QAElement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public class QALD {

    private List<QAElement> qaldQuestions = new ArrayList<QAElement>();

    public QALD(String qaldFileName) {
        this.getQuestionSparql(qaldFileName);
    }

    private void getQuestionSparql(String qaldFileName) {
        List<QAElement> qaElement = new ArrayList<QAElement>();
        QAElement aqElement = new QAElement(" ","");
        qaElement.add(aqElement);
    }

    public List<QAElement> getQaldQuestions() {
        return qaldQuestions;
    }

   

}
