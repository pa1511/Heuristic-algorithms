package heuristic.routing.cvrp.neighborhood;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;
import optimization.solution.neighborhood.INeighborhood;

public final class MergeRoutes implements INeighborhood<DemandRoutesSolution> {
	private final CVRPDescription description;
	private final Function<DemandRoutesSolution,Predicate<DemandRoutesSolution>> neighborAcceptanceTest;

	public MergeRoutes(CVRPDescription description,Function<DemandRoutesSolution,Predicate<DemandRoutesSolution>> neighborAcceptanceTest) {
		this.description = description;
		this.neighborAcceptanceTest = neighborAcceptanceTest;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void neighborhood(DemandRoutesSolution element, List<DemandRoutesSolution> neighborhood) {

		for(int cr=0,size=element.routes.size(); cr<size; cr++) {
			for(int nr=cr+1; nr<size; nr++) {
				
				if(element.get(cr).isEmpty() || element.get(nr).isEmpty())
					continue;
				
				if(element.get(cr).getDemand()+element.get(nr).getDemand()>description.capacity)
					continue;
				
					
				DemandRoutesSolution neighbor = element.clone();
				DemandRoute route1 = neighbor.get(cr);
				DemandRoute route2 = neighbor.get(nr);

				double originalLength = route1.getLength()+route2.getLength();

				route1.addAll(route2);
				route2.removeAll();
				
				CVRPUtility.optimizeRoute(route1);
				
				double newLength = route1.getLength()+route2.getLength();
				neighbor.length+=-originalLength+newLength;
				
				if(neighborAcceptanceTest.apply(element).test(neighbor)) {
					neighborhood.add(neighbor);
				}
			}
		}
		
	}
}