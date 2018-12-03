package heuristic.annealing;

import java.util.Random;
import java.util.function.ToDoubleFunction;

import javax.annotation.Nonnull;

import heuristic.annealing.schedule.ITempSchedule;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.solution.ISolution;
import optimization.solution.neighborhood.INeighborGenerator;
import utilities.random.RNGProvider;

/**
 * {@link SimulatedAnnealing} is a optimization algorithm which tries to find
 * the function optimum. </br>
 * It simulates the industrial process of annealing metal. </br>
 * This approach is taken in order to try and get out of local optimum. </br>
 */
public class SimulatedAnnealing<I, T extends ISolution<T>> implements IOptimizationAlgorithm<T> {

	private final @Nonnull IDecoder<T, I> decoder;
	private final @Nonnull INeighborGenerator<T> neighborhood;
	private final @Nonnull T startSolution;
	private final @Nonnull ToDoubleFunction<I> function;
	private final @Nonnull ITempSchedule tempSchedule;

	/**
	 * Constructor
	 * 
	 * @param decoder
	 *            - decodes the solution so it can be applied to the given function
	 * @param neighborhood
	 *            - generates a neighbor of the current solution
	 * @param startSolution
	 * @param function
	 *            - function to be optimized
	 * @param tempSchedule
	 *            - temperature schedule
	 * @param minimize
	 *            - should the function be minimized or maximized
	 */
	public SimulatedAnnealing(@Nonnull IDecoder<T, I> decoder, @Nonnull INeighborGenerator<T> neighborhood,
			@Nonnull T startSolution, @Nonnull ToDoubleFunction<I> function, @Nonnull ITempSchedule tempSchedule) {
		this.decoder = decoder;
		this.neighborhood = neighborhood;
		this.startSolution = startSolution;
		this.function = function;
		this.tempSchedule = tempSchedule;
	}

	@Override
	public @Nonnull T run() {

		Random random = RNGProvider.getRandom();
		T solution = startSolution;
		double solutionValue = function.applyAsDouble(decoder.decode(solution));
		T neighbor = solution.clone();

		double temperature;
		while ((temperature = tempSchedule.getNextTemperature()) >= 1e-3) {
			neighborhood.randomNeighbor(solution, neighbor);
			double neighborValue = function.applyAsDouble(decoder.decode(neighbor));
			double change = neighborValue - solutionValue;

			if (change <= 0 || random.nextDouble() <= Math.exp(-1 * change / temperature)) {
				T solutionSwapHelp = solution;

				solution = neighbor;
				solutionValue = neighborValue;
				neighbor = solutionSwapHelp;
			}

		}
		
		return solution;
	}

}
