package org.islamkosh.solrclient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.islamkosh.dataindexer.Indexer;
import org.islamkosh.metadata.Metadata;
import org.islamkosh.mysqlhandler.MySqlAdapter;

public class SolrIndexer implements Indexer {
	private static final Log LOG = LogFactory.getLog(SolrIndexer.class.getName());
	
	/**
	 * returns true if indexing successful
	 * returns false if indexing unsuccessful
	 */
	public boolean indexData(ArrayList<Metadata> metadataCollection) {
		LOG.info("Testing solr availability");
		try {
			PushToSolr.ConnectWithSolr();
		} catch (ClassNotFoundException | SQLException e) {
            LOG.fatal(e.getMessage(),e);
            return  false;
		} catch (Exception e) {
			LOG.fatal(e.getMessage(),e);
            return  false;
		}
		
		LOG.info("Indexing data in solr");
		try {
			PushToSolr.addToIndex(metadataCollection);
		} catch (SolrServerException e) {
			LOG.fatal(e.getMessage(),e);
            return  false;
		} catch (IOException e) {
			LOG.fatal(e.getMessage(),e);
            return  false;
		}
		PushToSolr.commitAndOptimize();
		return true;
	}

}
