package org.brainswap.main;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Precision;

import au.com.bytecode.opencsv.CSVReader;

/**
 * This class analysis and extracts data from CSV files
 * @author Blackhat
 *
 */
public class Extract extends AbstractOperations {
	private Map<dataType,ArrayList<Double>> dataMap=new HashMap<dataType,ArrayList<Double>>();
	private ArrayList<String> date= new ArrayList<String>();
	private File file;
	private CSVReader reader;
	private String extractionType ;
	/**
	 * ArrayList Containing CSV files Paths
	 */
	private List<Path> primaryDataPath= new ArrayList<Path>();
	
	
	public Extract() {
		super();
	}
	
	public Extract(File file) {
		this.file= file;
	}
	
	public void startExtract(String extractionType) {
	this.extractionType= extractionType;
	
	this.startExtract();
	}

	@SuppressWarnings("static-access")
	public void startExtract() {
			String [] nextLine;
			if (file == null){
			this.listDirectory();// scan directory for files
			}
			else{
				
			this.primaryDataPath.add(file.toPath());
			}
			
	  if(!this.primaryDataPath.isEmpty()){
		  System.out.printf("\nStep 1: Extract process started\n");
			
		  ArrayList<Double> forVol = null,realVol= null, impVol= null,undPrice= null, premium= null, gamma= null, delta= null;
		  
		  ArrayList<Double> imVol= null, vol= null, open= null, high= null,low= null,close= null, ret= null,intRet= null;
		  
		  for(Path filePath:this.primaryDataPath){
				
		  try {
			  int firstRecordColumnNb= this.firstRecordColumnNb(filePath); //Get first column number
			  
			  System.out.printf("\nProcessing %s . %d columns.",filePath.getFileName().toString(),
					  firstRecordColumnNb);
			  
			  this.initCSVReader(filePath);
			  
			  if(extractionType.equals("BT")){
				 
				   forVol= new ArrayList<Double>();
					  realVol= new ArrayList<Double>();
					  impVol= new ArrayList<Double>();
					  undPrice= new ArrayList<Double>();
					 
					  premium= new ArrayList<Double>();
					  gamma= new ArrayList<Double>();
					  delta= new ArrayList<Double>();
				  
			  }else {
			  imVol= new ArrayList<Double>();
			  vol= new ArrayList<Double>();
			  open= new ArrayList<Double>();
			  high= new ArrayList<Double>();
			  low= new ArrayList<Double>();
			  close= new ArrayList<Double>();
			  ret= new ArrayList<Double>();
			  intRet= new ArrayList<Double>();
			  }
			 int i=0;
			 try{
			  while ((nextLine = this.reader.readNext()) != null) {
				 //nextLine[] is an array of values from the line
				  if(extractionType.equals("BT") && i>=1){
					  date.add(nextLine[0]);
					  forVol.add(Precision.round(Double.parseDouble(nextLine[1]),6));
					  realVol.add(Precision.round(Double.parseDouble(nextLine[2]),6));
					  impVol.add(Precision.round(Double.parseDouble(nextLine[3]),6));
					  undPrice.add(Precision.round(Double.parseDouble(nextLine[4]),6));
					  
					  premium.add(Precision.round(Double.parseDouble(nextLine[5]),6));
					  gamma.add(Precision.round(Double.parseDouble(nextLine[6]),6));
					  delta.add(Precision.round(Double.parseDouble(nextLine[7]),6));
					  
					 // d1.add(Precision.round(Double.parseDouble(nextLine[8]),6));
					 // d2.add(Precision.round(Double.parseDouble(nextLine[9]),6)); 
				  }
				  else if (i>=2){
				date.add(nextLine[0]);
				imVol.add(Precision.round(Double.parseDouble(nextLine[1]),6));
				vol.add(Precision.round(Double.parseDouble(nextLine[2]),6));
				open.add(Precision.round(Double.parseDouble(nextLine[3]),6));
				high.add(Precision.round(Double.parseDouble(nextLine[4]),6));
				low.add(Precision.round(Double.parseDouble(nextLine[5]),6));
				close.add(Precision.round(Double.parseDouble(nextLine[6]),6));
				ret.add(Precision.round(Double.parseDouble(nextLine[7]),6));
				intRet.add(Precision.round(Double.parseDouble(nextLine[8]),6));
				  
				  }
				i++;
			}
			 } catch(Exception e){
				 System.out.print("cln number "+ i+ " is empty");
			 }
			 if(extractionType.equals("BT")){
				  
				  dataMap.put(dataType.forVol ,new ArrayList<Double>(forVol));
				  dataMap.put(dataType.realVol,new ArrayList<Double>(realVol));
				  dataMap.put(dataType.imVol,new ArrayList<Double>(impVol));
				  dataMap.put(dataType.undPrice,new ArrayList<Double>(undPrice));
				 // dataMap.put(dataType.d1,new ArrayList<Double>(d1));
				//  dataMap.put(dataType.d2,new ArrayList<Double>(d2));
				  dataMap.put(dataType.premium,new ArrayList<Double>(premium));
				  dataMap.put(dataType.gamma,new ArrayList<Double>(gamma));
				  dataMap.put(dataType.delta,new ArrayList<Double>(delta));
				 
			 }
			 else {
			  dataMap.put(dataType.imVol,new ArrayList<Double>(imVol));
			  dataMap.put(dataType.vol,new ArrayList<Double>(vol));
			  dataMap.put(dataType.open,new ArrayList<Double>(open));
			  dataMap.put(dataType.high,new ArrayList<Double>(high));
			  dataMap.put(dataType.low,new ArrayList<Double>(low));
			  dataMap.put(dataType.close,new ArrayList<Double>(close));
			  dataMap.put(dataType.ret,new ArrayList<Double>(ret));
			  dataMap.put(dataType.intRet,new ArrayList<Double>(intRet));
			 }
			 
			 if(super.writer != null){ 
				  super.writer.close();
				  }
			  
			} 
		  catch (IOException e) {
				e.printStackTrace();
				throw(new RuntimeException(e));
				}
			catch (NullPointerException e){
				e.printStackTrace();
				throw(e);
			}
		 
		}
			
			
			System.out.printf("\n\nETL Process finished.\n%s"
			,(AbstractOperations.invalidDataNb!=0)?"Some event(s) were not Loaded.check the generated log file(s) for more details.":"");
	  }
	  
	  else {
		  System.err.print("Critical Error Occured !\n Paths are not Loaded\nSystem Will Exit.");
		  throw new NullPointerException() ;
		  
	  }
	}
	
	private void initCSVReader(Path primaryDataPath) throws FileNotFoundException{
		
		reader = new CSVReader(new FileReader(primaryDataPath.toString()));
	
	}
	/*
	 * This method loads the csv files paths in an arraylist. The user is given the choice
	 * to use a custom specified directory or the default directory.
	 */
	public void listDirectory(){
		String  userInput;
		Path 	mainDirectorypath;
//		do{
//		System.out.print("\nDo you want to use the default data folder ? [Y/N] :");
//		userInput = input.nextLine();
//		}
//		while(!userInput.toLowerCase().equals("y") && !userInput.toLowerCase().equals("n") );
//		
//		if(userInput.toLowerCase().equals("n")){
//			
//			System.out.print("Insert path containing the required CSV Files:");
//			
//			userInput = input.nextLine();
//			
//			mainDirectorypath = Paths.get(userInput);
//		}
//		else
//		{
			mainDirectorypath = Paths.get("data");
//		}
			
		try {
			/*
			 * pathMatcher use pattern matching to locate files in the specified path.In our case CSV files.
			 *Ref: http://docs.oracle.com/javase/tutorial/essential/io/find.html
			 */
			PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.{CSV,csv}");
			
			
			System.out.println("File(s) to Import: ");
				DirectoryStream<Path> stream = Files.newDirectoryStream(mainDirectorypath); 
			    for (Path file: stream) {
			    	
			    	if(matcher.matches(file.getFileName())){
			        System.out.println(file.getFileName());
			        this.primaryDataPath.add(file);// Saving CSV path in arraylist
			        
			        }
			    	
			    }
			} catch (IOException | DirectoryIteratorException x) {
			    System.err.print("An error occured!\nPlease make sure that the directory you specified"
			    		+ " exists and is not right protected\nError Details: "+x);
			    listDirectory();
			}
		if(this.primaryDataPath.isEmpty()){
			System.out.print("No file(s) found. \nMake sure that you specified the right "
							+"path and your file(s) are in the right format."
							+ "\n Press any key to retry or Q to exit: ");
			userInput = input.nextLine().toLowerCase();
			if (userInput.equals("q")){
				super.exitApplication();
				
			}
			else{
				listDirectory();
			}
		}
		 
	}
	

	
	/*
	 * in case a file can't be recognized, the application asks the user interference
	 * and asks the user to set the file type(OrderDetails, OrderHistory or TradeReport)
	 */
	public dataType unrecognizedData(Path primaryDataPath){
		String userInput;
		System.out.printf("The system couldn't recognize the file \"%s\".\nPlease specify it youself by pressing:\n"
				+ "[D] for Data\n[Q] to exit:  "
				+ "",primaryDataPath.getFileName().toString());
		userInput=input.next().toLowerCase();
		if(!userInput.equals("d") && !userInput.equals("q")){
			this.unrecognizedData(primaryDataPath);
			}
		if(userInput.equals("q")){
			super.exitApplication();
			this.unrecognizedData(primaryDataPath);
		}
		if(userInput.equals("d")){
			if(firstRecordColumnNb(primaryDataPath)==DATA_COLNB){
				return dataType.vol;
			}
			else
				return this.unrecognizedData(primaryDataPath);	
		}
		
				return this.unrecognizedData(primaryDataPath);	
	}
	
	
	/*
	 * return the provided csv file first row column number
	 */
	public int firstRecordColumnNb(Path primaryDataPath){
			int columnNb=0;
			String [] nextLine;		
				try {
					initCSVReader(primaryDataPath)	;
					nextLine=this.reader.readNext();
					columnNb= nextLine.length;
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
				
				
			return columnNb;
		}

	public Map<dataType, ArrayList<Double>> getDataMap() {
		return dataMap;
	}

	public ArrayList<String> getDate() {
		return date;
	}

	public void setDate(ArrayList<String> date) {
		this.date = date;
	}
	
	
}
