package heuristic.routing.tsp;

import java.util.List;

import javax.annotation.Nonnull;

public class CompositeTSPArrayOptimizer implements ITSPArrayOptimizer{

	private final @Nonnull List<ITSPArrayOptimizer> optimizers;

	public CompositeTSPArrayOptimizer(List<ITSPArrayOptimizer> optimizers) {
		this.optimizers = optimizers;
	}
	
	
	@Override
	public double optimizeImplicit(double[][] distance, int[] station) {
		
		double result = 0;
		
		for(ITSPArrayOptimizer optimizer:optimizers)
			result = optimizer.optimizeImplicit(distance, station);
		
		
		return result;
	}

	@Override
	public double optimizeImplicit(double[][] distance, int[] station, int N) {
		double result = 0;
		
		for(ITSPArrayOptimizer optimizer:optimizers)
			result = optimizer.optimizeImplicit(distance, station, N);
		
		
		return result;
	}

}
