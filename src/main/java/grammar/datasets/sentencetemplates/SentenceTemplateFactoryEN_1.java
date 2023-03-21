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
import static grammar.datasets.sentencetemplates.TempConstants.IntransitivePPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.NounPPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.PERSON_CAUSE;
import static grammar.datasets.sentencetemplates.TempConstants.TransitiveFrame;
import static grammar.datasets.sentencetemplates.TempConstants.WHEN_WHAT_PAST_THING;
import static grammar.datasets.sentencetemplates.TempConstants.WHEN_WHO_PAST_PERSON;
import static grammar.datasets.sentencetemplates.TempConstants.WHERE_WHO_PAST_PERSON;
import static grammar.datasets.sentencetemplates.TempConstants.backward;
import static grammar.datasets.sentencetemplates.TempConstants.booleanQuestionDomain;
import static grammar.datasets.sentencetemplates.TempConstants.booleanQuestionDomainRange;
import static grammar.datasets.sentencetemplates.TempConstants.forward;
import static grammar.datasets.sentencetemplates.TempConstants.location;
import static grammar.datasets.sentencetemplates.TempConstants.noun;
import static grammar.structure.component.FrameType.APP;
import static grammar.datasets.sentencetemplates.TempConstants.Prepositional_Adjuct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static grammar.datasets.sentencetemplates.TempConstants.NOUN_PHRASE;


public class SentenceTemplateFactoryEN_1 implements Factory<SentenceTemplateRepository>,TempConstants{

  private final SentenceTemplateRepository sentenceTemplateRepository;
  private final Language language;
  
  public static List<String> nounPPTemplates=new ArrayList<String>(Arrays.asList(Prepositional_Adjuct_What,Prepositional_Adjuct_Who,HOW_MANY_THING,NOUN_PHRASE));

  
  public SentenceTemplateFactoryEN_1() {
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
          //What is the capital of Cameron?
          "What is the (reference:singular) preposition Variable"+"\n"+
          "What are the (reference:plural) preposition Variable"+"\n"+
          "What was the (reference:singular) preposition Variable"+"\n"+
          "What were the (reference:plural) preposition Variable"+"\n"+
          //which city is the capital of Cameron?
          "which (range:singular) is the (reference:singular) preposition Variable"+"\n"+
          "which (range:plural) are the (reference:plural) preposition Variable"+"\n"+
          "which (range:singular) was the (reference:singular) preposition Variable"+"\n"+
          "which (range:plural) are the (reference:plural) preposition Variable"+"\n"+        
           //List all the musicals von Elton John.
          "List all the (reference:plural) preposition Variable" +"\n"+
          //List all the musicals with music by Elton John.
          //"verb(imperative_verb:present:plural) determiner(all) determiner(component_the) noun(range:plural) noun(singular) preposition adjunct(domain)." 
          //Give me all members of Prodigy.
          "Give me all (reference:plural) preposition Variable"+"\n"+ 
          //Show me all books in Asimov's Foundation series.
          "Show me all (reference:plural) preposition Variable" 
        ),
        NounPPFrame,
        THING_SENTENCE
      )
    );
     
      //NounPPFrame
     sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
           //"Who is the mayor of Paris?",
          "Who is the (reference:singular) preposition Variable"+"\n"+ 
          "Who was the (reference:singular) preposition Variable"+"\n"+   
          //"Who was Samuel Schmid's vice president?", 
          "Who was Variable's (reference:singular)?"+"\n"+ 
           //List all the musicals von Elton John.
          "List all the (reference:plural) preposition Variable" +"\n"+
          //Give me all members of Prodigy.
          "Give me all reference:plural preposition Variable"+"\n"+ 
          //Show me all books in Asimov's Foundation series.
          "Show me all (reference:plural) preposition Variable" 
        ),
        NounPPFrame,
        PERSON_SENTENCE
      )
    );
    
    /* sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
           //"<NPrange,dbo:mayor> is the mayor of which city? | <NPrange,dbo:mayor> was the mayor of which city?
          "subject(range) verb(component_be:present:singular) determiner(component_the) noun(reference:singular) preposition interrogativeDeterminer(domain:singular)", 
          "subject(range) verb(component_be:past:singular) determiner(component_the) noun(reference:singular) preposition interrogativeDeterminer(domain:singular)"  

                ),
        NounPPFrame,
        Copulative_Subject
      )
    );*/
    
        sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"How many [NounPlural] does <NPmap(prepositionalAdjunct),Property> have?
        "How many (reference:singular) does Variable have"         
              ),
        NounPPFrame,
        HOW_MANY_THING
      )
    );
    
    
    
    //NounPPFrame
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
          //the capital of germany
         "the (reference:singular) preposition Variable?"+"\n"+
         "the (reference:plural) preposition Variable?"
         //the Dracula's creator
         //"the Variable's (reference:singular)?"
                
                ),
        NounPPFrame,
        NOUN_PHRASE
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
         //Who presents BBC Wildlife Specials?
        "Who (3rd-present) Variable?"+"\n"+
        //Who presented BBC Wildlife Specials?
        "Who (past) Variable?"+"\n"+
        //Which person presented BBC Wildlife Specials?
        //"which river crosses Brooklyn Bridge?"
        "which (range:singular) (3rd-present) Variable?"+"\n"+
        "which (range:singular) (past) Variable?"+"\n"+
        "which (range:plural) (infinitive) Variable?"+"\n"+
        "which (range:plural) (past) Variable?"+"\n"+
        //Which river does the Brooklyn Bridge cross?
        "Which (range:singular) does Variable (infinitive)?"+"\n"+
        //"interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:thridPerson)?",
        //Which books did Muhammad Ali write?   //"Which films did Stanley Kubrick direct?"
        "Which (range:plural) did Variable (infinitive)?"+"\n"+          
         //Give me all actors starring in X.
         //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
        //List all actors starring in X.
        //"verb(imperative_verb:present:plural) determiner(all) noun(range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
        //Who was the pope that founded the Vatican Television?
        "Who was the (range:singular) that (past) Variable?"
        ),
       TransitiveFrame,
       PERSON_CAUSE_FORWARD
      )
    );
    // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //"What was developed by X?
        "What was (perfect) preposition Variable?"+"\n"+
        //"What were developed by X?
        "What were (perfect) preposition Variable?" +"\n"+
         //Which movie did Kurosawa direct?
         "Which (domain:singular) did Variable (infinitive)?"+"\n"+
         //Which movies did Kurosawa direct?
         "Which (domain:plural) did Variable (infinitive)?"+"\n"+
         //Give me all video games published by Mean Hamster Software.
         "Give me all (domain:plural) (perfect) preposition Variable?"+"\n"+
         //List all video games published by Mean Hamster Software.
         "List all (domain:plural) (perfect) preposition Variable?"+"\n"+
         //"Which books were developed by X?
        "Which (domain:plural) were (perfect) preposition Variable?"+"\n"+
        //"Which books were developed by X?
        "Which (domain:singular) was (perfect) preposition Variable?"+"\n"+
        //"Which books were developed by X?
         //interrogativeVariableDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Show me the book that Muhammad Ali wrote.
         "Show me the (domain:singular) that Variable (past)." 
         //Show me the books that Muhammad Ali wrote.
         //"verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(all) noun(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson)." 

        ),
        TransitiveFrame,
       PERSON_CAUSE_BACKWARD)
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
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(domain:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //List all video games published by Mean Hamster Software.
         "verb(imperative_verb:present:plural) determiner(all) noun(domain:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which books were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"Which books were developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //"Which (X_Book) were developed by X?
        //interrogativeVariableDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //Show me the book that Muhammad Ali wrote.
         "verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(component_the) noun(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson)." 
         //Show me the books that Muhammad Ali wrote.
         //"verb(component_imperative_show:present:singular) pronoun(pronoun_personal) determiner(all) noun(domain:singular) determiner(that) object(range) verb(mainVerb:past:thridPerson)." 

        ),
        TransitiveFrame,
        PERSON_CAUSE_NOUN_PHRASE,
        passiveTransitive
      )
    );
    
      // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //Who produce hovercrafts?
        "interrogativePronoun(range) verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronoun(range) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Which companies produce hovercrafts?
        "interrogativeDeterminer(domain:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminer(domain:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminer(domain:plural) verb(mainVerb:past:thridPerson) object(domain)?"
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
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(range:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //List all video games published by Mean Hamster Software.
         "verb(imperative_verb:present:plural) determiner(all) noun(range:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
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
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(domain:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
        //List all actors starring in X.
        "verb(imperative_verb:present:plural) determiner(all) noun(domain:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?"
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
         "verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(range:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
         //List all video games published by Mean Hamster Software.
         "verb(imperative_verb:present:plural) determiner(all) noun(range:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
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
        "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) noun(range:singular) determiner(that) verb(mainVerb:past:thirdPerson) object(domain)?"
    
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
         "interrogativePronoun(domain) verb(component_be:past:singular) adjunct(range) verb(mainVerb:perfect:thridPerson) preposition?"
        ),
        TransitiveFrame,
        PERSON_PERSON,
        passiveTransitive
      )
    );
    
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How much does Pulp Fiction cost?
        "How much does Variable (infinitive)?"+"\n"+
        //How much did Pulp Fiction cost?
        "How much did Variable (infinitive)?"
        ),
       TransitiveFrame,
       HOW_MANY_TOTAL_FORWARD)
    );
    
     // TransitiveFrame passive amount
    /*sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //How many ethnic groups live in Slovenia? this should be on aktive side.
        "How many (range:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?"          
            ),
        TransitiveFrame,
        HOW_MANY_THING_AKTIVE
      )
    );
    
     // TransitiveFrame passive amount
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
         ///How often did Jane Fonda marry?
        "How often did Variable(range) verb(mainVerb:present:thridPerson)?"             
            ),
        TransitiveFrame,
        HOW_MANY_THING_PASSIVE
      )
    );*/
   
    // TransitiveFrame passive amount
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //How many ethnic groups live in Slovenia? this should be on aktive side.
        //"interrogativeAmount(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?" ,
        ///How often did Jane Fonda marry?
        "interrogativeOften(domain:singular) verb(component_do:past:singular) adjunct(range) verb(mainVerb:present:thridPerson)?"         
                
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
        "How many (range:singular) (infinitive) preposition "+Variable+"?"+"\n"+
        //How many people lived in Poland?
        "How many (past) preposition "+Variable+"?"+"\n"+
         //How many seats does (X_Statdium) have?        
        "How many(range:singular) does "+Variable+" (infinitive)?"
      
           ),
       IntransitivePPFrame,
       HOW_MANY_TOTAL_FORWARD      
       )
    );
    
     // InTransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Which flim costed X?
        //"interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
           ),
       IntransitivePPFrame,
       HOW_MANY_TOTAL_BACKWARD
      )
    );
    

    
     sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        //Which country does X flow through?
        "Which (range:singular) does "+Variable+" (infinitive) preposition?"+"\n"+
        "Which (range:plural) does "+Variable+" (infinitive) preposition?"+"\n"+
         //"In which city does the Chile Route 68 end?
        "preposition which (range:singular) does "+Variable+" (infinitive)?"+"\n"+
        "preposition which (range:plural) does "+Variable+" (infinitive)?"+"\n"+
         //In which programming language is GIMP written?
        "preposition which (range:singular) is "+Variable+" (perfect)?"+"\n"+
        "preposition which (range:plural) are "+Variable+" (perfect)?"+"\n"+
         //Where is Fort Knox located?
         "Where is Variable (perfect)?"
         //In which countries do people speak Japanese?
         //"preposition interrogativeDeterminer(range:plural) verb(component_do:present:singular) verb(mainVerb:present:singular) object(domain)?"
          ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1_FORWARD
      )
    );
     
     
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
          //what flows through Germany?
          "what (3rd-present) preposition "+Variable+"?"+"\n"+
          //what does X stand for?
          "what does "+Variable+" (infinitive) preposition?"+"\n"+
          //Which river flows through Germany?
          "Which (domain:singular) (3rd-present) preposition "+Variable+"?"+"\n"+
          //what flew through Germany?
          "what (past) preposition "+Variable+"?"+"\n"+
          //Which rivers flow through Germany?
          "Which (domain:plural) (infinitive) preposition "+Variable+"?"+"\n"+
          //Give me all launch pads operated by NASA.
           //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) object(range) verb(mainVerb:present:thridPerson) preposition adjunct(domain)."
           //Where does Piccadilly start? 
           "where does "+Variable+" (infinitive)?"
        ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1_BACKWARD)
    );
      
        sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        // How many rivers flow through <NPrange,dbo:city>?
        "interrogativeAmount(domain:plural) verb(mainVerb:present:singular) preposition adjunct(range)?"  
        ),
        IntransitivePPFrame,
        HOW_MANY_THING_BACKWARD      )
    );
    
      //lst working
     sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        //In which country is the Limerick Lake?
        "preposition which (range:singular) is the "+Variable+"?"+"\n"+
        "preposition which (range:singular) are the "+Variable+"?"+"\n"+
        //What country is Mount Everest in?
         "What (range:singular) is "+Variable+" preposition?"+"\n"+
        //In which programming language is GIMP written?
        "preposition which (range:plural) is "+Variable+" (perfect)?"+"\n"+
        "preposition which (range:singular) are "+Variable+" (perfect)?"+"\n"+
         //Where is Fort Knox located?
         "Where is "+Variable+" (perfect)?"
                
                ),
        IntransitivePPFrame,
        WHAT_WHICH_LOCATION_FORWARD
      )
    );
     
    /*sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
        //In which country is the Limerick Lake?
        "preposition which (range:singular) is the "+Variable+" "+"(perfect)"+"?" +"\n"+
        "preposition which (range:singular) are the "+Variable+" "+"(perfect)"+"?" +"\n"+
        //What country is Mount Everest in?
         "What (range:singular) is "+Variable+" "+"(perfect)"+" preposition?"+"\n"+
        //In which programming language is GIMP written?
        "preposition which (range:plural) is "+Variable+" (perfect)?"+"\n"+
        "preposition which (range:singular) are "+Variable+" (perfect)?"+"\n"+
         //Where is Fort Knox located?
         "Where is "+Variable+" (perfect)?"
                
                ),
        IntransitivePPFrame,
        WHAT_WHICH_LOCATION_FORWARD
      )
    );*/
    
    
    
    
    
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
          //what flows through Germany?
          "what (3rd-present) preposition "+Variable+"?"+"\n"+
          //what does X stand for?
          "interrogativePronoun(domain) (component_do:present:singular) "+Variable+" (infinitive) preposition?",
          //Which river flows through Germany?
          "interrogativeDeterminer(domain:singular) (infinitive) preposition "+Variable+"?",
          //what flew through Germany?
          "interrogativePronoun(domain) (past) preposition "+Variable+"?",
          //Which rivers flow through Germany?
          "interrogativeDeterminer(domain:plural) (infinitive) preposition "+Variable+"?"             
            ),
        IntransitivePPFrame,
        WHAT_WHICH_LOCATION_BACKWARD
      )
    );
      
      
      
      
      
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(    
         //Which types of grapes grow in Oregon?
         "What (range:singular) (3rd-present) preposition "+Variable+"?"+"\n"+ 
         //Which types of grapes grow in Oregon?
         "Which (range:plural) (infinitive) preposition "+Variable+"?"+"\n"+ 
         //What types of grapes grows in Oregon?
         "what (range:plural) (infinitive) preposition "+Variable+"?"+"\n"+ 
          //Which types of grapes grow in Oregon?
         "Which (range:singular) (past) preposition "+Variable+"?"+"\n"+ 
         //Which types of grapes grow in Oregon?
         "Which (range:plural) (past) preposition "+Variable+"?"+"\n"+ 
         //What types of grapes grows in Oregon?
         "What (range:plural) (past) preposition "+Variable+"?"
               ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2_FORWARD
      )
    );
    
    
    
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //"In which city does the Chile Route 68 end?
        "preposition which (domain:singular) does "+Variable+" (infinitive)?"+"\n"+
        "preposition which (domain:plural) does "+Variable+" (infinitive)?"+"\n"+
         //In which programming language is GIMP written?
        "preposition which (domain:plural) is "+Variable+" (perfect)?"+"\n"+
        "preposition which (domain:singular) was "+Variable+" (perfect)?"
                 ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2_BACKWARD      
      )
    );
      ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //When was X completed?
          "When was "+Variable+" (past)?"+"\n"+
           //When were X completed?
          "When were "+Variable+" (past)?"+"\n"+
          //When did Operation Overlord commence?
          "When did "+Variable+" (infinitive)?"
        ),
        IntransitivePPFrame,
        WHEN_WHAT_PAST_THING_FORWARD      
        )
    );
       
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //what took place in Date?  
        //"interrogativePronoun(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
         ////what happened in Date?
        //"interrogativePronoun(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
              ),
        IntransitivePPFrame,
        WHEN_WHAT_PAST_BACKWARD
      )
    );
       
        // TransitiveFrame passive amount
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?"  
            ),
        IntransitivePPFrame,
        HOW_MANY_THING,
        backward
      )
    );
    
    
    ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //When was X completed?
         "When was "+Variable+" (past)?"+"\n"+
         //In which year was Joanna MacGregor born?
         "preposition which year was "+Variable+" (past)?"+"\n"+
         //In which year was Joanna MacGregor born?
         "preposition which date was "+Variable+" (past)?"+"\n"+
         //When did Elisabeth of Saxony die?
         "When did "+Variable+" (infinitive)?"
        ),
        "IntransitivePPFrame",
        WHEN_WHO_PAST_PERSON_FORWARD
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
           ),
        "IntransitivePPFrame",
        WHEN_WHO_PAST_PERSON      
      )
    );
      
       ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //Where was Bach born? and Where was Sparkling wine produced?
         "Where was "+Variable+" (perfect)?"+"\n"+
         //Where is Sparkling wine produced?
         "Where was "+Variable+" (perfect)?"+"\n"+
         /* //Where in France is sparkling wine produced?
         "interrogativePlace preposition noun(range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:singular)?", 
          //Where in France is sparkling wine produced?
         "interrogativePlace preposition noun(range:singular) verb(component_be:past:singular) adjunct(domain) (mainVerb:perfect:singular)?", 
         */
         //In which city did John F. Kennedy die?
         "preposition which (range:singular) did "+Variable+" (infinitive)?"+"\n"+
          //Which country was Bill Gates born in?
         "which (range:singular) was "+Variable+" (infinitive) preposition?"+"\n"+
          //Which country does X come from?
         "which (range:singular) does "+Variable+" (infinitive) preposition?"
          
             ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON_FORWARD
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //Who was born in X?
         "Who (domain:singular) was (perfect) preposition "+Variable+"?"+"\n"+
         //Which actors were born on the Philippines?
         "Which (domain:singular) was (perfect) preposition "+Variable+"?"+"\n"+
         //Which actors were born on the Philippines?
         "Which (domain:plural) were (perfect) preposition "+Variable+"?"

        ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON_BACKWARD
      )
    );
      
      
          ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //"Welche Profisurfer wurden auf den Philippinen geboren?",
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"

             ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //Wo wurde Donald Trump geboren?
         //"interrogativePlace(nominativeCase:singular) verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
        ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        backward
      )
    );
      
            ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //what stars in Piccadilly?
        "interrogativePronoun(domain:singular) verb(mainVerb:present3rd:thridPerson) preposition adjunct(range)?",
        //what stared in Piccadilly?
        "interrogativePronoun(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
             ),
        IntransitivePPFrame,
        WHERE_WHAT_PRESENT_THING,
        forward
      )
    );
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //Where does Piccadilly start?
         "interrogativePlace verb(component_do:present:singular) adjunct(domain) verb(mainVerb:present:thridPerson)?"
        ),
        IntransitivePPFrame,
        WHERE_WHAT_PRESENT_THING,
        backward
      )
    );
      
        // IntransitivePPFrame 
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //What did Abraham Lincoln die from?
        "What did "+Variable+" (infinitive) preposition?"+"\n"+         
        //What did Abraham Lincoln die from?
        "What does "+Variable+" (infinitive) preposition?" 
      
        ),
       IntransitivePPFrame,
       PERSON_CAUSE_FORWARD      
       )
    );
    // IntransitivePPFrame 
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //Which person is died from X?
        "Which (domain:singular) is (perfect) preposition "+Variable+"?"+"\n"+  
        "Which (domain:plural) are (perfect) preposition "+Variable+"?"+"\n"+  
        "Which (domain:singular) was (perfect) preposition "+Variable+"?"+"\n"+  
        "Which (domain:plural) were (perfect) preposition "+Variable+"?"+"\n"+  
        //Who is died from X?
         "Who is (perfect) preposition "+Variable+"?"+"\n"+  
         //Who was died from X?
         "Who is (perfect) preposition "+Variable+"?"
          ),
        IntransitivePPFrame,
        PERSON_CAUSE_BACKWARD
      )
    );
      
            ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //How many people live in Poland?
        "How many (range:plural) preposition "+Variable+" preposition (perfect)?"
    
        ),
        IntransitivePPFrame,
        HOW_MANY_THING_FORWARD      
       )
    );
   
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
             
        ),
        IntransitivePPFrame,
        HOW_MANY_THING_BACKWARD     
      )
    );
      
    
    
       
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //How high is the lighthouse in Colombo?
         //"interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) determiner(component_the) noun(range:singular) preposition adjunct(domain)?",
         //temporay closed       
         //How high is the lighthouse?
         "interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) object(range)?"
        
                
        /*
        //How high is the lighthouse in Colombo?
        "interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) determiner(component_the) adjunct(range)?",
        //How high is the lighthouse?
        "interrogativeEvalution adjective(adjectiveBaseForm) verb(component_be:present:singular) determiner(component_the) noun(range) preposition adjunct(domain)?"
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
         
        ),
        AdjectiveSuperlativeFrame,
        comperative
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //What is the highest mountain in Australia?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) noun(range:singular) preposition adjunct(domain)?"
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
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) noun(range:singular) preposition adjunct(domain)?",
        //What is the highest mountain in Australia?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativePerson,
        forward
      )
    );
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //What is the largest country in the world?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) noun(range:singular) preposition determiner(component_the) adjunct(component_obj)?"
         //What is the largest country?
        //"interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) noun(range:singular)?"
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
  
   /*//Where in France is sparkling wine produced?
     "interrogativePlace preposition noun(range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:singular)?", 
     //Where in France was sparkling wine produced?
     "interrogativePlace preposition noun(range:singular) verb(component_be:past:singular) adjunct(domain) verb(mainVerb:perfect:singular)?" 
     */  

    public SentenceTemplateRepository getSentenceTemplateRepository() {
        return sentenceTemplateRepository;
    }
          
}