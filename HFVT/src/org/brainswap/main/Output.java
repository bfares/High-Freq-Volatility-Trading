package org.brainswap.main;



import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.brainswap.GP.VModel;
import org.brainswap.main.AbstractOperations.dataType;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.representation.CandidateProgram;



public class Output {
	private String fileName;
	private Map<dataType,ArrayList<Double>> initialData;
	private Double[][] processedData;
	private PrintStream writer;
	private GPCandidateProgram best;
	private VModel model;
	private int start,nbOfPoints;
	private int lag;
	private ArrayList<String> date= new ArrayList<String>();
	private SummaryStatistics stat;
	
	public Output(String fileName,ArrayList<String> date, Map<dataType,ArrayList<Double>> initialData,VModel model, CandidateProgram best, int start, int nbOfPoints, int lag) {
		super();
		this.fileName = fileName;
		this.initialData = initialData;
		this.best = (GPCandidateProgram)best;
		this.model= model;
		this.processedData= new Double[500][lag];
		this.start= start;
		this.nbOfPoints= nbOfPoints;
		this.lag=lag;
		this.date=date;
		this.stat= new SummaryStatistics();
	}

	
	
	
public void writeOutput() throws Exception{
	processedData=this.getValues();
	if (this.fileName != null && this.processedData != null){
		 writer = new PrintStream(fileName + ".csv");
		
		 for(int i = start; i < start+this.nbOfPoints; i++){
			 
	     writer.println(date.get(i)+","+processedData[i-start][0]+","+initialData.get(dataType.vol).get(i+lag));
		 }
		
		 writer.close();
		
		}
}



public Double[][] getValues() {
	
	
	//int noPoints= initialData.get(dataType.ret).size();
	stat.clear();
	for (int i = start; i <start+this.nbOfPoints; i++) {
		
		model.getImpVol().setValue(initialData.get(dataType.imVol).get(i));
		model.getRealVol().setValue(initialData.get(dataType.vol).get(i));
		model.getRet().setValue(initialData.get(dataType.ret).get(i));
		model.getOpen().setValue(initialData.get(dataType.open).get(i));
		model.getHigh().setValue(initialData.get(dataType.high).get(i));
		model.getLow().setValue(initialData.get(dataType.low).get(i));
		model.getLast().setValue(initialData.get(dataType.close).get(i));
		model.getIntVol().setValue(initialData.get(dataType.intRet).get(i));
		model.maxVol.setLower(i-50);
		model.maxVol.setUpper(i);
		
		model.minVol.setLower(i-50);
		model.minVol.setUpper(i);
		
		model.maxRet.setLower(i-50);
		model.maxRet.setUpper(i);
		
		model.minRet.setLower(i-50);
		model.minRet.setUpper(i);
		
		model.avgVol.setLower(i-50);
		model.avgVol.setUpper(i);
		
		model.avgRet.setLower(i-50);
		model.avgRet.setUpper(i);
		
		model.dataVol.setLower(i-50);
		model.dataVol.setUpper(i);
		
		model.dataRet.setLower(i-50);
		model.dataRet.setUpper(i);
		
		processedData[i-start][0] = (Double) best.evaluate();
		
		double trueOutput = initialData.get(dataType.vol).get(i+lag);
		//System.out.println(processedData[i-start][0]+"  true:  "+trueOutput);
		stat.addValue(Math.pow(processedData[i-start][0]- trueOutput,2));
		
	}

	return processedData;
}




public String getFileName() {
	return fileName;
}




public SummaryStatistics getStat() {
	return stat;
}




public void setStat(SummaryStatistics stat) {
	this.stat = stat;
}




public void setFileName(String fileName) {
	this.fileName = fileName;
}

}
