package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import grammar.structure.component.Language;

import java.util.List;

import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
import static grammar.datasets.sentencetemplates.TempConstants.AdjectivePPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.AdjectiveSuperlativeFrame;
import static grammar.datasets.sentencetemplates.TempConstants.HOW_MANY_THING;
import static grammar.datasets.sentencetemplates.TempConstants.NounPPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.PERSON_ACTIVITY;
import static grammar.datasets.sentencetemplates.TempConstants.TransitiveFrame;
import static grammar.datasets.sentencetemplates.TempConstants.WHAT_WHICH_PRESENT_THING_1;
import static grammar.datasets.sentencetemplates.TempConstants.WHEN_WHAT_PAST_THING;
import static grammar.datasets.sentencetemplates.TempConstants.WHEN_WHO_PAST_PERSON;
import static grammar.datasets.sentencetemplates.TempConstants.WHERE_WHO_PAST_PERSON;
import static grammar.datasets.sentencetemplates.TempConstants.backward;
import static grammar.datasets.sentencetemplates.TempConstants.booleanQuestionDomain;
import static grammar.datasets.sentencetemplates.TempConstants.booleanQuestionDomainRange;
import static grammar.datasets.sentencetemplates.TempConstants.forward;
import static grammar.datasets.sentencetemplates.TempConstants.location;
import static grammar.datasets.sentencetemplates.TempConstants.noun;
import static grammar.datasets.sentencetemplates.TempConstants.nounPhrase;
import static grammar.datasets.sentencetemplates.TempConstants.Prepositional_Adjuct;
import static grammar.datasets.sentencetemplates.TempConstants.InTransitivePPFrame;


class SentenceTemplateFactoryEN_LAST implements Factory<SentenceTemplateRepository>,TempConstants{

  private final SentenceTemplateRepository sentenceTemplateRepository;
  private final Language language;

  SentenceTemplateFactoryEN_LAST() {
    this.language = Language.EN;
    this.sentenceTemplateRepository = new SentenceTemplateDataset();
  }

  public SentenceTemplateRepository init() {
    this.initByLanguage(language);
    return sentenceTemplateRepository;
  }

  private void initByLanguage(Language language) {
    initEN(language);
  }

  private void initEN(Language language) {

    //NounPPFrame
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(     
           //What city is the capital of the Philiphines?
          "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition determiner(component_the) adjunct(domain)",
          "interrogativePronoun(range:singular) verb(component_be:present:plural) determiner(component_the) noun(reference:plural) preposition determiner(component_the) adjunct(domain)",
          "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition determiner(component_the) adjunct(domain)",
          "interrogativePronoun(range:singular) verb(component_be:past:plural) determiner(component_the) noun(reference:plural) preposition determiner(component_the) adjunct(domain)",
          //which city is the capital of the Philiphines?
          "interrogativeDeterminer(range:singular) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition determiner(component_the) adjunct(domain)",
          "interrogativeDeterminer(range:plural) verb(component_be:present:plural) determiner(component_the) noun(reference:plural) preposition determiner(component_the) adjunct(domain)",
          "interrogativeDeterminer(range:singular) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition determiner(component_the) adjunct(domain)",
          "interrogativeDeterminer(range:plural) verb(component_be:past:plural) determiner(component_the) noun(reference:plural) preposition determiner(component_the) adjunct(domain)",
           //What is the capital of Cameron?
          "interrogativePronoun(singular) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition adjunct(domain)",
          "interrogativePronoun(plural) verb(component_be:present:plural) determiner(component_the) noun(reference:plural) preposition adjunct(domain)",
          "interrogativePronoun(sinular) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition adjunct(domain)",
          "interrogativePronoun(plural) verb(component_be:past:plural) determiner(component_the) noun(reference:plural) preposition adjunct(domain)",
           //What is the capital of the Cameron?
          "interrogativePronoun(singular) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition determiner(component_the) adjunct(domain)",
          "interrogativePronoun(plural) verb(component_be:present:plural) determiner(component_the) noun(reference:plural) preposition determiner(component_the) adjunct(domain)",
          "interrogativePronoun(singular) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition determiner(component_the) adjunct(domain)",
          "interrogativePronoun(plural) verb(component_be:past:plural) determiner(component_the) noun(reference:plural) preposition determiner(component_the) adjunct(domain)",
          //which city is the capital of Cameron?
          "interrogativeDeterminer(range:singular) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition adjunct(domain)",
          "interrogativeDeterminer(range:plural) verb(component_be:present:plural) determiner(component_the) noun(reference:plural) preposition adjunct(domain)",
          "interrogativeDeterminer(range:singular) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition adjunct(domain)",
          "interrogativeDeterminer(range:plural) verb(component_be:past:plural) determiner(component_the) noun(reference:plural) preposition adjunct(domain)",
           ///What is Batman"s real name? Apostrophe temporary losed..
          "interrogativePronoun(range:singular) verb(component_be:present:singular) adjunct(domain) Apostrophe noun(reference:singular)",
          "interrogativePronoun(range:singular) verb(component_be:past:singular) adjunct(domain) Apostrophe noun(reference:singular)",
           //"Who is the mayor of Paris?",
          "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition adjunct(domain)",
          "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition adjunct(domain)",   
           //"Who is the mayor of Paris?",
          "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition determiner(component_the) adjunct(domain)", 
          "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition determiner(component_the) adjunct(domain)" , 
          //"Who was Samuel Schmid's vice president?", 
          "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjunct(domain) appos noun(singular)?", 
          "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) adjunct(domain) appos noun(singular)?",   
           //List all the musicals by Elton John.
          "verb(imperative_verb:present:plural) determiner(all) determiner(component_the) noun(reference:plural) preposition adjunct(domain)" ,
          "verb(imperative_verb:present:plural) determiner(all) noun(reference:plural) preposition adjunct(domain)" ,
          //List all the musicals with music by Leonard Bernstein.
          "verb(imperative_verb:present:plural) determiner(all) determiner(component_the) noun(range:plural) determiner(with) noun(singular) preposition adjunct(domain)." ,
          //Give me all Apollo 14 astronauts.
          "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) adjunct(domain) noun(reference:plural)", 
          //Give me all Prodigy.
          "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(reference:plural) preposition adjunct(domain)", 
         
          //"Give me the Apollo 14 astronauts?
            "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(component_the) adjunct(domain) noun(reference:singular)",
            "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(component_the) adjunct(domain) noun(reference:plural)" ,
          //Show me all books in Asimov's Foundation series.
          "verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(all) noun(reference:plural) preposition adjunct(domain)" 
        
        ),
        NounPPFrame,
        Prepositional_Adjuct
      )
    );
    
     sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"<NPrange,dbo:mayor> is the mayor of which city? | <NPrange,dbo:mayor> was the mayor of which city?
        "subject(range) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition interrogativeDeterminer(domain:singular)", 
        "subject(range) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition interrogativeDeterminer(domain:singular)"  
                  ),
        NounPPFrame,
        Copulative_Subject
      )
    );
    
        sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"How many [NounSingular] does <NPmap(prepositionalAdjunct),Property> have?
        //"interrogativeAmount noun(reference:singular) verb(component_do:present:singular) adjunct(domain) verb(component_have:present:plural)" ,
        //"How many [NounPlural] does <NPmap(prepositionalAdjunct),Property> have?
        "interrogativeAmount noun(reference:plural) verb(component_do:present:singular) adjunct(domain) verb(component_have:present:plural)?"         
               
              ),
        NounPPFrame,
        HOW_MANY_THING
      )
    );
    
    
    
    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //the capital of germany
         "determiner(component_the) noun(reference:singular) preposition adjunct(domain)?",
         "determiner(component_the) noun(reference:plural) preposition adjunct(domain)?",
         //the Dracula's creator
         "determiner adjunct(domain) Apostrophe noun(reference:singular)?"
                
                ),
        NounPPFrame,
        nounPhrase
      )
    );
    
    
     //NounPPFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         //"Is Rita Wilson the wife of Tom Hanks?"
        "verb(component_be:present:singular) subject(range) determiner(component_the) noun(reference:singular) preposition adjunct(domain)?"
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
         // "verb(component_haben:past:singular) object(domain) noun(nominativeCase)?" 
        
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
    
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "noun(singular)"
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
          "interrogativePlace(nominativeCase:range:singular) verb(component_be:present:singular) object(domain)?"
          ),
        NounPPFrame,
        location
      )
    );
    
    
    //////////////////////////////////////// Transitive ///////////////////////////////////
    
     // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        
         //"Who creates the song Captain America? (X) (X) does not work for the moment
        //"interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) nounVariable(domain:singular) object(domain)?",
        //"interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) nounVariable(domain:plural) object(domain)?",
         //"Who creates the song Captain America?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the)  object(domain)?",
         //"Who created the song Captain America?(X) (X) does not work for the moment
        //"interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) determiner(component_the) nounVariable(domain:singular) object(domain)?",
        //"interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) determiner(component_the) nounVariable(domain:plural) object(domain)?",
         //Who presented BBC Wildlife Specials?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Who wrote the Game of Thrones?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) determiner(component_the) object(domain)?",
        //Which person presented BBC Wildlife Specials?
        //"which river crosses Brooklyn Bridge?"
        "interrogativeDeterminer(range:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:plural) verb(mainVerb:past:thridPerson) object(domain)?",
         //"interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        //Give me all actors starring in X.Give me all films produced by Hal Roach.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
         //Give me all writers that won the Nobel Prize in literature.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) determiner(that) verb(mainVerb:past:thridPerson) adjunct(domain)?",
        //List all actors starring in X.
        "verb(imperative_verb:present:plural) determiner(all) nounVariable(range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
        //Who was the pope that founded the Vatican Television?
        "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) nounVariable(range:singular) determiner(that) verb(mainVerb:past:thirdPerson) object(domain)?",
         //"Give me all writers that won the Nobel Prize in literature."
        "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) determiner(that) verb(mainVerb:past:thridPerson) determiner(component_the) adjunct(domain)."
 
                ),
       TransitiveFrame,
       PERSON_CAUSE,
        activeTransitive
      )
    );
    // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //"What was developed by X?
        "interrogativePronoun(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativePronoun(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //"What was developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Which movies did Kurosawa direct?
         "interrogativeDeterminer(domain:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //Which movies did Kurosawa direct?
         "interrogativeDeterminer(domain:plural) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //"Which books were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which bridges  cross the X?
        "interrogativeDeterminer(domain:plural) verb(mainVerb:present:thridPerson) determiner(component_the) adjunct(range)?", 
        //"Which bridge  crosses the X?
        "interrogativeDeterminer(domain:singular) verb(mainVerb:present3rd:thridPerson) determiner(component_the) adjunct(range)?", 
        //"Which (X_Book) were developed by X?
        //interrogativeVariableDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Show me the book that Muhammad Ali wrote.
         "verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(component_the) nounVariable(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson).", 
         //Show me the books that Muhammad Ali wrote.
         "verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson).",
         //"What kind of music did Lou Reed play?
        "interrogativePronoun(domain) determiner(component_kind) determiner(of) nounVariable(domain:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
             //Give me all writers that won the Nobel Prize in literature.
         //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:plural) determiner(that) verb(mainVerb:past:thridPerson) adjunct(range)?",
           //Give me all video games published by Mean Hamster Software.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //List all video games published by Mean Hamster Software.
         "verb(imperative_verb:present:plural) determiner(all) nounVariable(domain:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Which books did Muhammad Ali write?   //"Which films did Stanley Kubrick direct?"
         "interrogativeDeterminer(range:plural) verb(component_do:past:singular) object(domain) verb(mainVerb:present:thridPerson)?" ,         
         //Which river does the Brooklyn Bridge cross?
        "interrogativeDeterminer(domain:singular) verb(component_do:present:singular) object(range) verb(mainVerb:present:thridPerson)?",
         //Which rivers does the Brooklyn Bridge cross?
        "interrogativeDeterminer(domain:plural) verb(component_do:present:singular) object(range) verb(mainVerb:present:thridPerson)?"
        ),
        TransitiveFrame,
        PERSON_CAUSE,
        passiveTransitive
      )
    );
    
      // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //"What was developed by X?
        "interrogativePronoun(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativePronoun(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //"What was developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Which movie/movies did Kurosawa direct?
         "interrogativeDeterminer(domain:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         "interrogativeDeterminer(domain:plural) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //Which country/countries adopted the Euro?
         "interrogativeDeterminer(domain:singular) verb(mainVerb:present:thridPerson) adjunct(range)?",
         "interrogativeDeterminer(domain:plural) verb(mainVerb:present:thridPerson) adjunct(range)?",
         "interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) adjunct(range)?",
         "interrogativeDeterminer(domain:plural) verb(mainVerb:past:thridPerson) adjunct(range)?",
         //Give me all video games published by Mean Hamster Software.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //List all video games published by Mean Hamster Software.
         "verb(imperative_verb:present:plural) determiner(all) nounVariable(domain:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which books were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which (X_Book) were developed by X?
        //interrogativeVariableDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Show me the book that Muhammad Ali wrote.
         "verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(component_the) nounVariable(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson)." 
         //Show me the books that Muhammad Ali wrote.
         //"verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson)." 

         
         ),
       TransitiveFrame,
       PERSON_CAUSE_OPPOSITITE,
        activeTransitive
      )
    );
    // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(                
             //"Who creates the song Captain America?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) nounVariable(domain:singular) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) nounVariable(domain:plural) object(domain)?",
         //"Who creates the song Captain America?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the)  object(domain)?",
         //"Who created the song Captain America?
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) determiner(component_the) nounVariable(domain:plural) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) determiner(component_the) nounVariable(domain:plural) object(domain)?",
         //Who presented BBC Wildlife Specials?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Who wrote the Game of Thrones?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) determiner(component_the) object(domain)?",
        //Which person presented BBC Wildlife Specials?
        //"which river crosses Brooklyn Bridge?"
        "interrogativeDeterminer(range:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:plural) verb(mainVerb:past:thridPerson) object(domain)?",
        //Which river does the Brooklyn Bridge cross?
        "interrogativeDeterminer(range:singular) verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
         //Which river does the Brooklyn Bridge cross?
        "interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        //"interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        //Which books did Muhammad Ali write?   //"Which films did Stanley Kubrick direct?"
        "interrogativeDeterminer(range:plural) verb(component_do:past:singular) object(domain) verb(mainVerb:present:thridPerson)?",          
         //Give me all actors starring in X.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
        //List all actors starring in X.
        //"verb(imperative_verb:present:plural) determiner(all) nounVariable(range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
        //Who was the pope that founded the Vatican Television?
        "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) nounVariable(range:singular) determiner(that) verb(mainVerb:past:thirdPerson) object(domain)?"
       
        ),
        TransitiveFrame,
        PERSON_CAUSE_OPPOSITITE,
        passiveTransitive
      )
    );
    
        // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //"What was developed by X?
        "interrogativePronoun(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativePronoun(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //"What was developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Which movies did Kurosawa direct?
         "interrogativeDeterminer(domain:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //Which movies did Kurosawa direct?
         "interrogativeDeterminer(domain:plural) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //Give me all video games published by Mean Hamster Software.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //List all video games published by Mean Hamster Software.
         "verb(imperative_verb:present:plural) determiner(all) nounVariable(domain:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which books were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which (X_Book) were developed by X?
        //interrogativeVariableDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Show me the book that Muhammad Ali wrote.
         "verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(component_the) nounVariable(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson).", 
         //Show me the books that Muhammad Ali wrote.
         "verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson)." 

        ),
        TransitiveFrame,
        PERSON_CAUSE_NOUN_PHRASE,
        passiveTransitive
      )
    );
    
      // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
           //"Who creates the song Captain America?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) nounVariable(domain:singular) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) nounVariable(domain:plural) object(domain)?",
         //"Who creates the Captain America?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the)  object(domain)?",
          //Who presented BBC Wildlife Specials?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Who wrote the Game of Thrones?
        "interrogativePronoun(range:singular) verb(mainVerb:present3rd:thirdPerson) determiner(component_the) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) determiner(component_the) object(domain)?",
        //Which person presented BBC Wildlife Specials?
        //"which river crosses Brooklyn Bridge?"
        "interrogativeDeterminer(range:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:plural) verb(mainVerb:past:thridPerson) object(domain)?",
        //Which river does the Brooklyn Bridge cross?
        "interrogativeDeterminer(range:singular) verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
         //Which river does the Brooklyn Bridge cross?
        "interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        //"interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        //Which books did Muhammad Ali write?   //"Which films did Stanley Kubrick direct?"
        "interrogativeDeterminer(range:plural) verb(component_do:past:singular) object(domain) verb(mainVerb:present:thridPerson)?",          
             //Who was the pope that founded the Vatican Television?
        "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) nounVariable(range:singular) determiner(that) verb(mainVerb:past:thirdPerson) object(domain)?",
   
         //Who produce hovercrafts?
        "interrogativePronoun(range) verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronoun(range) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Which companies produce hovercrafts?
        "interrogativeDeterminer(range:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:plural) verb(mainVerb:past:thridPerson) object(domain)?",
       //"Give me all writers that won the Nobel Prize in literature."
        "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) Give me all(range:plural) determiner(that) verb(mainVerb:past:thridPerson) determiner(component_the) adjunct(domain).",
        //Give me all actors starring in X.Give me all films produced by Hal Roach.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
         //Give me all writers that won the Nobel Prize in literature.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) determiner(that) verb(mainVerb:past:thridPerson) determiner(component_the) adjunct(domain)?",
           //Give me all writers that won the Nobel Prize in literature.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) determiner(that) verb(mainVerb:past:thridPerson) adjunct(domain)?",
       
          //List all actors starring in X.
          "verb(imperative_verb:present:plural) determiner(all) nounVariable(range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?"
        ),
       TransitiveFrame,
       PERSON_CAUSE_SUBJECT,
        activeTransitive
      )
    );
    // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //"who was developed by X? temporary closed
        //"interrogativePronoun(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"who were developed by X?temporary closed
        //"interrogativePronoun(range:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //"What was developed by X?
        "interrogativeDeterminer(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativeDeterminer(range:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Which movies did Kurosawa direct?
         "interrogativeDeterminer(range:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //Which movies did Kurosawa direct?
         "interrogativeDeterminer(range:plural) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //Give me all video games published by Mean Hamster Software.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //List all video games published by Mean Hamster Software.
         "verb(imperative_verb:present:plural) determiner(all) nounVariable(range:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which books were developed by X?
        "interrogativeDeterminer(range:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which (X_Book) were developed by X?
        "interrogativeVariableDeterminer(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?"
        
        ),
        TransitiveFrame,
        PERSON_CAUSE_SUBJECT,
        passiveTransitive
      )
    );
    
         // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //Who produce hovercrafts?
        "interrogativePronoun(domain) verb(mainVerb:present:thirdPerson) object(domain)?",
        "interrogativePronoun(domain) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Which companies produce hovercrafts?
        "interrogativeDeterminer(domain:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminer(domain:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminer(domain:plural) verb(mainVerb:past:thridPerson) object(domain)?",
         //Give me all actors starring in X.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
        //List all actors starring in X.
        "verb(imperative_verb:present:plural) determiner(all) nounVariable(domain:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?"
        ),
       TransitiveFrame,
       PERSON_CAUSE_SUBJECT_PREPOSITION,
        activeTransitive
      )
    );
    // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //"What was developed by X?
        "interrogativePronoun(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativePronoun(range:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //"What was developed by X?
        "interrogativeDeterminer(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativeDeterminer(range:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Which movies did Kurosawa direct?
         "interrogativeDeterminer(range:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //Which movies did Kurosawa direct?
         "interrogativeDeterminer(range:plural) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?",
         //Give me all video games published by Mean Hamster Software.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //List all video games published by Mean Hamster Software.
         "verb(imperative_verb:present:plural) determiner(all) nounVariable(range:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which books were developed by X?
        "interrogativeDeterminer(range:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which (X_Book) were developed by X?
        "interrogativeVariableDeterminer(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?"
        
        ),
        TransitiveFrame,
        PERSON_CAUSE_SUBJECT_PREPOSITION,
        passiveTransitive
      )
    );
    
       // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //Who presented BBC Wildlife Specials?
        "interrogativePronoun(range) verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronoun(range) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Which person presented BBC Wildlife Specials?
        //"which river crosses Brooklyn Bridge?"
        "interrogativeDeterminer(range:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        //Whom did Lance Bass marry?
        "interrogativePronounWhom verb(component_be:past:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        "interrogativePronounWhom verb(component_be:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
         //Who was the pope that founded the Vatican Television?
        "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) nounVariable(range:singular) determiner(that) verb(mainVerb:past:thirdPerson) object(domain)?"
        
            ),
       TransitiveFrame,
       PERSON_PERSON,
        activeTransitive
      )
    );
    // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //"What was developed by X?
        "interrogativePronoun(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativePronoun(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //"What was developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //Whom did Lance Bass marry?
        "interrogativePronounWhom verb(component_do:past:singular) object(domain) verb(mainVerb:present:thridPerson)?",
         // //Who was Omar Khayyam inspired by?
         "interrogativePronoun(domain) verb(component_be:past:singular) adjunct(range) verb(mainVerb:perfect:thridPerson) preposition?",
           //Which university did Angela Merkel attend?
         "interrogativeDeterminer(domain:singular) verb(component_do:past:singular) object(range) verb(mainVerb:present:thridPerson) ?",
         "interrogativeDeterminer(domain:singular) verb(component_do:present:singular) object(range) verb(mainVerb:present:thridPerson) ?"

        ),
        TransitiveFrame,
        PERSON_PERSON,
        passiveTransitive
      )
    );
    
    
        // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //Who presented BBC Wildlife Specials?
        "interrogativePronoun(range) verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronoun(range) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Which person presented BBC Wildlife Specials?
        //"which river crosses Brooklyn Bridge?"
        "interrogativeDeterminer(range:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        //Whom did Lance Bass marry?
        "interrogativePronounWhom verb(component_be:past:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        "interrogativePronounWhom verb(component_be:present:singular) object(domain) verb(mainVerb:present:thridPerson)?"
            ),
       TransitiveFrame,
       PERSON_ACTIVITY,
        activeTransitive
      )
    );
    // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //"What was developed by X?
        "interrogativePronoun(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativePronoun(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //"What was developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //Whom did Lance Bass marry?
        "interrogativePronounWhom verb(component_do:past:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        "interrogativePronounWhom verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?"
        ),
        TransitiveFrame,
        PERSON_ACTIVITY,
        passiveTransitive
      )
    );
    
    
    //////////////////////////////////////// Transitive ///////////////////////////////////
    
    // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How much does Pulp Fiction cost?
        "interrogativeMuch verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        //How much did Pulp Fiction cost?
        "interrogativeMuch verb(component_do:past:singular) object(domain) verb(mainVerb:present:thridPerson)?"
              
        ),
       TransitiveFrame,
       HOW_MANY_TOTAL,
       activeTransitive
      )
    );
    
     // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Which flim costed X?
        "interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
           ),
       TransitiveFrame,
       HOW_MANY_TOTAL,
       passiveTransitive
      )
    );
    
    
    // TransitiveFrame passive amount
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //How many ethnic groups live in Slovenia? this should be on aktive side.
        //"interrogativeAmount(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?" ,
        ///How often did Jane Fonda marry?
        "interrogativeOften(domain:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?" ,        
        ///How many films did Hal Roach produce?
        "interrogativeAmount(domain:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?"       
                       
            ),
        TransitiveFrame,
        HOW_MANY_THING,
        passiveTransitive
      )
    );
    
    
    
    
    // InTransitiveFrame 
    sentenceTemplateRepository.add(createSentenceTemplate(language,
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
       InTransitivePPFrame,
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
       InTransitivePPFrame,
       HOW_MANY_TOTAL,
       backward
      )
    );
    
    
    /*sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        //Which country does X flow through?
        "interrogativeDeterminer(range:singular) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?", 
        "interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?",
         //"In which city does the Chile Route 68 end?
        "preposition interrogativeDeterminer(range:singular) verb(component_do:present:singular) determiner(component_the) object(domain) verb(mainVerb:present:singular)?",
        "preposition interrogativeDeterminer(range:plural) verb(component_do:present:plural) object(domain) verb(mainVerb:present:singular)?",
         //In which programming language is GIMP written?
        "preposition interrogativeDeterminer(range:plural) verb(component_be:present:plural) object(domain) verb(mainVerb:perfect:singular)?",
        "preposition interrogativeDeterminer(range:singular) verb(component_be:present:singular) object(domain) verb(mainVerb:perfect:singular)?",
         //Where is Fort Knox located?
         "interrogativePlace verb(component_be:present:singular) object(domain) verb(mainVerb:perfect:singular)?",
         //In which countries do people speak Japanese?
         "preposition interrogativeDeterminer(range:plural) verb(component_do:present:singular) verb(mainVerb:present:singular) object(domain)?"

                
                ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        forward
      )
    );*/
    
     sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        //Which country does X flow through?
        "interrogativeDeterminer(range:singular) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?", 
        "interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?",
        //Which country does the X flow through?
        "interrogativeDeterminer(range:singular) verb(component_do:present:singular) determiner(component_the) object(domain) verb(mainVerb:present:singular) preposition?", 
        "interrogativeDeterminer(range:plural) verb(component_do:present:singular) determiner(component_the) object(domain) verb(mainVerb:present:singular) preposition?", 
        //"In which city does the Chile Route 68 end?
        "preposition interrogativeDeterminer(range:singular) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular)?",
        "preposition interrogativeDeterminer(range:plural) verb(component_do:present:plural) object(domain) verb(mainVerb:present:singular)?",
         //In which programming language is GIMP written?
        "preposition interrogativeDeterminer(range:plural) verb(component_be:present:plural) object(domain) verb(mainVerb:perfect:singular)?",
        "preposition interrogativeDeterminer(range:singular) verb(component_be:present:singular) object(domain) verb(mainVerb:perfect:singular)?",
         //Where is Fort Knox located?
         "interrogativePlace verb(component_be:present:singular) object(domain) verb(mainVerb:perfect:singular)?",
         //In which countries do people speak Japanese?
          "preposition interrogativeDeterminer(range:plural) verb(component_do:present:singular) verb(mainVerb:present:singular) object(domain)?",
         //What movies does Jesse Eisenberg play in?
        "interrogativePronoun(range:singular) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?", 
        "interrogativePronoun(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?",
        //Which writers studied in Istanbul?
        "interrogativeDeterminer(range:singular) verb(mainVerb:present:singular) preposition object(domain)?", 
        "interrogativeDeterminer(range:plural) verb(mainVerb:past:singular) preposition object(domain)?",
        //Give me all islands that belong to Japan.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) determiner(that) verb(mainVerb:present:thridPerson) preposition adjunct(domain)?"

        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        forward
      )
    );
     
     
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //what flows/flew through Germany?
        "interrogativePronoun(domain) verb(mainVerb:present3rd:thridPerson) preposition adjunct(range)?",
        "interrogativePronoun(domain) verb(mainVerb:past:thridPerson) preposition adjunct(range)?",
        //Which river/rivers flows/flew through Germany?
        "interrogativeDeterminer(domain:singular) verb(mainVerb:present3rd:thridPerson) preposition adjunct(range)?",
        "interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?",
         "interrogativeDeterminer(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         "interrogativeDeterminer(domain:plural) verb(mainVerb:past:thridPerson) preposition adjunct(range)?",
         //Which river/rivers was/were flawn through Germany?
         "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //Give me all launch pads operated by NASA.
         //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) object(range) verb(mainVerb:present:thridPerson) preposition adjunct(domain)."
         //Where does Piccadilly start? 
         "interrogativePlace verb(component_do:present:singular) adjunct(range) verb(mainVerb:present:singular)?",
         //Give me all islands that belong to Japan.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:plural) determiner(that) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
          //Give me all islands that belong to Japan.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:plural) determiner(that) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //what does X stand for?
         "interrogativePronoun(domain:singular) verb(component_do:present:singular) adjunct(range) verb(mainVerb:present:thridPerson) preposition?",
         //what did X stand for?
         "interrogativePronoun(domain:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson) preposition?"

        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        backward
      )
    );
      
        sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        // How many rivers flow through <NPrange,dbo:city>?
        "interrogativeAmount(domain:plural) verb(mainVerb:present:singular) preposition adjunct(range)?" ,
        //How many films did X star in?        
        "interrogativeAmount(domain:plural) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:singular) preposition?",
        //How many languages are spoken in Turkmenistan?      
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:singular) preposition adjunct(range)?" 
        ),
        InTransitivePPFrame,
        HOW_MANY_THING_BACKWARD+"_"+WHAT_WHICH_PRESENT_THING_1,
        backward
      )
    );
    
      
     sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        //In which country is the Limerick Lake?
        "preposition interrogativeDeterminer(range:singular) verb(component_be:present:singular) determiner(component_the) object(domain)?",
        "preposition interrogativeDeterminer(range:singular) verb(component_be:present:plural) determiner(component_the) object(domain)?",
        //What country is Mount Everest in?
         "interrogativePronounDeterminer(range:singular) verb(component_be:present:singular) adjunct(domain) preposition?",
        //In which programming language is GIMP written?
        "preposition interrogativeDeterminer(range:plural) verb(component_be:present:plural) object(domain) verb(mainVerb:perfect:singular)?",
        "preposition interrogativeDeterminer(range:singular) verb(component_be:present:singular) object(domain) verb(mainVerb:perfect:singular)?",
         //Where is Fort Knox located?
         "interrogativePlace verb(component_be:present:singular) object(domain) verb(mainVerb:perfect:singular)?"
                
                ),
        InTransitivePPFrame,
        WHAT_WHICH_LOCATION,
        forward
      )
    );
    
    
    
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
          //what flows through Germany?
          "interrogativePronoun(domain) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
          //what does X stand for?
          "interrogativePronoun(domain) verb(component_do:present:singular) adjunct(range) verb(mainVerb:present:thridPerson) preposition?",
          //Which river flows through Germany?
          "interrogativeDeterminer(domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
          //what flew through Germany?
          "interrogativePronoun(domain) verb(mainVerb:past:thridPerson) preposition adjunct(range)?",
          //Which rivers flow through Germany?
          "interrogativeDeterminer(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?"             
            ),
        InTransitivePPFrame,
        WHAT_WHICH_LOCATION,
        backward
      )
    );
      
      
      
      
      
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(    
         //Which types of grapes grow in Oregon?
         "interrogativeDeterminer(range:singular) verb(mainVerb:present3rd:singular) preposition object(domain)?", 
         //Which types of grapes grow in Oregon?
         "interrogativeDeterminer(range:plural) verb(mainVerb:present:singular) preposition object(domain)?",
         //What types of grapes grows in Oregon?
         "interrogativePronoun(range:plural) verb(mainVerb:present:singular) preposition object(domain)?",
          //Which types of grapes grow in Oregon?
         "interrogativeDeterminer(range:singular) verb(mainVerb:past:singular) preposition object(domain)?", 
         //Which types of grapes grow in Oregon?
         "interrogativeDeterminer(range:plural) verb(mainVerb:past:singular) preposition object(domain)?",
         //What types of grapes grows in Oregon?
         "interrogativePronoun(range:plural) verb(mainVerb:past:singular) preposition object(domain)?"
               ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2,
        forward
      )
    );
    
    
    
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //"In which city does the Chile Route 68 end?
        "preposition interrogativeDeterminer(domain:singular) verb(component_do:present:singular) object(range) verb(mainVerb:present:singular)?",
        "preposition interrogativeDeterminer(domain:plural) verb(component_do:present:singular) object(range) verb(mainVerb:present:singular)?",
         //In which programming language is GIMP written?
        "preposition interrogativeDeterminer(domain:plural) verb(component_be:present:plural) object(range) verb(mainVerb:perfect:singular)?",
        "preposition interrogativeDeterminer(domain:singular) verb(component_be:present:singular) object(range) verb(mainVerb:perfect:singular)?"
                 ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2,
        backward
      )
    );
      ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
          //When was X completed?
          "interrogativeTemporal verb(component_be:past:singular) object(domain) verb(mainVerb:past:thridPerson)?",
           //When were X completed?
          "interrogativeTemporal verb(component_be:past:plural) object(domain) verb(mainVerb:past:thridPerson)?",
           //When was the X completed?
          "interrogativeTemporal verb(component_be:past:singular) determiner(component_the) object(domain) verb(mainVerb:past:thridPerson)?",
           //When were the X completed?
          "interrogativeTemporal verb(component_be:past:plural) determiner(component_the) object(domain) verb(mainVerb:past:thridPerson)?",
           //When were X completed?
          //"interrogativeTemporal verb(component_do:past:plural) object(domain) verb(mainVerb:past:thridPerson)?"
          //When did Operation Overlord commence?
          "interrogativeTemporal verb(component_do:past:singular) object(domain) verb(mainVerb:present:thridPerson)?",
           //When did Operation Overlord commence?
          "interrogativeTemporal verb(component_do:past:singular) determiner(component_the) object(domain) verb(mainVerb:present:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        forward
      )
    );
       
        // TransitiveFrame passive amount
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?"  
            ),
        InTransitivePPFrame,
        HOW_MANY_THING,
        backward
      )
    );
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //what took place in Date?  
        "interrogativePronoun(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
         ////what happened in Date?
        //"interrogativePronoun(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
              ),
        InTransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        backward
      )
    );
    ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //When was X completed?
         "interrogativeTemporal verb(component_be:past:singular) object(domain) verb(mainVerb:past:thridPerson)?",
         //In which year was Joanna MacGregor born?
         "preposition interrogativeDeterminer(range:singular) verb(component_be:past:singular) object(domain) verb(mainVerb:past:thridPerson)?",
         //When did Elisabeth of Saxony die?
         "interrogativeTemporal verb(component_do:past:singular) object(domain) verb(mainVerb:present:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHEN_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //which organisation was founded in ($x | date_NP)?     
         "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //which organisations were founded in ($x | date_NP)?     
         "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //Who was founded in ($x | date_NP)?,
         "interrogativePronoun(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?"
        ),
        InTransitivePPFrame,
        WHEN_WHO_PAST_PERSON,
        backward
      )
    );
      
       ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
                //Which actors were born on the Philippines?
         "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //Which actors were born on the Philippines?
         "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //Give me all actors who were born in Berlin.
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(domain:plural) interrogativePronoun(range:singular) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
          //Which people were born in Heraklion?
         "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         // Give all swimmers that were born in Moscow.
         "verb(component_imperative_transitive:present:singular) determiner(all) nounVariable(domain:plural) determiner(that) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
              //Who was born in X?
         "interrogativePronoun(domain:singular) verb(component_be:past:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
 
         
             ),
        InTransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
              //Where was Bach born? and Where was Sparkling wine produced?
         "interrogativePlace verb(component_be:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         //Where is Sparkling wine produced?
         "interrogativePlace verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         /* //Where in France is sparkling wine produced?
         "interrogativePlace preposition nounVariable(range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:singular)?", 
          //Where in France is sparkling wine produced?
         "interrogativePlace preposition nounVariable(range:singular) verb(component_be:past:singular) adjunct(domain) verb(mainVerb:perfect:singular)?", 
         */
         //In which city did John F. Kennedy die?
         "preposition interrogativeDeterminer(range:singular) verb(component_do:past:singular) adjunct(domain) verb(mainVerb:present:thridPerson)?",
          //Which country was Bill Gates born in?
         "interrogativeDeterminer(range:singular) verb(component_be:past:singular) adjunct(domain) verb(mainVerb:present:thridPerson) preposition?",
          //Which country does X come from?
         "interrogativeDeterminer(range:singular) verb(component_do:present:singular) adjunct(domain) verb(mainVerb:present:thridPerson) preposition?",
          //"Where did Abraham Lincoln die?"
         "interrogativePlace verb(component_do:past:singular) adjunct(domain) verb(mainVerb:present:thridPerson)?"
        
        ),
        InTransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        backward
      )
    );
      
      
          ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //"Welche Profisurfer wurden auf den Philippinen geboren?",
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"

             ),
        InTransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Wo wurde Donald Trump geboren?
         //"interrogativePlace(nominativeCase:singular) verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        backward
      )
    );
      
            ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //what stars in Piccadilly?
        "interrogativePronoun(domain:singular) verb(mainVerb:present3rd:thridPerson) preposition adjunct(range)?",
        //what stared in Piccadilly?
        "interrogativePronoun(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
             ),
        InTransitivePPFrame,
        WHERE_WHAT_PRESENT_THING,
        forward
      )
    );
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Where does Piccadilly start?
         "interrogativePlace verb(component_do:present:singular) adjunct(domain) verb(mainVerb:present:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHERE_WHAT_PRESENT_THING,
        backward
      )
    );
      
        // IntransitivePPFrame 
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //What did Abraham Lincoln die from?
        "interrogativePronoun(range:singular) verb(component_do:past:singular) object(domain) verb(mainVerb:present:singular) preposition?",
         //What did Abraham Lincoln die from?
        "interrogativePronoun(range:singular) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?" 
      
        ),
       InTransitivePPFrame,
       PERSON_CAUSE,
        forward
      )
    );
    // IntransitivePPFrame 
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //Which person is died from X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:singular) preposition adjunct(range)?",
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:singular) preposition adjunct(range)?",
        "interrogativeDeterminer(domain:singular) verb(component_be:present:singular) verb(mainVerb:perfect:singular) preposition adjunct(range)?",
        "interrogativeDeterminer(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:singular) preposition adjunct(range)?",
        //Who is died from X?
         "interrogativePronoun(domain:singular) verb(component_be:present:singular) verb(mainVerb:perfect:singular) preposition adjunct(range)?",
         //Who was died from X?
         "interrogativePronoun(domain:plural) verb(component_be:past:singular) verb(mainVerb:perfect:singular) preposition adjunct(range)?"
          ),
        InTransitivePPFrame,
        PERSON_CAUSE,
        backward
      )
    );
      
            ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many people live in Poland?
        "interrogativeAmount(nominativeCase:range:plural) verb(component_werden:present:plural) preposition object(domain) verb(mainVerb:perfect:thridPerson)?"
    
        ),
        InTransitivePPFrame,
        HOW_MANY_THING,
        forward
      )
    );
   
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
             
        ),
        InTransitivePPFrame,
        HOW_MANY_THING,
        backward
      )
    );
      
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //Give me all Danish films.
        "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) adjective(adjectiveBaseForm)",
        //what is a Danish film.
        //"interrogativePronoun(domain:plural) verb(component_be:present:singular) adjective(adjectiveBaseForm)",
        //what are Danish film.
        "interrogativePronoun(domain:plural) verb(component_be:present:plural) adjective(adjectiveBaseForm)"
 
        ),
        AdjectivePredicateFrame,
        predicateAdjectiveBaseForm,
        forward
      )
    );
    
       
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //How high is the lighthouse in Colombo?
         //"interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) determiner(component_the) nounVariable(range:singular) preposition adjunct(domain)?",
         //temporay closed       
         //How high is the lighthouse?
         "interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) object(range)?",
         //How high is the lighthouse?
         "interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) determiner(component_the) object(range)?"
                
        /*
        //How high is the lighthouse in Colombo?
        "interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) determiner(component_the) adjunct(range)?",
        //How high is the lighthouse?
        "interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) determiner(component_the) nounVariable(range) preposition adjunct(domain)?"
        */
        ),
        AdjectiveSuperlativeFrame,
        adjectiveBaseForm,
        forward
      )
    );


    // AdjectivePPFrame...comparative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Show me all basketball players that are higher than 2 meters. 
        "verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(all) nounVariable(range:plural) determiner(that) verb(component_be:present:plural) adjective(comperative) determiner(than) object(domain)." 
        //Which mountains are higher than the object(domain?
        //"interrogativeDeterminer(domain:singular) verb(component_be:present:plural) adjective(comperative) determiner(than) determiner(the) object(domain)?"
        //"interrogativeDeterminer(range:singular) verb(component_be:past:plural) adjective(comperative) determiner(than) determiner(the) adjunct(range)?"       
        ),
        AdjectiveSuperlativeFrame,
        comperative,
        backward
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //What is the highest mountain in Australia?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular) preposition adjunct(domain)?",
          //What is the highest mountain in Australia?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) determiner(component_the) nounVariable(range:singular) preposition adjunct(domain)?",
        //What is the highest mountain in the ?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular) preposition determiner(component_the) adjunct(domain)?",
          //What is the highest mountain in the ?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) determiner(component_the) nounVariable(range:singular) preposition determiner(component_the) adjunct(domain)?"
       
        ),
        AdjectiveSuperlativeFrame,
        superlativeCountry,
        forward
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //What is the highest mountain in Australia?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular) preposition adjunct(domain)?",
          //What is the highest mountain in Australia?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) determiner(component_the) nounVariable(range:singular) preposition adjunct(domain)?",
        //What is the highest mountain in the ?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular) preposition determiner(component_the) adjunct(domain)?",
          //What is the highest mountain in the ?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) determiner(component_the) nounVariable(range:singular) preposition determiner(component_the) adjunct(domain)?"
       
        ),
        AdjectiveSuperlativeFrame,
        superlativeCountry,
        forward
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Who is the tallest player of the Atlanta Falcons?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular) preposition adjunct(domain)?",
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular) preposition determiner(component_the) adjunct(domain)?"

        ),
        AdjectiveSuperlativeFrame,
        superlativeTeamPlayer,
        forward
      )
    );
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //What is the highest mountain?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular)?",
         //What is the largest country in the world?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular) preposition determiner(component_the) adjunct(component_obj)?"
         //What is the largest country?
        //"interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) nounVariable(range:singular)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativeWorld,
        forward
      )
    );
    
      // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        "interrogativePlace verb(component_be:present:singular) adjunct(range) verb(verb_location:past:singular)?",
        "preposition interrogativeDeterminer(domain:singular) adjunct(range) verb(component_be:present:singular) verb(verb_location:past:singular)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativeCountry,
        backward
      )
    );
    
    
  }
 
}