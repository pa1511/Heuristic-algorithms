package heuristic.popvnd.neighborhoodMaintenance;

import java.util.Comparator;

import javax.annotation.Nonnull;

public class QualityBasedNeighborhooddMaintenanceAdapter<T> extends NeighborhooddMaintenanceAdapter<T> {

	protected final @Nonnull Comparator<T> solutionQualityComaprator;
	
	public QualityBasedNeighborhooddMaintenanceAdapter(@Nonnull Comparator<T> solutionQualityComaprator) {
		this.solutionQualityComaprator = solutionQualityComaprator;
	}
}
