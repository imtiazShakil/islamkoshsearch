/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.islamkosh.ai.keywordExtraction;
import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author imtiaz
 */
public class GeneralTerm implements Comparable<GeneralTerm>{
    public String term;
    public int sentenceFreq[];
    protected int nw1,nw2;
    protected double score1,score2;
    
    public GeneralTerm(String term) {
        this.term = term;
        nw1 = 0;
        nw2 = 0;
        score1 = 0.0;
        score2 = 0.0;
    }
    
    public void initializeSentenceFreq(int capacity) {
        if(sentenceFreq == null) {
            sentenceFreq = new int[capacity];
        }
    }
    
//    protected void generateScore(ArrayList<BasicTerm>basicTermList , int mostFrequentWordsCount) {
//        double mxsc2=0.0;
//        double tmp;
//        for(int i=0;i<mostFrequentWordsCount;i++) {
//            BasicTerm t = basicTermList.get(i);
//            tmp = ( this.sentenceFreq[i] - this.nw1*t.getProb1() );
//            
//            if(this.nw1==0) {
//                this.score1 = -99999999;
//                mxsc2 = 0.0;
//                break;
//            }
//            this.score1 +=  (tmp*tmp)/(this.nw1*t.getProb1());
//            
//            tmp = ( this.sentenceFreq[i] - this.nw2*t.getProb2() );
//            
//            if(this.nw2==0 || t.getProb2()<=0.0 ) {
//                continue;
//            }
//            
//            tmp = (tmp*tmp)/ (this.nw2*t.getProb2());
//            mxsc2 = Double.max(mxsc2, tmp);
//        }
//        this.score2 = this.score1 - mxsc2;
//    }
    public void generateScore(ArrayList<BasicTerm>basicTermList , int mostFrequentWordsCount) {
        double mxsc2=0.0;
        double tmp;
        for(int i=0;i<mostFrequentWordsCount;i++) {
            BasicTerm t = basicTermList.get(i);
            tmp = ( this.sentenceFreq[i] - this.nw2*t.getProb2() );
            
            if(this.nw2*t.getProb2()<=0.0) {
                continue;
            }
            double val = (tmp*tmp)/(this.nw2*t.getProb2());
            this.score1 +=  val;
            mxsc2 = Double.max(mxsc2, val);
        }
        this.score2 = this.score1 - mxsc2;
    }
    
    
    @Override
    public String toString() {
        
        return "GeneralTerm{" + "term=" + term + ", sentenceFreq=" + Arrays.toString(sentenceFreq) + ", nw1=" + nw1 + ", nw2=" + nw2 + ", score1=" + score1 + ", score2=" + score2 + '}';
    }

    @Override
    public int compareTo(GeneralTerm ob) {
//        return Double.compare(this.score2, ob.score2 ); //increasing order
        return Double.compare(ob.score2 , this.score2 ); //descreasing order
        
        /**
	    	int tmp = (int) (ob.score2 - this.score2);
	        if(tmp!=0) return tmp;
	        
	        tmp = (int) (ob.score1 - this.score1);
	        if(tmp!=0) return tmp;
	        
	        tmp = ob.nw1 - this.nw1;
	        if(tmp!=0) return tmp;
	        return ob.hashCode() - this.hashCode();
        */        
    }
    
    
}
