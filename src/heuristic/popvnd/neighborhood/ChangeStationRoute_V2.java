package heuristic.popvnd.neighborhood;

import java.util.List;

import javax.annotation.Nonnull;

import heuristic.popvnd.neighborAcceptanceTest.NeighborAcceptanceTest;
import heuristic.popvnd.neighborhoodMaintenance.NeighborhoodMaintenance;
import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;

public final class ChangeStationRoute_V2 extends INeighborhood_V2<DemandRoutesSolution> {
	
	private final @Nonnull CVRPDescription description;

	public ChangeStationRoute_V2(@Nonnull CVRPDescription description,
			@Nonnull NeighborAcceptanceTest<DemandRoutesSolution> neighborAcceptanceTest,
			@Nonnull NeighborhoodMaintenance<DemandRoutesSolution> neighborhoodGenerationStopTest) {
		super(neighborAcceptanceTest, neighborhoodGenerationStopTest);
		this.description = description;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void neighborhood(DemandRoutesSolution[] population, int id, List<DemandRoutesSolution> neighbors) {
		neighborAcceptanceTest.startingNegihborhoodGeneration(population);
		neighborhoodGenerationStopTest.startingNegihborhoodGeneration(population);

		DemandRoutesSolution element = population[id];
		all:
		for(int cr=0,size=element.routes.size();cr<size;cr++) {
			DemandRoute originalRoute = element.routes.get(cr);
			for(int cs=0,routeSize=originalRoute.size(); cs<routeSize;cs++) {
				for(int nr=0; nr<size;nr++) {
					if(cr==nr)
						continue;
					
					int station = originalRoute.get(cs);
					
					DemandRoute nextDemandRoute = element.routes.get(nr);
					if(nextDemandRoute.getDemand()+description.demand[station]>nextDemandRoute.capacity)
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
					if(neighborAcceptanceTest.shouldAcceptTest(population, element, neighbor)) {
						if(neighborhoodGenerationStopTest.addNeighbor(population, element, neighbor,neighbors)) {
							break all;
						}
					}
				}
			}
		}
	}
}