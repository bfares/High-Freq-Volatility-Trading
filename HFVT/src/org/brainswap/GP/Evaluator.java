package org.brainswap.GP;


import java.util.ArrayList;
import java.util.List;
import org.brainswap.GP.functions.*;
import org.epochx.epox.*;
import org.epochx.tools.eval.MalformedProgramException;
public class Evaluator  {
private Node processedNode;
private String lisp;
private EpoxParser parser;
public Evaluator(String lisp) {
	super();
	this.lisp = lisp;
	parser= new EpoxParser();
	
	parser.declareFunction("LN", new LogProtectedFunction());
	parser.declareFunction("SQRT", new SquareRootProtectedFunction());
	parser.declareFunction("EXP", new ExponentialProtectedFunction());
	parser.declareFunction("Avg_ret(43)",Average.class);
	parser.declareFunction("Data_ret(48)",Data.class);
	parser.declareFunction("Max_ret(20)",Max.class);
	parser.declareFunction("Min_ret(1)",Min.class);
	 addVar();
}

	public Node parseToNode() throws MalformedProgramException{
	processedNode=parser.parse(lisp);
	return processedNode;
}

	public void addVar(){
		Variable open, high, low,last,logRet,intRet;
		 
		logRet = new Variable("dailyRet", Double.class);
		open = new Variable("open", Double.class);
		high = new Variable("high", Double.class);
		low = new Variable("low", Double.class);
		last = new Variable("last", Double.class);
		intRet = new Variable("intRet", Double.class);
		 List<Variable> syntax = new ArrayList<Variable>();
		

		    syntax.add(logRet);
		    syntax.add( open);
		    syntax.add(high);
		    syntax.add(low);
		    syntax.add(last);
		    syntax.add(intRet);
		    
		    parser.declareVariables(syntax);
		   
	
}
}
