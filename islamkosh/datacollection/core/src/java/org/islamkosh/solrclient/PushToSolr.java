package org.islamkosh.solrclient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.islamkosh.metadata.Metadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PushToSolr {
	
	private static final Log LOG = LogFactory
			.getLog(PushToSolr.class.getName());
	
	private static final String SOLR_URL = System.getProperty("solr.url", "http://localhost:8983/solr/islamkosh");

	private static SolrClient solr;	
	public static void ConnectWithSolr() throws Exception{
		solr = new HttpSolrClient(SOLR_URL);
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.setRequestHandler("select");
		try{
			QueryResponse response = solr.query(query);
		} catch(Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}


	// private static SolrClient solr;

	public static void removeIndex(String id) throws SolrServerException,
			IOException {

		solr.deleteById(id);

	}

	public static SolrDocument getIndexedDoc(String id) {
		try {
			return solr.getById(id);
		} catch (SolrServerException e) {
			LOG.fatal("SolrServerException Occured. Check the log to see the details");
			if (LOG.isErrorEnabled()) {
				LOG.error(e.toString());
			}
		} catch (IOException e) {
			LOG.fatal("IO Exception Occured. Check the log to see the details.");
			if (LOG.isErrorEnabled()) {
				LOG.error(e.toString());
			}
		}
		return null;
	}

	public static void commitAndOptimize() {
		try {
			solr.commit();
			solr.optimize();
			LOG.info("SOLR is committed and optimized.");
		} catch (SolrServerException e) {
			LOG.fatal("SolrServerException. Check the log to see the details");
			if (LOG.isErrorEnabled()) {
				LOG.error(e.toString());
			}
		} catch (IOException e) {
			LOG.fatal("IOException. Check the log to see the details");
			if (LOG.isErrorEnabled()) {
				LOG.error(e.toString());
			}
		}
	}

	public static void addToIndex(SolrInputDocument doc)
			throws SolrServerException, IOException {
		solr.add(doc);
	}
	
	public static void addToIndex(ArrayList<Metadata> metadataCollection)
			throws SolrServerException, IOException {
		for (Metadata metadata : metadataCollection) {
			addToIndex(createSolrInputDoc(metadata));
		}
	}

	public static SolrInputDocument createSolrInputDoc(Map<String, String> map) {
		SolrInputDocument sid = new SolrInputDocument();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sid.addField(entry.getKey(), entry.getValue());
		}
		return sid;
	}
	
	public static SolrInputDocument createSolrInputDoc(Metadata map) {
		SolrInputDocument sid = new SolrInputDocument();
		String keys[] = map.names();
		for(String key : keys) {
			String values[] = map.getValues(key);
			for(String value : values) {
				sid.addField(key, value);
			}
		}
		
		return sid;
	}

}
