/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.islamkosh.ai.keywordExtraction;

import org.islamkosh.languages.LanguageCheck;
import org.islamkosh.stemmer.banglastemmer.BnStemmer;
import org.islamkosh.stemmer.kstemmer.KStemmer;
import org.islamkosh.stopword.StopwordFilter;


/**
 *
 * @author imtiaz
 */
public class Filters {
//	private static final Log LOG = LogFactory.getLog(Filters.class.getName());
	private static KStemmer kstemmer = new KStemmer();

	public static String filter(String term) {
		term = StopwordFilter.filter(term);
		if (term == null)
			return null;
//		term = BnStemmer.stem(term);
//		term = kstemmer.stem(term);
		if (LanguageCheck.checkWords(term) == LanguageCheck.BANGLAWORD) {
			term = BnStemmer.stem(term);
		} else if (LanguageCheck.checkWords(term) == LanguageCheck.BASICLATINWORD) {
			term = kstemmer.stem(term);
		}
		term = StopwordFilter.filter(term);
		return term;
	}

}
