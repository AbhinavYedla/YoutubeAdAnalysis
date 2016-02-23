<!--
 * @author Abhinav
 * Insert values in to database and send navigation back to the index page.
 * If no database connection then throw an error
 *
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insertion To Database</title>
<META HTTP-EQUIV="Refresh" CONTENT="2;URL=http://localhost:8080/AdAnalysis/index.jsp">
</head>
<body>
	<%
		try {
			//Establishing a connection
			String connectionURL = "jdbc:mysql://localhost/ad_analysis";
			Connection connection = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "root", "admin");

			//Get value from the form
			String transcription = request.getParameter("transcription");
			//transcription = transcription.replace("'", "");
			out.println("Transcription: \n" + transcription);

			//Do the insertion
			String insertSQL = "INSERT INTO ads_dump(video) VALUES('" + transcription + "')";
			PreparedStatement statement = connection.prepareStatement(insertSQL);
			statement.executeUpdate();
			connection.close();
			
	%>
	Successfully Inserted into Database!
	<br />
	<%
		} catch (Exception e) {
			System.out.println("Error: Cannot make a connection to database");
			System.out.print(e.toString());
	%>Error: Cannot make a connection to database.
	<br />
	<%
		}
	%>
</body>
</html>