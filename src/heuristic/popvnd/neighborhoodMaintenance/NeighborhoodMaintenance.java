package heuristic.popvnd.neighborhoodMaintenance;

import java.util.List;

public interface NeighborhoodMaintenance<T> {
	public void startingNegihborhoodGeneration(T[] population); 
	/**
	 * Returns true if the generation should stop
	 */
	public boolean addNeighbor(T[] population, T unit, T neighbor, List<T> neighbors);
}
