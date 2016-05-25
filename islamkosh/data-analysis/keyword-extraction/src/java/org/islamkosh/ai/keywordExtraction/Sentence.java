/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.islamkosh.ai.keywordExtraction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author imtiaz
 */
public class Sentence {
    Set<Integer>set;
    ArrayList<Integer>term;
    
    public Sentence(ArrayList<Integer> sentence) {
        this.term = sentence;
        set = new HashSet<Integer>();
        for(int i=0;i<term.size();i++) {
            set.add(term.get(i));
        }
    }
    
    /// --> counts the total frequency for each term in this sentence
    /// --> for each term w
    ///     counts the total no of terms in this sentence where w exists
    
    public void process(ArrayList<BasicTerm> basicTermList, ArrayList<GeneralTerm> generalTermList) {
        if(term == null || basicTermList == null || generalTermList == null) return ;
        
        for(int id : term) {
            basicTermList.get(id).addFreq(1);
        }
        int id;
        Iterator iter = set.iterator();
        while(iter.hasNext()) {
            id = (int)iter.next();
            generalTermList.get(id).nw2 += term.size();
        }
    }
    
    public void process(int topSelectedWords,ArrayList<BasicTerm> basicTermList, ArrayList<GeneralTerm> generalTermList) {
        
        /// --> for each term w in generalTermList
        ///     count its sentenceFrequency for each term t(topTerm) in basicTermList
        ///     increment that value in nw1 for term w
        ///     nw1 means "sum of all sentenceFrequency for term w"
        int id;
        Iterator iter = set.iterator();
        while(iter.hasNext()) {
            id = (int)iter.next();
            for(int i=0;i<topSelectedWords;i++) {
                if( set.contains( basicTermList.get(i).getId()  ) == true ) { 
                    
                                                        /// the term t exists in this
                                                        /// current sentence
                    GeneralTerm w = generalTermList.get(id);
                    w.initializeSentenceFreq(topSelectedWords);
                    
                    if(id == basicTermList.get(i).getId() ) continue;
                                                        /// the term t == term w
                                                        /// 
                    w.sentenceFreq[i]++;
                    w.nw1++;
                }
            }
        }
    }
}
