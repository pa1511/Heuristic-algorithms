package heuristic;

import java.util.function.ToDoubleFunction;

import javax.annotation.Nonnull;

import optimization.algorithm.IOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.fittnesEvaluator.IFitnessEvaluator;
import optimization.solution.ISolution;
import optimization.solution.neighborhood.INeighborGenerator;
import optimization.stopper.IOptimisationStopper;

/**
 * Generic greedy algorithm implementation. </br>
 * This can be used for a iterative greedy search. </br>
 *
 */
public class GreedyAlgorithm<I,T extends ISolution<T>>  implements IOptimizationAlgorithm<T> {

	private final @Nonnull IDecoder<T,I> decoder;
	private final @Nonnull IFitnessEvaluator<T> evaluator;
	private final @Nonnull INeighborGenerator<T> neighborhood;
	private final @Nonnull T startSolution;
	private final @Nonnull ToDoubleFunction<I> function;
	private final @Nonnull IOptimisationStopper<T> stopper;
	
	public GreedyAlgorithm(IDecoder<T,I> decoder, INeighborGenerator<T> neighborhood, IFitnessEvaluator<T> evaluator, IOptimisationStopper<T> stopper,ToDoubleFunction<I> function, T startSolution) {
		this.decoder = decoder;
		this.evaluator = evaluator;
		this.neighborhood = neighborhood;
		this.stopper = stopper;
		this.startSolution = startSolution;
		this.function = function;
	}
	
	@Override
	public @Nonnull T run() {
		
		T solution = startSolution;
		double solutionValue = function.applyAsDouble(decoder.decode(solution));
		double solutionFitness = evaluator.evaluate(solution, solutionValue);
		T neighbor = solution.clone();
		
		do{			
			neighborhood.randomNeighbor(solution,neighbor);			
			double neighborValue = function.applyAsDouble(decoder.decode(neighbor));
			double neighborFitness = evaluator.evaluate(neighbor, neighborValue);
			
			if(neighborFitness>=solutionFitness){
				T solutionSwapHelp = solution;
				
				solution = neighbor;
				solutionValue = neighborValue;
				solutionFitness = neighborFitness;
				
				neighbor = solutionSwapHelp;
			}
			
			//neighbor was changed (mutated or is solution) and should be reset
			neighbor.copy(solution);
			
		}while(!stopper.shouldStop(solution, solutionFitness, solutionValue));
				
		return solution;
	}

}
