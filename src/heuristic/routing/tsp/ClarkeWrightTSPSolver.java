package heuristic.routing.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import heuristic.routing.DemandRoute;
import heuristic.routing.ImplicitLoopDemandRoute;
import heuristic.routing.ImplicitLoopRoute;
import heuristic.routing.Route;
import heuristic.routing.RoutingUtility;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPSolutionGenerator;

public class ClarkeWrightTSPSolver implements ITSPArraySolver{

	@Override
	public double solveImplicit(double[][] distances, int[] station) {
		return solveImplicit(distances, station, station.length);
	}

	@Override
	public double solveImplicit(double[][] distances, int[] station, int N) {
		if(N<=2)
			return RoutingUtility.calculateImplicitLoopLength(station, N, distances);
		
		//Calculate savings
		List<Saving> savings = new ArrayList<>();
		int max = 0;
		for(int i=0; i<N;i++) {
			for(int j=i+1;j<N;j++ ) {
				int s1 = station[i];
				int s2 = station[j];
				double saveValue = distances[0][s1]+distances[s2][0]-distances[s1][s2];
				savings.add(new Saving(s1, s2, saveValue));
				max = Math.max(max, s1);
				max = Math.max(max, s2);
			}
		}
		savings.sort((s1,s2)->-1*Double.compare(s1.saving, s2.saving));
		
		//Generate initial routes
		List<ImplicitLoopRoute> routes = new ArrayList<>();
		//
		//
		int[] routeAffiliation = new int[max+1];
		int[] possitionInRoute = new int[max+1];
		
		for(int i=0; i<N;i++) {
			ImplicitLoopRoute route = new ImplicitLoopRoute(distances);
			int s = station[i];
			route.add(s);
			routes.add(route);
			routeAffiliation[s] = i;
			possitionInRoute[s] = 0;
		}
		
		//Join routes
		for(Saving saving:savings) {
			int r1 = routeAffiliation[saving.start];
			int r2 = routeAffiliation[saving.end];
			
			if(r1==r2) {
				continue;
			}
			
			int caseId = 0;
			
			Route route1 = routes.get(r1);
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
			
			Route route2 = routes.get(r2);
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
					route1.flip();
					route1.addAll(route2);
					route2.removeAll();
					break;
				case 1:
					route2.addAll(route1);
					route1.removeAll();
					break;
				case 2:
					route1.addAll(route2);
					route2.removeAll();
					break;
				case 3:
					route2.flip();
					route1.addAll(route2);
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


		Route solution = null;
		for(Route route:routes) {
			if(!route.isEmpty()) {
				solution = route;
				break;
			}
		}
		
		System.arraycopy(solution.toArray(), 0, station, 0, N);
		return solution.getLength();
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
		
		@Override
		public String toString() {
			return "["+start+","+end+"]->"+saving;
		}
	}

}
