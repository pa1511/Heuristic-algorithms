package heuristic.popvnd.neighborAcceptanceTest;

import java.util.Comparator;

import javax.annotation.Nonnull;

public abstract class NeighborAcceptanceTest<T> {
	
	protected final @Nonnull Comparator<T> solutionQualityComaprator;
	
	public NeighborAcceptanceTest(@Nonnull Comparator<T> solutionQualityComaprator) {
		this.solutionQualityComaprator = solutionQualityComaprator;
	}

	public abstract void startingNegihborhoodGeneration(T[] population); 
	public abstract boolean shouldAcceptTest(T[] population, T unit, T neighbor);	
}
