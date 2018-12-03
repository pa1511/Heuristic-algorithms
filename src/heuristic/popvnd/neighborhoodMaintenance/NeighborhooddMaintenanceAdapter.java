package heuristic.popvnd.neighborhoodMaintenance;

import java.util.List;

public class NeighborhooddMaintenanceAdapter<T> implements NeighborhoodMaintenance<T>{

	@Override
	public void startingNegihborhoodGeneration(T[] population) {
	}

	@Override
	public boolean addNeighbor(T[] population, T unit, T neighbor, List<T> neighbors) {
		return false;
	}

}
