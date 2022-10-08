package com.faap.scheduler.job_application.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.faap.scheduler.job_application.enums.Flag;

public class DataFileRepository extends AbstractRepository {
	
	public boolean existDataFile(Connection con, PreparedStatement stmt, ResultSet rs, String data) throws SQLException {
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
	
	public void saveDataFile(String data, Flag flag) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = this.connect();
			
			if(!this.existDataFile(con, stmt, rs, data)) {
				System.out.println(data);
				
				stmt = con.prepareStatement("insert into data_file (created_at, raw_data, updated_at, flag) values (?, ?, ?, ?)");
		        stmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
		        stmt.setString(2,data);
		        stmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
		        stmt.setString(4,flag.toString());
		                     
		        int retorno = stmt.executeUpdate();
		        
		        if (retorno>0)
		            System.out.println("Data file record inserted successfully."); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			this.close(con, stmt, rs);
	    } 
	}
}
