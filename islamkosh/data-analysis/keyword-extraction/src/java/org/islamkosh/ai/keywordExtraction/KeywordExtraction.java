/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.islamkosh.ai.keywordExtraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.metadata.Metadata;

/**
 * Extracts Keywords from a gven content. 
 * The algorithm is based on {@link http://www.aaai.org/Papers/FLAIRS/2003/Flairs03-076.pdf}
 * <p>
 * 	Note: if you call the filter method(with or without nutch) it will fetch contents from 
 * {@link com.chorki.variables.metadata.MetadataConstants#BOILERPIPIE_FIELD_NAME} and extract keywords
 * </p>
 * @author imtiaz
 */
public class KeywordExtraction {
	private static final Log LOG = LogFactory.getLog(KeywordExtraction.class
			.getName());
	/**
	 * defines the value of topFrequentWords i.e. topFrequentWords = CONTENT_RATIO*TOTAL_WORDS;
	 */
	private static final Double CONTENT_RATIO = 0.30;
	/**
	 * defines maximum Keyword Size
	 */
	private static final String MAX_KEYWORDS_SIZE = "100";
	/**
	 * defines the minimum score2 that is required of each {@link GeneralTerm} to be selected as a keyword.
	 */
	private static final Double MIN_SCORE_THRESHOLD = 0.4;

	/**
	 * <p>Use outside nutch environment</p>
	 * @author sazid
	 * @param metadata
	 */
	public void filter(Metadata metadata, String field) {
		long start = System.currentTimeMillis();
		if (!metadata.get(field).isEmpty()) {
			KeywordExtraction ob = new KeywordExtraction();
			String keywords = ob.process(metadata
					.get(field));
			metadata.add(field, keywords);

		}
		long end = System.currentTimeMillis();
		LOG.info("Keyword Extraction Completed in ("
				+ Long.toString(end - start) + ")ms");
	}



	private ArrayList<ArrayList<String>> dataFormatter(String doc) {
		if (doc == null)
			return null;

		// splitting the sentence
		String[] sentenceList = doc.split("\\.|\\?|\\!|\\;|\\।");
		if (sentenceList == null)
			return null;
		if (LOG.isTraceEnabled()) {
			LOG.trace("Splitting the sentence");
			for (String str : sentenceList) {
				LOG.trace("sentence-->" + str);
				LOG.trace("===========================================");
			}
		}

		// splitting the words from the sentence
		// and putting them in newdoc
		ArrayList<ArrayList<String>> newdoc = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < sentenceList.length; i++) {
			String[] words = sentenceList[i].split("\\r|\\n|\\t|\\,| ");
			ArrayList<String> sentence = new ArrayList<String>();
			for (String word : words) {
				word = Filters.filter(word);
				if (word != null)
					sentence.add(word);
			}
			if (sentence != null)
				newdoc.add(sentence);
		}

		return newdoc;
	}

	public String process(String content) {
		ArrayList<ArrayList<String>> doc = dataFormatter(content);
		ArrayList<ArrayList<Integer>> newdoc = new ArrayList<ArrayList<Integer>>();
		// hm is the hashmap which maps the word with an unique id
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		int newId, COUNTER = 0;

		ArrayList<BasicTerm> basicTermList = new ArrayList<BasicTerm>();
		ArrayList<GeneralTerm> GeneralTermList = new ArrayList<GeneralTerm>();

		// converting doc to newdoc
		for (ArrayList<String> sentence : doc) {
			ArrayList<Integer> intSentence = new ArrayList<Integer>();

			for (int j = 0; j < sentence.size(); j++) {
				if (hm.containsKey(sentence.get(j)))
					newId = hm.get(sentence.get(j));
				else {
					hm.put(sentence.get(j), COUNTER);
					newId = COUNTER;
					basicTermList.add(new BasicTerm(newId));
					GeneralTermList.add(new GeneralTerm(sentence.get(j)));
					COUNTER++;
				}
				// System.out.println(sentence.get(j) +"--"+newId);
				intSentence.add(newId);
			}
			newdoc.add(intSentence);
		}

		// =======================================================================
		// calculating frequency for all words
		ArrayList<Sentence> finaldoc = new ArrayList<Sentence>();
		for (ArrayList<Integer> terms : newdoc) {
			Sentence sent = new Sentence(terms);
			sent.process(basicTermList, GeneralTermList);
			finaldoc.add(sent);
		}
		Collections.sort(basicTermList);

		// for(BasicTerm word : basicTermList) {
		// System.out.println( word.getId()+"<-->"+word.getFreq() );
		// }
		// for(GeneralTerm word : GeneralTermList) {
		// System.out.println( word.toString() );
		// }

		// =======================================================================
		int TOTAL_UNIQUE_WORDS = hm.size();
		int topFrequentWords = (int) (CONTENT_RATIO * (double) TOTAL_UNIQUE_WORDS);
		topFrequentWords = Integer.min(topFrequentWords, Integer
				.parseInt(System.getProperty("keywords.size", MAX_KEYWORDS_SIZE)));

		if (LOG.isTraceEnabled()) {
			LOG.trace("Total-->" + TOTAL_UNIQUE_WORDS + " selected-->"
					+ topFrequentWords);
		}

		// =======================================================================
		// calculating sentence freq for each term w in GeneralTermList
		for (Sentence sent : finaldoc) {
			sent.process(topFrequentWords, basicTermList, GeneralTermList);
		}

		// =======================================================================
		// calculating prob1 for each term t found in basicTermList
		// prob1 = freq(t) / Sum( freq(t1)+freq(t2)+freq(t3)+...upto
		// top20percent )
		int sumOfFreqOfTopFreqWords = 0;
		for (int i = 0; i < topFrequentWords; i++) {
			BasicTerm t = basicTermList.get(i);
			sumOfFreqOfTopFreqWords += t.getFreq();
		}
		for (int i = 0; i < topFrequentWords; i++) {
			BasicTerm t = basicTermList.get(i);
			t.setProb1((double) t.getFreq() / (sumOfFreqOfTopFreqWords * 1.0));
		}
		// =======================================================================
		// calculating prob2 for each term t found in basicTermList
		for (int i = 0; i < topFrequentWords; i++) {
			BasicTerm t = basicTermList.get(i);
			int nc = 0;
			for (GeneralTerm w : GeneralTermList) {
				if (w.sentenceFreq == null)
					w.initializeSentenceFreq(topFrequentWords);
				nc += ((w.sentenceFreq[i] > 0) ? 1 : 0);
			}
			t.setProb2((double) nc / (TOTAL_UNIQUE_WORDS * 1.0));
		}

		// =======================================================================
		// display basicTerm sorted in desc order according to frequency
		if (LOG.isTraceEnabled()) {
			for (int i = 0; i < topFrequentWords; i++) {
				BasicTerm word = basicTermList.get(i);
				LOG.trace("id-->" + word.getId() + ",term-->"
						+ GeneralTermList.get(word.getId()).term + "<->"
						+ word.getFreq() + "<-->" + word.getProb2());
			}
		}

		// =======================================================================
		// calculating score1,score2 for each term w in GeneralTermList
		// with respective to top20percent terms t
		// then sorting all the generalTerm
		for (GeneralTerm word : GeneralTermList) {
			word.generateScore(basicTermList, topFrequentWords);
		}
		Collections.sort(GeneralTermList);

		// =======================================================================
		// display the result
		if (LOG.isTraceEnabled()) {
			for (int i = 0; i < topFrequentWords; i++) {
				GeneralTerm word = GeneralTermList.get(i);
				LOG.trace(word.toString());
			}
		}

		// =======================================================================
		// Returning the result
		StringBuilder sb = new StringBuilder();
		Double bestScore = 0.0;
		if (GeneralTermList.size() > 0)
			bestScore = GeneralTermList.get(0).score2;

		if (bestScore > 0.0) {
			for (int i = 0; i < topFrequentWords; i++) {
				if (GeneralTermList.get(i).score2 / bestScore < MIN_SCORE_THRESHOLD)
					break;
				sb.append(GeneralTermList.get(i).term).append(" ");
			}
		}
		return sb.toString();
	}

}