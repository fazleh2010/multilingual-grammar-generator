# Automatic Grammar Generation (QueGG)

This command-line tool is designed for automatic grammar generation.

### Resource: lexicon and grammar

| Language      |                |       |      |      |      | 
| :------------ |:---------------| :-----|:-----|:-----|:-----|
| English       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/lemon)|[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/evaluation)|
| German        |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/lemon) |[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/evaluation)|
| Italian       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/lemon) |[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/evaluation)|
| Spanish       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/lemon)|[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/evaluation)|


## Compile And Run
<p>The source code can be compiled and run using <em>Java 11</em> and <em>Maven</em>.</p>

```shell script
git clone https://github.com/fazleh2010/question-grammar-generator.git 
```
build the jar file
```shell script
mvn clean install
mvn clean package
```
The file (inputConf.json) contains input parameter for the system:
- languageCode: `en` (English), `de` (German), `it` (Italian)
- inputDir: The input directory that contains lemon csv files  that will be processed by QueGG. 
- outputDir: The output directory for the json grammar entry files that are produced by QueGG.
- qaldDir: The directory contains qald questions
- numberOfEntities: The number of entities in binding list. 
- similarityThresold: The thresold for similary check between questions for evalution.
- csvToTurtle: the indicator for generating lemon from turtle file.
- turtleToProtoType: the indicator for generating lemon from turtle file.
- protoTypeToQuestion: the indicator for generating questions from prototype questions.
- evalution: Mark true if we want evalution against qald
````input configuration file
{
  "languageCode" : "de",
  "inputDir" : "lexicon",
  "outputDir" : "output",
  "qaldDir" : "qald",
  "numberOfEntities" : 10,
  "similarityThresold" : 80.0,
  "csvToTurtle" : true,
  "turtleToProtoType" : true,
  "protoTypeToQuestion" : true,
  "evalution" : true
}

````
The file (dbpedia.json) contains the input configuration for linked data.
- endpoint: the sparql endpoint of the linked data.
- prefix: the prefixes of Uris.

````
{
  "endpoint" : "https://dbpedia.org/sparql",
  "prefix" : {
    "dbc" : "http://dbpedia.org/resource/Category:",
    "dbo" : "http://dbpedia.org/ontology/",
    "dbp" : "http://dbpedia.org/property/"
...
}
   
````


Run the system:
````shell script
java -jar <jar file> <input_configuration_file> <linked_data_configuration_file>

german
java -jar target/QuestionGrammarGenerator.jar inputConf_de.json dataset/dbpedia_de.json   

spanish
java -jar target/QuestionGrammarGenerator.jar inputConf_es.json dataset/dbpedia_es.json        

italain
java -jar target/QuestionGrammarGenerator.jar inputConf_it.json dataset/dbpedia_it.json  

english and dbpedia
java -jar target/QuestionGrammarGenerator.jar inputConf_en.json dataset/dbpedia_en.json 

english and aifd
java -jar target/QuestionGrammarGenerator.jar inputConf_en.json dataset/aifd.json 
                                 
````  








