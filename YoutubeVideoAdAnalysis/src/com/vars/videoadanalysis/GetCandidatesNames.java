/**
 * 
 */
package com.vars.videoadanalysis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Abhinav
 *
 */
public class GetCandidatesNames {

	static Connection conn = null;

	/**
	 * Parameterized constructor. Used to make connection to database
	 * 
	 * @param userName
	 *            , password, url
	 */

	protected GetCandidatesNames(String userName, String password, String url) {

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
	protected ArrayList<String> retrieve(String query) {

		if (conn == null)
			throw new NullPointerException();

		ArrayList<String> valueList = new ArrayList<String>();

		try {

			// Prepare statement
			PreparedStatement querySailors = conn.prepareStatement(query);

			// Execute the query and store it in result set
			ResultSet rs = querySailors.executeQuery();

			while (rs.next()) {
				valueList.add(rs.getString("Name"));
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
	 * Execute query and retrieve results based on type
	 * 
	 * @param query
	 *            , type
	 * @return valueList
	 */
	protected ArrayList<String> retrieveText(String query) {

		if (conn == null)
			throw new NullPointerException();

		ArrayList<String> valueList = new ArrayList<String>();

		try {

			// Prepare statement
			PreparedStatement querySailors = conn.prepareStatement(query);

			// Execute the query and store it in result set
			ResultSet rs = querySailors.executeQuery();

			while (rs.next()) {
				valueList.add(rs.getString("Data"));
			}

			// Close result set and close query
			rs.close();
			querySailors.close();
		} catch (SQLException e) {
			System.out.println("Exception while Retrieval");
		}

		return valueList;
	}

	public static void main(String[] args) {
		GetCandidatesNames obj = new GetCandidatesNames("root", "admin",
				"jdbc:mysql://localhost/ad_analysis");
		String query = "Select Name from republicans";
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> dataList = new ArrayList<String>();
		nameList = obj.retrieve(query);
		query = "Select Name from democrates";
		nameList.addAll(obj.retrieve(query));

		HashMap<String, Integer> countNames = new HashMap<String, Integer>();
		String data = null, name = null;
		for (int j = 0; j < dataList.size(); j++) {
			for (int i = 0; i < nameList.size(); i++) {
				data = dataList.get(j);
				name = nameList.get(i);
				if (data.contains(name)) {
					if (countNames.containsKey(name))
						countNames.put(name, countNames.get(name) + 1);
					else
						countNames.put(name, 1);
				}
			}
		}

	}
}
