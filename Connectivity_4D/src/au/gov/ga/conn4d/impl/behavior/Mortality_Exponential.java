package au.gov.ga.conn4d.impl.behavior;

import au.gov.ga.conn4d.Mortality;
import au.gov.ga.conn4d.Particle;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.RandomSeedTable;

/**
 * Implements mortality using an exponential function.
 * 
 * @author Johnathan Kool
 */

public class Mortality_Exponential implements Mortality {

	private int seed = RandomSeedTable.getSeedAtRowColumn(
			Uniform.staticNextIntFromTo(0, Integer.MAX_VALUE),
			Uniform.staticNextIntFromTo(0, RandomSeedTable.COLUMNS));
	private RandomEngine re = new MersenneTwister64(seed);
	private Uniform uni = new Uniform(re);
	private long timeInterval;
	private double mrate;

	public Mortality_Exponential(double mrate) {
		this.mrate = mrate;
	}

	/**
	 * Applies probabilistic mortality the given particle
	 */

	@Override
	public void apply(Particle p) {

		if (uni.nextDouble() > Math.exp(-1.0 * mrate * timeInterval)) {
			p.setDead(true);
		}
	}

	/**
	 * Applies probabilistic mortality the given particle
	 */

	@Override
	public void apply(Particle p, double cycles) {

		if (uni.nextDouble() > Math.exp(-1.0 * mrate * cycles * timeInterval)) {
			p.setDead(true);
		}
	}

	/**
	 * Retrieves the mortality rate
	 * 
	 * @return - the mortality rate
	 */

	public double getMrate() {
		return mrate;
	}

	/**
	 * Returns the time interval over which mortality occurs
	 * 
	 * @return - the time interval over which mortality occurs
	 */

	public long getTimeIntervalMillis() {
		return timeInterval;
	}

	/**
	 * Sets the mortality rate
	 * 
	 * @param mrate
	 *            - the mortality rate
	 */

	public void setMrate(double mrate) {
		this.mrate = mrate;
	}

	/**
	 * Sets the time interval over which mortality occurs
	 */

	@Override
	public void setTimeInterval(long millis) {
		this.timeInterval = millis;
	}

	/**
	 * Generates a copy of the class instance
	 */

	@Override
	public Mortality_Exponential clone() {
		Mortality_Exponential me = new Mortality_Exponential(mrate);
		me.setTimeInterval(timeInterval);
		return me;
	}
}