package heuristic.routing.cvrp.neighborhoodGenerators;

import java.util.Random;

import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;
import optimization.solution.neighborhood.INeighborGenerator;

public final class MergeRouteNG implements INeighborGenerator<DemandRoutesSolution> {
	private final CVRPDescription description;
	private final Random random;

	public MergeRouteNG(CVRPDescription description, Random random) {
		this.description = description;
		this.random = random;
	}

	@Override
	public void randomNeighbor(DemandRoutesSolution element, DemandRoutesSolution neighbor) {				
		DemandRoute route1 = neighbor.getNonEmpty(random);
		DemandRoute route2 = neighbor.getNonEmpty(random);
		
		if(route1.capacity>route2.capacity) {
			DemandRoute t = route1;
			route1 = route2;
			route2 = t;
		}

								
		if(route1==route2 || route1.getDemand()+route2.getDemand()>route2.capacity) {
			return;
		}

		double route1Length = route1.getLength();
		double route2Length = route2.getLength();

		route2.addAll(route1);
		route1.removeAll();;

		CVRPUtility.optimizeRoute(route2);
		
		neighbor.length += -route1Length-route2Length+route1.getLength()+route2.getLength();
	}
}