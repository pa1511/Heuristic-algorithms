package heuristic.popvnd.neighborhood;

import java.util.List;

import javax.annotation.Nonnull;

import heuristic.popvnd.neighborAcceptanceTest.NeighborAcceptanceTest;
import heuristic.popvnd.neighborhoodMaintenance.NeighborhoodMaintenance;
import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;

public final class SwapStations_V2 extends INeighborhood_V2<DemandRoutesSolution> {


	private final @Nonnull CVRPDescription description;

	public SwapStations_V2(@Nonnull CVRPDescription description,
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
}