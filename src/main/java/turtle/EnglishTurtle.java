/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

import static grammar.datasets.sentencetemplates.TempConstants.AdjectiveAttributiveFrame;
import static grammar.datasets.sentencetemplates.TempConstants.IntransitivePPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.NounPPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.TransitiveFrame;
import grammar.structure.component.Language;
import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.stream.Stream;
import util.io.CsvFile;
import util.io.FileProcessUtils;
import linkeddata.LinkedData;
import util.io.DomainRangeDictionary;
import util.io.GenderUtils;
import static util.io.GenderUtils.nounWrittenForms;
import util.io.Property;
import util.io.Tupples;

/**
 *
 * @author elahi
 */
public class EnglishTurtle extends TurtleCreation implements TutleConverter {

    private String lemonEntry = "";
    private String partOfSpeech = "";
    private String writtenForm_plural = "";
    private String writtenFormInfinitive = "";
    private String writtenForm3rdPerson = "";
    private String writtenFormPast = "";
    private String writtenFormPerfect = "";
    private String preposition = "";
    private static Integer index = 0;
    private Map<String,List<String>>domainOrRange=new TreeMap<String,List<String>>();

    private EnglishCsv.NounPPFrameCsv nounPPFrameCsv = new EnglishCsv.NounPPFrameCsv();
    private EnglishCsv.TransitFrameCsv transitiveFrameCsv = new EnglishCsv.TransitFrameCsv();
    private EnglishCsv.InTransitFrame IntransitiveFrameCsv = new EnglishCsv.InTransitFrame();
    private EnglishCsv.AttributiveAdjectiveFrame attributiveAdjectiveFrame = new EnglishCsv.AttributiveAdjectiveFrame();
    private EnglishCsv.GradbleAdjectiveFrameCsv gradableAdjectiveFrameCsv = new EnglishCsv.GradbleAdjectiveFrameCsv();

    public EnglishTurtle(String inputDir, LinkedData linkedData, Language language) throws Exception {
        super(inputDir, linkedData, language);
        super.setSyntacticFrameIndexes(nounPPFrameCsv.getSyntacticFrameIndex(), transitiveFrameCsv.getSyntacticFrameIndex(), IntransitiveFrameCsv.getSyntacticFrameIndex(), attributiveAdjectiveFrame.getSyntacticFrameIndex(), gradableAdjectiveFrameCsv.getSyntacticFrameIndex());
        this.generateTurtle();
    }

    private void generateTurtle() throws IOException, Exception {
        String lemonEntry = null;
        File f = new File(inputDir);
        Boolean flag = false;
        String[] pathnames = f.list();
        DomainRangeDictionary domainRangeDictionary=new DomainRangeDictionary(inputDir, pathnames);
        domainOrRange=domainRangeDictionary.getDomainOrRange();
       
        for (String pathname : pathnames) {
            if(pathname.contains(".csv#")){
                continue;
            }
            System.out.println(pathname);
            String[] files = new File(inputDir + File.separatorChar + pathname).list();
            for (String fileName : files) {
                if(fileName.contains("DomainOrRange.csv")){
                    continue;
                }
                
                if (!fileName.contains(".csv")) {
                    continue;
                }

                CsvFile csvFile = new CsvFile();
                String directory = inputDir + "/" + pathname + "/";
                List<String[]> rows = csvFile.getRowsManual(new File(directory + fileName));
                Integer index = 0;
                Map<String, List<String[]>> keyRows = new HashMap<String, List<String[]>>();
                for (String[] row : rows) {
                    
                    if (index == 0) {
                        index = index + 1;
                        continue;
                    }
                    if(row.length<2){
                       throw new Exception("the format of CSV file is wrong!!!!");
                    }
                    String key = row[0];

                    List<String[]> values = new ArrayList<String[]>();
                    if (keyRows.containsKey(key)) {
                        values = keyRows.get(key);
                    }
                    values.add(row);
                    keyRows.put(key, values);
                    index = index + 1;
                }
                try {
                    Integer count = 0;
                    for (String key : keyRows.keySet()) {
                        count = count + 1;
                        List<String[]> values = keyRows.get(key);
                        setSyntacticFrame(key, values);
                        FileProcessUtils.stringToFile(this.turtleString, directory + this.tutleFileName);
                        if (this.turtleString != null) {
                            this.conversionFlag = true;
                        }

                    }

                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(EnglishTurtle.class.getName()).log(Level.SEVERE, null, ex);
                    throw new Exception(ex.getMessage());
                }

            }

        }

    }

    private void setSyntacticFrame(String key, List<String[]> rows) throws Exception {
        String syntacticFrame = findSyntacticFrame(rows);
        key=key.trim().strip().stripLeading().stripTrailing();
        if (syntacticFrame.equals(NounPPFrame)) {
            setNounPPFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(TransitiveFrame)) {
            setTransitiveFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(IntransitivePPFrame)) {
            setIntransitivePPFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(AdjectiveAttributiveFrame)) {
            setAdjectiveFrame(key, rows, syntacticFrame);
        }else if (syntacticFrame.equals(AdjectiveSuperlativeFrame)) {
            setAdjectiveGradableFrame(key, rows, syntacticFrame);
        }
        else {
            System.out.println("no syntactic frame is found!!");            
        }

    }

    @Override
    public void setNounPPFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);

        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;String copulativeArg=null;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = nounPPFrameCsv.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive = nounPPFrameCsv.getWrittenFormInfinitive(row);
                this.writtenForm_plural = nounPPFrameCsv.getWrittenFormPluralIndex(row);
                this.preposition = nounPPFrameCsv.getPrepositionIndex(row);
            }
            copulativeArg=nounPPFrameCsv.getCopulativeArgIndex(row);
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    setReference(nounPPFrameCsv.getReferenceIndex(row)),
                    setReference(nounPPFrameCsv.getDomainIndex(row)),
                    setReference(nounPPFrameCsv.getRangeIndex(row)));
            tupplesList.add(tupple);
            index = index + 1;
             nounPPFrameCsv.setArticle(domainOrRange);
        }
        this.turtleString
                = nounPPFrameCsv.getNounPPFrameHeader(this.lemonEntry, this.preposition, this.language)
                + nounPPFrameCsv.getIndexing(this.lemonEntry, tupplesList)
                + nounPPFrameCsv.getWrittenTtl(this.lemonEntry, this.writtenFormInfinitive, this.writtenFormInfinitive,this.writtenForm_plural,this.language,copulativeArg)
                + nounPPFrameCsv.getSenseDetail(tupplesList, NounPPFrame, this.lemonEntry, this.writtenFormInfinitive, this.preposition, this.language)
                + nounPPFrameCsv.getPreposition(this.lemonEntry,this.preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);
         
    }

    @Override
    public void setTransitiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);
        List<Tupples> tupples = new ArrayList<Tupples>();
         String subject=null;
          //System.out.println(GenderUtils.nounWrittenForms );
          //exit(1);
         
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = transitiveFrameCsv.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive = transitiveFrameCsv.getWrittenFormInfinitive(row);
                this.writtenForm3rdPerson = transitiveFrameCsv.getWrittenForm3rdPerson(row);
                this.writtenFormPast = transitiveFrameCsv.getWrittenFormPast(row);
                this.writtenFormPerfect = transitiveFrameCsv.getWrittenFormPerfect(row);
                this.preposition = transitiveFrameCsv.getPassivePrepositionIndex(row);
            }
            
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(transitiveFrameCsv.getReferenceIndex(row)),
                    this.setReference(transitiveFrameCsv.getDomainIndex(row)),
                    this.setReference(transitiveFrameCsv.getRangeIndex(row)));

            transitiveFrameCsv.setArticle(domainOrRange);
            transitiveFrameCsv.setVerbInfo(partOfSpeech,  writtenFormInfinitive,  writtenForm3rdPerson,  writtenFormPast,writtenFormPerfect);
            tupples.add(tupple);
            index = index + 1;
            subject=  transitiveFrameCsv.getSubjectIndex(row);

        }
        this.turtleString
                = transitiveFrameCsv.getHeader(this.lemonEntry, this.preposition, this.language)
                + transitiveFrameCsv.getSenseIndexing(tupples, lemonEntry)
                + transitiveFrameCsv.getWritten(this.lemonEntry, this.partOfSpeech,this.writtenFormInfinitive, this.writtenForm3rdPerson, this.writtenFormPast, this.writtenFormPerfect,this.language,subject)
                + transitiveFrameCsv.getSenseDetail(tupples, syntacticFrame, this.lemonEntry, this.writtenFormInfinitive, this.preposition, this.language)
                + transitiveFrameCsv.getPrepostion(this.lemonEntry,this.preposition, this.language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setIntransitivePPFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;String   subject= null;
        for (String[] row : rows) {
            if (index == 0) {
                this.setLemonEntryId(IntransitiveFrameCsv.getLemonEntryIndex(row));
                this.partOfSpeech = IntransitiveFrameCsv.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive = IntransitiveFrameCsv.getWrittenFormInfinitive(row);
                this.writtenForm3rdPerson = IntransitiveFrameCsv.getWrittenForm3rdPerson(row);
                this.writtenFormPast = IntransitiveFrameCsv.getWrittenFormPast(row);
                this.writtenFormPerfect = IntransitiveFrameCsv.getWrittenFormPerfect(row);
                this.preposition = IntransitiveFrameCsv.getPreposition(row);

            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(IntransitiveFrameCsv.getReferenceIndex(row)),
                    this.setReference(IntransitiveFrameCsv.getDomainIndex(row)),
                    this.setReference(IntransitiveFrameCsv.getRangeIndex(row)));

            IntransitiveFrameCsv.setArticle(tupple, domainOrRange);
            IntransitiveFrameCsv.setVerbInfo(partOfSpeech,  writtenFormInfinitive,  writtenForm3rdPerson,  writtenFormPast, this.writtenFormPerfect);
            tupples.add(tupple);
            subject=  GermanCsv.InTransitFrameCsv.getSubjectIndex(row);
            index = index + 1;
        }
        this.turtleString
                = IntransitiveFrameCsv.getHeader(this.lemonEntry, this.preposition, this.language)
                + IntransitiveFrameCsv.getSenseIndexing(tupples, this.lemonEntry)
                + IntransitiveFrameCsv.getWritten(lemonEntry, writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast,this.writtenFormPerfect, this.language,subject)
                + IntransitiveFrameCsv.getSenseDetail(tupples, syntacticFrame, this.lemonEntry, this.writtenFormInfinitive, this.preposition, this.language)
                + IntransitiveFrameCsv.getPrepostion(this.lemonEntry,this.preposition, this.language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setAdjectiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.setLemonEntryId(attributiveAdjectiveFrame.getLemonEntryIndex(row));
                this.partOfSpeech = attributiveAdjectiveFrame.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive = attributiveAdjectiveFrame.getWrittenFormInfinitive(row);

            }

            tupples.add(new Tupples(this.lemonEntry,
                    index + 1,
                    "",
                    this.setReference(attributiveAdjectiveFrame.getOwl_onPropertyIndex(row)),
                    this.setReference(attributiveAdjectiveFrame.getOwl_hasValueIndex(row))));
            index = index + 1;
        }
        this.turtleString
                = attributiveAdjectiveFrame.getAtrributiveFrameHeader(this.lemonEntry, tupples, this.language)
                + attributiveAdjectiveFrame.getAtrributiveFrameIndexing(tupples, this.lemonEntry)
                + attributiveAdjectiveFrame.getAtrrtibutiveWrittenForm(lemonEntry, writtenFormInfinitive, this.language)
                + attributiveAdjectiveFrame.getSenseDetail(tupples, syntacticFrame, this.lemonEntry, "", "", this.language);
        this.tutleFileName = getFileName("NounPredicateFrame");
    }
    
    
    @Override
    public void setAdjectiveGradableFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);

        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = gradableAdjectiveFrameCsv.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive=gradableAdjectiveFrameCsv.getWrittenFormIndex(row);
                this.writtenForm3rdPerson=gradableAdjectiveFrameCsv.getComparativIndex(row);
                this.writtenFormPast=gradableAdjectiveFrameCsv.getSuperlativeIndex(row);
                this.preposition=gradableAdjectiveFrameCsv.getPrepostion(row);

            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    setReference(gradableAdjectiveFrameCsv.getReferenceIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getDomainIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getRangeIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getOils_boundToIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getOils_degreeIndex(row)));

            tupplesList.add(tupple);
            index = index + 1;
            gradableAdjectiveFrameCsv.setArticle(tupple, domainOrRange);;
        }
        this.turtleString
                = gradableAdjectiveFrameCsv.getHeader(this.lemonEntry, this.language)
                + gradableAdjectiveFrameCsv.getIndexing(this.lemonEntry, tupplesList)
                + gradableAdjectiveFrameCsv.getWrittenTtl(this.lemonEntry, this.writtenFormInfinitive, this.writtenForm3rdPerson,this.writtenFormPast,this.language)
                + gradableAdjectiveFrameCsv.getSenseDetail(lemonEntry, tupplesList, language)
                + gradableAdjectiveFrameCsv.getPrepostion(lemonEntry, this.preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);

        /*for (String[] row : rows) {
            Tupples tupple = new Tupples(lemonEntry,
                    index + 1,
                    setReference(gradableAdjectiveFrameCsv.getOils_boundToIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getDomainIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getRangeIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getOils_boundToIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getOils_degreeIndex(row)));

        }
        this.turtleString = gradableAdjectiveFrameCsv.prepareTurtile(row, this.language);
        this.tutleFileName = getFileName(syntacticFrame);
        exit(1);*/
    }

   


    private String modify(String string) {
        /*string = string.replaceAll("[^a-zA-Z0-9]", " ");
        string = string.toLowerCase().strip().trim().replace(" ", "_");*/
        index = index + 1;
        //return "LexicalEntry_" + string+"_"+index.toString();
        string = string.toLowerCase().strip().trim().replace(" ", "_")+"_"+index;
        return string;
    }

    private void setLemonEntryId(String writtenForm) {
        this.lemonEntry = this.modify(writtenForm);

    }

    private String getFileName(String syntacticFrame) {
        return syntacticFrame + "-lexicon" + "-" + lemonEntry.replace("/", "") + ".ttl";

    }

    

}
