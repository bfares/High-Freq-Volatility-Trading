package org.brainswap.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.Observable;
import au.com.bytecode.opencsv.CSVWriter;


/**
 * This class contains common operations used by the different ETL classes
 * @author Blackhat
 *
 */
public abstract class AbstractOperations extends Observable {
	 protected Scanner input= new Scanner(System.in);
	 public static Date date= new Date();
	 private static String logFileName;
	 protected static int invalidDataNb=0;
	 protected static CSVWriter writer;
	 
		
	 public enum dataType{date,imVol,vol,open, high, low, close,ret,intRet,forVol, undPrice, gamma, realVol, delta, premium,d1,d2}
	
	 protected final int 
	 DATA_COLNB=9 ;
	
	 
	public AbstractOperations() {
		super();
		
	}
	//method that asks the user whether he wants to terminate the application
	public void exitApplication(){
		
		String userInput;
		do{
			System.out.print("\nAre you sure you want to exit ? [Y/N] ");
			userInput=input.next().toLowerCase();
		
		if(userInput.equals("y")){
			
			System.exit(-1);
		}
		}while(!userInput.equals("y") && !userInput.equals("n"));
		
	}
	/*
	 * Archive invalid data and save them to CSV
	 */
	public void archive(dataType type ,String[] invalidData){
		try {
		if(logFileName==null){
			logFileName=type.toString()+"_log_"+date.toString()+".csv";
		 writer = new CSVWriter(new FileWriter(logFileName,true), '\t');
		}
		invalidDataNb++;
		
		 writer.writeNext(invalidData);
			//writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     // feed in your array (or convert your data to an array)
	     
	}
	
	
}
