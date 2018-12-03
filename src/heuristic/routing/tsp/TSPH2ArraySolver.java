package heuristic.routing.tsp;

import java.util.Arrays;
import java.util.LinkedList;

import heuristic.routing.RoutingUtility;

public class TSPH2ArraySolver implements ITSPArraySolver{
	
	@Override
	public double solveImplicit(double[][] distances, int[] station) {
		return solveImplicit(distances, station, station.length);
	}

	/**
	 * Solves TSP for the first N stations in station array. </br>
	 * We assume a symmetric distances matrix.
	 */
	@Override
	public double solveImplicit(double[][] distances, int[] station, int N) {
		
		if(N<=2)
			return RoutingUtility.calculateImplicitLoopLength(station, N, distances);

		LinkedList<Integer> solution = new LinkedList<>();
		boolean[] routeContains = new boolean[N+1];
		Arrays.fill(routeContains, false);
		double length = 0;
		
		for(int i=0; i<N; i++) {

			int minLinkId = -1;
			int minStationId = -1;
			double minChange = Double.MAX_VALUE;
			
			//determine which link to break and by adding which station to cause a minimal change in the route length
			for(int j=0; j<N; j++) {
				if(routeContains[j])
					continue;
				
				int s = station[j];
				

				
				for(int k=0,size=solution.size(); k<=size;k++) {
					int rs1 = (k==0) ? 0 : solution.get(k-1).intValue();
					int rs2 = (k==size) ? 0 : solution.get(k).intValue();

					double change = -distances[rs1][rs2]+distances[rs1][s]+distances[s][rs2];
					if(change<minChange) {
						minLinkId = k;
						minStationId = j;
						minChange = change;
					}
				}
				
			}

			//adjust route
			int s = station[minStationId];
			solution.add(minLinkId, Integer.valueOf(s));
			routeContains[minStationId] = true;
			length+=minChange;
		}
		
		for(int i=0; i<N; i++)
			station[i] = solution.get(i).intValue();

		return length;
	}
		
}
