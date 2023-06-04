/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author elahi creates a Grammar from a file in which the grammar is
 * serialised in some suitable format.
 */
public class GrammarFactory {

    private Grammar grammar = new Grammar();

    public GrammarFactory(String grammarFileName) {
    }

    public Grammar getGrammar() {
        return grammar;
    }

}
