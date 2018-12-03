package heuristic.popvnd.neighborhood;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import heuristic.popvnd.neighborAcceptanceTest.NeighborAcceptanceTest;
import heuristic.popvnd.neighborhoodMaintenance.NeighborhoodMaintenance;

public abstract class INeighborhood_V2<T> {
	
	protected final @Nonnull NeighborAcceptanceTest<T> neighborAcceptanceTest;
	protected final @Nonnull NeighborhoodMaintenance<T> neighborhoodGenerationStopTest;

	public INeighborhood_V2(@Nonnull NeighborAcceptanceTest<T> neighborAcceptanceTest,
			@Nonnull NeighborhoodMaintenance<T> neighborhoodGenerationStopTest) {
				this.neighborAcceptanceTest = neighborAcceptanceTest;
				this.neighborhoodGenerationStopTest = neighborhoodGenerationStopTest;
	}

	public List<T> neighborhood(T[] population, int id) {
		List<T> neighbors = new ArrayList<>();
		neighborhood(population, id, neighbors);
		return neighbors;
	}
	
	public abstract void neighborhood(T[] population, int id, List<T> neighbors);	
	
}
