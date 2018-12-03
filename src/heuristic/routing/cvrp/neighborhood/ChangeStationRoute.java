package heuristic.routing.cvrp.neighborhood;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;
import optimization.solution.neighborhood.INeighborhood;

public final class ChangeStationRoute implements INeighborhood<DemandRoutesSolution> {
	private final CVRPDescription description;
	private final Function<DemandRoutesSolution,Predicate<DemandRoutesSolution>> neighborAcceptanceTest;

	
	public ChangeStationRoute(CVRPDescription description,Function<DemandRoutesSolution,Predicate<DemandRoutesSolution>> neighborAcceptanceTest) {
		this.description = description;
		this.neighborAcceptanceTest = neighborAcceptanceTest;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void neighborhood(DemandRoutesSolution element, List<DemandRoutesSolution> neighborhood) {
		
		for(int cr=0,size=element.routes.size();cr<size;cr++) {
			DemandRoute originalRoute = element.routes.get(cr);
			for(int cs=0; cs<originalRoute.size();cs++) {
				for(int nr=0; nr<size;nr++) {
					if(cr==nr)
						continue;
					
					int station = originalRoute.get(cs);
					
					if(element.routes.get(nr).getDemand()+description.demand[station]>description.capacity)
						continue;

					
					DemandRoutesSolution neighbor = element.clone();
					DemandRoute route1 = neighbor.get(cr);
					DemandRoute route2 = neighbor.get(nr);
					
					double originalLength = route1.getLength()+route2.getLength();
												
					station = route1.get(cs);
					route1.removeAt(cs);
					route2.add(station);
																			
					CVRPUtility.optimizeRoute(route1);
					CVRPUtility.optimizeRoute(route2);		
					
					double newLength = route1.getLength()+route2.getLength();

					neighbor.length+=-originalLength+newLength;
					if(neighborAcceptanceTest.apply(element).test(neighbor)) {
						neighborhood.add(neighbor);
					}
				}
			}
		}
		
	}
}