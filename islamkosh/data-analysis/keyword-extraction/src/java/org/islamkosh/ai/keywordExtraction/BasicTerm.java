
package org.islamkosh.ai.keywordExtraction;

/**
 *
 * @author imtiaz
 */
public class BasicTerm implements Comparable<BasicTerm> {
    private int freq,id;
    private double prob1,prob2;
    
    public BasicTerm(int id) {
        this.id = id;
        this.freq = 0;
        this.prob1 = 0.0;
        this.prob2 = 0.0;
    }
    
    public void addFreq(int newFreq) {
        this.freq += newFreq;
    }
    public int getId() {
        return id;
    }

    public int getFreq() {
        return freq;
    }

    public double getProb1() {
        return prob1;
    }

    public double getProb2() {
        return prob2;
    }

    public void setProb1(double prob1) {
        this.prob1 = prob1;
    }

    public void setProb2(double prob2) {
        this.prob2 = prob2;
    }
    
    @Override
    public int compareTo(BasicTerm ob) {
        return ob.freq - this.freq;
    }

    @Override
    public String toString() {
        return "BasicTerm{" + "freq=" + freq + ", id=" + id + ", prob1=" + prob1 + ", prob2=" + prob2 + '}';
    }
    
}
