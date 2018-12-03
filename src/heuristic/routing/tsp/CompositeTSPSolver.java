package heuristic.routing.tsp;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

public class CompositeTSPSolver  implements ITSPArraySolver{

	
	private final @Nonnull List<ITSPArraySolver> solvers;
	private final @Nonnull Random random = new Random();

	public CompositeTSPSolver(@Nonnull List<ITSPArraySolver> solvers) {
		this.solvers = solvers;
	}
	
	@Override
	public double solveImplicit(double[][] distances, int[] station) {
		ITSPArraySolver solver = getSolver();
		return solver.solveImplicit(distances, station);
	}

	@Override
	public double solveImplicit(double[][] distances, int[] station, int N) {
		ITSPArraySolver solver = getSolver();
		return solver.solveImplicit(distances, station, N);
	}
	
	private ITSPArraySolver getSolver() {
		return solvers.get(random.nextInt(solvers.size()));
	}

}
