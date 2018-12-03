package heuristic.popvnd.neighborhood;

import java.util.List;

import javax.annotation.Nonnull;

import heuristic.popvnd.neighborAcceptanceTest.NeighborAcceptanceTest;
import heuristic.popvnd.neighborhoodMaintenance.NeighborhoodMaintenance;
import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;

public final class MergeRoutes_V2 extends INeighborhood_V2<DemandRoutesSolution> {

	private final @Nonnull CVRPDescription description;

	public MergeRoutes_V2(@Nonnull CVRPDescription description,
			@Nonnull NeighborAcceptanceTest<DemandRoutesSolution> neighborAcceptanceTest,
			@Nonnull NeighborhoodMaintenance<DemandRoutesSolution> neighborhoodGenerationStopTest) {
		super(neighborAcceptanceTest, neighborhoodGenerationStopTest);
		this.description = description;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void neighborhood(DemandRoutesSolution[] population, int id, List<DemandRoutesSolution> neighbors) {
		neighborAcceptanceTest.startingNegihborhoodGeneration(population);
		neighborhoodGenerationStopTest.startingNegihborhoodGeneration(population);
		
		DemandRoutesSolution element = population[id];
		all:
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
				
				if(neighborAcceptanceTest.shouldAcceptTest(population, element, neighbor)) {
					if(neighborhoodGenerationStopTest.addNeighbor(population, element, neighbor,neighbors)) {
						break all;
					}
				}
			}
		}
		
	}
}