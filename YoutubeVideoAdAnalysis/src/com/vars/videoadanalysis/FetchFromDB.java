/**
 * 
 */
package com.vars.videoadanalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Abhinav
 *
 */
public class FetchFromDB {

	static Connection conn = null;

	/**
	 * Parameterized constructor. Used to make connection to database
	 * 
	 * @param userName
	 *            , password, url
	 */

	protected FetchFromDB(String userName, String password, String url) {

		try {
			/**
			 * Load JDBC driver for MySQL.
			 */
			Class.forName("com.mysql.jdbc.Driver");

			/**
			 * Connect to the database.
			 */

			conn = DriverManager.getConnection(url, userName, password);

		} catch (Exception e) {
			System.err.println("Cannot connect to database server:\n ");
			conn = null;
		}

	}

	/**
	 * Execute query and retrieve results based on type
	 * 
	 * @param query
	 *            , type
	 * @return valueList
	 */
	protected ArrayList<String> retrieve(String query, String type) {

		if (conn == null)
			throw new NullPointerException();

		ArrayList<String> valueList = new ArrayList<String>();

		try {

			// Prepare statement
			PreparedStatement querySailors = conn.prepareStatement(query);

			// Execute the query and store it in result set
			ResultSet rs = querySailors.executeQuery();

			while (rs.next()) {
				valueList.add(rs.getString(type));
			}

			// Close result set and close query
			rs.close();
			querySailors.close();
		} catch (SQLException e) {
			System.out.println("Exception while Retrieval");
		}

		return valueList;
	}

	/**
	 * Execute query and retrieve results based on type = "word" and the
	 * "weight"
	 * 
	 * @param query
	 * @return wordsAndweight
	 */

	protected HashMap<String, String> retrieve(String query) {

		if (conn == null)
			throw new NullPointerException();

		HashMap<String, String> wordsAndweight = new HashMap<String, String>();

		try {

			// Prepare statement
			PreparedStatement querySailors = conn.prepareStatement(query);

			// Execute the query and store it in result set
			ResultSet rs = querySailors.executeQuery();

			while (rs.next()) {
				wordsAndweight
						.put(rs.getString("word"), rs.getString("weight"));
			}

			// Close result set and close query
			rs.close();
			querySailors.close();
		} catch (SQLException e) {
			System.out.println("Exception while Retrieval");
		}

		return wordsAndweight;
	}

	/**
	 * Execute query and retrieve results based on video_name and the video
	 * 
	 * @param
	 * @return videoData
	 */
	protected HashMap<String, String> retrieve() {

		if (conn == null)
			throw new NullPointerException();

		HashMap<String, String> videoData = new HashMap<String, String>();

		try {

			// Prepare statement
			PreparedStatement querySailors = conn
					.prepareStatement("SELECT video,video_name FROM ads_processed;");

			// Execute the query and store it in result set
			ResultSet rs = querySailors.executeQuery();

			while (rs.next()) {

				videoData
						.put(rs.getString("video_name"), rs.getString("video"));

			}

			// Close result set and close query
			rs.close();
			querySailors.close();
		} catch (SQLException e) {
			System.out.println("Exception while Retrieval");
		}
		return videoData;
	}

	String readFileAndInsert(String filePath) {
		String com = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;

			while ((line = br.readLine()) != null) {
				com = com + line;
			}
			br.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return com;
	}

}
