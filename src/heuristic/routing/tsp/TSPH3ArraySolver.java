package heuristic.routing.tsp;

import java.util.LinkedList;

import heuristic.routing.RoutingUtility;

public class TSPH3ArraySolver implements ITSPArraySolver {
	
	@Override
	public double solveImplicit(double[][] distances, int[] station) {
		return solveImplicit(distances, station, station.length);
	}

	@Override
	public double solveImplicit(double[][] distances, int[] station, int N) {
		
		if(N<=2)
			return RoutingUtility.calculateImplicitLoopLength(station, N, distances);

		LinkedList<Integer> solution = new LinkedList<>();
		double length = 0;
		
		for(int i=0; i<N; i++) {

			//Determine where in the route should the station be added
			int sMin = station[i];
			
			double minChange = Double.MAX_VALUE;
			int minChangeId = -1;
			
			for(int j=0,size=solution.size(); j<=size;j++) {
				
				int ss = (j==0) ? 0 : solution.get(j-1).intValue();
				int es = (j==size) ? 0 : solution.get(j).intValue();

				double change = -distances[ss][es]+distances[ss][sMin]+distances[sMin][es];

				if(minChange>change) {
					minChange = change;
					minChangeId = j;
				}
			}
			
			//Add station to solution
			solution.add(minChangeId, Integer.valueOf(sMin));
			length+=minChange;
		}
		
		for(int i=0; i<N; i++)
			station[i] = solution.get(i).intValue();

		return length;
	}
		
}
