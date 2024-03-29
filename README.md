# Automatic Grammar Generation (QueGG-Generator)

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
git clone https://github.com/fazleh2010/multilingual-grammar-generator.git 
```
build the jar file
```shell script
mvn clean package
```
input_configuration_file: The file (inputConf.json) contains input parameter for the system:
- languageCode: `en` (English), `de` (German), `it` (Italian), and `es` (Spanish)
- inputDir: The input directory that contains lexical entries (i.e., csv files). 
- outputDir: The output directory for the grammar (Json files).
- csvToTurtle: the indicator for generating lemon (turtle file) from csv files.
- turtleToProtoType: the indicator for generating grammar from lemon.
````input configuration file
{
  "languageCode" : "en",
  "inputDir" : "lexicon",
  "outputDir" : "output",
  "csvToTurtle" : true,
  "turtleToProtoType" : true,
  "protoTypeToQuestion" : false
 }

````
linked_data_configuration_file: The file (dbpedia.json) contains the input configuration for linked data.
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

english and DBpedia
java -jar target/QuestionGrammarGenerator.jar inputConf_en.json dataset/dbpedia_en.json 

german
java -jar target/QuestionGrammarGenerator.jar inputConf_de.json dataset/dbpedia_de.json   

spanish
java -jar target/QuestionGrammarGenerator.jar inputConf_es.json dataset/dbpedia_es.json        

italain
java -jar target/QuestionGrammarGenerator.jar inputConf_it.json dataset/dbpedia_it.json  
                                 
````  

The output can be seen the folder output/

- output/en/ (English)
- output/de/ (German)
- output/it/ (Italian)
- output/es/ (Spanish)


## Developers
* **Mohammad Fazleh Elahi**
### Supervisors:
* **Dr. Philipp Cimiano**





