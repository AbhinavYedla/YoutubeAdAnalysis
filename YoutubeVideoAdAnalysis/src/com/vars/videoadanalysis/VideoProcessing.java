package com.vars.videoadanalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * 
 * This class uses JDBC to access a relational database
 * 
 * @author Abhinav
 *
 */
public class VideoProcessing {

	/**
	 * Establish a database connection for given database name
	 * 
	 * @return connection
	 * @param databasename
	 */
	private static Connection openConnection(String database) {
		Connection conn = null;

		try {

			String userName = "root"; // username of DBMS; modified to use your
										// username
			String password = "admin"; // password of DBMS; modified to
												// use
			// your password
			String url = "jdbc:mysql://127.0.0.1/" + database; // hostname and
																// database name
																// modified to
																// use your
																// database
			Class.forName("com.mysql.jdbc.Driver"); // Load JDBC driver for
													// MySQL
			conn = DriverManager.getConnection(url, userName, password); // connect
																			// to
																			// the
																			// Database
		} catch (Exception e) {
			System.err.println("Cannot connect to database server:\n "
					+ e.fillInStackTrace());//
			conn = null;
		}

		return conn;

	}

	/**
	 * 
	 * main function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Connection conn = openConnection("ad_analysis");
		Connection conn1 = openConnection("ad_analysis");
		ResultSet rs;
		int i = 1;
		try {

			PreparedStatement dumpdb;
			PreparedStatement processeddb = conn1
					.prepareStatement(" insert into ads_processed (vid, video) values(?,?) ");

			// prepares the statement for executing the query
			dumpdb = conn.prepareStatement("select * from ads_dump");

			rs = dumpdb.executeQuery();

			while (rs.next()) {
				String s = rs.getString("video");

				s = s.toLowerCase();
				s = s.replace("\\s+", " ");

				// removing stop words
				StringBuilder builder = new StringBuilder(s);
				StringBuilder out = new StringBuilder("");
				String[] words = builder.toString().split("\\s");
				BufferedReader br = null;
				StringBuilder read = new StringBuilder("");
				try {

					String sCurrentLine;

					br = new BufferedReader(new FileReader("stopwords.txt"));

					while ((sCurrentLine = br.readLine()) != null) {
						read.append(sCurrentLine + " ");

					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				// converting verbs to base forms
				Hashtable<String, String> hashtable = new Hashtable<String, String>();
				try {

					String sCurrentLine;

					br = new BufferedReader(new FileReader("verbs.txt"));

					while ((sCurrentLine = br.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(sCurrentLine);
						hashtable.put((String) st.nextElement(),
								(String) st.nextElement());

					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				for (String word : words) {
					if (!(read).toString().contains(word)) {

						if (hashtable.containsKey(word)) {
							out.append(hashtable.get(word) + " ");
						} else
							out.append(word + " ");
					}
				}

				// storing the processed videos in db

				// exception for catching if there are duplicate entries in the
				// new table
				try {
					processeddb.setInt(1, i);
					processeddb.setString(2, out.toString());
					processeddb.executeUpdate();
					// System.out.println("inserted "+key+" "+save.get(key)+" into new table");
				} catch (SQLException a) {
					// System.out.println("duplicate entries in the new table");
				}

				System.out.println(out);
				System.out.println();
				i++;
			}

			processeddb.close();
			conn1.commit();
			rs.close();
			dumpdb.close();
		} catch (SQLException e) {
			System.out.println("exception at count" + i);
		}

	}

}