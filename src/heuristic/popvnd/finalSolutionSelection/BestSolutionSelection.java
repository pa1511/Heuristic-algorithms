package heuristic.popvnd.finalSolutionSelection;

import java.util.Comparator;
import java.util.function.Function;

import javax.annotation.Nonnull;

public class BestSolutionSelection<T> implements Function<T[], T>{

	private final @Nonnull Comparator<T> qualityComparator;

	public BestSolutionSelection(Comparator<T> qualityComparator) {
		this.qualityComparator = qualityComparator;
	}
	
	@Override
	public T apply(T[] population) {
		
		T best = population[0];
		for(int i=1; i<population.length;i++) {
			if(population[i]==null)
				break;
			if(qualityComparator.compare(best, population[i])>0)
				best = population[i];
		}
		
		return best;
	}

}
