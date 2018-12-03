package heuristic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import function.IFunction;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.fittnesEvaluator.IFitnessEvaluator;
import optimization.solution.ISolution;
import optimization.solution.neighborhood.INeighborhood;
import optimization.solution.neighborhood.selection.IPopulationSelection;
import optimization.utility.OptimizationAlgorithmsUtility;
import utilities.PArrays;
import utilities.streamAndParallelization.AbstractParallelizable;
import utilities.streamAndParallelization.PStreams;

public class PopulationBasedVariableNeighborhoodDescent<T extends ISolution<T>,D> extends AbstractParallelizable implements IOptimizationAlgorithm<T>{
	
	private final T startSolution;
	private final int maxPopulationSize;
	private final IDecoder<T, D> decoder;
	private final List<INeighborhood<T>> neghborhoods;
	private final boolean shuffleNegihborhoods;
	private final IFitnessEvaluator<T> evaluator;
	private final IPopulationSelection<T> populationSelection;
	private final IFunction<D> function;

	public PopulationBasedVariableNeighborhoodDescent(T startSolution, int maxPopulationSize, IDecoder<T, D> decoder,
			List<INeighborhood<T>> neghborhoods,boolean shuffleNegihborhoods,  IFitnessEvaluator<T> evaluator, 
			IPopulationSelection<T> populationSelection, IFunction<D> function) {
				this.startSolution = startSolution;
				this.maxPopulationSize = maxPopulationSize;
				this.decoder = decoder;
				this.neghborhoods = neghborhoods;
				this.shuffleNegihborhoods = shuffleNegihborhoods;
				this.evaluator = evaluator;
				this.populationSelection = populationSelection;
				this.function = function;
	}

	@Override
	public T run() {

		//Define population		
		T[] population = PArrays.getGenericArray(maxPopulationSize, startSolution.getClass());
		double[] populationFitness = new double[population.length];
		Arrays.fill(populationFitness, Double.NEGATIVE_INFINITY);
		
		T[] nextPopulation = PArrays.getGenericArray(maxPopulationSize, startSolution.getClass());
		double[] nextPopulationFitness = new double[nextPopulation.length];
		Arrays.fill(nextPopulationFitness, Double.NEGATIVE_INFINITY);
		
		
		population[0] = startSolution;
		populationFitness[0] = evaluator.evaluate(population[0], function.applyAsDouble(decoder.decode(population[0])));

		//Start algorithm
		int k = 0;
		
		int neghborhoodsSize = neghborhoods.size();
		do {
			
			//get neighbors
			INeighborhood<T> neighborhood = neghborhoods.get(k);
			List<T> neighbors = Arrays.stream(population)
					.filter(unit->unit!=null)
					.parallel()
					.map(unit->neighborhood.neighborhood(unit))
					.flatMap(s->s.stream())
					.collect(Collectors.toList());
			
			//calculate their quality
			double[] qualities = new double[neighbors.size()];
			PStreams.getIndexStream(qualities.length, parallel).forEach(i->{
				T chromosome = neighbors.get(i);
				qualities[i] = evaluator.evaluate(chromosome, function.applyAsDouble(decoder.decode(chromosome)));
			});
			
			//select improving if possible
			populationSelection.select(population, populationFitness, nextPopulation, nextPopulationFitness, neighbors, qualities);
			boolean noNextPopulation = nextPopulation[0]==null;
			
			if(noNextPopulation) {
				k++;
			}
			else {
				
				T[] swapPopulation = population;
				population = nextPopulation;
				nextPopulation = swapPopulation;
				
				double[] swapFitness = populationFitness;
				populationFitness = nextPopulationFitness;
				nextPopulationFitness = swapFitness;
				
				k = 0;
				if(shuffleNegihborhoods)
					Collections.shuffle(neghborhoods);
			}
			
			
		}while(k!=neghborhoodsSize);
		
		return OptimizationAlgorithmsUtility.getBestSolution(population, populationFitness);
	}

}
