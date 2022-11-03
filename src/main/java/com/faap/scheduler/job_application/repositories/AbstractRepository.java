package com.faap.scheduler.job_application.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractRepository {
	private static String DRIVER="org.postgresql.Driver";
	private static String URL="jdbc:postgresql://localhost:5433/log_api";
	private static String USERNAME = "postgres";
	private static String PASSWORD="123456";
	
	public Connection connect() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		return DriverManager.getConnection(URL,USERNAME,PASSWORD);
	}
	
	public void close(Connection con, PreparedStatement stmt, ResultSet rs) {
		if (con != null) {
           try{
        	  if(rs != null) rs.close();
        	  if(stmt != null) stmt.close();
        	  if(con != null) con.close();
           } catch(Exception e){
              e.printStackTrace();
           }
        }
	}
}
