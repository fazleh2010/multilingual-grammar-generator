#!/bin/sh
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_lexicon_1_en.json dataset/dbpedia_en.json
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_lexicon_2_en.json dataset/dbpedia_en.json
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_lexicon_3_en.json dataset/dbpedia_en.json
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_lexicon_4_en.json dataset/dbpedia_en.json
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_lexicon_5_en.json dataset/dbpedia_en.json
java -jar target/QuestionGrammarGenerator.jar conf/inputConf_lexicon_6_en.json dataset/dbpedia_en.json
