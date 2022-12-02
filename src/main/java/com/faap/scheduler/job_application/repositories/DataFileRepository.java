package com.faap.scheduler.job_application.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.faap.scheduler.job_application.enums.Flag;
import com.faap.scheduler.job_application.models.thing_to_do.ThingToDo;

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
	
	
	public List<ThingToDo> readReactThingToDo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ThingToDo> thingToDoList = new ArrayList<>();
		
		try {
			con = this.connect();
				
			stmt = con.prepareStatement("select id, category, created_at, email_sent, "
					+ "estimated_date, estimated_date_sent, execution_date, incidence_date, "
					+ "json_params, priority, status, thing_todo, ttd.token, updated_at, "
					+ "user_name from thing_to_do ttd "
					+ "where ttd.user_name  = 'REACT' "
					+ "order by priority asc, estimated_date desc NULLS last");
			
	                     
	        rs = stmt.executeQuery();
	        
	        while ( rs.next() ) {
	        	ThingToDo ttd = new ThingToDo();
	        	ttd.setId(rs.getInt(1));
	        	ttd.setCategory(rs.getString(2));
	        	ttd.setCreatedAt(rs.getTimestamp(3).toLocalDateTime());
	        	ttd.setEmailSent(rs.getBoolean(4));
	        	ttd.setEstimatedDate(rs.getDate(5) != null ? rs.getDate(5).toLocalDate() : null);
	        	ttd.setEstimatedDateSent(rs.getBoolean(6));
	        	ttd.setExecutionDate(rs.getDate(7) !=  null ? rs.getDate(7).toLocalDate() : null);
	        	ttd.setIncidenceDate(rs.getDate(8) != null ? rs.getDate(8).toLocalDate() : null);
	        	ttd.setJsonParams(rs.getString(9));
	        	ttd.setPriority(rs.getString(10));
	        	ttd.setStatus(rs.getString(11));
	        	ttd.setThingToDo(rs.getString(12));
	        	ttd.setToken(rs.getString(13));
	        	ttd.setUpdatedAt(rs.getTimestamp(14).toLocalDateTime());
	        	ttd.setUserName(rs.getString(15));
	        	
	        	thingToDoList.add(ttd);
	        	
	        }
		         
	        
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			this.close(con, stmt, rs);
	    } 
		
		return thingToDoList;
	}
	
	public void updateReactThingToDo(int id) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = this.connect();
				
				stmt = con.prepareStatement("update thing_to_do set user_name = 'EXCEL' where id = ?");
		        stmt.setInt(1,id);
		                     
		        int retorno = stmt.executeUpdate();
		        
		        if (retorno>0)
		            System.out.println("Data file record update successfully."); 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			this.close(con, stmt, rs);
	    } 
	}
}
