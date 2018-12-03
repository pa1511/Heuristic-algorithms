package heuristic.popvnd.neighborAcceptanceTest;

import java.util.Comparator;

public class BetterThanBestInPopulation<T> extends NeighborAcceptanceTest<T>{

	private T best = null;

	public BetterThanBestInPopulation(Comparator<T> solutionQualityComaprator) {
		super(solutionQualityComaprator);
	}

	@Override
	public void startingNegihborhoodGeneration(T[] population) {
		best = population[0];
		for(int i=1; i<population.length;i++) {
			if(population[i]==null)// there shouldn't be any others after the first null
				break;
			
			if(solutionQualityComaprator.compare(best, population[i])>0)
				best = population[i];
		}
	}

	@Override
	public boolean shouldAcceptTest(T[] population, T unit, T neighbor) {
		return solutionQualityComaprator.compare(best, neighbor)>0;
	}

}
