package com.faap.scheduler.job_application.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Stream;

public class TradeTask extends TimerTask {

	@Override
	public void run() {
		System.out.println(LocalDateTime.now());
	}

	public void readFileAndSave() {
		String openTradefileName =  System.getProperty("user.home") + "/Desktop/trades/five_minutes_open_short_trades.txt";
		String closedTradefileName =  System.getProperty("user.home") + "/Desktop/trades/five_minutes_closed_short_trades.txt";
		List<String> openTrades = this.readFile(openTradefileName);
		List<String> closedTrades = this.readFile(closedTradefileName);
		
		for (int i = 0; i < openTrades.size(); i++) {
			if((i + 1) <= closedTrades.size()) {
				this.saveTrade(openTrades.get(i) + "-" + closedTrades.get(i));
			}
			else {
				this.saveTrade(openTrades.get(i) + "-");
			}
			
		}
	}
	
	public List<String> readFile(String fileName) {
		List<String> trades = new ArrayList<>();
		System.out.println("reading filename: " + fileName);
		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach((l) -> {
				trades.add(l);
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		return trades;
	}
	
	public void saveTrade(String trade) {
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
			
			if(!this.existTrade(con, stmt, rs, trade)) {
				System.out.println(trade);
				
				stmt = con.prepareStatement("insert into data_file (created_at, raw_data, updated_at, flag) values (?, ?, ?, ?)");
		        stmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
		        stmt.setString(2,trade);
		        stmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
		        stmt.setString(4,"TRADE");
		                     
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
	
	public boolean existTrade(Connection con, PreparedStatement stmt, ResultSet rs, String trade) throws SQLException {
		stmt = con.prepareStatement("select id from data_file where raw_data = ?");
		stmt.setString(1, trade);
		rs = stmt.executeQuery();
		 
		boolean exist = rs.next();
		
		rs.close();
  	   	stmt.close();
  	   	
  	   	rs = null;
  	   	stmt = null;
		
		return exist;
	}

}
