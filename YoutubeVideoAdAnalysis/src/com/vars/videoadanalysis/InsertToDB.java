/**
 * 
 */
package com.vars.videoadanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Abhinav
 *
 */
public class InsertToDB {

	static Connection conn = null;

	/**
	 * Parameterized constructor. Used to make connection to database
	 * 
	 * @param userName
	 *            , password, url
	 */

	protected InsertToDB(String userName, String password, String url) {

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

	String readFileAndInsert(String filePath) {
		String com = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;

			while ((line = br.readLine()) != null) {
				com = com + line;
			}
			br.close();
			String query = "insert into ads_dump(video) values " + com;

			Statement stmt;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return com;
	}

	public static void main(String[] args) {
		InsertToDB objInsertToDB = new InsertToDB("root", "admin",
				"jdbc:mysql://localhost/ad_analysis");

		File f = null;
		String[] paths;
		String dir = "C:\\Users\\yedla\\Downloads\\transcripts (1)\\transcripts\\without_profile2";
		f = new File(dir);

		// array of files and directory
		paths = f.list();

		// for each name in the path array
		for (String path : paths) {
			// prints filename and directory name
			System.out.println(path);

			objInsertToDB.readFileAndInsert(dir + "\\" + path);
		}

	}

}
