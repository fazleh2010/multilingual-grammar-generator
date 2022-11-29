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

| Language      | Lexicon        | Sentene Templates | Grammar | Questions | Web Interface |
| :------------ |:---------------| :-----|:-----|:-----|:-----|
| English       |[en_lexicon](https://www.google.com)| [en_template](https://www.google.com)|[en_grammar](https://www.google.com)|[en_questions](https://www.google.com)|[en_Interface](https://www.google.com)|
| German        |[de_lexicon](https://www.google.com)| [de_template](https://www.google.com) |[de_grammar](https://www.google.com)|[de_questions](https://www.google.com)|[de_Interface](https://www.google.com)|
| Italian       |[it_lexicon](https://www.google.com)| [it_template](https://www.google.com) |[it_grammar](https://www.google.com)|[it_questions](https://www.google.com)|[it_Interface](https://www.google.com)|
| Spanish       |[es_lexicon](https://www.google.com)| [es_template](https://www.google.com)|[es_grammar](https://www.google.com)|[es_questions](https://www.google.com)|[es_Interface](https://www.google.com)|


| Language      | NounPPFrame    | TransitiveFrame | InTransitivePPFrame | Attributive Adjection | Gradable Adjection|
| :------------ |:---------------| :-----|:-----|:-----|:-----|
| English       | some wordy textT| $1600 |:-----|:-----|:-----|
| German        | centered       |   $12 |:-----|:-----|:-----|
| Italian       | are neat       |    $1 |:-----|:-----|:-----|
| Spanish       | are neat       |    $1 |:-----|:-----|:-----|




## Used Frameworks And Libraries

- Lemon API: QueGG uses the Lemon API to parse and access the properties of the turtle lexicon files. The API and more information on the Lemon API can be found here: https://github.com/monnetproject/lemon.api
- DBPedia: QueGG uses the [DBPedia](https://wiki.dbpedia.org) [SPARQL endpoint](http://dbpedia.org/sparql) to access the DBPedia Ontology. 
