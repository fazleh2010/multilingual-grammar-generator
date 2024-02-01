
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.monnetproject.lemon.LemonModel;
import evaluation.EvaluateAgainstQALD;
import static evaluation.EvaluateAgainstQALD.REAL_QUESTION;
import grammar.generator.GrammarRuleGeneratorRoot;
import grammar.read.questions.ProtoToRealQuesrion;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.Language;
import lexicon.LexiconImporter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.jena.sys.JenaSystem;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import static java.util.Objects.isNull;
import util.io.CsvFile;
import util.io.FileProcessUtils;
import turtle.GermanTurtle;
import java.io.File;
import java.io.IOException;
import grammar.sparql.SPARQLRequest;
import java.util.logging.Level;
import turtle.EnglishTurtle;
import linkeddata.LinkedData;
import turtle.ItalianTurtle;
import turtle.SpanishTurtle;
import turtle.TutleConverter;
import util.io.FileFolderUtils;
import util.io.InputCofiguration;
import grammar.read.questions.PrettyGrammar;

@NoArgsConstructor
public class QueGG {

    private static final Logger LOG = LogManager.getLogger(QueGG.class);
    private static String questionsFile = "questions";
    private static String summaryFile = "summary";
    public static String propertyReport = "propertyReport";

    private static Boolean externalEntittyListflag = false;
    private static String grammar_FULL_DATASET = "grammar_FULL_DATASET";
    private static String grammar_COMBINATIONS = "grammar_COMBINATIONS";
    private static Boolean online = false;
    private static Boolean genericFlag = false;




    /*public static void main(String[] args) throws Exception {
        JenaSystem.init();
        QueGG queGG = new QueGG();
        List<String[]> languageDBs = new ArrayList<String[]>();
        //languageDBs.add(new String[]{"inputConf_en.json", "dataset/dbpedia_en.json"});
        //languageDBs.add(new String[]{"inputConf_de.json", "dataset/dbpedia_de.json"});
        languageDBs.add(new String[]{"inputConf_it.json", "dataset/dbpedia_it.json"});
        //languageDBs.add(new String[]{"inputConf_es.json", "dataset/dbpedia_es.json"});

        for (String[] languageDB : languageDBs) {
           runGrammarGeneration(languageDB); 
           System.out.println("completed gramar generation completed!!!!"+languageDB[0]);
        }

    }*/
    
     public static void main(String[] args) throws Exception {
        JenaSystem.init();
        QueGG queGG = new QueGG();
        String configFile = null, dataSetConfFile = null;   
        
         Properties batch = new Properties();
         //String[] args=new String[]{"inputConf_en.json","dataset/dbpedia_en.json"};
       
        try {
            if (args.length < 2) {
                 System.err.printf("Too few parameters (%s/%s)", args.length);
                throw new IllegalArgumentException(String.format("Too few parameters (%s/%s)", args.length));
            } else if (args.length == 2) {
                configFile = args[0];
                dataSetConfFile= args[1];
                //configFile ="inputConf_en.json";
                //dataSetConfFile="dataset/dbpedia_en.json";
                InputCofiguration inputCof = FileProcessUtils.getInputConfig(new File(configFile));
                inputCof.setLinkedData(dataSetConfFile);                
                online=inputCof.getOnline();
                externalEntittyListflag=inputCof.getExternalEntittyList();
          
                if (inputCof.isCsvToTurtle()) {
                    if (queGG.csvToProto(inputCof)) {
                        queGG.turtleToProto(inputCof);
                        System.out.println("successfully converted csv files to turtle file!!");
                    }
                }
                if (inputCof.getTurtleToProtoType()) {
                    queGG.turtleToProto(inputCof);
                    System.out.println("successfully converted csv files to turtle file!!");
  
                }
                if (inputCof.getProtoTypeToQuestion()) {
                    String outputFileName =grammar_FULL_DATASET+"_"+inputCof.getLanguage()+".json";
                    File file = new File(inputCof.getOutputDir() +"/"+ outputFileName);
                    List<File> protoSimpleQFiles = new ArrayList<File>();
                    protoSimpleQFiles.add(file);
                    outputFileName ="FINAL_"+grammar_FULL_DATASET+"_"+inputCof.getLanguage()+".json";
                    PrettyGrammar prepareGrammarJson = 
                            new PrettyGrammar(genericFlag,inputCof.getOutputDir(),protoSimpleQFiles, outputFileName);
                }
                /*if (inputCofiguration.isProtoTypeToQuestion()) {  
                    queGG.protoToReal(inputCofiguration, grammar_FULL_DATASET, grammar_COMBINATIONS);
                    System.out.println("successfully converted turtle files to grammar file (.json)!!");
                }*/
                if (inputCof.isEvalution()) {
                    Language language = inputCof.getLanguage();
                    String qaldDir = inputCof.getQaldDir();
                    String outputDir = inputCof.getOutputDir();
                    LinkedData linkedData = inputCof.getLinkedData();
                    Double similarity = inputCof.getSimilarityThresold();
                    queGG.evalution(qaldDir, outputDir, inputCof,language, linkedData.getEndpoint(), EvaluateAgainstQALD.REAL_QUESTION, similarity);
                }

            }

        } catch (IllegalArgumentException | IOException e) {
            System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
            System.err.printf("Usage: <%s> <input directory> <output directory>%n", Arrays.toString(Language.values()));
        }

    }

    public void evalution(String qaldDir, String outputDir, InputCofiguration inputCofiguration,Language language, String endpoint, String questionType, Double similarityMeasure) {
        ObjectMapper objectMapper = new ObjectMapper();
        String queGGJson = null, queGGJsonCombined = null, qaldFile = null, qaldModifiedFile = null;
        String languageCode = language.name().toLowerCase();
        String resultFileName = outputDir + File.separator + "QALD-QueGG-Comparison_" + languageCode + ".csv";
        String qaldRaw = outputDir + File.separator + "QALD-2017-dataset-raw.csv";
        EvaluateAgainstQALD evaluateAgainstQALD = new EvaluateAgainstQALD(languageCode, endpoint);

        for (String fileName : new File(qaldDir).list()) {
            if (fileName.contains("qald")) {
                if (fileName.contains("train-multilingual_modified.json")) {
                    qaldModifiedFile = qaldDir + File.separator + fileName;
                } else if (fileName.contains("train-multilingual.json")) {
                    qaldFile = qaldDir + File.separator + fileName;
                }
            }
            else if(fileName.contains("lcquad")){
                qaldFile=qaldModifiedFile = qaldDir + File.separator + fileName;
            }
            
        }
        

        //temporary code for qald entity creation...
        //System.out.println(entityLabelDir+File.separator+"qaldEntities.txt");
        //FileUtils.stringToFile(string, entityLabelDir+File.separator+"qaldEntities.txt");
        
        /*if (questionType.contains(PROTOTYPE_QUESTION)) {
            EvaluateAgainstQALD_1 evaluateAgainstQALD_1 = new EvaluateAgainstQALD_1(languageCode, endpoint);
            for (String fileName : new File(outputDir).list()) {
                if (fileName.contains("grammar_FULL_DATASET") && fileName.contains(language.name())) {
                    queGGJson = outputDir + File.separator + fileName;
                } else if (fileName.contains("grammar_COMBINATIONS") && fileName.contains(language.name())) {
                    queGGJsonCombined = outputDir + File.separator + fileName;
                }

            }
            File grammarEntriesFile = new File(queGGJson);
            File grammarEntriesFile2 = new File(queGGJsonCombined);
            GrammarWrapper grammarWrapper = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class);
            GrammarWrapper gw2 = objectMapper.readValue(grammarEntriesFile2, GrammarWrapper.class);
            grammarWrapper.merge(gw2);
        
            evaluateAgainstQALD_1.evaluateAndOutput(grammarWrapper, qaldFile, qaldModifiedFile, resultFileName, qaldRaw, languageCode, questionType, similarityMeasure);

        } else*/ 
        if (questionType.contains(REAL_QUESTION)) {
            Map<String, String[]> queGGQuestions = new HashMap<String, String[]>();
            List<String[]> rows = new ArrayList<String[]>();
            String questionDir=inputCofiguration.getQuestionDir();
            String[] files = new File(questionDir).list();
            for (String fileName : files) {
                if (fileName.contains(questionsFile) && fileName.contains(".csv")) {
                    File file = new File(questionDir + File.separator + fileName);
                    CsvFile csvFile = new CsvFile(file);
                    rows = csvFile.getRows(file);
                    for (String[] row : rows) {
                        String question = row[1];
                        String cleanQuestion = question.toLowerCase().trim().strip().stripLeading().stripTrailing();
                        queGGQuestions.put(cleanQuestion, row);
                    }
                }
            }
           
            try {
                evaluateAgainstQALD.evaluateAndOutput(queGGQuestions, qaldFile, qaldModifiedFile, resultFileName, qaldRaw, languageCode, questionType, similarityMeasure);
            } catch (Exception ex) {
                System.out.println("error in reading gold standard::"+ex.getMessage());
                java.util.logging.Logger.getLogger(QueGG.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private Boolean csvToProto(InputCofiguration inputCofiguration) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getInputDir();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        setDataSet(linkedData);
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
            System.out.println("ignore for time being!!!"+ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    private void turtleToProto(InputCofiguration inputCofiguration) throws IOException {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getInputDir();
        String outputDir = inputCofiguration.getOutputDir();
        this.init(language, inputDir, outputDir);
    }

    private void protoToReal(InputCofiguration inputCofiguration, String grammar_FULL_DATASET, String grammar_COMBINATIONS) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getOutputDir();
        Boolean combinedFlag=inputCofiguration.getCompositeFlag();
        Boolean singleFlag=inputCofiguration.getSingleFlag();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        List<File> protoToQuestions=new ArrayList<File>();
        setDataSet(linkedData);

        if (singleFlag) {
            protoToQuestions.addAll(FileProcessUtils.getFiles(inputDir + "/", grammar_FULL_DATASET + "_" + language.name(), ".json"));
        }
        if (combinedFlag) {
            protoToQuestions.addAll(FileProcessUtils.getFiles(inputDir + "/", grammar_COMBINATIONS + "_" + language.name(), ".json"));
        }

        String langCode = language.name().toLowerCase().trim();
        //String questionAnswerFile = questionDir + File.separator + questionsFile + "_" + langCode + ".csv";
        //String questionSummaryFile = questionDir + File.separator + summaryFile + "_" + langCode + ".csv";
        ProtoToRealQuesrion readAndWriteQuestions = new ProtoToRealQuesrion(linkedData,inputCofiguration);
        if(online)
         readAndWriteQuestions.onlineQaGeneration(protoToQuestions);
        else
         readAndWriteQuestions.offline(protoToQuestions);

    }

    private void questionEvaluation(InputCofiguration inputCofiguration) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String qaldDir = inputCofiguration.getQaldDir();
        String outputDir = inputCofiguration.getOutputDir();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        Double similarityMeasure = inputCofiguration.getSimilarityThresold();
        Boolean combinedFlag=inputCofiguration.getCompositeFlag();
        evalution(qaldDir, outputDir, inputCofiguration,language, linkedData.getEndpoint(), EvaluateAgainstQALD.REAL_QUESTION, similarityMeasure);

    }

    public void init(Language language, String inputDir, String outputDir) throws IOException {
        try {
            loadInputAndGenerate(language, inputDir, outputDir);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            LOG.error("Could not create grammar: {}", e.getMessage());
        }
    }

    private void loadInputAndGenerate(Language lang, String inputDir, String outputDir) throws
            IOException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        LexiconImporter lexiconImporter = new LexiconImporter();
        LemonModel lemonModel = lexiconImporter.loadModelFromDir(inputDir, lang.toString().toLowerCase());
        printInputSummary(lemonModel);
        generateByFrameType(lang, lemonModel, outputDir);
    }

    private void printInputSummary(LemonModel lemonModel) {
        lemonModel
                .getLexica()
                .forEach(
                        lexicon
                        -> {
                    LOG.info("The input lexicon contains the following grammar frames:");
                    Arrays.stream(FrameType.values()).forEach(
                            frameType -> {
                                LOG.info(
                                        "{}: {}",
                                        frameType.getName(),
                                        // count of elements that have that frame
                                        lexicon.getEntrys()
                                                .stream()
                                                .filter(lexicalEntry
                                                        -> lexicalEntry.getSynBehaviors()
                                                        .stream()
                                                        .anyMatch(frame
                                                                -> frame.getTypes()
                                                                .stream()
                                                                .anyMatch(
                                                                        uri -> uri.getFragment().equals(frameType.getName())
                                                                )
                                                        )
                                                )
                                                .count()
                                );
                            });
                }
                );
    }

    private void generateByFrameType(Language language, LemonModel lemonModel, String outputDir) throws
            IOException,
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        GrammarWrapper grammarWrapper = new GrammarWrapper();
        for (FrameType frameType : FrameType.values()) {
            if (!isNull(frameType.getImplementingClass())) {
                GrammarWrapper gw = generateGrammarGeneric(
                        lemonModel,
                        (GrammarRuleGeneratorRoot) frameType.getImplementingClass()
                                .getDeclaredConstructor(Language.class)
                                .newInstance(language)
                );
                grammarWrapper.merge(gw);
            }
        }
        PrettyGrammar.prettyGrammarFuntion(grammarWrapper,language,outputDir);
        PrettyGrammar.outputForParser(grammarWrapper,language,outputDir);
    }


    
   
    private GrammarWrapper generateGrammarGeneric(LemonModel lemonModel, GrammarRuleGeneratorRoot grammarRuleGenerator) {
        GrammarWrapper grammarWrapper = new GrammarWrapper();
        lemonModel.getLexica().forEach(lexicon -> {
            LOG.info("Start generation for FrameType {}", grammarRuleGenerator.getFrameType().getName());
            grammarRuleGenerator.setLexicon(lexicon);
            grammarWrapper.setGrammarEntries(grammarRuleGenerator.generate(lexicon));
        });
        return grammarWrapper;
    }

    private static void setDataSet(LinkedData linkedData) throws Exception {
        String endpoint = linkedData.getEndpoint();
        if (linkedData.getEndpoint().contains("dbpedia")) {
            SPARQLRequest.setEndpoint(endpoint);
        }
        GrammarRuleGeneratorRoot.setEndpoint(endpoint);

    }

   

   

}
