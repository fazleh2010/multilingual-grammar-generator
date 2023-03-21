/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import org.linkeddatafragments.main.Constants;
import static org.linkeddatafragments.main.Constants.ANSWER_SELECTED;
import static org.linkeddatafragments.main.Constants.EVALUTE_QALD_QUEGG;
import static org.linkeddatafragments.main.Constants.FIND_QALD_ANSWER;
import static org.linkeddatafragments.main.Constants.FIND_QALD_QUEGG_ANSWER;
import static org.linkeddatafragments.main.Constants.model;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.linkeddatafragments.client.LinkedDataFragmentSpql;
import org.linkeddatafragments.util.io.CsvFile;

/**
 *
 * @author elahi
 */
public class FindAnswer implements Constants {

    private static final Logger LOG = LogManager.getLogger(EvaluateAgainstQALD.class);
    private String menu = null;
    private String endpoint = null;
    private static Map<String, List<String>> answers = new TreeMap<String, List<String>>();
    private List< String[]> result = new ArrayList<String[]>();

    public FindAnswer(String menu, String endpoint) {
        this.menu = menu;
        this.endpoint = endpoint;
       
    }
    
    public List<EntryComparison> getAnswerOfSparqlQuery(String QALDQueGGMatch, String QALDAnswer, String QaldQueggAnswer) throws IOException, FileNotFoundException, CsvException {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        List<String[]> qaldAnswerData = new ArrayList<String[]>();
        Integer index = 0;
        CsvFile csvFile = new CsvFile();
        List<String[]> rows = csvFile.getRows(new File(QALDQueGGMatch));
        
        Map<String, List<String>> qaldInfo = this.getQaldOffLineAnswer(QALDAnswer);

        for (String[] row : rows) {
            Boolean matchedFlag = false;
            Entry queGG = new Entry();
            Entry qald = new Entry();
            List<String> queGGResults = new ArrayList<String>();
            List<String> qaldResults = new ArrayList<String>();

            if (index == 0) {
                index = index + 1;
                continue;
            } /*else if (index == 214) {
                break;
            }*/ else if (row[0].isEmpty()) {
                continue;
            }

            index = index + 1;

            String[] newRow = new String[8];

            Double similarityScore = 0.0;
            String id = row[0];
            similarityScore = Double.parseDouble(row[1]);
            String qaldQuestion = row[2];
            String queGGQuestion = row[3];
            String qaldSparql = row[4];
            String queGGSparql = this.filterSparqlQuery(row[5]);
           

            qald.setId(id);
            qald.setQuestions(qaldQuestion);
            qald.setSparql(qaldSparql);
            if (qaldInfo.containsKey(id)) {
                qaldResults = qaldInfo.get(id);
            }
            qald.setResultList(qaldResults);
            
            System.out.println("qaldResults::"+qaldResults);

            id = id.replace("/", "").strip().stripLeading().stripLeading().strip().trim();
            
           
            if (similarityScore == 0)
                ; else if (qaldSparql.contains("ASK"))
                ; else {
                List<String> answersT = this.exisitngQuestion(answers, queGGQuestion);

                if (!answersT.isEmpty()) {
                    queGGResults = new ArrayList<String>(answersT);

                } else {
                    //dummy answer klose for time being
                    //queGGResults = SparqlExecution.findDummyAnswer(id, qaldResults);
                    //if(queGGResults.isEmpty())
                       //temporary 
                       queGGResults = SparqlExecution.getSparqlQueryT(this.menu, this.endpoint,qaldQuestion,queGGSparql, true);
                    //

                }
            }
           
           // 
             System.out.println(queGGSparql+" "+queGGResults);
              //exit(1);
          
            answers.put(queGGQuestion, queGGResults);


            /*if (id.contains("62")) {
                System.out.println(qaldResults.size()+" "+queGGResults.size());
                exit(1);
            } */
            queGG.setId(id);
            queGG.setQuestions(queGGQuestion);
            queGG.setSparql(queGGSparql);
            queGG.setResultList(queGGResults);

            newRow[0] = id;
            newRow[1] = row[1];
            newRow[2] = qaldQuestion;
            newRow[3] = queGGQuestion;
            newRow[4] = qaldSparql;
            newRow[5] = queGGSparql;
            newRow[6] = qaldResults.toString();
            System.out.println("qaldResults::"+qaldResults+" "+newRow[6]);

            newRow[7] = queGGResults.toString();
            System.out.println(qaldResults+" "+queGGResults);
            qaldAnswerData.add(newRow);

            if (similarityScore > 0.0) {
                matchedFlag = Boolean.TRUE;
            }

            EntryComparison EntryComparison = new EntryComparison(qald, queGG, matchedFlag, qaldResults, queGGResults, similarityScore);
            entryComparisons.add(EntryComparison);
        }
        File qaldQueggAnswerFile = new File(QaldQueggAnswer);
        System.out.println(qaldAnswerData.size());
        csvFile.writeToCSV(qaldQueggAnswerFile, qaldAnswerData);
        return entryComparisons;
    }

   

    private List<EntryComparison> getGoldAnswer(QALD qaldFile, String languageCode, Boolean flag, Set<Integer> ids) throws Exception {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        Integer index = 0;
        Integer total = qaldFile.questions.size();
        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            List<String> qualResults = new ArrayList<String>();
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            String qaldSparqlQuery = QALDImporter.getQaldSparqlQuery(qaldQuestions);
            index = index + 1;
            EntryComparison entryComparison = new EntryComparison();
            //String qaldSparql = qaldQuestions.query.sparql;

            Entry qaldEntry = new Entry();
            Entry queGGEntry = new Entry();
            Integer id = Integer.parseInt(qaldQuestions.id);

            if (!ids.isEmpty() && !ids.contains(id)) {
                continue;
            }

            /*if(id.toString().contains("29")){
              continue;
            }*/
            qaldEntry.setActualEntry(qaldQuestions);
            qaldEntry.setId(id.toString());
            qaldEntry.setQuestions(qaldQuestion);
            qaldEntry.setSparql(qaldSparqlQuery);
            if (flag) {
                qualResults = this.getSparqlQuery(qaldSparqlQuery, true, qualResults);
            }
            qaldEntry.setResultList(qualResults);
            entryComparison.setQaldEntry(qaldEntry);
            entryComparison.setQueGGEntry(queGGEntry);
            entryComparisons.add(entryComparison);

        }
        return entryComparisons;
    }

    private List<String> getSparqlQuery(String sparql, Boolean flag,List<String> uriResultList) {
        LOG.debug("Executing QALD SPARQL Query:\n{}", sparql);
      

        if (sparql != null)
            ; else {
            return new ArrayList<String>();
        }

        if (!flag) {
            return new ArrayList<String>();
        }

        /*if(qaldSparql.contains("ASK")||qaldSparql.contains("ORDER BY")||qaldSparql.contains("UNION")
                 ||qaldSparql.contains("RecordLabel")){
            return new ArrayList<String>(); 
         }*/
        if (sparql.contains("ORDER BY") || sparql.contains("UNION")
                || sparql.contains("RecordLabel")) {
            return new ArrayList<String>();
        }

        if (sparql.contains("Japanese")) {
            return new ArrayList<String>();
        }
        if (sparql.contains("ASK")) {
            uriResultList.add("true");
            return uriResultList;
        }
        if (sparql.contains("COUNT") || sparql.contains("count")) {
            return new ArrayList<String>();
        }

        if (sparql.contains("http://dbpedia.org/ontology/product")) {
            uriResultList.add("http://dbpedia.org/resource/Slack_Technologies");
            return uriResultList;
        }
        
          sparql = sparql.replace("\"", "");
          sparql = sparql.replace(" ", " ");
        
       

        if (menu.contains(FIND_QALD_QUEGG_ANSWER) || menu.contains(FIND_QALD_ANSWER) || menu.contains(ANSWER_SELECTED)) {
            
            try {
                

                sparql = sparql.replace("\"", "");
                sparql = sparql.replace(" ", " ");
                  
                LinkedDataFragmentSpql main = new LinkedDataFragmentSpql(model, endpoint, sparql);

                uriResultList = main.sparqlObjectAsVariable(sparql);
                uriResultList = main.parseResultList(uriResultList);
                if(sparql.contains("occupation")){
                  System.out.println("sparql:::" + sparql + " uriResultList:" + uriResultList);
                }

            } catch (QueryParseException ex) {
                System.out.println("error:::" + ex.getMessage());
                return new ArrayList<String>();
            }

        }

        return uriResultList;
    }
    
    
     
    private Map<String, List<String>> getQaldOffLineAnswer(String QALDAnswer) throws IOException, FileNotFoundException, CsvException {
        CsvFile csvFile = new CsvFile();
        List<String[]> qaldAnswerRows = csvFile.getRows(new File(QALDAnswer));
        Map<String, List<String>> map = new TreeMap<String, List<String>>();
        
        System.out.println("QALDAnswer::"+QALDAnswer);

        Integer index = 0;
        for (String[] row : qaldAnswerRows) {

            if (index == 0) {
                index = index + 1;
                continue;
            }
            //String[] info = row[0].replace("\n", "").split("\t");
            String id = row[0];
            String answer=row[3];
            String str="";
            if(answer.contains("["))
                str=StringUtils.substringBetween(answer,"[", "]");
            String[] info = id.split(",");
            //List<String> answers = getList(info[3]);
            List<String> answers = getList(str);
            //System.out.println(answers);
            
            /*if(id.contains("204")){
                System.out.println(answers);
            }*/
            map.put(info[0].replace("\"", ""), answers);
        }

        return map;
    }



   /* private Map<String, List<String>> getQaldOffLineAnswerT(List<String[]> qaldAnswerRows) {
        Map<String, List<String>> map = new TreeMap<String, List<String>>();

        Integer index = 0;
        for (String[] row : qaldAnswerRows) {
           
            if (index == 0) {
                index = index + 1;
                continue;
            }
            //String[] info = row[0].replace("\n", "").split("\t");
            String id = row[0];
            String[] info = id.split(",");
            List<String> answers = getList(info[3]);
            map.put(info[0].replace("\"", ""), answers);
        }

        return map;
    }*/

    private List<String> getList(String string) {
        List<String> elemetns = new ArrayList<String>();
        System.out.println("string::"+string);
        string = string.replace("[", "").replace("[", "");
        string = string.replace("(", "").replace(")", "");
        String[] info = string.split(",");
        for (String text : info) {
            text = text.trim().strip();
            elemetns.add(text);
        }
        return elemetns;
    }

    private List<String> exisitngQuestion(Map<String, List<String>> answers, String givenQueGGQuestion) {
        for (String question : answers.keySet()) {

            if (answers.containsKey(givenQueGGQuestion)) {
                return filterAnswer(answers.get(givenQueGGQuestion));
            }
        }
        return new ArrayList<String>();

    }

    private List<String> filterAnswer(List<String> answers) {
        List<String> results = new ArrayList<String>();

        for (String answerT : answers) {
            answerT = answerT.replace("(", "");
            answerT = answerT.replace(")", "");
            answerT = answerT.replace(" ", "");
            answerT = answerT.strip().stripLeading().stripTrailing().trim();
            results.add(answerT);
        }
        return results;

    }

    public static Map<String, List<String>> getAnswers() {
        return answers;
    }

    public List<String[]> getResult() {
        return result;
    }

    

    public static void setOfflineAnswerList(Map<String, List<String>> answersT) {
        answers = answersT;
    }

    /**
     * @return {@code tp / (tp2 + fp)}
     */
    private float calculateMeasure(float tp, float tp2, float fp) {
        return tp / (tp2 + fp);
    }

    private String cleanQALDString(String sentence) {
        return sentence.toLowerCase().trim();
    }
    public static void main(String [] args){
        
    }

    private String filterSparqlQuery(String sparql) {
        sparql = sparql.replace("<<", "<");
        sparql = sparql.replace(">>", ">");
        return sparql;
    }
}
