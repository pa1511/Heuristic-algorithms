package heuristic.routing.cvrp.neighborhood;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;
import optimization.solution.neighborhood.INeighborhood;

public final class SwapStations implements INeighborhood<DemandRoutesSolution> {
	private final CVRPDescription description;
	private final Function<DemandRoutesSolution,Predicate<DemandRoutesSolution>> neighborAcceptanceTest;

	public SwapStations(CVRPDescription description,Function<DemandRoutesSolution,Predicate<DemandRoutesSolution>> neighborAcceptanceTest) {
		this.description = description;
		this.neighborAcceptanceTest = neighborAcceptanceTest;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void neighborhood(DemandRoutesSolution element, List<DemandRoutesSolution> neighborhood) {
		
		for(int cr=0,size=element.routes.size();cr<size;cr++) {
			DemandRoute originalRoute1 = element.routes.get(cr);
			for(int cs=0,sizeR1=originalRoute1.size(); cs<sizeR1;cs++) {
				for(int nr=0; nr<size;nr++) {
					if(cr==nr)
						continue;
					DemandRoute originalRoute2 = element.routes.get(nr);
					for(int ns=0,sizeR2=originalRoute2.size(); ns<sizeR2;ns++) {
						
						int s1 = originalRoute1.get(cs);
						int s2 = originalRoute2.get(ns);
						double[] demands = description.demand;
						
						if(originalRoute1.getDemand()-demands[s1]+demands[s2]>description.capacity ||
								originalRoute2.getDemand()-demands[s2]+demands[s1]>description.capacity)
							continue;
						
						
						DemandRoutesSolution neighbor = element.clone();
						DemandRoute route1 = neighbor.get(cr);
						DemandRoute route2 = neighbor.get(nr);
						
						double originalLength = route1.getLength()+route2.getLength();
						
						s1 = route1.removeAt(cs);
						s2 = route2.removeAt(ns);
						
						route1.add(s2);
						route2.add(s1);
																				
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
}