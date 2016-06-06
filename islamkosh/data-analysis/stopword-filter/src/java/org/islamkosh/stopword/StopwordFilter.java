/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.islamkosh.stopword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.configuration.Configuration;



/**
 *
 * @author imtiaz
 */
public class StopwordFilter {
	private static final Log LOG = LogFactory.getLog(StopwordFilter.class.getName());
    private static boolean dataLoaded = false, errorOccured=false;
    
    private static Set<String>set = new HashSet<String>();
    
    private StopwordFilter() {
//        System.out.println( StopwordFilter.class.getResource(StopwordFilter.class.getSimpleName()+".class") ); 
    }
    private static void init() {
        dataLoaded = true;
        
        String stopWordFileName = Configuration.getProperty("stopword.file", "stopwords.txt");
        
        BufferedReader br = null;
        InputStream is = StopwordFilter.class.getClassLoader().getResourceAsStream(stopWordFileName);
		if(is == null) {
			dataLoaded = false;
			errorOccured = true;
			LOG.error("Failed to load stopwords.txt file");
			return ;
		}
        br = new BufferedReader( new InputStreamReader(is,StandardCharsets.UTF_8) );
        try {
            String line = br.readLine();
            while (line != null) {
                line = line.toLowerCase(Locale.ROOT);
                if(line.length()>0) set.add(line);
                line = br.readLine();
            }
        } catch (IOException ex) {
        	dataLoaded = false;
        	errorOccured = true;
        	LOG.error(ex.getMessage(), ex);
            return ;
        } finally {
            try {
                br.close();
                is.close();
            } catch (IOException ex) {
            	dataLoaded = false;
            	errorOccured = true;
            	LOG.error(ex.getMessage(), ex);
            }
        }
    }
    
    
    public static String filter(String term) {
        if(dataLoaded==false && errorOccured==true) return term;
    	if(dataLoaded==false) StopwordFilter.init();

        term = term.toLowerCase(Locale.ROOT);
        
        term=term.replaceAll("[\\+\\^\"\\{\\}\\(\\[\\]\\)\\'\\/\\:\\n\\`\\।]", "");
        term=term.replaceAll("\\p{Punct}", "");
        
        if(term.length()<=2) return null;
        
        
        if(set.contains(term)) return null;
        else return term;
    }
    
    
}
