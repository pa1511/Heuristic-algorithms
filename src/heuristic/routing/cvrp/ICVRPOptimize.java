package heuristic.routing.cvrp;

import heuristic.routing.cvrp.CVRPDescription;

public interface ICVRPOptimize {
	public DemandRoutesSolution optimize(CVRPDescription description, DemandRoutesSolution routes);
}
