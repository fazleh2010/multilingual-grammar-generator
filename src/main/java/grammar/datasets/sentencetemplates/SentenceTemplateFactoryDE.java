package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import static grammar.datasets.sentencetemplates.TempConstants.PERSON_CAUSE;
import grammar.structure.component.Language;
import java.util.List;


class SentenceTemplateFactoryDE implements Factory<SentenceTemplateRepository>, TempConstants {

  private final SentenceTemplateRepository sentenceTemplateRepository;
  private final Language language;

  SentenceTemplateFactoryDE() {
    this.language = Language.DE;
    this.sentenceTemplateRepository = new SentenceTemplateDataset();
  }

  public SentenceTemplateRepository init() {
    this.initByLanguage(language);
    return sentenceTemplateRepository;
  }

  private void initByLanguage(Language language) {
    initDE(language);
  }

  private void initDE(Language language) {

    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
//Was ist die Hauptstadt von Kamerun?
"interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) determiner(component_the_nominative:reference:singular) noun(nominativeCase:singular) preposition adjunct(domain)",
"interrogativePronoun(nominativeCase:range:plural) verb(component_be:present:plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain)",
"interrogativePronoun(nominativeCase:range:singular) verb(component_be:past:singular) determiner(component_the_nominative:reference:singular) noun(nominativeCase:singular) preposition adjunct(domain)",
"interrogativePronoun(nominativeCase:range:plural) verb(component_be:past:plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain)",
//Was sind die Amtssprachen der Philippinen?
"interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) determiner(component_the_nominative:reference:singular) noun(nominativeCase:singular) adjunct(domain)",
"interrogativePronoun(nominativeCase:range:plural) verb(component_be:present:plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) adjunct(domain)",
"interrogativePronoun(nominativeCase:range:singular) verb(component_be:past:singular) determiner(component_the_nominative:reference:singular) noun(nominativeCase:singular) adjunct(domain)",
"interrogativePronoun(nominativeCase:range:plural) verb(component_be:past:plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) adjunct(domain)",
 
//Wer war der Vizepräsident unter Samuel Schmid?
/*"interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)",
"interrogativePronoun(nominativeCase:range:plural) verb(component_be:present:plural) determiner(component_the_nominative:reference) noun(nominativeCase:plural) preposition adjunct(domain)", 
"interrogativePronoun(nominativeCase:range:singular) verb(component_be:past:singular) determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)",
"interrogativePronoun(nominativeCase:range:plural) verb(component_be:past:plural) determiner(component_the_nominative:reference) noun(nominativeCase:plural) preposition adjunct(domain)", 
*/
//welche (Y) waren die Buergermeister von (X)?
"interrogativeDeterminer(nominativeCase:range:singular) verb(component_be:present:singular) nounPhrase",
"interrogativeDeterminer(nominativeCase:range:singular) verb(component_be:past:singular) nounPhrase",
"interrogativeDeterminer(nominativeCase:range:plural) verb(component_be:present:plural) nounPhrase",
"interrogativeDeterminer(nominativeCase:range:plural) verb(component_be:past:plural) nounPhrase",           
//Gib mir die Enkel von Elvis Presley.
"verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(component_the_accusative:reference:singular) noun(accusativeCase:singular) preposition adjunct(domain).",
"verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(component_the_accusative:reference:plural) noun(accusativeCase:plural) preposition adjunct(domain).",
//Gib mir alle Bandmitglieder von Prodigy
"verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) noun(accusativeCase:plural) preposition adjunct(domain).",
//"Liste die Kinder von Margaret Thatcher auf.",
"verb(imperative_verb:present:plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain) preposition(auf)."  ,   
//"Liste die Kinder von Margaret Thatcher auf.",
"verb(imperative_verb:present:plural) determiner(alle) noun(nominativeCase:plural) preposition adjunct(domain) preposition(auf).",
//"Liste alle Brettspiele von GMT. ",
"verb(imperative_verb:present:plural) determiner(alle) noun(nominativeCase:plural) preposition adjunct(domain)."
////"In welchem Land ist der Mount Everest?", In welcher Stadt ist die Heinekenbrauerei?
////"preposition interrogativeDeterminer(dativeCase:range:singular) verb(component_be:present:singular) adjunct(domain)?"
////"interrogativePlace(nominativeCase:singular) verb(component_be:present:singular) determiner(component_the_nominative:domain:singular) object(domain)?"             
////Was ist Batmans richtiger Name?
//// "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjunct(domain) Apostrophe noun(nominativeCase:singular)?"

        ),
        NounPPFrame,
        Prepositional_Adjuct
      )
    );
    
     //NounPPFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Ist Rita Wilson die Frau von Tom Hanks?"
          "verb(component_be:present:singular) subject(range) determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?",
          "verb(component_be:present:plural) subject(range) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain)?"
          //Heißt die Frau von Präsident Obama Michelle?"
          //"verb(component_heißen:present:singular) determiner(component_the_nominative:reference:singular) noun(nominativeCase:singular) preposition adjunct(domain) subject(range)?",
          //"verb(component_heißen:present:plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain) subject(range)?"    
          //Ist Proinsulin ein Protein?
          //"verb(component_be:present:singular) subject(range) noun(nominativeCase:singular) article(definite_article:nominativeCase:neuter) object(domain)?",   
          //"Sind Laubfrösche Amphibien?"
          //"verb(component_be:present:plural) subject(domain) noun(nominativeCase:singular) object(range)?",
          //Hat Abraham Lincolns Sterbeort eine Webseite?", 
          //"verb(component_haben:present:singular) object(domain) article(definite_article:nominativeCase:feminine) subject(range)?"
          //"Hatte Che Guevara Kinder?",
          //"verb(component_haben:past:singular) object(domain) noun(nominativeCase)?"
          //Gibt es ein Videospiel, das Battle Chess heißt?
          // "verb(imperative_transitive) pronoun(object_pronoun_es) article(definite_article:nominativeCase:neuter), noun(nominativeCase), article(component_the_nominative:nominativeCase:neuter) object(domain)"    
           //"Was ist Batmans richtiger Name?",                
           
                 ),
        NounPPFrame,
        booleanQuestionDomainRange
      )
    );
     //NounPPFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Hatte Che Guevara Kinder?",
          "verb(component_haben:past:singular) object(domain) noun(nominativeCase)?" 
        
          //Gibt es ein Videospiel, das Battle Chess heißt?
          // "verb(imperative_transitive) pronoun(object_pronoun_es) article(definite_article:nominativeCase:neuter), noun(nominativeCase), article(component_the_nominative:nominativeCase:neuter) object(domain)"    
          //Hat Abraham Lincolns Sterbeort eine Webseite?", 
          //"verb(component_haben:present:singular) object(domain) article(definite_article:nominativeCase:feminine) noun(nominativeCase)?"
           //"Was ist Batmans richtiger Name?",                
           

            // "interrogativePronoun(range:singular)  verb(component_be:present:singular) object(range) noun(nominativeCase)?"    
            //"Welche Regierungsform hat Russland?",
            //"interrogativeDeterminer(range:singular) verb(component_haben:present:singular) object(domain)?"   
            //"Aus welcher Region ist der Melon de Bourgogne?"
            // "preposition(auf) interrogativeDeterminer(range:singular) verb(component_be:present:singular) adjunct(domain)? "
           // Wieviele Seiten hat Krieg und Frieden?
           // "interrogativeAmount(range:singular) noun(nominativeCase:plural) verb(component_haben:present:singular) object(domain)?"        
                 ),
        NounPPFrame,
        booleanQuestionDomain
      )
    );
    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?"
         //"noun(nominativeCase:plural) preposition adjunct(domain)"      
         //"determiner(component_the_nominative:reference) noun(nominativeCase) preposition nounPhrase"
          //Welche Person ist das Mitglied von...?
          //"interrogativeDeterminer noun(condition:copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
          //Wer ist das Mitglied von...?
          //"interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
          //Gib mir das Mitglied von...?
          //"verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the_accusative) noun(root:accusativeCase) preposition prepositionalAdjunct"
          ),
        NounPPFrame,
        nounPhrase
      )
    );
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "determiner(component_the_nominative:reference) noun(nominativeCase)"
          //Welche Person ist das Mitglied von...?
          //"interrogativeDeterminer noun(condition:copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
          //Wer ist das Mitglied von...?
          //"interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
          //Gib mir das Mitglied von...?
          //"verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the_accusative) noun(root:accusativeCase) preposition prepositionalAdjunct"
          ),
        NounPPFrame,
        noun
      )
    );
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Wo ist der Westminster-Palast?",
          //"interrogativePlace(nominativeCase:range:singular) verb(component_be:present:singular) object(domain)?"
          ),
        NounPPFrame,
        location
      )
    );
    
     // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
//Wer moderiert die BBC Wildlife Specials?
"interrogativePronoun(nominativeCase:range:singular) verb(mainVerb:present:thirdPerson) object(domain)?",
//Welche Person moderiert die BBC Wildlife Specials?
"interrogativeDeterminer(nominativeCase:range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
 "interrogativeDeterminer(nominativeCase:range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
//Wer entwickelt Skype?
"interrogativePronoun(nominativeCase:range:singular) verb(mainVerb:perfect:thridPerson) object(domain)?",    
//Wer hat Slack entwickelt?
"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",    
///Welche Person hat Slack entwickelt?
"interrogativeDeterminer(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?" ,           
//Wer hat sich Family Guy ausgedacht?"
//"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) pronoun(reflexive_pronoun) object(domain) verb(RefVerb:perfect:thridPerson)?"
//"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(RefVerb:perfect:thridPerson)?"
// Trenn Verb
"interrogativePronoun(nominativeCase:range:singular) verb(TrennVerbPart1:past:thridPerson) object(domain) verb(TrennVerbPart2:past:thridPerson)?",
//Welche Flughäfen fliegt Air China an?
"interrogativeDeterminer(nominativeCase:range:plural) verb(TrennVerbPart1:present3rd:thridPerson) object(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",

"interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) object(domain) verb(TrennVerb:perfect:thridPerson)?",
"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(TrennVerb:perfect:thridPerson)?",
"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) pronoun(reflexive_pronoun) object(domain) verb(TrennVerb:perfect:thridPerson)?",
//Gib mir alle von der NASA betriebenen Startrampen.
"verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:singular).",
"verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:plural)."
//Zeig mir das Buch, das Muhammad Ali geschrieben hat.
//"verb(imperative_transitive_show:present:singular) pronoun(object_pronoun) determiner(component_the_accusative:range:singular) noun(accusativeCase:singular), verb(mainVerb:perfect:thridPerson) verb(component_haben:present:singular)."
//Wieviel hat Pulp Fiction gekostet?
//"interrogativeAmount(singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?"
//Zeig mir das Buch, das Muhammad Ali geschrieben hat.
//"verb(imperative_transitive_show:present:singular) pronoun(object_pronoun) determiner(component_the_accusative:range:singular) noun(accusativeCase:singular), verb(mainVerb:perfect:thridPerson) verb(component_haben:present:singular)."
        
        ),
       TransitiveFrame,
        PERSON_CAUSE,
        activeTransitive
      )
    );
      // TransitiveFrame
      //"Was wird von ($x | PERSON_NP) entwickelt?", "Was wurde von ($x | PERSON_NP) entwickelt?", 
      //"Werk wird von ($x | PERSON_NP) entwickelt?", "Werk wurde von ($x | PERSON_NP) entwickelt?", 
      //"Werke werden von ($x | PERSON_NP) entwickelt?", 
      //"Werke wurden von ($x | PERSON_NP) entwickelt?"
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"Was wird von ($x | PERSON_NP) entwickelt?"
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
         //Was wurde von ($x | PERSON_NP) entwickelt?
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        //"Werke werden von ($x | PERSON_NP) entwickelt?", 
        //"Werke wurden von ($x | PERSON_NP) entwickelt?"
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:singular) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
         //Wer war mit Präsident Chirac verheiratet?
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:past:singular) preposition(mit) object(domain) verb(mainVerb:perfect:thridPerson)?", 
        //Trenn
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(TrennVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(TrennVerb:perfect:thridPerson)?"

        ),
        TransitiveFrame,
        PERSON_CAUSE,
        passiveTransitive
      )
    );
    
         // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //Wer verheitet X?
        "interrogativePronoun(nominativeCase:range:singular) verb(mainVerb:present:thirdPerson) object(domain)?",
        //Wer hat X verheitet?
        "interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",
        //Welche Person verheitet X?
        "interrogativeDeterminer(nominativeCase:range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminer(nominativeCase:range:plural) verb(mainVerb:present:thridPerson) object(domain)?"
         ),
       TransitiveFrame,
        PERSON_PERSON,
        activeTransitive
      )
    );
 
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //Wer war mit Jacques Chirac verheiratet?
        //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:past:singular) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        //Wen hat Lance Bass geheiratet?
        "interrogativePronounWhom verb(component_haben:past:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",
        "interrogativePronounWhom verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?"
        ),
        TransitiveFrame,
        PERSON_PERSON,
        passiveTransitive
      )
    );
    
          ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
                 //"Wieviel hat Pulp Fiction gekostet?","Wieviele Sprachen werden in Turkmenistan gesprochen?"
         "interrogativeAmount(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?"
          //was kostet der film?
          //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:present:thridPerson) determiner(component_the_nominative:domain) object(domain)?"
         //"Wieviele Sprachen werden in Turkmenistan gesprochen?"  
         //"interrogativeAmountDeterminer(nominativeCase:range:singular) verb(component_werden:present:singular) preposition determiner(component_the_nominative:domain) object(domain) verb(mainVerb:perfect:thridPerson)?"  

       
        //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      
       
    
        ),
        TransitiveFrame,
        HOW_MANY_TOTAL,
        activeTransitive
      )
    );
   
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
        //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:perfect:present) object(range)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      

           
        ),
        TransitiveFrame,
        HOW_MANY_TOTAL,
        passiveTransitive
      )
    );
    

    //TransitiveFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Ist Rita Wilson die Frau von Tom Hanks?"
          "verb(component_be:present:singular) subject(range) determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?",
          "verb(component_be:present:plural) subject(range) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain)?"      
                 ),
        TransitiveFrame,
        booleanQuestionDomainRange
      )
    );
    
    
    /*sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:singular) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:plural) preposition object(range) verb(mainVerb:past:thridPerson)?"
    
        ),
        TransitiveFrame,
        APP.toString()
      )
    );*/
    //In welchem Museum ist der Schrei ausgestellt?
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(   //Durch welches Land fließt der Rhein?
         "preposition interrogativeDeterminer(preposition:range:singular) verb(mainVerb:present3rd:thridPerson) adjunct(domain)?",
         "preposition interrogativeDeterminer(preposition:range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
          //In welchem Museum ist Der Schrei ausgestellt?
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:plural) verb(component_be:present:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
           
         //TrennVerb: In welcher Stadt hört die Ruta 68 auf?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(TrennVerbPart1:present3rd:thridPerson) adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(TrennVerbPart1:past:thridPerson) adjunct(domain) verb(TrennVerbPart2:past:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(TrennVerb:perfect:thridPerson)?",
          //Wo f├ñngt Piccadilly an?
         "interrogativePlace verb(TrennVerbPart1:present3rd:thridPerson) adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         //Wo ist der Westminster-Palast?  
         "interrogativePlace verb(component_be:present:singular) adjunct(domain)?",
         // Wo liegt Fort Knox?
          "interrogativePlace verb(mainVerb:present3rd:thridPerson) adjunct(domain)?"     

        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        forward
      )
    );
    //Durch welches Land fließt der Rhein?
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         //Was fließt durch...?
        "interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Welcher Fluss fließt durch...?
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(mainVerb:present3rd:thridPerson) preposition adjunct(range)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Trenn
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(range) verb(TrennVerbPart2:past:thridPerson)? ",
          //Für was steht YiCM?
         "preposition interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present3rd:thridPerson) adjunct(domain)?",
         //In welcher Ländern wird Japanisch gesprochen?
         "preposition interrogativeDeterminer(preposition:domain:singular) verb(component_werden:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:domain:plural) verb(component_werden:present:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?" 
         //"preposition interrogativePronounThing(dativeCase:masculine:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
       
        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        backward
      )
    );
      
       //In welchem Museum ist der Schrei ausgestellt?
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(   //Durch welches Land fließt der Rhein?
         "preposition interrogativeDeterminer(preposition:range:singular) verb(mainVerb:present3rd:thridPerson) adjunct(domain)?",
         "preposition interrogativeDeterminer(preposition:range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
         // welches Land fließt Durch der Rhein?
         "interrogativeDeterminer(preposition:range:singular) verb(mainVerb:present3rd:thridPerson) preposition adjunct(domain)?",
         "interrogativeDeterminer(preposition:range:plural) verb(mainVerb:present:thridPerson) preposition adjunct(domain)?",
          //In welchem Museum ist Der Schrei ausgestellt?
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:plural) verb(component_be:present:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
           
         //TrennVerb: In welcher Stadt hört die Ruta 68 auf?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(TrennVerbPart1:present3rd:thridPerson) adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(TrennVerbPart1:past:thridPerson) adjunct(domain) verb(TrennVerbPart2:past:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(TrennVerb:perfect:thridPerson)?",
          //Wo f├ñngt Piccadilly an?
         "interrogativePlace verb(TrennVerbPart1:present3rd:thridPerson) adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         //Wo ist der Westminster-Palast?  
         "interrogativePlace verb(component_be:present:singular) adjunct(domain)?",
         // Wo liegt Fort Knox?
          "interrogativePlace verb(mainVerb:present3rd:thridPerson) adjunct(domain)?"     

        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2,
        forward
      )
    );
    //Durch welches Land fließt der Rhein?
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         //Was fließt durch...?
        "interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Welcher Fluss fließt durch...?
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(mainVerb:present3rd:thridPerson) preposition adjunct(range)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Trenn
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(range) verb(TrennVerbPart2:past:thridPerson)? ",
          //Für was steht YiCM?
         "preposition interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present3rd:thridPerson) adjunct(range)?",
         //In welcher Ländern wird Japanisch gesprochen?
         "preposition interrogativeDeterminer(preposition:domain:singular) verb(component_werden:present:singular) adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:domain:plural) verb(component_werden:present:plural) adjunct(range) verb(mainVerb:perfect:thridPerson)?" 
         //"preposition interrogativePronounThing(dativeCase:masculine:singular) verb(component_be:present:singular) adjunct(range) verb(mainVerb:perfect:thridPerson)?",
       
        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2,
        backward
      )
    );
      
      
      
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        "preposition interrogativeDeterminer(preposition:range:singular) verb(mainVerb:present3rd:thridPerson) adjunct(domain)?",
         "preposition interrogativeDeterminer(preposition:range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
          //In welchem Museum ist Der Schrei ausgestellt?
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:plural) verb(component_be:present:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
          // //Für was steht YiCM?
         //"preposition interrogativePronounThing(dativeCase:masculine:singular) verb(mainVerb:present3rd:thridPerson) adjunct(domain)?",
         //"preposition interrogativePronounThing(dativeCase:masculine:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         
         //TrennVerb: In welcher Stadt hört die Ruta 68 auf?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(TrennVerbPart1:present3rd:thridPerson) adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(TrennVerbPart1:past:thridPerson) adjunct(domain) verb(TrennVerbPart2:past:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(TrennVerb:perfect:thridPerson)?",
          //Wo f├ñngt Piccadilly an?
         "interrogativePlace verb(TrennVerbPart1:present3rd:thridPerson) adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
           
         //In welchem Land ist der Mount Everest?
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain)?",
          //Im welche US Zustand ist Bereich 51 gelegen? 
          "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(TrennVerb:perfect:thridPerson)?"
      
                ),
        InTransitivePPFrame,
        WHAT_WHICH_LOCATION,
        forward
      )
    );
    
    
    
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
            //Was fließt durch...?
        "interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Welcher Fluss fließt durch...?
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Trenn
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(range) verb(TrennVerbPart2:past:thridPerson)? "
            ),
        InTransitivePPFrame,
        WHAT_WHICH_LOCATION,
        backward
      )
    );
      
      ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Wann wurde die Titanic fertiggestellt?"
          "interrogativeTemporal verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
          //Wann endete die Ming-Dynastie?
          "interrogativeTemporal verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //Wann fand die Schlacht von Gettysburg statt?
          "interrogativeTemporal verb(TrennVerbPart1:past:thridPerson) object(domain) verb(TrennVerbPart2:past:thridPerson)?",
          "interrogativeTemporal verb(component_werden:past:singular) adjunct(domain) verb(TrennVerb:perfect:thridPerson)?"
         
        ),
        InTransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        forward
      )
    );
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         //Was wurde 2010 fertiggestellt?
         "interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)",
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)",
         "interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)?",
         "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)?"
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:past:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        backward
      )
    );
    ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"In welchem Jahr wurde Rachel Stevens geboren?",
         "preposition interrogativeDeterminer(dativeCase:range:singular) verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
          //"In welchem Jahr wurde Rachel Stevens geboren?",
         "preposition interrogativeDeterminer(dativeCase:range:singular) verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         //Wann wurde Abrham Lincon geboren?
        "interrogativeTemporal verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
        //Wann ist Draculas Sch├Âpfer gestorben?
        "interrogativeTemporal verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHEN_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHEN_WHO_PAST_PERSON,
        backward
      )
    );
      
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //Wer war mit Pr├ñsident Chirac verheiratet?
         "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)??"


        ),
        InTransitivePPFrame,
        WHO_WHO_PERSON,
        forward
      )
    );
      
      
      
          ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //Wo wurde Donald Trump geboren?
        "interrogativePlace verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
        //In welcher Stadt ist der Pr├ñsident von Montenegro geboren?
        "preposition interrogativeDeterminer(dativeCase:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         //In welcher Stadt wohnt Sylvester Stallone?
        "preposition interrogativeDeterminer(dativeCase:range:singular) verb(mainVerb:present3rd:thridPerson) adjunct(domain)?",
         //Woher hat getan Abraham Lincoln? 
        "interrogativePronounWoher verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
              
               ),
        InTransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        forward
      )
    );
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(             
        // welcher Person wurde in ($x | Place_NP) geboren?
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
        //wer ist in ($x | Place_NP) geboren?
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
        //Welche Menschen wurden geboren im Heraklion?  
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_werden:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?"
        ),
        InTransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        backward
      )
    );
      
              ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //Woran ist Bruce Carver gestorben?
        "interrogativeCause(preposition) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
        //An was ist Bruce Carver gestorben?
        "preposition interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",     
        //Welche Flughäfen fliegt Air China an?
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(TrennVerbPart1:present3rd:thridPerson) adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?"

        ),
        InTransitivePPFrame,
        PERSON_CAUSE,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"Wer ist an Malaria gestorben?
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
        //"Wer war an Malaria gestorben?
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"
      
          ),
        InTransitivePPFrame,
        PERSON_CAUSE,
        backward
      )
    );
      
      
      
      
             ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         //Wo f├ñngt Piccadilly an? Trenn example
         "interrogativePlace verb(TrennVerbPart1:present3rd:thridPerson) object(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         "interrogativePlace verb(TrennVerbPart1:past:thridPerson) object(domain) verb(TrennVerbPart2:past:thridPerson)?"
             ),
        InTransitivePPFrame,
        WHERE_WHAT_PRESENT_THING,
        backward
      )
    );
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //Was beginnt in Piccadilly?      
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:present3rd:thridPerson) preposition adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(domain) verb(TrennVerbPart2:past:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHERE_WHAT_PRESENT_THING,
        forward
      )
    );
      
          ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         //"Wieviel hat Pulp Fiction gekostet?","Wieviele Sprachen werden in Turkmenistan gesprochen?"
         "interrogativeAmount(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",
         //Wieviele Menschen leben in Polen? 
          "interrogativeAmount(nominativeCase:range:plural) verb(mainVerb:present:thridPerson) preposition object(domain)"  
         //was kostet der film?
          //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:present:thridPerson) determiner(component_the_nominative:domain) object(domain)?"
         //"Wieviele Sprachen werden in Turkmenistan gesprochen?"  
         //"interrogativeAmountDeterminer(nominativeCase:range:singular) verb(component_werden:present:singular) preposition determiner(component_the_nominative:domain) object(domain) verb(mainVerb:perfect:thridPerson)?"  

       
        //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      
       
    
        ),
        InTransitivePPFrame,
        HOW_MANY_TOTAL,
        forward
      )
    );
   
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
        //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:perfect:present) object(range)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      

           
        ),
        InTransitivePPFrame,
        HOW_MANY_TOTAL,
        backward
      )
    );
      
       // InTransitiveFrame 
    /*sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many people live in Poland?
        "interrogativeAmount verb(mainVerb:present:thridPerson) preposition adjunct(domain)?",
        //How many people lived in Poland?
        "interrogativeAmount verb(mainVerb:past:thridPerson) preposition adjunct(domain)?",
         //How many seats does (X_Statdium) have?        
        "interrogativeAmount(range:singular) verb(component_do:present:singular) object(domain:plural) verb(mainVerb:present:thridPerson)?"
        //       
        //"interrogativeAmount(range:singular) object(domain) verb(component_be:present:plural) particleLocation?"
   
           ),
       IntransitivePPFrame,
       HOW_MANY_TOTAL,
       forward
      )
    );
    
     // InTransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Which flim costed X?
        //"interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
           ),
       IntransitivePPFrame,
       HOW_MANY_TOTAL,
       backward
      )
    );*/
      
            ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        ////Wieviele Sprachen werden in Turkmenistan gesprochen?
        //"interrogativeAmount(nominativeCase:singular) interrogativeDeterminer(nominativeCase:range:singular) verb(component_werden:present:singular) preposition object(domain) verb(mainVerb:perfect:thridPerson)?",
       //Wieviele Sprachen werden in Turkmenistan gesprochen?
        "interrogativeAmount(nominativeCase:range:plural) verb(component_werden:present:plural) preposition object(domain) verb(mainVerb:perfect:thridPerson)?"
    
        ),
        InTransitivePPFrame,
        HOW_MANY_THING,
        forward
      )
    );
   
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
             
        ),
        InTransitivePPFrame,
        HOW_MANY_THING,
        backward
      )
    );
      
       //NounPPFrame
     sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?",
         "noun(nominativeCase:plural) preposition adjunct(domain)"      
         //"determiner(component_the_nominative:reference) noun(nominativeCase) preposition nounPhrase"
          //Welche Person ist das Mitglied von...?
          //"interrogativeDeterminer noun(condition:copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
          //Wer ist das Mitglied von...?
          //"interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
          //Gib mir das Mitglied von...?
          //"verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the_accusative) noun(root:accusativeCase) preposition prepositionalAdjunct"
          ),
        NounPPFrame,
        nounPhrase
      )
    );
    
    
    
    
     /*sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //Welche Person entwickelte...?
          "interrogativeDeterminer noun(condition:subject) VP(directObject)?",
          //Wer entwickelte...?
          "interrogativePronoun VP(directObject)?"
        ),
        "subject",
        "directObject"
      )
    );
    // VP(directObject)
    sentenceTemplateRepository.add(
      createVPTemplate(
        language,
        List.of(
          //entwickelte...
          "verb(root) directObject"
        ),
        "directObject"
      )
    );*/
    
    
  
  
    /* Not working yet
    // IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //Welcher Fluss fließt durch...?
          "interrogativeDeterminer noun(condition:subject) VP(prepositionalAdjunct)?",
          //Was fließt durch...?
          "interrogativePronoun VP(prepositionalAdjunct)?"
        ),
        "subject",
        "prepositionalAdjunct"
      )
    );
    // VP(prepositionalAdjunct)
    sentenceTemplateRepository.add(
      createVPTemplate(
        language,
        List.of(
          //fließt durch...
          "verb(root) preposition prepositionalAdjunct"
        ),
        "prepositionalAdjunct"
      )
    );
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "TemporalDeterminer noun(condition:subject) VP(temporalAdjunct)?"
        ),
        "subject",
        "temporalAdjunct"
      )
    );*/
    // TransitiveFrame
   
      // AdjectiveAttributiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "interrogativePronoun verb(reference:component_be) NP(attributiveArg)?"
        ),
        "attributiveArg"
      )
    );
    // AdjectivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //Welches Kunstwerk wird von ... ausgestellt
          "interrogativeDeterminer noun(condition:copulativeSubject) verb(reference:component_be_passive) AP(prepositionalAdjunct)?",
          //Was wird von ... ausgestellt
          "interrogativePronoun verb(reference:component_be_passive) AP(prepositionalAdjunct)?"
        ),
        "copulativeSubject",
        "prepositionalAdjunct"
      )
    );
    // AdjectivePPFrame NP
    sentenceTemplateRepository.add(
      createNPTemplate(
        language,
        List.of(
          //Kunstwerke ausgestellt von...
          "noun(condition:copulativeSubject,number:plural) verb(root,verbFormMood:participle) preposition prepositionalAdjunct"
        ),
        "copulativeSubject",
        "prepositionalAdjunct"
      )
    );
    // AP(prepositionalAdjunct)
    sentenceTemplateRepository.add(
      createAPTemplate(
        language,
        List.of(
          //von ... ausgestellt
          "preposition prepositionalAdjunct verb(root,verbFormMood:participle)"
        ),
        "prepositionalAdjunct"
      )
    );
    
        // AdjectivePPFrame...adjectiveBaseForm
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //Wie hoch ist der Leuchtturm in Colombo?
        "interrogativeAmountSize(nominativeCase:range:singular) adjective(adjectiveBaseForm) verb(component_be:present:singular) determiner(component_the_nominative:range) noun(range:singular) preposition adjunct(domain)?"
      ),
        AdjectiveSuperlativeFrame,
        adjectiveBaseForm,
        forward
      )
    );

    // AdjectivePPFrame...comparative
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         
        ),
        AdjectiveSuperlativeFrame,
        comperative
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Was ist der h├Âchste Berg in Australien?
        "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) determiner(component_the_nominative:domain) adjective(superlative) noun(range:singular) preposition adjunct(domain)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativeCountry,
        forward
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Wer ist der jüngste Dartspieler?",
        "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjective(superlative) noun(range:singular) preposition adjunct(domain)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativePerson,
        forward
      )
    );
      // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //Was ist das gr├Â├ƒte Land der Welt?
        "interrogativeDeterminer(nominativeCase:range:singular) verb(component_be:present:singular) adjective(superlative) noun(range:singular) preposition adjunct(domain)?",
         //Was ist das gr├Â├ƒte Land?
        "interrogativeDeterminer(nominativeCase:range:singular) verb(component_be:present:singular) adjective(superlative) noun(range:singular)?",
         //Was ist das gr├Â├ƒte Land der Welt?
        "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjective(superlative) noun(range:singular) preposition adjunct(domain)?",
         //Was ist das gr├Â├ƒte Land?
        "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjective(superlative) noun(range:singular)?"
       
        ),
        AdjectiveSuperlativeFrame,
        superlativeWorld,
        forward
      )
    );
    
    
    
  }
  
        
           //"Hatte Che Guevara Kinder?",
           //"verb(component_haben:past:singular) object(domain) noun(nominativeCase:singular)?",
           //"verb(component_haben:past:plural) object(domain) noun(nominativeCase:plural)?"
           //Hat Abraham Lincolns Sterbeort Kinder?", 
           // "verb(component_haben:present:plural) object(domain) noun(nominativeCase:plural)?",
           // "verb(component_haben:present:singular) object(domain) noun(nominativeCase:singular)?"     
           //"Liste die Kinder von Margaret Thatcher auf.",
           //"verb(imperative_plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain) preposition(auf)."     
           
           //"Welche Regierungsform hat Russland?", 
           //"interrogativeDeterminer(reference:singular) verb(component_haben:present:singular) object(domain)?"     
           
                
                
          //Gibt es ein Videospiel, das Battle Chess heißt?
          // "verb(imperative_transitive) pronoun(object_pronoun_es) article(definite_article:nominativeCase:neuter), noun(nominativeCase), article(component_the_nominative:nominativeCase:neuter) object(domain)"    
           //"Was ist Batmans richtiger Name?",                
           // "interrogativePronoun(range:singular)  verb(component_be:present:singular) object(range) noun(nominativeCase)?"    
            //"Welche Regierungsform hat Russland?",
            //"interrogativeDeterminer(range:singular) verb(component_haben:present:singular) object(domain)?"   
            //"Aus welcher Region ist der Melon de Bourgogne?"
            // "preposition(auf) interrogativeDeterminer(range:singular) verb(component_be:present:singular) adjunct(domain)? "
           // Wieviele Seiten hat Krieg und Frieden?
           // "interrogativeAmount(range:singular) noun(nominativeCase:plural) verb(component_haben:present:singular) object(domain)?"
               
                
                
           //Gib mir das Mitglied von...?
          //"verb(imperative_transitive) pronoun(object_pronoun) determiner(component_the_accusative) noun(accusativeCase) preposition adjunct(domain)."      
          //Who are the children of the children of Elvis Presley?",      
          //"interrogativePronoun(range:plural) verb(component_be:present:plural) nounPhrase?"   
          //Wo verb(component_be:present:singular)  noun?
         //  "interrogativePlace(range:singular) verb(component_be:present:singular) noun?"
          //In welchem Land ist der Mount Everest?
          //"preposition interrogativeDeterminer(range:singular) verb(component_be:present:singular) noun?"
          //Wieviel hat Pulp Fiction gekostet?
                

  
  
         //Ist Proinsulin ein Protein?
         // "verb(component_be:present:singular) subject(range) article(definite_article:nominativeCase:neuter) object(domain)?",   
          //"Sind Laubfrösche Amphibien?"
         // "verb(component_be:present:plural) subject(range) object(domain)?"
          
}