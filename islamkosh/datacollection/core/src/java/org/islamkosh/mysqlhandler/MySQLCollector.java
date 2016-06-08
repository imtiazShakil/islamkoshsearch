package org.islamkosh.mysqlhandler;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.datacollector.Collector;
import org.islamkosh.metadata.Metadata;

public class MySQLCollector implements Collector {
	private static final Log LOG = LogFactory.getLog(MySQLCollector.class.getName());
	
	/**
	 * returns ArrayList of Metadata if found any data
	 * returns null if no data is found or any error occurred
	 */
	public ArrayList<Metadata> collectData() {
		MySqlAdapter dbHandler = new MySqlAdapter();
		
		LOG.info("Connecting to database...");
		try {
			dbHandler.connectToDatabase();
		} catch (ClassNotFoundException e) {
            LOG.fatal(e.getMessage(),e);
            return  null;
		} catch (Exception e) {
			LOG.fatal(e.getMessage(),e);
            return  null;
		}
		
		ArrayList<Metadata> dataCollection = dbHandler.getAllRows();
		
		return dataCollection;
	}

}
