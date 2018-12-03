package heuristic.routing.tsp;

public interface ITSPArrayOptimizer {

	double optimizeImplicit(double[][] distance, int[] station);

	double optimizeImplicit(double[][] distance, int[] station, int N);

}