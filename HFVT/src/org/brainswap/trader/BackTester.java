package org.brainswap.trader;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.brainswap.main.AbstractOperations.dataType;

/**
 * this class test the generated forecasting model using a simple volatility arbitrage strategy 
 * @author BlackHat
 *
 */
public class BackTester {
	private SummaryStatistics stat= new SummaryStatistics();
	private Map<dataType,ArrayList<Double>> dataMap=new HashMap<dataType,ArrayList<Double>>();
	private ArrayList<String> date,log;
	private String openedTradeType,signal;
	private int  max,min,entryIndex,lag,costs,minWin,tradeId;
	private ArrayList<Double> impliedVol,forVol,realVol,undPrice,gamma;
	private PrintStream writer;
	private String filename;
	private double maxVol, minVol,theta,contractSize,entryGamma,entryVol;
	private boolean rehedged,openTrade;
	private int optimal;
	
	public BackTester(Map<dataType, ArrayList<Double>> dataMap,
			ArrayList<String> date, int lag, int costs,double theta,int minWin, double contractSize) {
		super();
		
		this.dataMap = dataMap;
		this.date = date;
		this.lag= lag;
		this.costs= costs;
		this.theta= theta/24; //Theta per Hour
		this.minWin=minWin;
		this.contractSize= contractSize;
		//------------------------------------------------------
		this.tradeId=0;
		this.filename= ".\\backTester\\tradeLog" +System.currentTimeMillis()%1000+ ".csv";
		this.log= new ArrayList<String>();
		this.rehedged=false;
		this.openTrade=false;
		this.impliedVol= this.dataMap.get(dataType.imVol);
		this.forVol= this.dataMap.get(dataType.forVol);
		this. realVol= this.dataMap.get(dataType.realVol);
		this.undPrice= this.dataMap.get(dataType.undPrice);
		this. gamma= this.dataMap.get(dataType.gamma);
		
	}
	
	public void process(){
		double expProfit;
		
		
		for(int i=lag-1; i<forVol.size()-lag; i++){
			maxVol=Double.NEGATIVE_INFINITY;//forVol.get((i-(lag-1))); //position with maximum volatility in the lag window
			minVol=Double.POSITIVE_INFINITY;//forVol.get((i-(lag-1))); //position with minimum volatility in the lag window
			//max=i-(lag-1);
			//min=i-(lag-1);
			
			
			for(int j=i-(lag-1); j<=i; j++){
				 if(this.rehedged){
					expProfit= Math.abs(0.5*(Math.pow(forVol.get(j)/100, 2)-Math.pow(realVol.get(optimal)/100, 2))*Math.pow(undPrice.get(optimal),2)*gamma.get(optimal))*this.contractSize- (j+lag-optimal)*theta;
					signal= getTradeSignal(forVol.get(j),realVol.get(optimal));
				 }
				else{
					expProfit= Math.abs(0.5*(Math.pow(forVol.get(j)/100, 2)-Math.pow(impliedVol.get(i)/100, 2))*Math.pow(undPrice.get(i),2)*gamma.get(i))*this.contractSize-(costs+(j+lag-i)*theta);
					signal= getTradeSignal(forVol.get(j),impliedVol.get(i));
				}
				//------Exit or Rehedge Strategy
				if(this.openTrade){
				//Exit Signal
					if(	this.openedTradeType.equals("Long") && signal.equals("Short") && expProfit> this.minWin){
						exitTrade(j+lag-1);	
						i=j+lag-1;
					}
					if(	this.openedTradeType.equals("Short") && signal.equals("Long") && expProfit> this.minWin){
						exitTrade(j+lag-1);
						i=j+lag-1;
					}
					
				}
				//------------------------------------------------------End Exit Signal
					
				//-----------Rehedge Signal	
				
					//if(this.openedTradeType.equals("Long")){	
						if(forVol.get(j)>maxVol){
							maxVol=forVol.get(j);
							max=j ;
							
						}
					//}
					
					//if(this.openedTradeType.equals("Short")){
						if(forVol.get(j)<minVol){
							min=j ;
							minVol=forVol.get(min);
							
							
						}
					//}	
				
				//}
			//----------------------------------------------------End Rehedge
				
				
				if(!this.openTrade){
					 if(signal.equals("Long") && expProfit>minWin){ //in case it should be a long trade
							
							this.entryIndex= i;
							
							
							
							enterTrade(signal,date.get(i), impliedVol.get(i),gamma.get(i));
							
					 }	
						
					 else 
						if (signal.equals("Short") && expProfit>minWin){//in case it should be a short trade
							
							this.entryIndex= i;
							
							
							
							enterTrade(signal,date.get(i), impliedVol.get(i),gamma.get(i));
							
							
						}
				}
			}
			//-------- Nested Loop End
			
			if(this.openTrade==true){
				if(min<=(i) && openedTradeType.equals("Short") ){
					
					i=min+lag-1;
					rehedge(min+lag);
				}
				
				 if( max <(i) && openedTradeType.equals("Long")){
					
					i=max+lag-1;
					rehedge(max+lag);
				}	
				
			}
		}
		//---------Loop End		
				
	}

	private void rehedge(int optimal) {
		this.optimal= optimal;
		double trueProfit=0;
		double transCosts=0;
		String exit= null;
		double timeCost=(optimal-entryIndex)*this.theta;
		if (this.openedTradeType.equals("Long")){
			exit="Rehedge";
			if(!this.rehedged){
				trueProfit= 0.5*(Math.pow(realVol.get(optimal)/100, 2)-Math.pow(entryVol/100, 2))*Math.pow(undPrice.get( entryIndex),2)*this.entryGamma*this.contractSize- (costs+timeCost);
				transCosts=costs;
			}
			else{
				 trueProfit= 0.5*(Math.pow(realVol.get(optimal)/100, 2)-Math.pow(entryVol/100, 2))*Math.pow(undPrice.get( entryIndex),2)*this.entryGamma*this.contractSize- timeCost;
			}	
		}
		
		if (this.openedTradeType.equals("Short")){
			exit="Rehedge";
			if(!this.rehedged){
				trueProfit= -0.5*(Math.pow(realVol.get(optimal)/100, 2)-Math.pow(entryVol/100, 2))*Math.pow(undPrice.get( entryIndex),2)*this.entryGamma*this.contractSize- (costs+timeCost);
				transCosts=costs;
			}
			else{
				 trueProfit= -0.5*(Math.pow(realVol.get(optimal)/100, 2)-Math.pow(entryVol/100, 2))*Math.pow(undPrice.get( entryIndex),2)*this.entryGamma*this.contractSize - timeCost;
			}
		}
		
		this.signal="No Signal";
		this.rehedged= true;
		this.openTrade= true;
		this.entryIndex=optimal;
		this.entryVol= realVol.get(optimal);
		this.entryGamma=gamma.get(optimal);
		String log="   ,"+date.get(optimal)+","+ exit+","+realVol.get(optimal)+","+transCosts+","+timeCost+","+trueProfit;
		
		addToLog(log);
		stat.addValue(trueProfit);
		
		
	}

	private void enterTrade(String tradeType, String date, Double priceVol, Double priceGamma) {
		this.openTrade= true;
		this.tradeId++;
		
		this.openedTradeType=tradeType;
		this.entryVol= priceVol;
		this.entryGamma=priceGamma;
		
		String log=this.tradeId+"," +date+","+ openedTradeType+","+this.entryVol+",,,,"+this.dataMap.get(dataType.premium).get(this.entryIndex)*this.contractSize;
		addToLog(log);
		maxVol=Double.NEGATIVE_INFINITY;
		minVol=Double.POSITIVE_INFINITY;
		
	}

	

	private String getTradeSignal(Double closingVol,Double openVol) {
		if(closingVol>openVol)
			return "Long";
		else if (closingVol<openVol)
			return "Short";
		else
			return "No Signal";
	}
	
public void exitTrade(int optimal){
		
		this.openTrade= false;
		double trueProfit=0;
		double transCosts=0;
		this.signal="No Signal";
		String exit=null;
		double timeCost=(optimal-entryIndex)*this.theta;
		
		if (this.openedTradeType.equals("Long")){
			
			if(!this.rehedged){
				trueProfit= 0.5*(Math.pow(realVol.get(optimal)/100, 2)-Math.pow(entryVol/100, 2))*Math.pow(undPrice.get( entryIndex),2)*this.entryGamma*this.contractSize-(costs+timeCost);
				 	transCosts=costs;
				 	
			}else{
				trueProfit= 0.5*(Math.pow(realVol.get(optimal)/100, 2)-Math.pow(entryVol/100, 2))*Math.pow(undPrice.get( entryIndex),2)*this.entryGamma*this.contractSize-(timeCost);
				 
			}
		 exit= "Short";
		}
		
		if (this.openedTradeType.equals("Short")){
			if(!this.rehedged){
			 trueProfit= -0.5*(Math.pow(realVol.get(optimal)/100, 2)-Math.pow(entryVol/100, 2))*Math.pow(undPrice.get( entryIndex),2)*this.entryGamma*this.contractSize-(costs+timeCost);
			 transCosts=costs;
			}
			else{
				trueProfit= -0.5*(Math.pow(realVol.get(optimal)/100, 2)-Math.pow(entryVol/100, 2))*Math.pow(undPrice.get( entryIndex),2)*this.entryGamma*this.contractSize-(timeCost);
			}
			
			exit= "Long";	
		}
		
		String log="   ,"+date.get(optimal)+","+exit+ ","+realVol.get(optimal)+","+transCosts+","+timeCost+","+trueProfit;
		addToLog(log);
		stat.addValue(trueProfit);
		this.rehedged=false;
		
	}

private void addToLog(String trade) {
	log.add(trade);
//	System.out.println(trade);
}

public void writeoutput(){
	try {
		writer = new PrintStream(filename);
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
	}
	writer.println("Trade Id, Date, Trade Type, Volatility, Transaction Cost, Theta Decay, P/L,Premium");
	for(String trade: log){
		writer.println(trade);
	}
	
 writer.close();
}
}
