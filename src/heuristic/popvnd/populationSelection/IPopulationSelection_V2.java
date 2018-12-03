package heuristic.popvnd.populationSelection;

import java.util.List;

public abstract class IPopulationSelection_V2<T> {
	public abstract void select(T[] population, T[] nextPopulation, List<T> neighbors);
}
