package com.faap.scheduler.job_application.file.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UtilFileService {
	public static DateTimeFormatter dateTimeFileFormatter = DateTimeFormatter.ofPattern("_dd_MM_yyyy_HH_mm_ss");

	public boolean copy(String originPath, String destinationPath) {
	    try {
			Files.copy(Paths.get(originPath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public String getFileName(String fullPathFile) {
		return fullPathFile.substring(fullPathFile.lastIndexOf("/"));
	}
	
	public String getFileNameBackup(String fullPathFile) {
		return fullPathFile.substring(fullPathFile.lastIndexOf("/"), fullPathFile.lastIndexOf(".")) +
				LocalDateTime.now().format(dateTimeFileFormatter) +
				fullPathFile.substring(fullPathFile.lastIndexOf("."));
	}
	
	public LocalDateTime getDateModified(String fullPathFile) {
		try {
			Path file = Paths.get(fullPathFile);

			BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

			FileTime fileTime = attr.lastModifiedTime();
			LocalDateTime convertedFileTime = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
			
		  	return convertedFileTime;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String makeBackup(String backupPath, String fileName) {
		System.out.println("Making backup: " + fileName);
		String destinationPath = backupPath + this.getFileNameBackup(fileName);
		boolean copied = this.copy(fileName, destinationPath);
//		if(copied) {
//			LocalDateTime originFileDateModified = this.getDateModified(jobFileName);
//			this.fileBackupRepository.saveFileBackup(destinationPath, originFileDateModified, Flag.JOB);
//		}
		if(copied) {
			return destinationPath;
		}
		return null;
	}
}
