package com.faap.scheduler.job_application.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Stream;

import com.faap.scheduler.job_application.enums.Flag;
import com.faap.scheduler.job_application.repositories.DataFileRepository;

public class TradeTask extends TimerTask {
	private DataFileRepository dataFileRepository;
	
	public TradeTask(DataFileRepository dataFileRepository) {
		this.dataFileRepository = dataFileRepository;
	}

	@Override
	public void run() {
		System.out.println("Trade Task: " + LocalDateTime.now());
		
		this.readFileAndSave();
	}

	public void readFileAndSave() {
		String openTradefileName =  System.getProperty("user.home") + "/Desktop/trades/five_minutes_open_short_trades.txt";
		String closedTradefileName =  System.getProperty("user.home") + "/Desktop/trades/five_minutes_closed_short_trades.txt";
		List<String> openTrades = this.readFile(openTradefileName);
		List<String> closedTrades = this.readFile(closedTradefileName);
		
		this.saveTrades(openTrades, closedTrades);
	}
	
	public void saveTrades(List<String> openTrades, List<String> closedTrades) {
		if(openTrades != null) {
			for (int i = 0; i < openTrades.size(); i++) {
				if(closedTrades != null && (i + 1) <= closedTrades.size()) {
					this.saveTrade(openTrades.get(i) + "-" + closedTrades.get(i));
				}
				else {
					this.saveTrade(openTrades.get(i) + "-");
				}
				
			}
		}
	}

	
	public void saveTrade(String trade) {
		dataFileRepository.saveDataFile(trade, Flag.TRADE);
	}
	
	public boolean existFile(String fileName) {
		File f = new File(fileName);
		return f.exists();
	}
	
	public List<String> readFile(String fileName) {
		if(!this.existFile(fileName)) {
			return null;
		}
		
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

}
