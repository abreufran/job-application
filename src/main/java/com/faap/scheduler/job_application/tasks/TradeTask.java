package com.faap.scheduler.job_application.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.stream.Stream;

public class TradeTask extends TimerTask {

	@Override
	public void run() {
		System.out.println(LocalDateTime.now());
	}

	public void readFileAndSave() {
		String fileName =  System.getProperty("user.home") + "/Desktop/trades/five_minutes_open_short_trades.txt";
		System.out.println("filename: " + fileName);
		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach((l) -> {
				System.out.println(l);
				this.saveTrade(l);
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveTrade(String trade) {
		String driver="org.postgresql.Driver";
		String url="jdbc:postgresql://localhost:5432/log_api";
		String username = "postgres";
		String password="123456";
		
		Connection con = null;
		PreparedStatement stmt = null;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,username,password);
			
			stmt = con.prepareStatement("insert into data_file (created_at, raw_data, updated_at, flag) values (?, ?, ?, ?)");
	        stmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
	        stmt.setString(2,trade);
	        stmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
	        stmt.setString(4,"TRADE");
	                     
	        int retorno = stmt.executeUpdate();
	        
	        if (retorno>0)
	            System.out.println("Insertado correctamente");  
			
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
	              stmt.close();
	              con.close();
	           } catch(Exception e){
	              e.printStackTrace();
	           }
	        }
	     } 
	}

}
