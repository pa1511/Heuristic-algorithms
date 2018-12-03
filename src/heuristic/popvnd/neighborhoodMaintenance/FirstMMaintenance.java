package heuristic.popvnd.neighborhoodMaintenance;

import java.util.List;

import javax.annotation.Nonnegative;

public class FirstMMaintenance<T> extends NeighborhooddMaintenanceAdapter<T> {
	
	private final @Nonnegative int M;

	public FirstMMaintenance(int M) {
		this.M = M;
	}

	@Override
	public boolean addNeighbor(T[] population, T unit, T neighbor, List<T> neighbors) {
		neighbors.add(neighbor);
		return neighbors.size()>=M;
	}
	
}
