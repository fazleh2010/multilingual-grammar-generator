# QueGG
The project creates QA system given a lemon lexica or Csv file (contains information Syntactic Frame such NounPPframe, TransitiveFrame, etc. )

## Compile And Run
<p>The source code can be compiled and run using <em>Java 11</em> and <em>Maven</em>.</p>

```shell script
git clone https://github.com/fazleh2010/question-grammar-generator.git 
```
build the jar file
```shell script
mvn clean install
mvn clean package

inputConf.json: The file contains input parameter for the system
```
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
dbpedia.json: The input configuration for linked data.
```
- endpoint: the sparql endpoint of the linked data.
- prefix: the prefixes of Uris.
````input configuration file
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

### results

| Language      | Lexical Entries        | Sentene Templates | Grammar | Questions | Browser |
| :------------ |:---------------| :-----|:-----|:-----|:-----|
| English       |[806 entries](https://docs.google.com/spreadsheets/d/1GnAGi3WE-qcx-YFTnCgNHBPgQQhR96FNfRfVvP1r0JA/edit?usp=sharing)| [141 templates](https://www.google.com)|[1727 rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/grammar)|[189015434](https://www.google.com)|[en_QaSystem](https://github.com/fazleh2010/QueGG-web-en.git)|
| German        |[404 entries](https://docs.google.com/spreadsheets/d/1jbmbmOuzwxf2g4G0olHfeozo3v2M85JyZFIZYJ-f9yE/edit?usp=sharing)| [160 templates](https://www.google.com) |[906 rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/grammar)|[111832454](https://www.google.com)|[de_QaSystem](https://www.google.com)|
| Italian       |[441 entries](https://docs.google.com/spreadsheets/d/1Lv_VUXG-0k5l-BRxo9GZ0AmcgSsUb2IeLnVR20qNu7k/edit?usp=sharing)| [48 templates](https://www.google.com) |[1042 rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/grammar)|[7522466](https://www.google.com)|[it_QaSystem](https://www.google.com)|
| Spanish       |[587 entries](https://docs.google.com/spreadsheets/d/1jls7tTbpSe9QZwqW8D4d-hmGdX-pw0UjTEunryeAIIk/edit?usp=sharing)| [71 templates](https://www.google.com)|[1298 rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/grammar)|[44010153](https://www.google.com)|[es_QaSystem](https://www.google.com)|






