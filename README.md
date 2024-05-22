# Automatic Grammar Generation (QueGG-Generator)

This command-line tool is designed for automatic grammar generation.



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
  "outputDir" : "output"
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
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_en.json dataset/dbpedia_en.json 

german
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_de.json dataset/dbpedia_de.json   

spanish
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_es.json dataset/dbpedia_es.json        

italain
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_it.json dataset/dbpedia_it.json  
                                 
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





