package org.linkeddatafragments.evaluation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import org.linkeddatafragments.evaluation.QALD.QALDQuestions;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.linkeddatafragments.util.io.CsvFile;
import org.linkeddatafragments.util.io.LcQuad;
import org.linkeddatafragments.util.io.LcQuadElement;

public class QALDImporter {
    private static final Logger LOG = LogManager.getLogger(QALDImporter.class);

    public QALDImporter() {
    }

    public void qaldToCSV(String qaldFile, String outputFile, String languageCode) {
        QALD qald = null;
        try {
            if (qaldFile.contains("qald")) {

                qald = readQald(qaldFile);

            } else {
                qald = lcQuad(qaldFile);
            }
            writeToCSV(qaldJsonToCSVTemplate(qald, languageCode), outputFile);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(QALDImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public QALD readQald(String qaldFile) throws IOException {
        if (qaldFile.contains("qald")) {
            return new ObjectMapper().readValue(new File(qaldFile), QALD.class);
        } else {
            return lcQuad(qaldFile);
        }
    }

    public QALD lcQuad(String file) throws IOException {
        LcQuad lcQuad=new LcQuad(file);
        return  new QALD(lcQuad.getDatasetName(),lcQuad.getLcQuadElements());
    }
    
    public void writeToCSV(List<String[]> dataLines, String fileName) throws IOException {
        /*CSVWriter writer = new CSVWriter(new FileWriter(fileName));
        dataLines.forEach(writer::writeNext);
        writer.close();*/
        CsvFile csvFile=new CsvFile(new File(fileName));
        csvFile.writeToCSV(dataLines);
        
    }

    /*public void writeToCSV(List<String[]> dataLines, String fileName) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(fileName), '\t', '"', '"', "\n");
        dataLines.forEach(writer::writeNext);
        writer.close();
    }*/

    private List<String[]> qaldJsonToCSVTemplate(QALD qaldFile, String languageCode) {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"id", "answertype", "question", "sparql"});

        for (QALDQuestions qaldQuestions : qaldFile.questions) {
            //System.out.println(qaldQuestions.id+" "+qaldQuestions.question+" "+qaldQuestions.query.sparql+" "+qaldQuestions.answertype);
            try{
            list.add(new String[]{qaldQuestions.id, qaldQuestions.answertype, getQaldQuestionString(qaldQuestions,
                languageCode
                ), qaldQuestions.query.sparql});
            }
            catch(Exception ex){
                
            }
        }
        /*qaldFile.questions.forEach(qaldQuestions -> {
            list.add(new String[]{qaldQuestions.id, qaldQuestions.answertype, getQaldQuestionString(qaldQuestions,
                languageCode
                ), qaldQuestions.query.sparql});
        });*/
        return list;
    }

    public static String getQaldQuestionString(QALD.QALDQuestions qaldQuestions, String languageAbbreviation) {
        return qaldQuestions.question.stream()
                .filter(qaldQuestion -> qaldQuestion.language.startsWith(languageAbbreviation))
                .findFirst()
                .orElseThrow().string;
    }
    
    

    public static String getQaldSparqlQuery(QALD.QALDQuestions qaldQuestions) {
        if(qaldQuestions.query!=null)
           return  qaldQuestions.query.sparql;
        return null;
    }
      public static String getQaldId(QALD.QALDQuestions qaldQuestions) {
        if(qaldQuestions.id!=null)
           return  qaldQuestions.id;
        return null;
    }

    public static void main(String[] args) throws IOException {

        QALDImporter qaldImporter = new QALDImporter();

    }

}