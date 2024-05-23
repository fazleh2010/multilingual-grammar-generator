
import eu.monnetproject.lemon.LemonModel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.jena.sys.JenaSystem;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import util.io.FileProcessUtils;
import turtle.GermanTurtle;
import java.io.File;
import java.io.IOException;
import turtle.EnglishTurtle;
import linkeddata.LinkedData;
import turtle.ItalianTurtle;
import turtle.SpanishTurtle;
import turtle.TutleConverter;
import util.io.FileFolderUtils;
import util.io.InputCofiguration;
import grammar.structure.component.Language;

@NoArgsConstructor
public class QueGG {

    private static final Logger LOG = LogManager.getLogger(QueGG.class);

    /*public static void main(String[] args) throws Exception {
        JenaSystem.init();
        QueGG queGG = new QueGG();
        List<String[]> languageDBs = new ArrayList<String[]>();
        languageDBs.add(new String[]{"conf/inputConf_en.json", "dataset/dbpedia_en.json"});
        //languageDBs.add(new String[]{"test/inputConf_de.json", "dataset/dbpedia_de.json"});
        //languageDBs.add(new String[]{"test/inputConf_it.json", "dataset/dbpedia_it.json"});
        //languageDBs.add(new String[]{"test/inputConf_es.json", "dataset/dbpedia_es.json"});

        for (String[] languageDB : languageDBs) {
            main1(languageDB);
            System.out.println("completed gramar generation completed!!!!" + languageDB[0]);
        }

    }*/

    public static void main(String[] args) throws Exception {
        JenaSystem.init();
        QueGG queGG = new QueGG();
        String configFile = null, dataSetConfFile = null;

        Properties batch = new Properties();

        try {
            if (args.length < 2) {
                System.err.printf("Too few parameters (%s/%s)", args.length);
                throw new IllegalArgumentException(String.format("Too few parameters (%s/%s)", args.length));
            } else if (args.length == 2) {
                configFile = args[0];
                InputCofiguration inputCof = FileProcessUtils.getInputConfig(new File(configFile));
                inputCof.setLinkedData(args[1]);
                queGG.csvToTurtle(inputCof);
            }

        } catch (Exception e) {
            System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
            System.err.printf("Usage: <%s> <input directory> <output directory>%n", Arrays.toString(Language.values()));
        }

    }

    private Boolean csvToTurtle(InputCofiguration inputCofiguration) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getInputDir();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        TutleConverter tutleConverter = null;
        FileFolderUtils.deleteFiles(inputDir, ".ttl");

        try {
            if (language.equals(Language.DE)) {
                tutleConverter = new GermanTurtle(inputDir, linkedData, language);
                return tutleConverter.getConversionFlag();
            } else if (language.equals(Language.EN)) {
                tutleConverter = new EnglishTurtle(inputDir, linkedData, language);
                return tutleConverter.getConversionFlag();
            } else if (language.equals(Language.ES)) {
                tutleConverter = new SpanishTurtle(inputDir, linkedData, language);
                return tutleConverter.getConversionFlag();
            } else if (language.equals(Language.IT)) {
                tutleConverter = new ItalianTurtle(inputDir, linkedData, language);
                return tutleConverter.getConversionFlag();
            }
        } catch (Exception ex) {
            System.out.println("ignore for time being!!!" + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

}
