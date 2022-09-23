package com.faap.scheduler.job_application.tasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.faap.scheduler.job_application.enums.Flag;

public class Task {
	public boolean existData(Connection con, PreparedStatement stmt, ResultSet rs, String data) throws SQLException {
		stmt = con.prepareStatement("select id from data_file where raw_data = ?");
		stmt.setString(1, data);
		rs = stmt.executeQuery();
		 
		boolean exist = rs.next();
		
		rs.close();
  	   	stmt.close();
  	   	
  	   	rs = null;
  	   	stmt = null;
		
		return exist;
	}
	
	protected void saveData(String data, Flag flag) {
		String driver="org.postgresql.Driver";
		String url="jdbc:postgresql://localhost:5432/log_api";
		String username = "postgres";
		String password="123456";
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,username,password);
			
			if(!this.existData(con, stmt, rs, data)) {
				System.out.println(data);
				
				stmt = con.prepareStatement("insert into data_file (created_at, raw_data, updated_at, flag) values (?, ?, ?, ?)");
		        stmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
		        stmt.setString(2,data);
		        stmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
		        stmt.setString(4,flag.toString());
		                     
		        int retorno = stmt.executeUpdate();
		        
		        if (retorno>0)
		            System.out.println("Insertado correctamente"); 
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
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
}
