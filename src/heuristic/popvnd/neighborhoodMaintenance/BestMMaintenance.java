package heuristic.popvnd.neighborhoodMaintenance;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class BestMMaintenance<T> extends QualityBasedNeighborhooddMaintenanceAdapter<T> {

	private final @Nonnegative int M;
	private final @Nonnull ThreadLocal<int[]> worstIdProvider = ThreadLocal.withInitial(()->new int[] {-1});
	
	public BestMMaintenance(int M, Comparator<T> solutionQualityComaprator) {
		super(solutionQualityComaprator);
		this.M = M;
	}
	
	@Override
	public boolean addNeighbor(T[] population, T unit, T neighbor, List<T> neighbors) {
		int size = neighbors.size();
		if(size<M) {
			neighbors.add(neighbor);
		}
		else {
			int[] worstIDMem = worstIdProvider.get();
			int worstID = worstIDMem[0];
			if(worstID==-1) {
				worstID = 0;
				T worst = neighbors.get(worstID);
				
				for(int i=1;i<size;i++) {
					T o2 = neighbors.get(i);
					if(solutionQualityComaprator.compare(worst, o2)<0) {
						worstID = i;
						worst = o2;
					}
				}
				worstIDMem[0]=worstID;
			}
			
			T worst = neighbors.get(worstID);			
			if(solutionQualityComaprator.compare(worst, neighbor)>0) {
				neighbors.remove(worstID);
				neighbors.add(neighbor);
				worstIDMem[0] = -1;
			}
		}
		return false;
	}
	
}
