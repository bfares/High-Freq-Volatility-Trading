package org.brainswap.GP.functions;

import java.util.ArrayList;




import org.epochx.epox.Literal;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;


/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */


import org.brainswap.main.AbstractOperations.dataType;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Defines an Average Fct that computes the average of the provided data over an n period of time.
 * n is an integer ephemeral random constant (ERC). An ERC is a literal with
 * a value which is randomly generated upon construction. This implementation
 * will generate an integer between the lower and upper bounds provided. All
 * values between the two bounds (inclusive), can appear with equal probability.
 * As with all nodes, instances may be constructed in any of 3 ways:
 * <ul>
 * <li>constructor - the new instance will be initialised with a value of
 * <code>null</code>.</li>
 * <li>clone method - will return an instance with a value equal to the cloned
 * value.</li>
 * <li>newInstance method - will return a new instance with a new, randomly
 * generated value.</li>
 * </ul>
 * 
 * @see BooleanERC
 * @see DoubleERC
 */
public class Average extends Literal {

	private RandomNumberGenerator rng;

	private ArrayList<Double> data;
	private int period;
	private SummaryStatistics stat= new SummaryStatistics();
	// The inclusive bounds.
	private int upper;
	private int lower;

	private dataType type;

	/**
	 * Constructs a new <code>IntegerERC</code> with a value of
	 * <code>null</code>. The given random number generator will be be used to
	 * generate a new value if the <code>newInstance</code> method is used.
	 * @param type 
	 * @param data 
	 * 
	 * @param rng the random number generator to use if randomly generating an
	 *        integer value.
	 * @param lower the inclusive lower bound of values that are generated.
	 * @param upper the inclusive upper bound of values that are generated.
	 */
	public  Average(dataType type,  ArrayList<Double> data, final RandomNumberGenerator rng, final int lower, final int upper) {
		super(null);
		
		
		this.data= data;
		this.type= type;
		if (rng == null) {
			throw new IllegalArgumentException("random generator must not be null");
		}

		this.rng = rng;
		this.lower = lower;
		this.upper = upper;
		this.period= generateValue();
//		// Set its value.
		setValue(evaluate(period));
	}

	/**
	 * Constructs a new <code>IntegerERC</code> node with a randomly generated
	 * value, selected using the random number generator. The value will be
	 * randomly selected with an equal probability from the set of values
	 * between the lower and upper bounds.
	 * 
	 * @return a new <code>IntegerERC</code> instance with a randomly generated
	 *         value.
	 */
	@Override
	public  Average newInstance() {
		final  Average erc = (Average) super.newInstance();
		this.period= generateValue();
		erc.setValue(evaluate(period));

		return erc;
	}

	/**
	 * Generates and returns a new integer value for use in a new
	 * <code>IntegerERC</code> instance. This implementation will return a value
	 * selected randomly from the set of values between the lower and upper
	 * bounds, inclusively.
	 * 
	 * @return an integer value to be used as the value of a new IntegerERC
	 *         instance.
	 */
	
	public Double evaluate(int period) {
		if (data == null){
			return 0.0;
		}
		for(int i=upper; i>=upper-period; i--){
			stat.addValue(data.get(i));
		}
		return stat.getMean();
	}
	
	protected int generateValue() {
		if (rng == null) {
			throw new IllegalStateException("random number generator must not be null");
		}
		final int range;
		
//		if ( data== null || data.size()<=1 ){
//			return 0;
//		}
//		else
		 range = upper - lower-1;

		return (rng.nextInt(range));//  + lower);
	}

	/**
	 * Returns the random number generator that is currently being used to
	 * generate integer values for new <code>IntegerERC</code> instances.
	 * 
	 * @return the random number generator
	 */
	public RandomNumberGenerator getRNG() {
		return rng;
	}

	/**
	 * Sets the random number generator to be used for generating the integer
	 * value of new <code>IntegerERC</code> instances.
	 * 
	 * @param rng the random number generator to set
	 */
	public void setRNG(final RandomNumberGenerator rng) {
		this.rng = rng;
	}

	/**
	 * Returns the lower bound of the newly generated values.
	 * 
	 * @return the lower bound of values.
	 */
	public int getLower() {
		return lower;
	}

	/**
	 * Sets the inclusive lower bound for newly generated values.
	 * 
	 * @param lower the lower bound for values.
	 */
	public void setLower(final int lower) {
		this.lower = lower;
	}

	/**
	 * Returns the upper bound of the newly generated values.
	 * 
	 * @return the upper bound of values.
	 */
	public int getUpper() {
		return upper;
	}

	/**
	 * Sets the inclusive upper bound for newly generated values.
	 * 
	 * @param upper the upper bound for values.
	 */
	public void setUpper(final int upper) {
		this.upper = upper;
	}


public ArrayList<Double> getData() {
		return data;
	}

	public void setData(ArrayList<Double> data) {
		this.data = data;
	}

@Override
public String getIdentifier() {
	
	return "Avg("+period+")";
}

@Override
public String toString() {
	return "Avg_"+this.type+"("+period+")";
}
}
