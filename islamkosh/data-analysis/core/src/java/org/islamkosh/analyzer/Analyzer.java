package org.islamkosh.analyzer;

import java.util.ArrayList;

import org.islamkosh.ai.keywordExtraction.KeywordExtraction;
import org.islamkosh.metadata.Metadata;

public class Analyzer {
	private static final String KEYWORD_INPUT_FIELD = "body";
	private KeywordExtraction keywordExtractor = new KeywordExtraction();
	
	public void analyze(Metadata metadata) {
		keywordExtractor.filter(metadata, KEYWORD_INPUT_FIELD);
	}
	
	public void analyze(ArrayList<Metadata> collection) {
		for (Metadata metadata : collection) {
			analyze(metadata);
		}
	}
}
