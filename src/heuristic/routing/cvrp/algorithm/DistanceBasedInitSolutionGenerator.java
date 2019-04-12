package heuristic.routing.cvrp.algorithm;

import gnu.trove.list.array.TIntArrayList;
import heuristic.routing.DemandRoute;
import heuristic.routing.ImplicitLoopDemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPSolutionGenerator;

public class DistanceBasedInitSolutionGenerator implements ICVRPSolutionGenerator{

	@Override
	public DemandRoutesSolution generateImplicitRoutes(CVRPDescription description) {
		DemandRoutesSolution routes = new DemandRoutesSolution();

		// Create set of unassigned stations
		TIntArrayList unassignedStations = new TIntArrayList();
		for (int i = 1; i < description.dimension; i++)
			unassignedStations.add(i);

		// Define routes
		do {
			DemandRoute route = new ImplicitLoopDemandRoute(description.distance,description.demand,description.getCapacity(routes.routes.size()));

			// add initial station to the route
			int station = unassignedStations.removeAt(0);
			route.add(station);

			// Select next station to be added to the route
			int newRouteStation;
			do {
				newRouteStation = -1;
				double minNewStationDistance = Double.POSITIVE_INFINITY;

				for (int i = 0,size=unassignedStations.size(); i < size; i++) {
					int potentialStation = unassignedStations.get(i);

					// the route would be overloaded
					if (route.getDemand() + description.demand[potentialStation] > route.capacity)
						continue;

					// calculate the distance from the potential station to the route
					double potentialStationDistanceToRoute = Double.POSITIVE_INFINITY;
					for (int j = 0; j < route.size(); j++) {
						int routeStation = route.get(j);
						potentialStationDistanceToRoute = Double.min(potentialStationDistanceToRoute,
								description.distance[potentialStation][routeStation]);
					}

					// if it is closer to the route then to the main station
					if (potentialStationDistanceToRoute <= description.distance[potentialStation][0]) {
						// if it is closer then the previous best choice
						if (minNewStationDistance > potentialStationDistanceToRoute) {
							newRouteStation = potentialStation;
							minNewStationDistance = potentialStationDistanceToRoute;
						}
					}
				}

				// If a new station should be added to the route
				if (newRouteStation != -1) {
					unassignedStations.remove(newRouteStation);
					route.add(newRouteStation);
				}

			} while (newRouteStation != -1);

			// Optimize the created route
			CVRPUtility.optimizeRoute(route);

			// Add route to solution
			routes.add(route);
			
		} while (!unassignedStations.isEmpty());

		return routes;
	}

}
