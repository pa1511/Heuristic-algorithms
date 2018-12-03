package heuristic.routing.cvrp;

import heuristic.routing.cvrp.CVRPDescription;

public interface ICVRPSolutionGenerator {
	public DemandRoutesSolution generateImplicitRoutes(CVRPDescription description);
}
