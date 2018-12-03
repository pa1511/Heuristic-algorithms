package heuristic.popvnd.neighborhoodMaintenance;

import java.util.List;

public class AcceptAllMaintenance<T> extends NeighborhooddMaintenanceAdapter<T> {

	@Override
	public boolean addNeighbor(T[] population, T unit, T neighbor, List<T> neighbors) {
		neighbors.add(neighbor);
		return false;
	}

}
