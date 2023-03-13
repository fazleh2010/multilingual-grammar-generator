
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;
import eu.monnetproject.lemon.LemonModel;
import evaluation.EvaluateAgainstQALD;
import static evaluation.EvaluateAgainstQALD.REAL_QUESTION;
import evaluation.QALD;
import evaluation.QALDImporter;
import grammar.generator.BindingResolver;
import grammar.generator.GrammarRuleGeneratorRoot;
import grammar.generator.GrammarRuleGeneratorRootImpl;
import grammar.read.questions.OffLineQuestionGeneration;
import grammar.read.questions.ProtoToRealQuesrion;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.Language;
import lexicon.LexiconImporter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.jena.sys.JenaSystem;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;
import util.io.CsvFile;
import util.io.FileProcessUtils;
import turtle.GermanTurtle;
import java.io.File;
import java.io.IOException;
import grammar.sparql.SparqlQuery;
import grammar.sparql.SPARQLRequest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import static java.lang.System.exit;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.apache.commons.text.similarity.CosineDistance;
import turtle.EnglishTurtle;
import util.io.GenderUtils;
import linkeddata.LinkedData;
import turtle.ItalianTurtle;
import turtle.SpanishTurtle;
import static util.io.ResourceHelper.loadResource;
import turtle.TutleConverter;
import util.io.BashScript;
import util.io.FileFolderUtils;
import util.io.InputCofiguration;

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



    public static void main(String[] args1) throws Exception {
        JenaSystem.init();
        QueGG queGG = new QueGG();
        String configFile = null, dataSetConfFile = null;  
        //this is for test..pull it bak when test is over.
        String[] args=new String[]{"conf/inputConf_lexicon_1_en.json","dataset/dbpedia_en.json"};
         Properties batch = new Properties();

       
        try {
            if (args.length < 2) {
                 System.err.printf("Too few parameters (%s/%s)", args.length);
                throw new IllegalArgumentException(String.format("Too few parameters (%s/%s)", args.length));
            } else if (args.length == 2) {
                configFile = args[0];
                InputCofiguration inputCofiguration = FileProcessUtils.getInputConfig(new File(configFile));
                inputCofiguration.setLinkedData(args[1]);                
                online=inputCofiguration.getOnline();
                externalEntittyListflag=inputCofiguration.getExternalEntittyList();
          
                if (inputCofiguration.isCsvToTurtle()) {
                    queGG.csvToTurle(inputCofiguration) ;
                    System.out.println("csvToTurle generation is successfull!!!");
                    
                }
                if (inputCofiguration.getTurtleToProtoType()) {
                    queGG.turtleToProto(inputCofiguration);

                }
                if (inputCofiguration.isProtoTypeToQuestion()) {
                    queGG.protoToReal(inputCofiguration, grammar_FULL_DATASET, grammar_COMBINATIONS);
                    writeQuestionInSingleFile(inputCofiguration);
                }
                
                 if (inputCofiguration.isEvalution()) {
                    Language language = inputCofiguration.getLanguage();
                    String qaldDir = inputCofiguration.getQaldDir();
                    String outputDir = inputCofiguration.getOutputDir();
                    LinkedData linkedData = inputCofiguration.getLinkedData();
                    Double similarity = inputCofiguration.getSimilarityThresold();
                    BashScript BashScript=new BashScript(null);
                }
                 
                /*if (inputCofiguration.isEvalution()) {
                    Language language = inputCofiguration.getLanguage();
                    String qaldDir = inputCofiguration.getQaldDir();
                    String outputDir = inputCofiguration.getOutputDir();
                    LinkedData linkedData = inputCofiguration.getLinkedData();
                    Double similarity = inputCofiguration.getSimilarityThresold();
                    queGG.evalution(qaldDir, outputDir, inputCofiguration,language, linkedData.getEndpoint(), EvaluateAgainstQALD.REAL_QUESTION, similarity);
                }*/
               
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

    private Boolean csvToTurle(InputCofiguration inputCofiguration) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getInputDir();
        String outputDir = inputCofiguration.getOutputDir();
        String domainAndRangeDir=inputCofiguration.getDomainAndRangeDir();
        Integer numberOfEntitiesString = inputCofiguration.getNumberOfEntities();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        setDataSet(linkedData);
        TutleConverter tutleConverter = null;
        FileFolderUtils.deleteFiles(inputDir, ".ttl");

        //try {
            if (language.equals(Language.DE)) {
                tutleConverter = new GermanTurtle(inputDir,domainAndRangeDir, linkedData, language);
                return tutleConverter.getConversionFlag();
            } else if (language.equals(Language.EN)) {
                tutleConverter = new EnglishTurtle(inputDir, domainAndRangeDir,linkedData, language);
                return tutleConverter.getConversionFlag();
            } else if (language.equals(Language.ES)) {
                tutleConverter = new SpanishTurtle(inputDir, domainAndRangeDir,linkedData, language);
                return tutleConverter.getConversionFlag();
            } else if (language.equals(Language.IT)) {
                tutleConverter = new ItalianTurtle(inputDir, domainAndRangeDir,linkedData, language);
                return tutleConverter.getConversionFlag();
            }
        /*} catch (Exception ex) {
            System.out.println("failed to geenrate turtle file from spreed sheet!!!\n"+ex.getMessage());
            return false;
        }*/
        return false;
    }

    private void turtleToProto(InputCofiguration inputCofiguration) throws IOException {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getInputDir();
        String outputDir = inputCofiguration.getOutputDir();
        FileFolderUtils.deleteFilesE(outputDir, ".json");
        FileFolderUtils.deleteFiles(inputCofiguration.getQuestionDir());
        try {
            loadInputAndGenerate(language, inputDir, outputDir);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            LOG.error("Could not create grammar: {}", e.getMessage());
        }
    }

    private void protoToReal(InputCofiguration inputCofiguration, String grammar_FULL_DATASET, String grammar_COMBINATIONS) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getOutputDir();
        Boolean combinedFlag=inputCofiguration.getCompositeFlag();
        Boolean singleFlag=inputCofiguration.getSingleFlag();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        List<File> protoToQuestions=new ArrayList<File>();
        setDataSet(linkedData);
        
        FileFolderUtils.deleteFiles(inputCofiguration.getQuestionDir());

        if (singleFlag) {
            protoToQuestions.addAll(FileProcessUtils.getFiles(inputDir + "/", grammar_FULL_DATASET + "_" + language.name(), ".json"));
        }
        if (combinedFlag) {
            protoToQuestions.addAll(FileProcessUtils.getFiles(inputDir + "/", grammar_COMBINATIONS + "_" + language.name(), ".json"));
        }

        String langCode = language.name().toLowerCase().trim();
        //String questionAnswerFile = questionDir + File.separator + questionsFile + "_" + langCode + ".csv";
        //String questionSummaryFile = questionDir + File.separator + summaryFile + "_" + langCode + ".csv";
       
        if (online) {
            ProtoToRealQuesrion readAndWriteQuestions = new ProtoToRealQuesrion(linkedData, inputCofiguration);
            readAndWriteQuestions.onlineQaGeneration(protoToQuestions);

        } else{
            OffLineQuestionGeneration offLineQuestionGeneration=new OffLineQuestionGeneration(linkedData,inputCofiguration);
            offLineQuestionGeneration.offline(protoToQuestions);
        }
           

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

  
    private void loadInputAndGenerate(Language lang, String inputDir, String outputDir) throws
            IOException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        
        
       LexiconImporter lexiconImporter = new LexiconImporter();
        Stream<Path> paths = Files.walk(Paths.get(inputDir));
        List<Path> list = filterFiles(paths);
        Map<Integer, List<Path>> parameterFiles=splitFiles(list,1000);
        for (Integer index : parameterFiles.keySet()) {
            List<Path> listT= parameterFiles.get(index);
            if(listT.isEmpty())
               continue; 
            LemonModel lemonModel = lexiconImporter.loadModelFromDir(inputDir, lang.toString().toLowerCase(), listT);
            printInputSummary(lemonModel);
            generateByFrameType(lang, lemonModel, outputDir,index);
            //test codes..
           /*if(index>=4)
                break;*/
        }

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

    private void generateByFrameType(Language language, LemonModel lemonModel, String outputDir,Integer parameter) throws
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
        // Make a GrammarRuleGeneratorRoot instance to use the combination function
        GrammarRuleGeneratorRoot generatorRoot = new GrammarRuleGeneratorRootImpl(language);
        //LOG.info("Start generation of combined entries");
        //grammarWrapper.getGrammarEntries().addAll(generatorRoot.generateCombinations(grammarWrapper.getGrammarEntries()));

        for (GrammarEntry grammarEntry : grammarWrapper.getGrammarEntries()) {
            grammarEntry.setId(String.valueOf(grammarWrapper.getGrammarEntries().indexOf(grammarEntry) + 1));
        }

        // Output file is too big, make two files
        GrammarWrapper regularEntries = new GrammarWrapper();
        regularEntries.setGrammarEntries(
                grammarWrapper.getGrammarEntries()
                        .stream()
                        .filter(grammarEntry -> !grammarEntry.isCombination())
                        .collect(Collectors.toList())
        );
        
        //combination and binding is temporarly closed
        //GrammarWrapper combinedEntries = new GrammarWrapper();
        
        
        /*combinedEntries.setGrammarEntries(
                grammarWrapper.getGrammarEntries().stream().filter(GrammarEntry::isCombination).collect(Collectors.toList())
        );*/

        // Generate bindings
        //LOG.info("Start generation of bindings");
        //grammarWrapper.getGrammarEntries().forEach(generatorRoot::generateBindings);

        generatorRoot.dumpToJSON(
                Path.of(
                        outputDir,
                        "grammar_" + generatorRoot.getFrameType().getName() + "_" + language +"_"+parameter+ ".json"
                ).toString(),
                regularEntries
        );
        /*generatorRoot.dumpToJSON(
                Path.of(outputDir, "grammar_COMBINATIONS" + "_" + language +"_"+ ".json").toString(),
                combinedEntries
        );*/

        // Insert those bindings and write new files
        /*LOG.info("Start resolving bindings");
        BindingResolver bindingResolver = new BindingResolver(grammarWrapper.getGrammarEntries());
        grammarWrapper = bindingResolver.resolve();
        generatorRoot.dumpToJSON(
                Path.of(outputDir, "grammar_FULL_WITH_BINDINGS_" + language +"_"+ ".json").toString(),
                grammarWrapper
        );*/
        

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
   
    private List<Path> filterFiles(Stream<Path> paths) {
        return paths
                .filter(Files::isRegularFile)
                .filter(f -> f.getFileName().toString().endsWith("ttl"))
                .collect(Collectors.toList());
    }

    /*private Map<String, List<Path>> findParameterFiles(Stream<Path> paths) {
         List<Path> lexFile= paths
                .filter(Files::isRegularFile)
                .filter(f -> f.getFileName().toString().endsWith("csv"))
                .collect(Collectors.toList());
         
         CsvFile csvFile=new CsvFile();
         for(){
         csvFile.getRows(qaldFile)
         
    }*/
    
    private Map<Integer, List<Path>> splitFiles(List<Path> list,Integer limit) {
        Integer index = 0;
        Map<Integer, List<Path>> seperatedFiles = new TreeMap<Integer, List<Path>>();
        List<Path> pathT = new ArrayList<Path>();
        List<Path> restT = new ArrayList<Path>();
        Integer numberOfLimit=1;
        for (Path path : list) {
            index = index + 1;
            pathT.add(path);
            if (index >= limit) {
                seperatedFiles.put(numberOfLimit, pathT);
                pathT = new ArrayList<Path>();
                restT = new ArrayList<Path>();
                numberOfLimit=numberOfLimit+1;
                index=0;                
            }
            else{
                restT.add(path);
                
            }
        }
        seperatedFiles.put(numberOfLimit, pathT);
       
        return seperatedFiles;
    }
    
    private static void writeQuestionInSingleFile(InputCofiguration inputCofiguration) throws IOException, FileNotFoundException, CsvException {
        CsvFile outputCsvFile = new CsvFile();
        String questionDir = inputCofiguration.getQuestionDir();
        String parameter = inputCofiguration.getParameter();
        String outputDir = inputCofiguration.getOutputDir() +File.separator;
        String[] questionFiles = new File(inputCofiguration.getQuestionDir()).list();
        List<String[]> csvData = new ArrayList<String[]>();
        for (String fileName : questionFiles) {
            if (fileName.contains("questions")) {
                CsvFile csvFile = new CsvFile();
                List<String[]> rows = csvFile.getRows(new File(questionDir + fileName));
                if (rows.size() > 1) {
                    System.out.println(fileName + "  elemetns size:::" + rows.size());
                    csvData.addAll(rows);
                }
            }
        }
        File outputFile = new File(outputDir + parameter + ".csv");
        outputCsvFile.writeToCSV(outputFile, csvData);
    }

}
