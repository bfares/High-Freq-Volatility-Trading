package org.brainswap.GP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.brainswap.GP.functions.*;
import org.brainswap.main.AbstractOperations.dataType;
import org.epochx.epox.DoubleERC;
import org.epochx.epox.Node;
import org.epochx.epox.Variable;
import org.epochx.epox.math.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Creates a genetic Programming model to fore cast volatility 
 * @author BlackHat
 *
 */

public class VModel extends GPModel {
	private SummaryStatistics stat= new SummaryStatistics();
	private SummaryStatistics localstat= new SummaryStatistics();
	private  Variable realVol,open, high, low,last,logRet,intVol,step,impVol;
	
			private final Map<dataType,ArrayList<Double>> dataMap;
			private  double outputs;
			private int lag;
			private RandomNumberGenerator rnd ;
			private ArrayList<Double> volData;
			private ArrayList<Double> retData;
			
	private final int noPoints;

	public Max maxVol;
	public Max maxRet;
	public Min minVol;
	public Min minRet;
	public Average avgVol, avgRet;
	public Data dataVol, dataRet;
	
	/**
	 *VMODEL should be initialized by providing a PSNG, Nb of Points to use from the given sample,
	 *a Map that contains all the needed data, Array List of the data date and time, the lag or number 
	 *of steps forward to forecast, a boolean array of the Fct Set to use, a boolean array of the Terminal Set to use.
	 *
	 */
	public VModel(RandomNumberGenerator rand,final int noPoints, Map<dataType,ArrayList<Double>> dataMap,ArrayList<String> dateList, int lag, boolean[] fctSet, boolean[] terminalSet){
		this.rnd=rand;
		this.dataMap= dataMap;
		this.noPoints= noPoints;
		this.lag=lag;
		this.volData= dataMap.get(dataType.vol);
		this.retData= dataMap.get(dataType.ret);
	
		// Terminal Set
		impVol = new Variable("impVol", Double.class);
		realVol = new Variable("realVol", Double.class);
		logRet = new Variable("dailyRet", Double.class);
		open = new Variable("open", Double.class);
		high = new Variable("high", Double.class);
		low = new Variable("low", Double.class);
		last = new Variable("last", Double.class);
		intVol = new Variable("intVol", Double.class);
		
		//Fct Set
		maxVol = new Max(dataType.vol,volData,rnd,0,50);
		maxRet = new Max(dataType.ret,retData,rnd,0,50);
		
		minVol = new Min(dataType.vol,volData,rnd,0,50);
		minRet = new Min(dataType.ret,retData,rnd,0,50);
		 
		avgVol= new Average(dataType.vol,volData,rnd,0,50);
		avgRet= new Average(dataType.ret,retData,rnd,0,50);
			
		dataVol=new Data(dataType.vol,volData,rnd,0,50);
		dataRet=new Data(dataType.ret,retData,rnd,0,50);
		 

					 
		  List<Node> syntax = new ArrayList<Node>();
		
		  // Functions.
		  if(fctSet[0]){
			  syntax.add(new AddFunction());
		    
		  }
		  if(fctSet[1]){
		    syntax.add(new MultiplyFunction());
		  }
		  if(fctSet[2]){
			  syntax.add(new SquareRootProtectedFunction());
		  }
		  if(fctSet[3]){
			  syntax.add(new ExponentialProtectedFunction());
		  }
		  if(fctSet[4]){
			  syntax.add(maxVol);
			  syntax.add(maxRet);
		  }
		  if(fctSet[5]){
			  syntax.add(new DoubleERC(rnd,0,1,6)); // add the ephemeral random constant
		  }
		  if(fctSet[6]){
			  syntax.add(dataVol);
			    syntax.add(dataRet);
		  }
		  if(fctSet[7]){
			  syntax.add(new SubtractFunction());
		  }
		  if(fctSet[8]){
			  syntax.add(new DivisionProtectedFunction(1));
		  }
		  if(fctSet[9]){
			  syntax.add(new LogProtectedFunction());  
		  }
		  if(fctSet[10]){
			  syntax.add(minVol);
			  syntax.add(minRet);
		  }
		    
		  if(fctSet[11]){
		    syntax.add(avgVol);
		    syntax.add(avgRet);
		  }
		    
		  // Terminals.
		  if(terminalSet[0]){
			  syntax.add(impVol);
		  }
		  if(terminalSet[1]){
			  syntax.add(realVol);
		  }
		  if(terminalSet[2]){
			  syntax.add(high);
		  }
		  if(terminalSet[3]){
			  syntax.add(low);  
		  }
		  if(terminalSet[4]){
			  syntax.add(open);
		  }
		  if(terminalSet[5]){
			  syntax.add(last); 
		  }
		  if(terminalSet[6]){
		    syntax.add(logRet);
		  }
		  if(terminalSet[7]){
		    syntax.add(intVol);
		  }
		  
		
		    setSyntax(syntax);
		  
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		final GPCandidateProgram program = (GPCandidateProgram) p;
		stat.clear();
		
		Double result;
		localstat.clear();
		for (int i = 49; i < noPoints; i++) {
			
			outputs = dataMap.get(dataType.vol).get(i+lag);
			
			maxVol.setLower(i-50);
			maxVol.setUpper(i);
			
			minVol.setLower(i-50);
			minVol.setUpper(i);
			
			maxRet.setLower(i-50);
			maxRet.setUpper(i);
			
			minRet.setLower(i-50);
			minRet.setUpper(i);
			
			avgVol.setLower(i-50);
			avgVol.setUpper(i);
			
			avgRet.setLower(i-50);
			avgRet.setUpper(i);
			
			dataVol.setLower(i-50);
			dataVol.setUpper(i);
			
			dataRet.setLower(i-50);
			dataRet.setUpper(i);
			
			impVol.setValue(dataMap.get(dataType.imVol).get(i));
			realVol.setValue(dataMap.get(dataType.vol).get(i));
			logRet.setValue(dataMap.get(dataType.ret).get(i));
			open.setValue(dataMap.get(dataType.open).get(i));
			high.setValue(dataMap.get(dataType.high).get(i));
			low.setValue(dataMap.get(dataType.low).get(i));
			last.setValue(dataMap.get(dataType.close).get(i));
			intVol.setValue(dataMap.get(dataType.intRet).get(i));
			
			  result = (Double) program.evaluate();
			
			  localstat.addValue(Math.pow(result - outputs,2));
			  
		
		}
		return localstat.getMean();
	}
	
	
	public Variable getImpVol() {
		return impVol;
	}

	public void setImpVol(Variable impVol) {
		this.impVol = impVol;
	}

	public Variable getIntVol() {
		return intVol;
	}

	public Variable getRealVol() {
		return realVol;
	}
	public void setRealVol(Variable realVol) {
		this.realVol = realVol;
	}
	public Variable getRet() {
		return logRet;
	}
	public void setRet(Variable dailyRet) {
		this.logRet = dailyRet;
	}
	
	public Variable getOpen() {
		return open;
	}
	public void setOpen(Variable open) {
		this.open = open;
	}
	public Variable getHigh() {
		return high;
	}
	public void setHigh(Variable high) {
		this.high = high;
	}
	public Variable getLow() {
		return low;
	}
	public void setLow(Variable low) {
		this.low = low;
	}
	public Variable getLast() {
		return last;
	}
	public void setLast(Variable last) {
		this.last = last;
	}
	public Variable getLogRet() {
		return logRet;
	}
	public void setLogRet(Variable logRet) {
		this.logRet = logRet;
	}
	public Variable getIntRet() {
		return intVol;
	}
	public void setIntVol(Variable intVol) {
		this.intVol = intVol;
	}
	public double getOutputs() {
		return outputs;
	}
	public void setOutputs(double outputs) {
		this.outputs = outputs;
	}
	
	public Variable getStep() {
		return step;
	}
	public void setStep(Variable step) {
		this.step = step;
	}
	@Override
	public Class<?> getReturnType() {

		return Double.class;
	}

	
	

}
