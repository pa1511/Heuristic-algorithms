package heuristic.popvnd.neighborhoodMaintenance;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class RandomMMaintenance<T> extends NeighborhooddMaintenanceAdapter<T> {
	
	private final @Nonnegative int M;
	private final @Nonnull Random random = new Random();
	private final @Nonnull ThreadLocal<int[]> nProvider = ThreadLocal.withInitial(()->new int[] {0});

	public RandomMMaintenance(int M) {
		this.M = M;
	}

	@Override
	public void startingNegihborhoodGeneration(T[] population) {
		nProvider.get()[0] = 0;
	}
	
	@Override
	public boolean addNeighbor(T[] population, T unit, T neighbor, List<T> neighbors) {
		int[] n = nProvider.get();
		n[0]++;
		
		if(n[0]<=M) {
			neighbors.add(neighbor);
		}
		else {
			double chance = ((double)M)/((double)n[0]);
			if(random.nextDouble()<=chance) {
				neighbors.remove(random.nextInt(M));
				neighbors.add(neighbor);
			}
		}
		return false;
	}
	
}
