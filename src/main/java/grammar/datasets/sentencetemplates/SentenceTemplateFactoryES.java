package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import grammar.structure.component.Language;

import java.util.List;

import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
import static grammar.datasets.sentencetemplates.TempConstants.InTransitivePPFrame;


class SentenceTemplateFactoryES  implements Factory<SentenceTemplateRepository>,TempConstants {

    private final SentenceTemplateRepository sentenceTemplateRepository;
    private final Language language;

    SentenceTemplateFactoryES() {
        this.language = Language.ES;
        this.sentenceTemplateRepository = new SentenceTemplateDataset();
    }

    public SentenceTemplateRepository init() {
        this.initByLanguage(language);
        return sentenceTemplateRepository;
    }

    private void initByLanguage(Language language) {
        init(language);
    }

    private void init(Language language) {
   //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"¿Cuál es la capital de Camerún?",
          "interrogativePronoun(range:singular) verb(component_be:present:singular) noun(singular) preposition adjunct(domain)?",
          "interrogativePronoun(range:plural) verb(component_be:present:plural) noun(plural) preposition adjunct(domain)?",
          "interrogativePronoun(range:singular) verb(component_be:past:singular) noun(singular) preposition adjunct(domain)?",
          "interrogativePronoun(range:plural) verb(component_be:past:plural) noun(plural) preposition adjunct(domain)?",
           //Wer war der Vizepräsident unter Samuel Schmid?
           "interrogativePronoun(range:plural) verb(component_be:past:plural) noun(plural) preposition adjunct(domain)?",
          
          //Gib mir die Enkel von Elvis Presley.Dame una lista de los hijos de Margaret Thatcher.
           "verb(component_imperative_transitive:present:singular) determiner(component_una) determiner(component_lista) noun(singular) preposition adjunct(domain).",
           "verb(component_imperative_transitive:present:singular) determiner(component_una) determiner(component_lista) noun(plural) preposition adjunct(domain).",
           //Dame todos los miembros de la banda Prodigy.
           "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(singular) preposition adjunct(domain).",
           "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(plural) preposition adjunct(domain).",
            //¿Qué forma de gobierno tiene Rusia?¿Qué pa�ses de gobierno lo hace ($x | Country_NP)?
           "interrogativeVariableDeterminer noun(singular) determiner(component_tiene) adjunct(domain)?",
           //"interrogativeDeterminer(range:singular) preposition  noun(singular) verb(component_do:present:singular) adjunct(domain)?"
           //¿Quién es el editor de Forbes?
           "interrogativePronounPerson(range:singular) verb(component_be:present:singular) noun(singular) preposition adjunct(domain)?",
           //¿En qué zona horaria esta Roma?
           "interrogativeDeterminerEn noun(singular) verb(component_esta:present:singular) object(domain)?",
           //¿Cuáles son los condados de Monarquía constitucional? 
           "interrogativePronounThingPlural verb(component_be:present:plural) article(los) noun(plural) preposition object(domain)?",
           "interrogativePronounThingPlural verb(component_be:present:plural) article(las) noun(plural) preposition object(domain)?",
           //¿Qué ingredientes son necesarios para una tarta de zanahorias?
            "interrogativeVariableDeterminer(range:singular) verb(component_be:present:plural) noun(singular) preposition adjunct(domain)?",
            ///Muéstrame todos los libros en la serie Fundación de Asimov
            "verb(component_imperative_show:present:singular) determiner(component_todos) noun(singular) preposition adjunct(domain).",
            ///Lista todas juegos de mesa por GMT 
            "verb(component_imperative_plural:present:plural) determiner(component_todas) noun(plural) preposition adjunct(domain)."

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
        //Quien desarrollado Skype? 
        "interrogativePronounPerson verb(mainVerb:present:thirdPerson) object(domain)?",
        "interrogativePronounPersonQuien verb(mainVerb:past:thirdPerson) object(domain)?",
        "interrogativePronounPerson verb(mainVerb:perfect:thirdPerson) object(domain)?",
        "interrogativePronounPersonQuien verb(mainVerb:perfect:thirdPerson) object(domain)?",
        "interrogativePronounPersonQuien verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronounPerson verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        //¿En qué museo está expuesto el Grito?
        "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerEn(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        //Dame todos los actores que hayan actuado en Last Action Hero.Dame todos protagonizar protagonizar ($x | Film_NP)
         //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(plural) verb(mainVerb:present:thridPerson) object(domain)"
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(singular) interrogativeRelative1 determiner(component_hayan) verb(mainVerb:present:thridPerson) object(domain)",
         //¿Qué contiene una galleta de chocolate? 
         "interrogativeVariableDeterminer verb(mainVerb:present:thirdPerson) object(domain)?",
         "interrogativeVariableDeterminer(domain:singular) verb(mainVerb:present:thirdPerson) object(domain)?"
         //¿Qué color expresa Riqueza?
         //"interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        /*"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",    
         */        
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
        //"¿Qué series televisivas ideó Walt Disney?"?"¿Qué series televisivas ideó Walt Disney?
        "interrogativeVariableDeterminer(domain:singular) verb(mainVerb:present:thridPerson) object(range)?",
        "interrogativeVariableDeterminer(domain:singular) verb(mainVerb:past:thridPerson) object(range)?",
        //Dame todas las plataformas de lanzamiento operadas por la NASA.    Dame todos operar operar por ($x | LaunchPad_NP) 
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(domain:singular) verb(mainVerb:perfect:thridPerson) preposition object(range)",
          //¿En qué conflictos militares participó Lawrence de Arabia?
         "interrogativeDeterminerEn(domain:singular) verb(mainVerb:past:thridPerson) object(range)?"
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
       //Wer moderiert die BBC Wildlife Specials?
        "interrogativePronoun(range:singular) verb(mainVerb:present:thirdPerson) object(domain)?",
        //¿En qué museo está expuesto el Grito?
        "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerEn(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(plural) verb(mainVerb:present:thridPerson) object(domain)"
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(singular) interrogativeRelative1 determiner(component_hayan) verb(mainVerb:present:thridPerson) object(domain)",
         //┬┐Quien fue el papa que fundo la televisi├│n Vaticana?
         //"interrogativePronoun(range:singular) verb(component_be:was:thirdPerson) noun(range:singular)? interrogativeRelative2 verb(mainVerb:past:thridPerson) object(domain)?"
         "interrogativePronoun(range:singular) verb(component_be:past:singular) noun(range:singular) interrogativeRelative2 verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Quién estuvo casado con el Jacques Chirac?
         "interrogativePronoun(range:singular) verb(component_estuvo:present:singular) verb(mainVerb:perfect:thridPerson) object(domain)?",
         //¿Quien actuó como el agente Smith?
         "interrogativePronounPerson verb(mainVerb:present:thridPerson) object(domain)?",
         "interrogativePronounPerson verb(mainVerb:past:thridPerson) object(domain)?"
   
                
        //Wer hat Slack entwickelt?
        /*"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",    
        ///Welche Person hat Slack entwickelt?
         "interrogativeDeterminer(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?" ,           
        //Wer hat sich Family Guy ausgedacht?"
         //"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) pronoun(reflexive_pronoun) object(domain) verb(RefVerb:perfect:thridPerson)?"
         //"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(RefVerb:perfect:thridPerson)?"
         // Trenn Verb
        "interrogativePronoun(nominativeCase:range:singular) verb(TrennVerbPart1:past:thridPerson) object(domain) verb(TrennVerbPart2:past:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) pronoun(reflexive_pronoun) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         //Gib mir alle von der NASA betriebenen Startrampen.
          "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:singular).",
          "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:plural).",
          //What is Batman"s real name?
          "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjunct(domain) Apostrophe noun(singular)?",
          "interrogativePronoun(nominativeCase:range:singular) verb(component_be:past:singular) adjunct(domain) Apostrophe noun(singular)?"
      */        
         ),
       TransitiveFrame,
        PERSON_PERSON,
        activeTransitive
      )
    );
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //¿Con quien se casó Donald Trump?
        "interrogativePronounWhom verb(component_be:present:singular) verb(mainVerb:past:thridPerson) object(domain)?"
            ),
        TransitiveFrame,
        PERSON_PERSON,
        passiveTransitive
      )
    );
    
    
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"¿Qué series televisivas ideó Walt Disney?"?"
        "interrogativePronounDeterminer(domain:singular) verb(mainVerb:past:thridPerson) object(range)?",
         //Wer moderiert die BBC Wildlife Specials?
        "interrogativePronoun(range:singular) verb(mainVerb:present:thirdPerson) object(domain)?",
        //¿En qué museo está expuesto el Grito?
        "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerEn(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(plural) verb(mainVerb:present:thridPerson) object(domain)"
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(singular) interrogativeRelative1 determiner(component_hayan) verb(mainVerb:present:thridPerson) object(domain)",
         //┬┐Quien fue el papa que fundo la televisi├│n Vaticana?
         //"interrogativePronoun(range:singular) verb(component_be:was:thirdPerson) noun(range:singular)? interrogativeRelative2 verb(mainVerb:past:thridPerson) object(domain)?"
         "interrogativePronoun(range:singular) verb(component_be:past:singular) noun(range:singular) interrogativeRelative2 verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Quién estuvo casado con el Jacques Chirac?
         "interrogativePronoun(range:singular) verb(component_estuvo:present:singular) verb(mainVerb:perfect:thridPerson) object(domain)?",
         //¿Quien actuó como el agente Smith?
         "interrogativePronounPerson verb(mainVerb:present:thridPerson) object(domain)?",
         "interrogativePronounPerson verb(mainVerb:past:thridPerson) object(domain)?"
         //¿Qué aeropuertos sirve Air China?
         //"interrogativePronounDeterminer(domain:singular) verb(mainVerb:present:thridPerson) object(domain)?"
        ),
        TransitiveFrame,
        PERSON_CAUSE,
        passiveTransitive
      )
    );
      
       // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿Cuanto costo Pulp Fiction?
        "interrogativeMuch verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeMuch verb(mainVerb:present3rd:thridPerson) object(domain)?",
        //How much did Pulp Fiction cost?
        "interrogativeMuch verb(mainVerb:past:thridPerson) object(domain)?"
              
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
        //"interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
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
        //"interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //How many ethnic groups live in Slovenia?
        //"interrogativeAmount(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?" ,
        ///¿Cuántas veces ha estado casada Jane Fonda?
        "interrogativeOften verb(component_ha:present:singular) verb(component_estado:present:singular) verb(mainVerb:past:thridPerson) adjunct(range)?"         
                
            ),
        TransitiveFrame,
        HOW_MANY_THING,
        passiveTransitive
      )
    );
    
 
     //In welchem Museum ist der Schrei ausgestellt?
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(   
         //¿En que ciudad termina la ruta Chilena 68?
         "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Por qué ciudades pasa el río Zeravshan?
         "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Dónde empieza Piccadilly?
         "interrogativePlace verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //¿Donde están situadas las Casas del Parlamento?
         "interrogativePlace2 verb(mainVerb:past:thridPerson) adjunct(domain)?",
         //¿En qué lenguaje de programación esta programado GIMP?
          "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿En qué país nace el Ganges?
         "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿De que región es el vino Melon de Bourgogne?..De que regi�n es vino ($x | Grape_NP)?
          "interrogativeDeterminerDe(range:singular) verb(component_be:present:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿En qué países se habla japonés?
         "interrogativeDeterminerEn(range:singular) verb(component_se:present:singular) verb(mainVerb:present:thridPerson) object(domain)?"

          /*"preposition interrogativeDeterminer(preposition:range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
          //In welchem Museum ist Der Schrei ausgestellt?
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:plural) verb(component_be:present:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
          // Wo liegt Fort Knox?
          "interrogativePlace verb(mainVerb:present3rd:thridPerson) adjunct(domain)?"  */   

        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        forward
      )
    );
    //Durch welches Land fließt der Rhein?
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //¿Qué ingredientes son necesarios para una tarta de zanahorias?
          "interrogativeVariableDeterminer(range:singular) verb(component_be:present:plural) verb(mainVerb:past:thridPerson) preposition object(range)?"
        /* //Was fließt durch...?
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
         */
        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        backward
      )
    );
      
       //In welchem Museum ist der Schrei ausgestellt?
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(   
         //¿Que tipo de uva crece en Oregon?
         "interrogativeVariableDeterminer(range:singular) verb(mainVerb:past:thridPerson) preposition object(domain)?"
       
        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2,
        forward
      )
    );
    //Durch welches Land fließt der Rhein?
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        /* //Was fließt durch...?
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
       */
        ),
        InTransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2,
        backward
      )
    );
      
      
      
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
       //¿En que ciudad termina la ruta Chilena 68?
         "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Por qué ciudades pasa el río Zeravshan?
         "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Dónde empieza Piccadilly?
          "interrogativePlace verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //¿En qué país se encuentra el lago Limerick?
           "interrogativeDeterminerEn(range:singular) verb(component_se:present:singular) verb(mainVerb:past:thridPerson) object(domain)?",
          //¿En qué estado american se encuentra Fort Knox?
          //En cual Nosotros estado es Zona 51 ¿situado? 
          "interrogativeDeterminerEnOnly interrogativeDeterminerCual(range:singular) verb(component_es:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?"
   
                ),
        InTransitivePPFrame,
        WHAT_WHICH_LOCATION,
        forward
      )
    );
    
    
    
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
            //Was fließt durch...?
       /* "interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Welcher Fluss fließt durch...?
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Trenn
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(range) verb(TrennVerbPart2:past:thridPerson)? "
          */  ),
        InTransitivePPFrame,
        WHAT_WHICH_LOCATION,
        backward
      )
    );
      
      ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
          //"¿Cuando se completo el Titanic?
          "interrogativeTemporal verb(component_se:present:singular) verb(mainVerb:present:thridPerson) object(domain)?",
          "interrogativeTemporal verb(component_se:present:singular) verb(mainVerb:past:thridPerson) object(domain)?"
          //┬┐Cu├índo tuvo lugar la batalla de Gettysburg?
          //"interrogativeTemporal verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //¿Quién es el anfitrión de la American Idol?
          //"interrogativePronounPerson verb(component_be:present:singular) verb(mainVerb:past:thridPerson) object(domain)?"
        ),
        InTransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        forward
      )
    );
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         /*//Was wurde 2010 fertiggestellt?
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
        */),
        InTransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        backward
      )
    );
    ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
          //"¿En qué ciudad murió John F. Kennedy?",
         "interrogativeDeterminerEn(range:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         //"¿Cuándo murió la princesa Diana?
         "interrogativeTemporal verb(mainVerb:past:thridPerson) adjunct(domain)?",
         //¿Cúando se fundó Jack Wolfskin?
          "interrogativeTemporal verb(component_se:present:singular) verb(mainVerb:present:thridPerson) adjunct(domain)?",
          "interrogativeTemporal verb(component_se:present:singular) verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //¿En que año nació Rachel Stevens?
          "interrogativeDeterminerEn(range:singular) verb(mainVerb:perfect:thridPerson) adjunct(domain)?",
          "interrogativeDeterminerEn(range:plural) verb(mainVerb:perfect:thridPerson) adjunct(domain)?"
        // "preposition interrogativeDeterminer(dativeCase:range:singular) verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         //Wann wurde Abrham Lincon geboren?
        //"interrogativeTemporal verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
        //Wann ist Draculas Sch├Âpfer gestorben?
        //"interrogativeTemporal verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
        /* "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerEn(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",*/
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
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"
        ),
        InTransitivePPFrame,
        WHEN_WHO_PAST_PERSON,
        backward
      )
    );
      
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿En qué ciudad murió John F. Kennedy?
         "interrogativeDeterminerEn(domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         "interrogativeDeterminerEn(domain:plural) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
         
        ),
        InTransitivePPFrame,
        WHO_WHO_PERSON,
        forward
      )
    );
      
      
      
          ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
          //¿Qué surfistas profesionales nacieron en las Filipinas?
         "interrogativeVariableDeterminer(range:singular) verb(mainVerb:present:thridPerson) preposition adjunct(domain)?",   
         "interrogativeVariableDeterminer(range:plural) verb(mainVerb:present:thridPerson) preposition adjunct(domain)?",
         //¿Dónde está enterrado Syngman Rhee?
        "interrogativePlace verb(component_be:past:singular) verb(mainVerb:past:thridPerson) adjunct(domain)?",
         //¿Dónde nació Bach?
         "interrogativePlace verb(mainVerb:past:thridPerson) adjunct(domain)?",
         //Cual gente fueron nacido en Heraklion? 
         "interrogativeDeterminerCual(range:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(domain)?",
         "interrogativeDeterminerCual(range:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(domain)?"
                   ),
        InTransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
    
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(     
          //¿En qué ciudad murió John F. Kennedy?
        "interrogativeDeterminerEn(domain:singular) verb(mainVerb:past:thridPerson) adjunct(range)?",
        "interrogativeDeterminerEn(domain:singular) verb(mainVerb:present:thridPerson) adjunct(range)?",
        //¿Qué surfistas profesionales nacieron en las Filipinas?
        "interrogativeVariableDeterminer noun(domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
        "interrogativeVariableDeterminer noun(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
        //Dónde hizo Abrahán Lincoln el? 
        "interrogativePlace verb(component_do:past:singular) adjunct(range) article(component_el)?"
            ),
        InTransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        backward
      )
    );
      
   
      
              ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿De qué murió Abraham Lincoln?
        "interrogativeCause verb(mainVerb:past:thridPerson) adjunct(domain)?",
        //¿Quién era llamado Frank The Tank?
        "interrogativePronounPerson verb(component_be:present:singular) verb(mainVerb:past:thridPerson) adjunct(domain)?"
        //An was ist Bruce Carver gestorben?
        //"preposition interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"     
               ),
        InTransitivePPFrame,
        PERSON_CAUSE,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //"Wer ist an Malaria gestorben?
        //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
        //"Wer war an Malaria gestorben?
        //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"
      
          ),
        InTransitivePPFrame,
        PERSON_CAUSE,
        backward
      )
    );
      
      
      
      
             ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
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
      sentenceTemplateRepository.add(createSentenceTemplate(language,
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
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //¿Cuántas personas viven en Polonia?
          "interrogativeAmount(range:plural) verb(mainVerb:present:thridPerson) preposition object(domain)?"  
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
   
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
        //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:perfect:present) object(range)?",
        //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      
        //¿Que ciudad tiene la mayor población?
           
        ),
        InTransitivePPFrame,
        HOW_MANY_TOTAL,
        backward
      )
    );
      
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //Dame todas las películas danesas.
        "verb(component_imperative_transitive:present:singular) determiner(component_todas) article(las) object(domain) adjective(adjectiveBaseForm).",
        "verb(component_imperative_transitive:present:singular) determiner(component_todas) article(los) object(domain) adjective(adjectiveBaseForm)."
        //what is a Danish film.
        //"interrogativePronoun(domain:plural) verb(component_be:present:singular) adjective(adjectiveBaseForm)",
        //what are Danish film.
        //"interrogativePronoun(domain:plural) verb(component_be:present:plural) adjective(adjectiveBaseForm)"
 
        ),
        AdjectivePredicateFrame,
        predicateAdjectiveBaseForm,
        forward
      )
    );
    
      // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿Cuál es la montaña más alta de Australia? 
        "interrogativePronounThing verb(component_be:present:singular) noun(range:singular) adjective(superlative) preposition adjunct(domain)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativeCountry,
        forward
      )
    );
    
        // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿Quién es el jugador mas jóven de dardos?
       "interrogativePronounPerson verb(component_be:present:singular) adjective(superlative) noun(range:singular)?",
        //Quien es el jugador de baloncesto mas alto?
       "interrogativePronounPerson verb(component_be:present:singular) noun(range:singular) adjective(superlative)?"
           
        ),
        AdjectiveSuperlativeFrame,
        superlativeWorld,
        forward
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿Cuál es el hijo mas mayor de Meryl Streep? ¿Cuál es m�s viejo ni�o of ($x | Person_NP)?
        "interrogativePronounThing verb(component_be:present:singular) noun(range:singular) adjective(superlative) preposition adjunct(domain)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativePerson,
        forward
      )
    );
    
         // AdjectivePPFrame...adjectiveBaseForm
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //¿Cómo de alta el faro de Colombo? How es adjectiveBaseForm XX faro?
         "interrogativeEvalution verb(component_be:present:singular) adjective(adjectiveBaseForm)  noun(range:singular) preposition adjunct(range)?",
         "interrogativeEvalution verb(component_be:present:singular) adjective(adjectiveBaseForm) noun(range:singular) preposition adjunct(range)?",
        //¿Cómo de alta es Claudia Schiffer?"¿Cómo adjectiveBaseForm es ($x | THING_NP)?
        "interrogativeEvalution preposition adjective(adjectiveBaseForm) verb(component_be:present:singular) adjunct(range)?",
        //¿Cómo de alta el faro de Colombo?
         "interrogativeEvalution preposition preposition adjective(adjectiveBaseForm) noun(range:singular) adjunct(range)?",
         //¿Cómo de alta el la torre Yokohama Marine?
          "interrogativeEvalution preposition preposition adjective(adjectiveBaseForm) adjunct(range)?"
      ),
        AdjectiveSuperlativeFrame,
        adjectiveBaseForm,
        forward
      )
    );
    
      
            ///////////////////////////////
       sentenceTemplateRepository.add(createSentenceTemplate(language,
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
   
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
             
        ),
        InTransitivePPFrame,
        HOW_MANY_THING,
        backward
      )
    );
      
      
   }


}