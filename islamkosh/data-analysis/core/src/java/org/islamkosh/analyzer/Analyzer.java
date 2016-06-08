package org.islamkosh.analyzer;

import java.util.ArrayList;

import org.islamkosh.ai.keywordExtraction.KeywordExtraction;
import org.islamkosh.configuration.Configuration;
import org.islamkosh.metadata.Metadata;

public class Analyzer {
	private String keywordInputField = Configuration.getProperty("keywords.input",
			"body");
	private KeywordExtraction keywordExtractor = new KeywordExtraction();
	
	public void analyze(Metadata metadata) {
		keywordExtractor.filter(metadata, keywordInputField);
	}
	
	public void analyze(ArrayList<Metadata> collection) {
		for (Metadata metadata : collection) {
			analyze(metadata);
		}
	}
}
