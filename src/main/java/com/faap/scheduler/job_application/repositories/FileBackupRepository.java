package com.faap.scheduler.job_application.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.faap.scheduler.job_application.enums.Flag;

public class FileBackupRepository extends AbstractRepository{
	
	public boolean existFileBackup(LocalDateTime originFileDateModified) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = this.connect();
			return this.existFileBackup(con, stmt, rs, originFileDateModified);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			this.close(con, stmt, rs);
	    }
		return false;
	}

	public boolean existFileBackup(Connection con, PreparedStatement stmt, ResultSet rs, LocalDateTime originFileDateModified) throws SQLException {
		stmt = con.prepareStatement("select id from file_backup where originfile_datemodified = ?");
		stmt.setTimestamp(1, Timestamp.valueOf(originFileDateModified));
		rs = stmt.executeQuery();
		 
		boolean exist = rs.next();
		
		rs.close();
  	   	stmt.close();
  	   	
  	   	rs = null;
  	   	stmt = null;
		
		return exist;
	}
	
	public void saveFileBackup(String backupFileName, LocalDateTime originFileDateModified, Flag flag) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = this.connect();
			
			if(!this.existFileBackup(con, stmt, rs, originFileDateModified)) {
				System.out.println(backupFileName);
				
				stmt = con.prepareStatement("insert into file_backup (backup_filename, flag, created_at, originfile_datemodified) values (?, ?, ?, ?)");
				stmt.setString(1,backupFileName);
				stmt.setString(2,flag.toString());
				stmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
		        stmt.setTimestamp(4,Timestamp.valueOf(originFileDateModified));
		        
		                     
		        int retorno = stmt.executeUpdate();
		        
		        if (retorno>0)
		            System.out.println("File backup record inserted successfully."); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			this.close(con, stmt, rs);
	    }
	}
}
