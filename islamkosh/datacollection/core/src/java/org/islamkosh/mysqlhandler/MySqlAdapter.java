package org.islamkosh.mysqlhandler;

import com.mysql.jdbc.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.configuration.Configuration;
import org.islamkosh.metadata.Metadata;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by sazid on 1/14/16.
 *
 * This class is written for accessing the MySqlDb
 */
public class MySqlAdapter {
	private static final Log LOG = LogFactory.getLog(MySqlAdapter.class
			.getName());

	private Connection connect;
	private ResultSet resultSet;
	private Statement statement;
	private ArrayList<Metadata> rows;

	private String DB_URL = Configuration.getProperty("mysql.db.url", 
			"jdbc:mysql://localhost/hadiths?");
	private String DB_USER = Configuration.getProperty("mysql.db.user", 
			"root");
	private String DB_PASS = Configuration.getProperty("mysql.db.pass", 
			"Sazid1462");
	private String TABLE_NAME = Configuration.getProperty("mysql.db.table", 
			"hadiths");
	private String TABLE_COLUMNS = Configuration.getProperty("mysql.db.table.columns", 
			"*");

	/**
	 * Only constructor for the class. It will initialize the database handler.
	 */
	public MySqlAdapter() {
		rows = new ArrayList<Metadata>();
	}

	/**
	 * Call this method to create the connection to the server. After the
	 * connection is established getter and setter methods can be called. Call
	 * the closeConnection() method after the usage of the object.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws UnsupportedEncodingException 
	 */
	public void connectToDatabase() throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
		// This will load the MySQL driver, each DB has its own driver
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		connect = (Connection) DriverManager.getConnection(DB_URL + "user="
				+ URLEncoder.encode(DB_USER,"UTF-8") + "&password=" + URLEncoder.encode(DB_PASS,"UTF-8"));
		LOG.info("Connection to database successful.");
	}

	/**
	 * Call this method to close the connection to the database
	 */
	public void closeConnection() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connect != null) {
				connect.close();
			}
			LOG.info("Connection to database is closed.");
		} catch (SQLException e) {
			LOG.fatal("SQLException Occurred. Check the log to see the details.");
			if (LOG.isErrorEnabled()) {
				LOG.error(e.toString());
			}
		}
	}

	/**
	 * Public getter method for all hadiths. This will execute the query and populate the
	 * ArrayList.
	 */
	public ArrayList<Metadata> getAllRows() {
		try {
			// Statements allow to issue SQL queries to the database
			LOG.info("Executing SQL Query...");
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery(String.format(
					"SELECT * FROM %s",
					TABLE_NAME));
			
			String[] tableColumns = TABLE_COLUMNS.split(",");
			while (resultSet.next()) {
				
				Metadata row = null;
				
				String[] column = tableColumns[0].split(" ");
				if (column[1].equalsIgnoreCase("int"))
				{
					row = new Metadata(Integer.toString(resultSet.getInt(column[0])));
				} else if (column[1].equalsIgnoreCase("string")) {
					row = new Metadata(resultSet.getString(column[0]));
				}
				for (int i=1; i<tableColumns.length; i++) {
					column = tableColumns[i].split(" ");
					if (column[1].equalsIgnoreCase("int"))
					{
						row.set(column[0], Integer.toString(resultSet.getInt(column[0])));
					} else if (column[1].equalsIgnoreCase("string")) {
						row.set(column[0], resultSet.getString(column[0]));
					}
				}					
				rows.add(row);
			
			}
			LOG.info("Found " + rows.size() + " rows.");
			// writeResultSet(resultSet);
			return rows;
		} catch (SQLException e) {
			LOG.fatal("SQLException Occurred. Check the log to see the details.");
			if (LOG.isErrorEnabled()) {
				LOG.error(e.toString());
			}
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				LOG.fatal("SQLException Occurred. Check the log to see the details.");
				if (LOG.isErrorEnabled()) {
					LOG.error(e.toString());
				}
			}
		}
		return null;
	}
/*
	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			long pid = resultSet.getLong("pid");
			// String status = resultSet.getString("status");
			// Date createDate = resultSet.getDate("create_date");
			// Date updateDate = resultSet.getDate("update_date");
			System.out.println("PID: " + pid);
			// System.out.println("Status: " + status);
			// System.out.println("Create Date: " + createDate);
			// System.out.println("Update Date: " + updateDate);
		}
	}
*/
}
