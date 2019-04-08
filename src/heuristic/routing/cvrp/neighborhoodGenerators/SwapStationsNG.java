package heuristic.routing.cvrp.neighborhoodGenerators;

import java.util.Random;

import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;
import optimization.solution.neighborhood.INeighborGenerator;

public final class SwapStationsNG implements INeighborGenerator<DemandRoutesSolution> {
	private final Random random;
	private final CVRPDescription description;

	public SwapStationsNG(Random random, CVRPDescription description) {
		this.random = random;
		this.description = description;
	}

	@Override
	public void randomNeighbor(DemandRoutesSolution element, DemandRoutesSolution neighbor) {
		
		DemandRoute route1 = neighbor.getNonEmpty(random);
		DemandRoute route2 = neighbor.getNonEmpty(random);

		if(route1==route2) {
			return;
		}
												
		double route1Length = route1.getLength();
		double route2Length = route2.getLength();						
		
		int s1 = route1.removeAt(random);
		int s2 = route2.removeAt(random);

		route1.add(s2);
		route2.add(s1);
		
		//If the produced solution is not valid we don't want to loose our time on it
		if(route1.getDemand()<=description.capacity && route2.getDemand()<=description.capacity) {
			CVRPUtility.optimizeRoute(route1);
			CVRPUtility.optimizeRoute(route2);
		}
								
		neighbor.length += -route1Length-route2Length+route1.getLength()+route2.getLength();
	}
}