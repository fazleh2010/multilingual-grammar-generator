# Automatic Lexicon Creation

This command-line tool is designed for automatic grammar generation.



## Compile And Run
<p>The source code can be compiled and run using <em>Java 11</em> and <em>Maven</em>.</p>

```shell script
git clone https://github.com/fazleh2010/multilingual-grammar-generator.git -b lexicon-creation
```
build the jar file
```shell script
mvn clean package
```
input_configuration_file: The file (inputConf.json) contains input parameter for the system:
- languageCode: `en` (English), `de` (German), `it` (Italian), and `es` (Spanish)
- inputDir: The lexicon directory that contains lexical entries (i.e., .csv files). 


````input configuration file
{
  "languageCode" : "en",
  "inputDir" : "lexicon"
 }

````
The lexicon director:

- lexicon/en/nouns (lexical entries of NouPPFrame)
- lexicon/en/verbs (lexical entries of TransitiveFrame and InTransitiveFrame)
- lexicon/en/adjectives (lexical entries of AttributiveFrame and SuperlativeFrame)
- lexicon/en/DomainOrRange.csv (inflection forms of Domain and Range)

The output turtle files will be found in same directory.

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




## Developers
* **Mohammad Fazleh Elahi**
### Supervisors:
* **Dr. Philipp Cimiano**





