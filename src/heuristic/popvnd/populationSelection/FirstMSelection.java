package heuristic.popvnd.populationSelection;

import java.util.List;

public class FirstMSelection<T> extends IPopulationSelection_V2<T> {

	@Override
	public void select(T[] population, T[] nextPopulation, List<T> neighbors) {
		int i=0;
		for(int size=neighbors.size();i<nextPopulation.length && i<size;i++) {
			nextPopulation[i] = neighbors.get(i);
		}
		while(i<nextPopulation.length) {
			nextPopulation[i]=null;
			i++;
		}
	}

}
