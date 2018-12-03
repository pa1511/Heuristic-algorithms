package heuristic.routing.cvrp.algorithm;

import java.util.List;

import javax.annotation.Nonnull;

import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPOptimize;

public class CompositeCVRPOptimize implements ICVRPOptimize{

	
	private final @Nonnull List<ICVRPOptimize> optimizers;

	public CompositeCVRPOptimize(List<ICVRPOptimize> optimizers) {
		this.optimizers = optimizers;	
	}
	
	@Override
	public DemandRoutesSolution optimize(CVRPDescription description, DemandRoutesSolution routes) {
		for(ICVRPOptimize optimizer:optimizers)
			routes = optimizer.optimize(description, routes);
		return routes;
	}

}
