/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import grammar.read.questions.GrammarEntriesLex;
import grammar.read.questions.ProtoToRealQuesrion;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class JsonWriter {

    public static void writeClassToJson(GrammarEntriesLex grammarEntriesLex, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(new File(fileName), grammarEntriesLex);
        } catch (IOException ex) {
            Logger.getLogger(ProtoToRealQuesrion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
