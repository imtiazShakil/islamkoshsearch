package org.islamkosh.controller;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.analyzer.Analyzer;
import org.islamkosh.datacollector.Collector;
import org.islamkosh.datacollector.MySQLCollector;
import org.islamkosh.dataindexer.Indexer;
import org.islamkosh.dataindexer.SolrIndexer;
import org.islamkosh.metadata.Metadata;

public class Controller {
	private static final Log LOG = LogFactory.getLog(Controller.class.getName());
	
	public static void main(String args[]) {
		Collector collector = new MySQLCollector();
		Analyzer analyzer = new Analyzer();
		Indexer indexer = new SolrIndexer();
		
		ArrayList<Metadata> collection = collector.collectData();
		if (collection == null) {
			LOG.info("No data is found. Stopping the process...");
			return;
		}
		
		analyzer.analyze(collection);
		
		boolean indexingResponse = indexer.indexData(collection);
		if (indexingResponse == false) {
			LOG.info("Indexing failed!");
			return;
		}
		LOG.info("Indexing successful.");
	}
}
