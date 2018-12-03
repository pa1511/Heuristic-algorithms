package heuristic.popvnd.populationSelection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

public class BestMSelection<T> extends IPopulationSelection_V2<T> {

	private final @Nonnull Comparator<T> solutionQualityComaprator;
	private final @Nonnull FirstMSelection<T> selection = new FirstMSelection<>();

	public BestMSelection(@Nonnull Comparator<T> solutionQualityComaprator) {
		this.solutionQualityComaprator = solutionQualityComaprator; 
	}
	
	@Override
	public void select(T[] population, T[] nextPopulation, List<T> neighbors) {
		if(neighbors.size()>nextPopulation.length)
			Collections.sort(neighbors, solutionQualityComaprator);
		selection.select(population, nextPopulation, neighbors);
	}

}
