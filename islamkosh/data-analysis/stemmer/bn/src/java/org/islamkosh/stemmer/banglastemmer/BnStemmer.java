package org.islamkosh.stemmer.banglastemmer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class BnStemmer {
    private static final Log LOG = LogFactory.getLog(BnStemmer.class.getName());

    private static RuleFileParser parser;
    private static boolean initialized = false, errorOccured = false;
    private static double threshold = .33f;
   
    public static void initializeStemmer () throws IOException {
        if (initialized) return;
        
        threshold = Double.parseDouble( System.getProperty("stemmer.bangla.threshold", "25") )/100f;
        try {
            ProtWordsList.loadProtWords();
            parser = new RuleFileParser( );
            
            initialized = true;
            LOG.info("BanglaStemmer initialized.");
        } catch (IOException e) {
        	errorOccured = true;
            throw e;
        }
    }
    
    public static String stem(String word) {
        if (initialized == false && errorOccured == false) {
        	try {
				initializeStemmer();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
        }
        if(errorOccured == true ) return word;
        
        if (ProtWordsList.isProtectedWord(word)) {
            return word;
        } else {
            String stem = parser.stemOfWord(word);
            if (((double)stem.length()/(double)word.length())<threshold) stem = word;
            return stem;
        }
    }
}