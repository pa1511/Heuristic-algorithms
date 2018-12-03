package heuristic.popvnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import heuristic.popvnd.neighborhood.INeighborhood_V2;
import heuristic.popvnd.populationSelection.IPopulationSelection_V2;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.solution.ISolution;
import optimization.startSolutionGenerator.IStartSolutionGenerator;
import utilities.PArrays;
import utilities.streamAndParallelization.AbstractParallelizable;

public class ImmuneSystemVariableNeighborhoodDescent_V2<T extends ISolution<T>,D> extends AbstractParallelizable implements IOptimizationAlgorithm<T>{
	
	//Constants
	private final @Nonnegative int maxPopulationSize;
	private final boolean shuffleNegihborhoods;
	//Strategies
	private final @Nonnull IStartSolutionGenerator<T> startSolutionGenerator;
	private final @Nonnull List<INeighborhood_V2<T>> neghborhoods;
	private final @Nonnull IPopulationSelection_V2<T> populationSelection;
	private final @Nonnull Function<T[], T> selectFinalSolution;
	//Other
	private final @Nonnull ExecutorService executorService;

	public ImmuneSystemVariableNeighborhoodDescent_V2(@Nonnegative int maxPopulationSize, boolean shuffleNegihborhoods,
			@Nonnull IStartSolutionGenerator<T> startSolutionGenerator, 
			@Nonnull List<INeighborhood_V2<T>> neghborhoods, @Nonnull IPopulationSelection_V2<T> populationSelection,
			@Nonnull Function<T[],T> selectFinalSolution,
			@Nonnull ExecutorService executorService) {
		this.maxPopulationSize = maxPopulationSize;
		this.shuffleNegihborhoods = shuffleNegihborhoods;
		//
		this.startSolutionGenerator = startSolutionGenerator;
		this.neghborhoods = neghborhoods;
		this.populationSelection = populationSelection;
		this.selectFinalSolution = selectFinalSolution;
		//
		this.executorService = executorService;
	}

	@Override
	public T run() {
		try {
			// Define population
			T startSolution = startSolutionGenerator.generate();

			T[] population = PArrays.getGenericArray(maxPopulationSize, startSolution.getClass());

			T[] nextPopulation = PArrays.getGenericArray(maxPopulationSize, startSolution.getClass());

			population[0] = startSolution;

			// Start algorithm
			int k = 0;
			int neghborhoodsSize = neghborhoods.size();

			while (k != neghborhoodsSize) {

				// get neighbors
				INeighborhood_V2<T> neighborhood = neghborhoods.get(k);
				List<Future<List<T>>> future = new ArrayList<>();
				for (int i = 0; i < population.length; i++) {
					if (population[i] != null) {
						final T[] fPopulation = population;
						final int fi = i;
						Future<List<T>> result = executorService
								.submit(() -> neighborhood.neighborhood(fPopulation, fi));
						future.add(result);
					}
				}
				List<T> neighbors = new ArrayList<>();
				for (Future<List<T>> neighborhhodResult : future) {
					neighbors.addAll(neighborhhodResult.get());
				}

				// select improving if possible
				populationSelection.select(population, nextPopulation, neighbors);
				boolean noNextPopulation = nextPopulation[0] == null;

				if (noNextPopulation) {
					k++;
				} else {

					T[] swapPopulation = population;
					population = nextPopulation;
					nextPopulation = swapPopulation;

					k = 0;
					if (shuffleNegihborhoods)
						Collections.shuffle(neghborhoods);
				}
			}

			return selectFinalSolution.apply(population);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
