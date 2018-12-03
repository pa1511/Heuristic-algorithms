package heuristic;

import java.util.Collections;
import java.util.List;

import function.IFunction;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.fittnesEvaluator.IFitnessEvaluator;
import optimization.solution.ISolution;
import optimization.solution.neighborhood.INeighborhood;
import optimization.solution.neighborhood.selection.INeighborSelection;
import utilities.streamAndParallelization.AbstractParallelizable;
import utilities.streamAndParallelization.PStreams;

public class VariableNeighborhoodDescent<T extends ISolution<T>,D> extends AbstractParallelizable implements IOptimizationAlgorithm<T>{
	
	private final T startSolution;
	private final IDecoder<T, D> decoder;
	private final List<INeighborhood<T>> neghborhoods;
	private final boolean shuffleNeighborhoods;
	private final IFitnessEvaluator<T> evaluator;
	private final INeighborSelection<T> neighborhoodSelection;
	private final IFunction<D> function;

	public VariableNeighborhoodDescent(T startSolution, IDecoder<T, D> decoder,
			List<INeighborhood<T>> neghborhoods, boolean shuffleNeighborhoods, IFitnessEvaluator<T> evaluator, 
			INeighborSelection<T> neighborhoodSelection, IFunction<D> function) {
				this.startSolution = startSolution;
				this.decoder = decoder;
				this.neghborhoods = neghborhoods;
				this.shuffleNeighborhoods = shuffleNeighborhoods;
				this.evaluator = evaluator;
				this.neighborhoodSelection = neighborhoodSelection;
				this.function = function;
	}

	@Override
	public T run() {
		
		T solution = startSolution;
		double solutionQuality = evaluator.evaluate(solution, function.applyAsDouble(decoder.decode(solution)));
		int k = 0;
		
		int neghborhoodsSize = neghborhoods.size();
		while(k!=neghborhoodsSize){
			
			//get neighbors
			List<T> neighbors = neghborhoods.get(k).neighborhood(solution);
			
			//calculate their quality
			double[] qualities = new double[neighbors.size()];
			PStreams.getIndexStream(qualities.length, parallel).forEach(i->{
				T chromosome = neighbors.get(i);
				qualities[i] = evaluator.evaluate(chromosome, function.applyAsDouble(decoder.decode(chromosome)));
			});
			
			//select improving if possible
			int nextId = neighborhoodSelection.select(solution, solutionQuality, neighbors, qualities);
			if(nextId==-1) {
				k++;
			}
			else {
				solution.copy(neighbors.get(nextId));
				solutionQuality = qualities[nextId];
				k = 0;
				if(shuffleNeighborhoods)
					Collections.shuffle(neghborhoods);
			}
			
			
		}
		
		return solution;
	}

}
