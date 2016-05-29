package org.islamkosh.mysqlhandler;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.datacollector.Collector;
import org.islamkosh.metadata.Metadata;

public class MySQLCollector implements Collector {
	private static final Log LOG = LogFactory.getLog(MySQLCollector.class.getName());
	
	private static final String DB_URL = System.getProperty("mysql.db.url", "jdbc:mysql://localhost/hadiths?");
	private static final String DB_USER = System.getProperty("mysql.db.user", "root");
	private static final String DB_PASS = System.getProperty("mysql.db.password", "Sazid1462");
	private static final String TABLE_NAME = System.getProperty("mysql.track.table", "hadiths");
	
	/**
	 * returns ArrayList of Metadata if found any data
	 * returns null if no data is found or any error occurred
	 */
	public ArrayList<Metadata> collectData() {
		MySqlAdapter dbHandler = new MySqlAdapter(DB_URL, DB_USER, DB_PASS, TABLE_NAME);
		
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
