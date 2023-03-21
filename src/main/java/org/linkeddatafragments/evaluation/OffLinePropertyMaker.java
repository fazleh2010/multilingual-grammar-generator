/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.evaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.linkeddatafragments.util.io.FileUtils;
import static org.linkeddatafragments.util.io.FileUtils.stringToFile;

/**
 *
 * @author elahi
 */
public class OffLinePropertyMaker {

    private String menu = null;
    private String endpoint = null;
    private String dir = null;
    private String language = null;
    public OffLinePropertyMaker(String menu, String endpoint) {
        this.menu = menu;
        this.endpoint = endpoint;
        this.dir = "../resources/en/property/";
        this.language="en";
    }

    public void make(QALD qaldFile, String languageCode, Boolean flag, Set<Integer> ids) throws Exception {
        Map<String, List<String>> propertyTripes = new TreeMap<String, List<String>>();
        Integer index = 0;
        Integer total = qaldFile.questions.size();
        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            List<String> qualResults = new ArrayList<String>();
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            /*for(QALD.QALDAnswer answer:answers){
                System.out.println(qaldQuestion);
                 System.out.println(answer);
            }*/
            
            

            String qaldSparqlQuery = QALDImporter.getQaldSparqlQuery(qaldQuestions);
            index = index + 1;
            String id = qaldQuestions.id;
            /*if (index>190) {
                ;
            }
            else
                 continue;
            */
            if (id.contains("196")) {
                continue;
            }
          
            if (flag) {
                qualResults = SparqlExecution.getSparqlQuery(menu, endpoint, id, qaldQuestion, qaldSparqlQuery, true);
                System.out.println(id + " " + total);
                qaldSparqlQuery = qaldSparqlQuery.replace("\n", "");
                String where = this.filterString(StringUtils.substringBetween(qaldSparqlQuery, "{", "}"));
                String lines = where.replace(" . ", "\n");
                //System.out.println("lines::" + lines);
                String[] allTriples = lines.split(System.lineSeparator());
                if (allTriples.length > 1) {
                    continue;
                }
                for (String line : allTriples) {
                    if(line.contains("rdf:type")){
                        continue;
                    }
                    String[] triple = line.split(" ");
                    if (triple.length >= 3) {
                        String subj = this.addBracket(this.modifyEntityToUri(filterString(triple[0])));
                        String property = this.modifyProperty(filterString(triple[1]));
                        String obj = this.addBracket(this.modifyEntityToUri(filterString(triple[2])));
                        //String subjLabel = SparqlExecution.getSparqlQuery(menu, endpoint, this.getLabelQuery(subj,language));
                        //String objLabel = SparqlExecution.getSparqlQuery(menu, endpoint,  this.getLabelQuery(obj,language));

                        
                        String tripleStr = null;
                        List<String> triples = new ArrayList<String>();

                        if (this.answerType(subj)) {
                            String objLabel=null;
                            if (obj.contains("http:")) {
                                    objLabel = SparqlExecution.getSparqlQuery(menu, endpoint, this.getLabelQuery(obj, language));
                                   //objLabel=this.filterString(StringUtils.substringBetween(objLabel, "(", ")"));
                              }
                            else
                                objLabel=obj;
                            for (String answer : qualResults) {
                                 try {
                                 String answerUri=answer;
                                //String answerUri = filterString(StringUtils.substringBetween(answer, "(", ")"));
                                String answerLabel="";
                                if(answerUri.contains("http:")){
                                   answerLabel = SparqlExecution.getSparqlQuery(menu, endpoint, this.getLabelQuery(answerUri,language));
                                   //answerLabel=this.filterString(StringUtils.substringBetween(answerLabel, "(", ")"));

                                }
                                tripleStr = answerUri + "=" + answerLabel + "=" + obj + "=" + objLabel;
                                triples.add(tripleStr);
                                } catch (Exception ex) {
                                    continue;
                                }
                            }
                        } else {
                            String subjLabel = null;
                            if (subj.contains("http:")) {
                                subjLabel = SparqlExecution.getSparqlQuery(menu, endpoint, this.getLabelQuery(subj, language));
                                //subjLabel=this.filterString(StringUtils.substringBetween(subjLabel, "(", ")"));

                            } else {
                                subjLabel = subj;
                            }
                            for (String answer : qualResults) {
                                try {
                                     String answerUri=answer;
                                    //String answerUri = filterString(StringUtils.substringBetween(answer, "(", ")"));
                                    String answerLabel = answerUri;
                                    if (answerUri.contains("http:")) {
                                        answerLabel = SparqlExecution.getSparqlQuery(menu, endpoint, this.getLabelQuery(answerUri, language));
                                        //answerLabel=this.filterString(StringUtils.substringBetween(answerLabel, "(", ")"));
                                    }
                                    tripleStr = subj + "=" + subjLabel + "=" + answerUri + "=" + answerLabel;
                                    triples.add(tripleStr);
                                } catch (Exception ex) {
                                    continue;
                                }

                            }
                        }

                        List<String> oldTriples = new ArrayList<String>();
                        if (propertyTripes.containsKey(property)) {
                            oldTriples = propertyTripes.get(property);
                            oldTriples.addAll(triples);
                            propertyTripes.put(property, triples);
                        } else {
                            oldTriples.addAll(triples);
                            propertyTripes.put(property, triples);
                        }

                    }
                }

            }

        }

        this.mapToFile(propertyTripes, this.dir);
    }

    private void mapToFile(Map<String, List<String>> propertyTripes, String dir) throws IOException {
        for (String property : propertyTripes.keySet()) {
            String str = "";
            System.out.println(property);
            String fileName = dir + modifyEntityToSlash(property) + ".txt";
            List<String> list = propertyTripes.get(property);
            for (String triple : list) {
                str += triple + "\n";
            }
            stringToFile(str, fileName);
        }

    }

    private String modifyEntityToUri(String reference) {
        reference = reference.replace("dbo:", "http://dbpedia.org/ontology/");
        reference = reference.replace("dbp:", "http://dbpedia.org/property/");
        reference = reference.replace("res:", "http://dbpedia.org/resource/");
        reference = reference.replace("dbr:", "http://dbpedia.org/resource/");
        reference = reference.replace("xsd:", "http://www.w3.org/2001/XMLSchema#");

        return reference;
    }

    private String modifyEntityToShort(String reference) {
        reference = reference.replace("http://dbpedia.org/ontology/", "dbo:");
        reference = reference.replace("http://dbpedia.org/property/", "dbp:");
        reference = reference.replace("http://dbpedia.org/resource/", "res:");
        reference = reference.replace("http://dbpedia.org/resource/", "dbr:");
        reference = reference.replace("http://www.w3.org/2001/XMLSchema#", "xsd:");
        return reference;
    }

    private String modifyEntityToSlash(String reference) {
        reference = reference.replace("dbo:", "dbo_");
        reference = reference.replace("dbp:", "dbp_");
        return reference;
    }

    private String addBracket(String entity) {
        if (entity.contains("<")) {
            return entity;
        } else {
            return "<" + entity + ">";
        }
    }

    private String modifyProperty(String string) {
        if (string.contains("dbo:") || string.contains("dbp:")) {
            return string;
        } else {
            string = modifyEntityToShort(string);
            string = string.replace("<", "");
            string = string.replace(">", "");
        }
        return string;
    }

    private boolean answerType(String subj) {
        if (subj.contains("?")) {
            return true;
        }
        return false;
    }

    private String filterString(String subj) {
        return subj.strip().trim().stripLeading().stripTrailing();
    }

    private String getLabelQuery(String obj,String language) {
        if(obj.contains("<"))
            ;
        else
            obj="<"+obj+">";
        String sparql = "select ?label {"+obj+" <http://www.w3.org/2000/01/rdf-schema#label> ?label .filter langMatches(lang(?label),\"en\")}";
        return sparql;
    }

}
