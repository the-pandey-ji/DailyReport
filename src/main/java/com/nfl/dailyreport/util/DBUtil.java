package com.nfl.dailyreport.util;

import java.sql.Connection;


public class DBUtil {

	
	private static Connection con;

	
	public static Connection getConnection() {
		
		  try { Class.forName("oracle.jdbc.driver.OracleDriver"); 
		  String url =
		  "jdbc:oracle:thin:@10.3.126.84:1521:ORCL"; 
		  con =
		  java.sql.DriverManager.getConnection(url,"PRODUCTION","PROD");

		 
		  } catch (Exception e) { e.printStackTrace(); }
		 

		
		
		return con;
	}
}
