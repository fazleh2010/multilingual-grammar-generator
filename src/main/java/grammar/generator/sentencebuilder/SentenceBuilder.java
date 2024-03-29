package grammar.generator;

import java.util.Collection;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.List;

public interface SentenceBuilder {
  List<String> generateFullSentencesForward(String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;

  List<String> generateFullSentencesBackward(String bindingVar,  LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;

  List<String> generateNounPhrase(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;

  List<String> generateBackwardAmount(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;
  
  List<String> generateForwardAmount(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil);
  
  List<String> generateBooleanSentences(String bindingVariable,  LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;

  List<String> generateBooleanQuestionsDomain(String bindingVariable, LexicalEntryUtil lexicalEntryUtil)  throws QueGGMissingFactoryClassException;
  
   public String  getTemplate();

   

}
