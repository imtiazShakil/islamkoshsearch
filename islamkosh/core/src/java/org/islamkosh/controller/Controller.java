package org.islamkosh.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.analyzer.Analyzer;
import org.islamkosh.configuration.Configuration;
import org.islamkosh.datacollector.Collector;
import org.islamkosh.dataindexer.Indexer;
import org.islamkosh.metadata.Metadata;
import org.islamkosh.mysqlhandler.MySQLCollector;
import org.islamkosh.solrclient.SolrIndexer;
import org.xml.sax.SAXException;

public class Controller {
	private static final Log LOG = LogFactory.getLog(Controller.class.getName());
	
	public static void main(String args[]) {
		try {
			Configuration.initialize();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOG.error(e.getMessage(), e);
			System.out.println("Configuration Loading Failed. Please check log");
			System.exit(1);
		}
		
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
