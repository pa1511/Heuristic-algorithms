package heuristic.routing.cvrp.algorithm;

import java.util.ArrayList;
import java.util.List;

import heuristic.routing.DemandRoute;
import heuristic.routing.ImplicitLoopDemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPSolutionGenerator;

public class ClarkeWrightSolutionGenerator implements ICVRPSolutionGenerator{

	@Override
	public DemandRoutesSolution generateImplicitRoutes(CVRPDescription description) {
		
		//Calculate savings
		List<Saving> savings = new ArrayList<>();
		for(int i=1; i<description.dimension;i++) {
			for(int j=i+1;j<description.dimension;j++ ) {
				double saveValue = description.distance[0][i]+description.distance[j][0]-description.distance[i][j];
				savings.add(new Saving(i, j, saveValue));
			}
		}
		savings.sort((s1,s2)->-1*Double.compare(s1.saving, s2.saving));
		
		//Generate initial routes
		DemandRoutesSolution solution = new DemandRoutesSolution();
		int[] routeAffiliation = new int[description.dimension];
		int[] possitionInRoute = new int[description.dimension];
		for(int i=1; i<description.dimension;i++) {
			int routeId = solution.routes.size();
			DemandRoute route = new ImplicitLoopDemandRoute(description.distance, description.demand,description.getCapacity(routeId));
			route.add(i);
			solution.add(route);
			routeAffiliation[i]=i-1;
			possitionInRoute[i] = 0;
		}
		
		//Join routes
		for(Saving saving:savings) {
			int r1 = routeAffiliation[saving.start];
			int r2 = routeAffiliation[saving.end];
			
			if(r1==r2) {
				continue;
			}
			
			int caseId = 0;
			
			DemandRoute route1 = solution.get(r1);
			if(possitionInRoute[saving.start]==0) {
				caseId = caseId | 0x0;
			}
			else 
			if(possitionInRoute[saving.start]==route1.size()-1) {
				caseId = caseId | 0x2;
			}
			else {
				continue;
				
			}
			
			DemandRoute route2 = solution.get(r2);
			if(possitionInRoute[saving.end]==0) {
				caseId = caseId | 0x0;
			}
			else 
			if(possitionInRoute[saving.end]==route2.size()-1) {
				caseId = caseId | 0x1;
			}
			else {
				continue;
			}
			

			switch (caseId) {
				case 0:
					if(route1.getDemand()+route2.getDemand()>route1.capacity) {
						continue;
					}
					route1.flip();
					for(int i=0,size=route2.size(); i<size; i++) {
						route1.add(route2.get(i));
					}
					route2.removeAll();
					break;
				case 1:
					if(route1.getDemand()+route2.getDemand()>route2.capacity) {
						continue;
					}
					for(int i=0,size=route1.size(); i<size; i++) {
						route2.add(route1.get(i));
					}
					route1.removeAll();
					break;
				case 2:
					if(route1.getDemand()+route2.getDemand()>route1.capacity) {
						continue;
					}
					for(int i=0,size=route2.size(); i<size; i++) {
						route1.add(route2.get(i));
					}
					route2.removeAll();
					break;
				case 3:
					if(route1.getDemand()+route2.getDemand()>route1.capacity) {
						continue;
					}
					route2.flip();
					for(int i=0,size=route2.size(); i<size; i++) {
						route1.add(route2.get(i));
					}
					route2.removeAll();
					break;
			}			
			
			for(int i=0,size=route1.size(); i<size; i++) {
				int s = route1.get(i);
				routeAffiliation[s] = r1;
				possitionInRoute[s] = i;
			}
			
			for(int i=0,size=route2.size(); i<size; i++) {
				int s = route2.get(i);
				routeAffiliation[s] = r2;
				possitionInRoute[s] = i;
			}

		}

		solution.removeEmptyRoutes();
		
		return solution;
	}
	
	private static class Saving{
		int start;
		int end;
		double saving;
		
		public Saving(int start, int end, double saving) {
			this.start = start;
			this.end = end;
			this.saving = saving;
		}
	}

}
