package heuristic.routing.cvrp.algorithm;

import java.util.List;

import javax.annotation.Nonnull;

import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPSolutionGenerator;
import utilities.random.RNGProvider;

public class CompositeCVRPSolutionGenerator implements ICVRPSolutionGenerator{

	private final @Nonnull List<ICVRPSolutionGenerator> solutionGenerators;

	public CompositeCVRPSolutionGenerator(List<ICVRPSolutionGenerator> solutionGenerators) {
		this.solutionGenerators = solutionGenerators;
	}
	
	@Override
	public DemandRoutesSolution generateImplicitRoutes(CVRPDescription description) {
		return solutionGenerators.get(RNGProvider.getRandom().nextInt(solutionGenerators.size())).generateImplicitRoutes(description);
	}

}
