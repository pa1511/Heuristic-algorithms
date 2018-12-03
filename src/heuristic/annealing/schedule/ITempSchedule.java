package heuristic.annealing.schedule;

import javax.annotation.Nonnegative;

import heuristic.annealing.SimulatedAnnealing;

/**
 * {@link ITempSchedule} can be used with the {@link SimulatedAnnealing} algorithm. </br>
 * A implementation of this class provides a temperature reduction schedule.</br>
 *
 */
public interface ITempSchedule {
	/**
	 * Returns next temperature to use in the {@link SimulatedAnnealing} algorithm
	 */
	public double getNextTemperature();
	/**
	 * Returns inner loop counter - number of iterations with the current temperature </br>
	 */
	public @Nonnegative int getInnerLoopCounter();
	/**
	 * Returns outer loop counter - number of temperatures went through </br>
	 */
	public @Nonnegative int getOuterLoopCounter();

}
