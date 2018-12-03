package heuristic.annealing.schedule;

import javax.annotation.Nonnegative;

import heuristic.annealing.SimulatedAnnealing;

/**
 * A geometric order reduction of temperature.  </br>
 * {@link GeometricTempSchhedule} implements {@link ITempSchedule} and can be used in {@link SimulatedAnnealing} as a temperature reduction schedule. </br>
 *
 */
public class GeometricTempSchhedule implements ITempSchedule{

	private final double initialT;
	private final double alpha;
	private final @Nonnegative int outerLimit;
	private final @Nonnegative int innerLimit;

	private double currentTemperature;
	private @Nonnegative int outer = 0;
	private @Nonnegative int inner = 0;

	/**
	 * Class constructor
	 * @param initialT - initial temperature of the annealing system 
	 * @param alpha - geometric order parameter
	 * @param innerLimit - maximum number of iterations with one temperature
	 * @param outerLimit -  number of temperature reductions to do
	 */
	public GeometricTempSchhedule(double initialT, double alpha,@Nonnegative int innerLimit,@Nonnegative int outerLimit) {
		this.initialT = initialT;
		this.alpha = alpha;
		this.innerLimit = innerLimit;
		this.outerLimit = outerLimit;
		
		currentTemperature = initialT;
	}
	
	@Override
	public double getNextTemperature() {
		
		if(inner>=innerLimit){
			outer++;
			inner = 0;
			
			if(outer>=outerLimit)
				return 0;
			
			currentTemperature = Math.pow(alpha,outer)*initialT;
		}
		inner++;
		return currentTemperature;
	}

	@Override
	public @Nonnegative int getInnerLoopCounter() {
		return inner;
	}

	@Override
	public @Nonnegative int getOuterLoopCounter() {
		return outer;
	}

}
