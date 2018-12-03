package heuristic.routing.cvrp;

import java.util.Arrays;

import heuristic.routing.Route;
import heuristic.routing.tsp.ClarkeWrightTSPSolver;
import heuristic.routing.tsp.CompositeTSPArrayOptimizer;
import heuristic.routing.tsp.CompositeTSPSolver;
import heuristic.routing.tsp.ITSPArrayOptimizer;
import heuristic.routing.tsp.ITSPArraySolver;
import heuristic.routing.tsp.TSPArraySolution2OptSwapOptimizer;
import heuristic.routing.tsp.TSPArraySolutionElementSwapOptimizer;
import heuristic.routing.tsp.TSPH2ArraySolver;

public class CVRPUtility {
	
	private static final ITSPArraySolver TSP_SOLVER = new CompositeTSPSolver(Arrays.asList(
				new ClarkeWrightTSPSolver(),
				new TSPH2ArraySolver()
			));
//	private static final ITSPArrayOptimizer TSP_OPTIMIZER1 = new CompositeTSPArrayOptimizer(Arrays.asList(
//				new TSPArraySolutionElementSwapOptimizer(),
//				new TSPArraySolution2OptSwapOptimizer()
//			));
	
	public static void optimizeRoute(Route<?> route) {
		double length = TSP_SOLVER.solveImplicit(route.getDistances(), route.getArray(), route.size());
		//length += TSP_OPTIMIZER1.optimizeImplicit(route.getDistances(), route.getArray(), route.size());
		route.setLength(length);
	}
	
	
}
