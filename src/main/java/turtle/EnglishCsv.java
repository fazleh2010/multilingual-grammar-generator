/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;


import java.util.*;
import static turtle.EnglishCsv.TempConstants.*;
import util.io.GenderUtils;
import util.io.MatcherExample;
import util.io.Property;
import util.io.Spliter;
import util.io.Tupples;

/**
 *
 * @author elahi
 */
public class EnglishCsv implements TempConstants {

    public static class NounPPFrameCsv {

        public static Integer lemonEntryIndex = 0;
        public static Integer partOfSpeechIndex = 1;
        public static Integer writtenFormInfinitive = 2;
        public static Integer writtenFormPluralIndex = 3;
        public static Integer prepositionIndex = 4;
        public static Integer syntacticFrameIndex = 5;
        public static Integer copulativeArgIndex = 6;
        public static Integer prepositionalAdjunctIndex = 7;
        public static Integer senseIndex = 8;
        public static Integer referenceIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;
        public static Integer domainWrittenSingular = 12;
        public static Integer domainWrittenPlural = 13;
        public static Integer rangeWrittenSingular = 14;
        public static Integer rangeWrittenPlural = 15;
        private static String proeposition_id;
        private static String arg1 = null;
        private static String arg2 = null;


        public NounPPFrameCsv() {

        }

        public String getNounPPFrameHeader(String lemonEntry, String preposition, String language) {
            proeposition_id = lemonEntry + "_form_preposition";
            return "@prefix :        <http://localhost:8080/lexicon#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    + "  lemon:entry    :" + lemonEntry + " ;\n"
                    + "  lemon:entry    :" + proeposition_id + " .\n"
                    + "\n";
        }

        public String getIndexing(String lemonEntry, List<Tupples> senseIds) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a       lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:noun ;\n"
                    + "  lemon:canonicalForm  :" + lemonEntry + "_form ;\n"
                    + "  lemon:otherForm  :" + lemonEntry + "_singular ;\n"
                    + "  lemon:otherForm  :" + lemonEntry + "_plural ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_nounpp .\n"
                    + "\n";
            return senseIdStr;
        }

        public String getWrittenTtl(String lemonEntry, String writtenFormInfinitive, String writtenFormSingular, String writtenFormPlural, String language, String copulativeArg) {

            if (writtenFormPlural.contains("-")) {
                writtenFormPlural = "XX";
            }

            if (copulativeArg.contains("domain")) {
                this.arg1 = "arg2"+"_"+lemonEntry;
                this.arg2 = "arg1"+"_"+lemonEntry;
            } else {
                this.arg1 = "arg1"+"_"+lemonEntry;
                this.arg2 = "arg2"+"_"+lemonEntry;
            }

            String writtenForm = ":" + lemonEntry + "_form a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormInfinitive + "\"@" + language + " .\n"
                    + "\n"
                    + ":" + lemonEntry + "_singular a    lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormSingular + "\"@" + language + " ;\n"
                    + "  lexinfo:number   lexinfo:singular .\n"
                    + "\n"
                    + ":" + lemonEntry + "_plural a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPlural + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:plural .\n"
                    + "\n";

            String copulativeStr = ":" + lemonEntry + "_nounpp" + " a        lexinfo:NounPPFrame ;\n"
                    + "  lexinfo:copulativeArg        :" + arg1 + " ;\n"
                    + "  lexinfo:prepositionalAdjunct :" + arg2 + " .\n"
                    + "\n";

            return writtenForm + copulativeStr;
        }

        /*public void setArticle(Tupples tupple,  String[] row) {
            GenderUtils.setWrittenForms(tupple.getDomain(), getDomainWrittenSingular(row), getDomainWrittenPlural(row));
            GenderUtils.setWrittenForms(tupple.getRange(), getRangeWrittenSingular(row), getRangeWrittenPlural(row));
        }*/

        public void setArticle(Map<String, List<String>> domainOrRange) {
            for (String key : domainOrRange.keySet()) {
                List<String> row = domainOrRange.get(key);
                GenderUtils.setWrittenForms(key, row.get(0), row.get(1));
            }

        }

        public static String getPreposition(String lemonEntry, String preposition, String language) {
            if (preposition.contains("X")) {
                preposition = "";
            }
            return ":"+arg2+" lemon:marker :" + proeposition_id + " .\n"
                    + "## Prepositions ##\n"
                    + ":" + proeposition_id + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + language + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                    + "\n"
                    + "";
        }
        
         public String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
            String str = "";
            if (syntacticFrame.equals(NounPPFrame)) {
                for (Tupples tupple : tupples) {
                    String line = ":" + tupple.getSenseId() + " a lemon:OntoMap, lemon:LexicalSense ;\n"
                            + "  lemon:ontoMapping         :" + tupple.getSenseId() + " ;\n"
                            + "  lemon:reference           <" + tupple.getReference() + "> ;\n"
                            + "  lemon:subjOfProp          :"+this.arg2+" ;\n"
                            + "  lemon:objOfProp           :"+this.arg1+" ;\n"
                            + "  lemon:condition           :" + tupple.getSenseId() + "_condition .\n"
                            + "\n"
                            + ":" + tupple.getSenseId() + "_condition a lemon:condition ;\n"
                            + "  lemon:propertyDomain  <" + tupple.getDomain() + "> ;\n"
                            + "  lemon:propertyRange   <" + tupple.getRange() + "> .\n"
                            + "\n";
                    str += line;
                }
            }

            return str;
        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormInfinitive(String[] row) {
            return row[writtenFormInfinitive];
        }

        public String getWrittenFormPluralIndex(String[] row) {
            return row[writtenFormPluralIndex];
        }

        public String getPrepositionIndex(String[] row) {
            return row[prepositionIndex];
        }

        public String getSyntacticFrameIndex(String[] row) {
            return row[syntacticFrameIndex];
        }

        public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getCopulativeArgIndex(String[] row) {
            return row[copulativeArgIndex];
        }

        public String getPrepositionalAdjunctIndex(String[] row) {
            return row[prepositionalAdjunctIndex];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getDomainIndex(String[] row) {
            return Spliter.splitString(row[domainIndex]);       
        }

        public static String getRangeIndex(String[] row) {
            return Spliter.splitString(row[rangeIndex]);
        }

        /* public String getDomainWrittenSingular(String[] row) {
            return row[domainWrittenSingular];
        }

        public String getDomainWrittenPlural(String[] row) {
            return row[domainWrittenPlural];
        }

        public String getRangeArticleIndex(String[] row) {
            return row[rangeArticleIndex];
        }

        public String getRangeWrittenSingular(String[] row) {
            return row[rangeWrittenSingular];
        }

        public String getRangeWrittenPlural(String[] row) {
            return row[rangeWrittenPlural];
        }*/
       

    }

    public static class TransitFrameCsv {

        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	SyntacticFrame	subject	directObject	sense	reference	domain	range	GrammarRule 1:question1	GrammarRule 1:question2	GrammarRule 1:sparql	GrammarRule 2:question1	GrammarRule2: sparql
        //compose	        verb	compose	composes	composed	TransitiveFrame	range	domain	1	dbo:musicComposer	dbo:Work	dbo:Person	Which dbo:Person (X) composes,composed Y(dbo:Work)?	Who dbo:Person (X) composes,composed Y(dbo:Work)?	SELECT ?X WHERE { Y dbo:musicComposer ?X.}	Which dbo:Work(X) was composed by Y(dbo:Person)?	SELECT ?X WHERE { ?X dbo:Person Y.}
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormInfinitive = 2;
        private Integer writtenForm3rdPerson = 3;
        private Integer writtenFormPast = 4;
        private Integer writtenFormPerfect = 5;
        private Integer syntacticFrameIndex = 6;
        private Integer subjectIndex = 7;
        private Integer directObjectIndex = 8;
        private Integer senseIndex = 9;
        private Integer referenceIndex = 10;
        private Integer domainIndex = 11;
        private Integer rangeIndex = 12;
        private Integer passivePrepositionIndex = 13;
        /*private Integer domainWrittenSingularFormIndex=14;
        private Integer domainWrittenPluralFormIndex=15;
        private Integer rangeWrittenSingularFormIndex=16;
        private Integer rangeWrittenPluralFormIndex=17;*/
        private static String proeposition_id;

        public String getHeader(String lemonEntry, String preposition, String language) {

            proeposition_id = "form_" + lemonEntry + "_preposition";

            return "@prefix :        <http://localhost:8080/lexicon#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    + "  lemon:entry    :" + "to_" + lemonEntry + " ;\n"
                    + "  lemon:entry    :" + lemonEntry + "ed" + " ;\n"
                    + "  lemon:entry    :" + proeposition_id + " .\n"
                    + "\n";
        }

        public String getSenseIndexing(List<Tupples> tupples, String lemonEntry) {
            String senseIdStr = getSenseId(tupples);
            senseIdStr = ":" + "to_" + lemonEntry + " a           lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                    + "  lemon:canonicalForm  :form_" + lemonEntry + " ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "s ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "ed ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "perfect" + " ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_frame_transitive .\n"
                    + "\n";
            return senseIdStr;
        }

        public String getWritten(String lemonEntry, String partOfSpeech, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String writtenFormPerfect, String language, String subject) {
            String subjectLemon = null, objectLemon = null;

            if (subject.contains("domain")) {
                objectLemon = lemonEntry + "_subj";
                subjectLemon = lemonEntry + "_obj";
            } else {
                subjectLemon = lemonEntry + "_subj";
                objectLemon = lemonEntry + "_obj";
            }

            return ":form_" + lemonEntry + " a         lemon:Form ;\n"
                    + "  lemon:writtenRep     \"" + writtenFormInfinitive + "\"@" + language + " ;\n"
                    + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "s a    lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenForm3rdPerson + "\"@" + language + " ;\n"
                    + "  lexinfo:person   lexinfo:secondPerson .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "ed a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPast + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:past .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "perfect" + " a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPerfect + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:perfect ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson .\n"
                    + "\n"
                    + ":" + lemonEntry + "_frame_transitive a lexinfo:TransitiveFrame ;\n"
                    + "  lexinfo:subject          :" + subjectLemon + " ;\n"
                    + "  lexinfo:directObject     :" + objectLemon + " .\n"
                    + "\n";
        }

        public String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
            String str = "";
            str = "";
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a   lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping :" + tupple.getSenseId() + " ;\n"
                        + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp  :" + lemonEntry + "_obj ;\n"
                        + "  lemon:objOfProp   :" + lemonEntry + "_subj ;\n"
                        + "  lemon:condition   :" + tupple.getSenseId() + "_condition .\n"
                        + "\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_condition a    lemon:condition ;\n"
                        + "  lemon:propertyDomain <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange  <" + tupple.getRange() + "> .\n"
                        + "\n";
                str += line;
            }
            String intransitiveStr = "";
            /*lemonEntry = lemonEntry + "ed";
            Integer index=1;
            for (Tupples tupple : tupples) {
                String line = ":" + lemonEntry + " a            lemon:LexicalEntry ;\n"
                        + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                        + "  lemon:canonicalForm  :form_" + lemonEntry + "_canonical ;\n"
                        + "  lemon:otherForm      :form_" + lemonEntry + "_by ;\n"
                        + "  lemon:synBehavior    :" + lemonEntry + "_frame_adjectivepp ;\n"
                        + "  lemon:sense          :" + lemonEntry + "_ontomap_"+index+" .\n"
                        + "\n"
                        + ":form_" + lemonEntry + "_canonical a lemon:Form ;\n"
                        + "  lemon:writtenRep         \"" + pastTense + "\"@" + language + " .\n"
                        + "\n"
                        + ":form_" + lemonEntry + "_by a    lemon:Form ;\n"
                        + "  lemon:writtenRep     \"" + pastTense + "\"@" + language + " ;\n"
                        + "  lexinfo:verbFormMood lexinfo:participle .\n"
                        + "\n"
                        + "\n"
                        + ":" + lemonEntry + "_frame_adjectivepp a  lexinfo:AdjectivePPFrame ;\n"
                        + "  lexinfo:copulativeSubject    :" + lemonEntry + "_subj ;\n"
                        + "  lexinfo:prepositionalAdjunct :" + lemonEntry + "_obj .\n"
                        + "\n"
                        + ":" + lemonEntry + "_ontomap_"+index + " a lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping :" + lemonEntry + "_ontomap" + " ;\n"
                        + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp  :" + lemonEntry + "_subj ;\n"
                        + "  lemon:objOfProp   :" + lemonEntry + "_obj ;\n"
                        + "  lemon:condition   :" + tupple.getSenseId() + "_condition .";
                intransitiveStr += line;
                index=index+1;
            }*/
            String prep = "\n"
                    + ":" + lemonEntry + "_obj lemon:marker :" + proeposition_id + " .\n"
                    + "\n";
            str = str + intransitiveStr + prep;

            return str;
        }

        /*public  void setArticle(Tupples tupple, String[] row) {
           GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
           GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }*/
        public void setArticle( Map<String, List<String>> domainOrRange) {
               for(String key:domainOrRange.keySet()){
               List<String> row= domainOrRange.get(key);
               GenderUtils.setWrittenForms(key, row.get(0), row.get(1)); 
            }

        }

        public void setVerbInfo(String partOfSpeech, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String writtenFormPerfect) {
            Map<String, String> verbTypes = Map.of(
                    present, writtenFormInfinitive,
                    present3rd, writtenForm3rdPerson,
                    past, writtenFormPast,
                    perfect, writtenFormPerfect
            );

            String[] verbs = new String[]{writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast, writtenFormPerfect};
            GenderUtils.setVerbTypes(partOfSpeech, verbs, verbTypes);
        }

        public String getPrepostion(String lemonEntry, String preposition, String language) {
            if (preposition.contains("X")) {
                preposition = "";
            }
            return "## Prepositions ##\n"
                    + ":" + this.proeposition_id + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + language + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                    + "\n"
                    + "";
        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormInfinitive(String[] row) {
            return row[writtenFormInfinitive];
        }

        public String getWrittenForm3rdPerson(String[] row) {
            return row[writtenForm3rdPerson];
        }

        public String getWrittenFormPast(String[] row) {
            return row[writtenFormPast];
        }

        public String getWrittenFormPerfect(String[] row) {
            return row[this.writtenFormPerfect];
        }

        public String getSyntacticFrameIndex(String[] row) {
            return row[syntacticFrameIndex];
        }

        public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getSubjectIndex(String[] row) {
            return row[subjectIndex];
        }

        public String getDirectObjectIndex(String[] row) {
            return row[directObjectIndex];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }

        public String getPassivePrepositionIndex(String[] row) {
            return row[passivePrepositionIndex];
        }

        /*public Integer  getDomainWrittenSingular() {
            return domainWrittenSingularFormIndex;
        }

        public Integer getDomainWrittenPlural() {
            return domainWrittenPluralFormIndex;
        }

        public Integer getRangeWrittenSingular() {
            return rangeWrittenSingularFormIndex;
        }

        public Integer getRangeWrittenPlural() {
            return rangeWrittenPluralFormIndex;
        }*/
    }

    public static class InTransitFrame {

        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	preposition	SyntacticFrame	subject	prepositionalAdjunct	sense	reference	domain	range	GrammarRule 1 :question 1	GrammarRule 1 :question 2	GrammarRule 1 :sparql	Grammar rule 2: question1	Grammar rule 2: question2	sparql2
        //flow_through	verb	flow	flows	flowed	through	IntransitivePPFrame	domain	range	1	dbo:country	dbo:River	dbo:Country	What dbo:River(X) flows through Y(dbo:Country)?	FALSE	SELECT ?X WHERE { ?X dbo:country Y.}	Which dbo:Country(X) does Y(dbo:River) flow through?	FALSE	SELECT ?X WHERE { Y dbo:country ?X.}
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormInfinitive = 2;
        private Integer writtenForm3rdPerson = 3;
        private Integer writtenFormPast = 4;
        private Integer writtenFormPerfect = 5;
        private Integer preposition = 6;
        private Integer syntacticFrameIndex = 7;
        private Integer subject = 8;
        private Integer prepositionalAdjunct = 9;
        private Integer senseIndex = 10;
        private Integer referenceIndex = 11;
        private Integer domainIndex = 12;
        private Integer rangeIndex = 13;
        private Integer domainWrittenSingularFormIndex = rangeIndex + 1;
        private Integer domainWrittenPluralFormIndex = domainWrittenSingularFormIndex + 1;
        private Integer rangeWrittenSingularFormIndex = domainWrittenPluralFormIndex + 1;
        private Integer rangeWrittenPluralFormIndex = rangeWrittenSingularFormIndex + 1;
        private static String preposition_id;
        private String subjectLemon=null;
        private String objectLemon=null;

        public String getHeader(String lemonEntry, String proposition, String language) {
            preposition_id = "form_" + lemonEntry + "_preposition";
            return "@prefix :        <http://localhost:8080/lexicon#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    + "  lemon:entry    :" + lemonEntry + " ;\n"
                    + "  lemon:entry    :" + preposition_id + " .\n"
                    + "\n";
        }

        public String getSenseIndexing(List<Tupples> senseIds, String lemonEntry) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                    + "  lemon:canonicalForm  :form_" + lemonEntry + " ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "_present3rd ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "_past ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "_perfect ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_frame .\n"
                    + "\n";
            return senseIdStr;
        }

        public String getWritten(String lemonEntry, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String writtenFormPerfect, String language, String subject) {
            if (subject.contains("domain")) {
                this.subjectLemon = lemonEntry + "_subj";
                this.objectLemon = lemonEntry + "_obj";
            } else {
                this.objectLemon = lemonEntry + "_subj";
                this.subjectLemon = lemonEntry + "_obj";
            }

            return ":" + "form_" + lemonEntry + " a           lemon:Form ;\n"
                    + "  lemon:writtenRep     \"" + writtenFormInfinitive + "\"@" + language + " ;\n"
                    + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                    + "\n"
                    + "\n"
                    + ":form_" + lemonEntry + "_present3rd a      lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenForm3rdPerson + "\"@" + language + " ;\n"
                    + "  lexinfo:number   lexinfo:singular ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense    lexinfo:present .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "_past a lemon:Form ;\n"
                    + "  lemon:writtenRep  \"" + writtenFormPast + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:singular ;\n"
                    + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense     lexinfo:past .\n"
                    + "\n"
                    + "\n"
                    + ":form_" + lemonEntry + "_perfect" + " a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPerfect + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:perfect ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson .\n"
                    + "\n"
                    + ":" + lemonEntry + "_frame a  lexinfo:IntransitivePPFrame ;\n"
                    + "  lexinfo:subject              :" + lemonEntry + "_subj" +" ;\n"
                    + "  lexinfo:prepositionalAdjunct :" + lemonEntry + "_obj" +" .\n"
                    + "\n";
        }

        public String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
            String str = "";

            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a     lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping :" + lemonEntry + "_ontomap ;\n"
                        + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp  :" + this.subjectLemon +" ;\n"
                        + "  lemon:objOfProp   :" + this.objectLemon + " ;\n"
                        + "  lemon:condition   :" + tupple.getSenseId() + "_condition .\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_condition a      lemon:condition ;\n"
                        + "  lemon:propertyDomain <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange  <" + tupple.getRange() + "> .\n"
                        + "\n";
                str += line;
            }
            String prep = "\n"
                    + ":" + lemonEntry + "_obj lemon:marker :" + preposition_id + " .\n"
                    + "\n";
            str = str + prep;

            return str;
        }

        /*public  void setArticle(Tupples tupple, String[] row) {
           GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
           GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }*/
        public void setArticle(Tupples tupple, Map<String, List<String>> domainOrRange) {
            String domain = Property.shortPrefix(tupple.getDomain());
            String range = Property.shortPrefix(tupple.getRange());
            /*if (domainOrRange.containsKey(domain)) {
                List<String> row = domainOrRange.get(domain);
                GenderUtils.setWrittenForms(domain, row.get(0), row.get(1));

            } else if (domainOrRange.containsKey(range)) {
                List<String> row = domainOrRange.get(range);
                GenderUtils.setWrittenForms(range, row.get(0), row.get(1));

            }*/
            for(String key:domainOrRange.keySet()){
               List<String> row= domainOrRange.get(key);
               GenderUtils.setWrittenForms(key, row.get(0), row.get(1)); 
            }

        }

        public void setVerbInfo(String partOfSpeech, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String writtenFormPerfect) {
            Map<String, String> verbTypes = Map.of(
                    present, writtenFormInfinitive,
                    present3rd, writtenForm3rdPerson,
                    past, writtenFormPast,
                    perfect, writtenFormPerfect
            );

            String[] verbs = new String[]{writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast, writtenFormPerfect};
            GenderUtils.setVerbTypes(partOfSpeech, verbs, verbTypes);
        }

        public String getPrepostion(String lemonEntry, String preposition, String language) {
            if (preposition.contains("X")) {
                preposition = "";
            }
            return "## Prepositions ##\n"
                    + ":" + preposition_id + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + language + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                    + "\n"
                    + "";
        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormInfinitive(String[] row) {
            return row[writtenFormInfinitive];
        }

        public String getWrittenForm3rdPerson(String[] row) {
            return row[writtenForm3rdPerson];
        }

        public String getWrittenFormPast(String[] row) {
            return row[writtenFormPast];
        }

        public String getWrittenFormPerfect(String[] row) {
            return row[this.writtenFormPerfect];
        }

        public String getPreposition(String[] row) {
            return row[preposition];
        }

        public String getSyntacticFrameIndex(String[] row) {
            return row[syntacticFrameIndex];
        }

        public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getSubject(String[] row) {
            return row[subject];
        }

        public String getPrepositionalAdjunct(String[] row) {
            return row[prepositionalAdjunct];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }

        public Integer getDomainWrittenSingular() {
            return domainWrittenSingularFormIndex;
        }

        public Integer getDomainWrittenPlural() {
            return domainWrittenPluralFormIndex;
        }

        public Integer getRangeWrittenSingular() {
            return rangeWrittenSingularFormIndex;
        }

        public Integer getRangeWrittenPlural() {
            return rangeWrittenPluralFormIndex;
        }

    }

    public static class AttributiveAdjectiveFrame {

        //LemonEntry	partOfSpeech	writtenForm	SyntacticFrame	copulativeSubject	
        //attributiveArg	sense	reference	owl:onProperty	owl:hasValue	
        //domain	range	question (attributive use)							
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormInfinitive = 2;
        private Integer syntacticFrameIndex = 3;
        private Integer copulativeSubjectIndex = 4;
        private Integer attributiveArgIndex = 5;
        private Integer senseIndex = 6;
        private Integer referenceIndex = 7;
        private Integer owl_onPropertyIndex = 8;
        private Integer owl_hasValueIndex = 9;
        private Integer domainIndex = 10;
        private Integer rangeIndex = 11;
        private Integer classIndex = 12;
        private Integer originalIndex = 13;
        private Integer size = originalIndex + 1;

        public String getAtrributiveFrameHeader(String lemonEntry, List<Tupples> senseIds, String language) {
            String senseIdStr = getSenseIdRes(senseIds);

            return "@prefix :        <http://localhost:8080/#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    //+ "  lemon:entry    :" + lemonEntry + "_res ;\n"
                    + senseIdStr
                    + "  lemon:entry    :" + lemonEntry + " .\n"
                    + "\n";
        }

        public String getAtrributiveFrameIndexing(List<Tupples> senseIds, String lemonEntry) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                    + "  lemon:canonicalForm  :" + lemonEntry + "_lemma ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_attrFrame, :" + lemonEntry + "_predFrame .\n"
                    + "\n";
            return senseIdStr;
        }

        public String getAtrrtibutiveWrittenForm(String lemonEntry, String writtenFormInfinitive, String language) {
            return ":" + lemonEntry + "_lemma lemon:writtenRep \"" + writtenFormInfinitive + "\"@" + language + " .\n"
                    + "\n"
                    + ":" + lemonEntry + "_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
                    + "  lexinfo:copulativeSubject :" + lemonEntry + "_PredSynArg .\n"
                    + "\n"
                    + ":" + lemonEntry + "_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
                    + "  lexinfo:attributiveArg :" + lemonEntry + "_AttrSynArg .\n"
                    + "\n";

        }

        public String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
            String str = "";
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a  lemon:LexicalSense ;\n"
                        + "  lemon:reference :" + tupple.getSenseId() + "_res ;\n"
                        + "  lemon:isA       :" + lemonEntry + "_AttrSynArg, :" + lemonEntry + "_PredSynArg .\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_res a   owl:Restriction ;\n"
                        + "  owl:onProperty <" + tupple.getDomain() + "> ;\n"
                        + "  owl:hasValue   <" + tupple.getRange() + "> .\n";
                str += line;
            }

            return str;
        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormInfinitive(String[] row) {
            return row[writtenFormInfinitive];
        }

        public String getSyntacticFrameIndex(String[] row) {
            return row[syntacticFrameIndex];
        }

        public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getCopulativeSubjectIndex(String[] row) {
            return row[copulativeSubjectIndex];
        }

        public String getAttributiveArgIndex(String[] row) {
            return row[attributiveArgIndex];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getOwl_onPropertyIndex(String[] row) {
            return row[owl_onPropertyIndex];
        }

        public String getOwl_hasValueIndex(String[] row) {
            return row[owl_hasValueIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }

        public String getClassIndex(String[] row) {
            return row[classIndex];
        }

        public String getOriginalIndex(String[] row) {
            return row[originalIndex];
        }

        public Integer getSize() {
            return size;
        }

    }
    
    public static class NounPredicateFrame {			
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormSingular = 2;
        private Integer writtenFormPlural = 3;
        private Integer syntacticFrameIndex =4 ;
        private Integer copulativeSubjectIndex = 5;
        private Integer prepositionalAdjunct = 6;
        private Integer senseIndex = 7;
        private Integer referenceIndex = 8;
        private Integer domainIndex = 9;
        private Integer rangeIndex = 10;

        public String getNounPredicateFrameTurtle(String[] row,String language,String lemonId) {
            //String lemonId=this.format(this.getLemonEntryIndex(row)+"_"+id);
            String singular=this.format(this.getWrittenFormSingular(row)).toLowerCase();
            String plural=this.format(this.getWrittenFormPlural(row)).toLowerCase();
            String property=Property.withPrefix(this.format(this.getReferenceIndex(row)));
            String resource=Property.withPrefix(this.format(this.getDomainIndex(row)));
            
            property=MatcherExample.replaceSpecial(property);
            resource=MatcherExample.replaceSpecial(resource);
            
            String turtle = "@prefix :        <http://localhost:8080/#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \""+language+"\" ;\n"
                    + "  lemon:entry    :"+lemonId+"_sense_1_res ;\n"
                    + "  lemon:entry    :"+lemonId+" .\n"
                    + "\n"
                    + ":"+lemonId+" a  lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:noun ;\n"
                    + "  lemon:canonicalForm  :"+lemonId+"_form ;\n"
                    + "  lemon:otherForm  :"+lemonId+"_singular ;\n"
                    + "  lemon:otherForm  :"+lemonId+"_plural ;\n"
                    + "  lemon:sense          :"+lemonId+"_sense_1 ;\n"
                    + "  lemon:synBehavior    :"+lemonId+"_predFrame .\n"
                    + "\n"
                    + ":"+lemonId+"_form\n"
                    + " lemon:writtenRep \""+singular+"\"@en .\n"
                    + "\n"
                    + ":"+lemonId+"_singular a    lemon:Form ;\n"
                    + "  lemon:writtenRep \""+singular+"\"@en ;\n"
                    + "  lexinfo:number   lexinfo:singular .\n"
                    + "\n"
                    + ":"+lemonId+"_plural a   lemon:Form ;\n"
                    + "  lemon:writtenRep \""+plural+"\"@en ;\n"
                    + "  lexinfo:number    lexinfo:plural .\n"
                    + "\n"
                    + ":"+lemonId+"_sense_1 a  lemon:LexicalSense ;\n"
                    + "  lemon:reference :"+lemonId+"_sense_1_res ;\n"
                    + "  lemon:isA       :"+lemonId+"_PredSynArg .\n"
                    + "\n"
                    + ":"+lemonId+"_sense_1_res a   owl:Restriction ;\n"
                    + "  owl:onProperty <"+property+"> ;\n"
                    + "  owl:hasValue   <"+resource+"> .";
            return turtle;
        }


        public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getLemonEntryIndex(String []row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String row[]) {
            return row[partOfSpeechIndex];
        }

       
        public String getWrittenFormSingular(String row[]) {
            return row[writtenFormSingular];
        }

        public String getWrittenFormPlural(String row[]) {
            return row[writtenFormPlural];
        }

        public String getSyntacticFrameIndex(String row[]) {
            return row[syntacticFrameIndex];
        }

        public String getCopulativeSubjectIndex(String row[]) {
            return row[copulativeSubjectIndex];
        }

        public String getPrepositionalAdjunct(String row[]) {
            return row[prepositionalAdjunct];
        }

        public String getSenseIndex(String row[]) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String []row) {
            return row[referenceIndex];
        }

        public String getDomainIndex(String [] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String []row) {
            return row[rangeIndex];
        }

        private String format(String string) {
            string=string.stripLeading().stripTrailing().strip().trim();
            return string;
        }

       
    }

    public static class GradbleAdjectiveFrameCsv {

        //LemonEntry	partOfSpeech	writtenForm	comparative	superlative	SyntacticFrame	
        //predFrame	sense	reference	oils:boundTo	oils:degree	domain	range
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormIndex = 2;
        private Integer comparativIndex = 3;
        private Integer superlativeIndex = 4;
        private Integer syntacticFrameIndex = 5;
        private Integer predFrameIndex = 6;
        private Integer senseIndex = 7;
        private Integer referenceIndex = 8;
        private Integer oils_boundToIndex = 9;
        private Integer oils_degreeIndex = 10;
        private Integer domainIndex = 11;
        private Integer rangeIndex = 12;
        private Integer prepostionIndex = 13;
        private Integer domainWrittenSingularFormIndex = 14;
        private Integer domainWrittenPluralFormIndex = 15;
        private Integer rangeWrittenSingularFormIndex = 16;
        private Integer rangeWrittenPluralFormIndex = 17;

        public String getHeader(String lemonEntry, String language) {
            return "@prefix :        <http://localhost:8080/#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "@prefix oils:     <http://lemon-model.net/oils#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    + "  lemon:entry    :" + lemonEntry + " ;\n"
                    + "  lemon:entry    :" + "form_" + lemonEntry + "_preposition" + " ;\n"
                    + "  lemon:entry    :" + lemonEntry + "_res .\n"
                    + "\n";
        }

        public String getIndexing(String lemonEntry, List<Tupples> senseIds) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr
                    = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                    + "  lemon:canonicalForm  :" + "form_" + lemonEntry + " ;\n"
                    + "  lemon:otherForm      :" + "form_" + lemonEntry + "_comperative" + " ;\n"
                    + "  lemon:otherForm      :" + "form_" + lemonEntry + "_superlative" + " ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_predFrame" + " .\n"
                    + "\n";

            return senseIdStr;
        }

        public String getWrittenTtl(String lemonEntry, String baseForm, String comparative, String superlative, String language) {
            String written
                    = ":" + "form_" + lemonEntry + " lemon:writtenRep \"" + baseForm + "\"@" + language + " .\n"
                    + "\n";
            String writtenFormComparative
                    = ":" + "form_" + lemonEntry + "_comperative" + " lemon:writtenRep \"" + comparative + "\"@" + language + " .\n"
                    + "\n";

            String writtenFormSuperlative
                    = ":" + "form_" + lemonEntry + "_superlative" + " lemon:writtenRep \"" + superlative + "\"@" + language + " .\n"
                    + "\n";

            String predFrame
                    = ":" + lemonEntry + "_predFrame" + " a        lexinfo:" + TempConstants.AdjectiveSuperlativeFrame + " ;\n"
                    + "  lexinfo:copulativeSubject :" + lemonEntry + "_PredSynArg .\n"
                    + "\n";

            return written + writtenFormComparative + writtenFormSuperlative + predFrame;
        }

        public String getSenseDetail(String lemonEntry, List<Tupples> tupples, String language) {
            String str = "";

            for (Tupples tupple : tupples) {
                String line
                        = ":" + tupple.getSenseId() + " a  lemon:LexicalSense ;\n"
                        + "  lemon:reference :" + lemonEntry + "_res" + " ;\n"
                        + "  lemon:isA       :" + lemonEntry + "_PredSynArg" + " ;\n"
                        + "  lemon:condition :" + tupple.getSenseId() + "_condition" + " .\n"
                        + "\n";
                String res
                        = ":" + lemonEntry + "_res" + " a   oils:CovariantScalar ;\n"
                        + "  oils:boundTo  <" + tupple.getOils_boundTo() + "> ;\n"
                        + "  oils:degree   <" + tupple.getOils_degree() + "> .\n"
                        + "\n";
                String condition
                        = ":" + tupple.getSenseId() + "_condition" + " a lemon:condition ;\n"
                        + "  lemon:propertyDomain   <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange    <" + tupple.getRange() + "> .\n";

                str += line + res + condition;
            }

            return str;
        }

        public String getPrepostion(String lemonEntry, String preposition, String language) {
            if (preposition.contains("X")) {
                preposition = "";
            }
            return "\n## Prepositions ##\n"
                    + ":" + "form_" + lemonEntry + "_preposition" + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + language + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                    + "\n"
                    + "";
        }

        /*public void setArticle(Tupples tupple, String[] row) {
            GenderUtils.setWrittenForms(tupple.getDomain(), getDomainWrittenSingularFormIndex(row), getRangeWrittenSingularFormIndex(row));
            GenderUtils.setWrittenForms(tupple.getRange(), getRangeWrittenSingularFormIndex(row), getRangeWrittenPluralFormIndex(row));
        }*/
        public void setArticle(Tupples tupple, Map<String, List<String>> domainOrRange) {
            String domain = Property.shortPrefix(tupple.getDomain());
            String range = Property.shortPrefix(tupple.getRange());

            /*if (domainOrRange.containsKey(domain)) {
                List<String> row = domainOrRange.get(domain);
                GenderUtils.setWrittenForms(domain, row.get(0), row.get(1));

            } else if (domainOrRange.containsKey(range)) {
                List<String> row = domainOrRange.get(range);
                GenderUtils.setWrittenForms(range, row.get(0), row.get(1));

            }*/
            for(String key:domainOrRange.keySet()){
               List<String> row= domainOrRange.get(key);
               GenderUtils.setWrittenForms(key, row.get(0), row.get(1)); 
            }

        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormIndex(String[] row) {
            return row[writtenFormIndex];
        }

        public String getPrepostion(String[] row) {
            return row[prepostionIndex];
        }

        public String getComparativIndex(String[] row) {
            return row[comparativIndex];
        }

        public String getSuperlativeIndex(String[] row) {
            return row[superlativeIndex];
        }

        public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getPredFrameIndex(String[] row) {
            return row[predFrameIndex];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getOils_boundToIndex(String[] row) {
            return row[oils_boundToIndex];
        }

        public String getOils_degreeIndex(String[] row) {
            return row[oils_degreeIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }

        public String getDomainWrittenSingularFormIndex(String[] row) {
            return row[domainWrittenSingularFormIndex];
        }

        public String getDomainWrittenPluralFormIndex(String[] row) {
            return row[domainWrittenPluralFormIndex];
        }

        public String getRangeWrittenSingularFormIndex(String[] row) {
            return row[rangeWrittenSingularFormIndex];
        }

        public String getRangeWrittenPluralFormIndex(String[] row) {
            return row[rangeWrittenPluralFormIndex];
        }

    }

    public static String getSenseId(List<Tupples> senseIds) {
        String str = "";
        for (Tupples tupple : senseIds) {
            String line = "  lemon:sense          :" + tupple.getSenseId() + " ;\n";
            str += line;
        }
        return str;
    }

    public static String getSenseIdRes(List<Tupples> senseIds) {
        String str = "";
        for (Tupples tupple : senseIds) {
            String line = "  lemon:entry    :" + tupple.getSenseId() + "_res ;\n";
            str += line;
        }
        return str;
    }

    /**
     *
     * @author elahi
     */
    public static interface TempConstants {

        public static String NounPPFrame = "NounPPFrame";
        public static String TransitiveFrame = "TransitiveFrame";
        public static String InTransitivePPFrame = "IntransitivePPFrame";
        public static String AdjectiveAttributiveFrame = "AdjectiveAttributiveFrame";
        public static String AdjectivePredicateFrame = "AdjectivePredicateFrame";
        public static String NounPredicateFrame = "NounPredicateFrame";
        public static String AdjectivePPFrame = "AdjectivePPFrame";
        public static String AdjectiveSuperlativeFrame = "AdjectiveSuperlativeFrame";
        public static String FULL_DATASET = "FULL_DATASET";
        public static String subject = "subject";
        public static String directObject = "directObject";
        public static String object = "object";
        public static String mainVerb = "mainVerb";
        public static String TrennVerb = "TrennVerb";
        public static String imperative = "imperative";
        public static String auxVerb = "auxVerb";
        public static String RefVerb = "RefVerb";
        public static String TrennVerbPart2 = "TrennVerbPart2";
        public static String TrennVerbPart1 = "TrennVerbPart1";
        public static final String verb = "verb";
        public static final String pronounReflexive = "reflexive_pronoun";
        public static final String reference = "reference";
        public static final String REGULAR_EXPRESSION_X = "(X)";
        public static final String REGULAR_EXPRESSION_Y = "(Y)";
        public static final String dativeCase = "dativeCase";
        public static final String accusativeCase = "accusativeCase";
        public static final String appos = "appos";
        public static String questionMark = "?";
        public static String space = " ";
        public static String DETERMINER = "determiner";
        public static String activeTransitive = "active";
        public static String activeBoolean = "activeBoolean";
        public static String passiveTransitive = "passive";
        public static String amount = "amount";
        public static String caseType = "case";
        public static String gender = "gender";
        public static String PREPOSITION = "preposition";
        public static String QUESTION_MARK = "?";
        public static String FULL_STOP = ".";
        public static String variableIndicator = "X";
        public static String colon = ":";
        public static String present = "present";
        public static String present3rd = "present3rd";
        public static String past = "past";
        public static String infinitive = "infinitive";
        public static String perfect = "perfect";
        public static String future = "future";
        public static String tense = "tense";
        public static String number = "number";
        public static String person = "person";
        public static String SLASH = "#";
        public static String singular = "singular";
        public static String plural = "plural";
        public static String range = "range";
        public static String domain = "domain";
        public static String property = "property";
        public static String defaultGender = "masculine";
        public static String defaultNumber = singular;
        public static String article = "article";
        public static String thirdPerson = "thirdPerson";
        public static String secondPerson = "secondPerson";
        public static final String Prepositional_Adjuct = "whQuestion";
        public static final String booleanQuestionDomainRange = "booleanQuestion";
        public static final String booleanQuestionDomain = "booleanQuestionWithoutReference";
        public static final String nounPhrase = "nounPhrase";
        public static final String noun = "noun";
        public static final String location = "location";
        public static Set<String> TENSES = new HashSet<String>(Arrays.asList(infinitive, present, present3rd, past, perfect, future));
        public static Set<String> NUMBERS = new HashSet<String>(Arrays.asList(singular, plural));
        public static Set<String> PERSONS = new HashSet<String>(Arrays.asList(secondPerson, thirdPerson));
        public static final String WHEN_WHAT_PAST_THING = "WHEN_WHAT_PAST_THING";
        public static final String WHAT_WHICH_PRESENT_THING_1 = "WHAT_WHICH_PRESENT_THING_1";
        public static final String WHAT_WHICH_PRESENT_THING_2 = "WHAT_WHICH_PRESENT_THING_2";
        public static final String HOW_MANY_TOTAL = "HOW_MANY_TOTAL";
        public static final String HOW_MANY_THING = "HOW_MANY_THING";
        public static final String WHERE_WHAT_PRESENT_THING = "WHERE_WHAT_PRESENT_THING";
        public static final String WHAT_WHICH_LOCATION = "WHAT_WHICH_LOCATION";
        public static final String WHAT_WHICH_LOCATION_COMPANY = "WHAT_WHICH_LOCATION_COMPANY";
        public static final String PERSON_PERSON = "PERSON_PERSON";
        public static final String Copulative_Subject = "Copulative_Subject";
        public static final String WHO_WHO_PERSON = "WHO_WHO_PERSON";
        public static final String WHEN_WHO_PAST_PERSON = "WHEN_WHO_PAST_PERSON";
        public static final String WHERE_WHO_PAST_PERSON = "WHERE_WHO_PAST_PERSON";
        public static final String PERSON_CAUSE = "PERSON_CAUSE";
        public static final String PERSON_CAUSE_OPPOSITITE = "PERSON_CAUSE_OPPOSITITE";
        public static final String PERSON_CAUSE_SUBJECT = "PERSON_CAUSE_SUBJECT";
        public static final String PERSON_CAUSE_SUBJECT_PREPOSITION = "PERSON_CAUSE_SUBJECT_PREPOSITION";
        public static final String HOW_MANY_THING_BACKWARD = "HOW_MANY_THING_BACKWARD";
        public static final String HOW_MANY_THING_FORWARD = "HOW_MANY_THING_FORWARD";
        public static final String PERSON_CAUSE_NOUN_PHRASE = "PERSON_CAUSE_NOUN_PHRASE";
        public static final String PERSON_ACTIVITY = "PERSON_ACTIVITY";
        //public static final String WHAT_PERSON_THING = "WHAT_PERSON_THING";
        //public static final String WHO_PERSON_THING = "WHO_PERSON_THING";
        public static final String Adjunct = "adjunct";
        public static final String determiner = "determiner";
        public static final String question = "question";
        public static final String forward = "forward";
        public static final String backward = "backward";
        public static final String preposition = "preposition";
        public static final String Apostrophe = "Apostrophe";
        public static final String particleLocation = "particleLocation";
        public static final String adjunct = "adjunct";
        public static final String QuestionMark = "?";
        public static final String component_be = "component_be";
        public static final String component_do = "component_do";
        public static final String component_se = "component_se";
        public static final String component_es = "component_es";
        public static final String component_ha = "component_ha";
        public static final String component_estado = "component_estado";
        public static final String component_esta = "component_esta";
        public static final String copulativeSubject = "copulativeSubject";
        public static final String component_aux_object_past = "component_aux_object_past";
        public static final String componentVerb = "componentVerb";
        public static final String DIRECT_OBJECT = "directObject";
        public static String pronoun = "pronoun";
        public static String adjective = "adjective";
        public static String superlative = "superlative";
        public static String superlativeCountry = "superlativeCountry";
        public static String superlativePerson = "superlativePerson";
        public static String superlativeLocation = "superlativeLocation";
        public static String superlativeTeamPlayer = "superlativeTeamPlayer";
        public static String superlativeWorld = "superlativeWorld";
        public static String attributiveSimple = "attributiveSimple";
        public static String adjectiveBaseForm = "adjectiveBaseForm";
        public static String predicateAdjectiveBaseForm = "predicateAdjectiveBaseForm";
        public static String comperative = "comperative";
        public static String degree = "degree";
        public static String past_imperfect = "past_imperfect";
        public static String past_indefinite = "past_indefinite";
        public static String interrogativePronounPerson = "interrogativePronounPerson";
        public static String interrogativePronounThing = "interrogativePronounThing";
        public static String interrogativePronoun = "interrogativePronoun";
        public static String interrogativeDeterminer = "interrogativeDeterminer";
        public static String interrogativePronounThingPlural = "interrogativePronounThingPlural";
    }

}
