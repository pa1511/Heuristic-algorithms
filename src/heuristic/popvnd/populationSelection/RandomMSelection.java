package heuristic.popvnd.populationSelection;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class RandomMSelection<T> extends IPopulationSelection_V2<T> {
	
	private final @Nonnull FirstMSelection<T> selection = new FirstMSelection<>();

	@Override
	public void select(T[] population, T[] nextPopulation, List<T> neighbors) {
		if(neighbors.size()>nextPopulation.length)
			Collections.shuffle(neighbors);
		selection.select(population, nextPopulation, neighbors);
	}

}
