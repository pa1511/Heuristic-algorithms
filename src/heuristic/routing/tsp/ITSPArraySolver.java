package heuristic.routing.tsp;

public interface ITSPArraySolver {

	double solveImplicit(double[][] distances, int[] station);

	/**
	 * Solves TSP for the first N stations in station array. </br>
	 * We assume a symmetric distances matrix.
	 */
	double solveImplicit(double[][] distances, int[] station, int N);

}