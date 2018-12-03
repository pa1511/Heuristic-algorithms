package heuristic.routing.tsp;

import java.util.LinkedList;

import heuristic.routing.RoutingUtility;

public class TSPH4ArraySolver implements ITSPArraySolver {
	
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
		//
		//double maxEdgeLength = 0;
		int maxEdgeId = 0;
		//
		boolean[] used = new boolean[N]; 
		
		for(int i=0; i<N; i++) {
			
			int ss = (maxEdgeId==0) ? 0 : solution.get(maxEdgeId-1).intValue();
			int es = (maxEdgeId==solution.size()) ? 0 : solution.get(maxEdgeId).intValue();
			
			double minDistance = Double.POSITIVE_INFINITY;
			int minDistanceId = -1;
			
			for(int j=0; j<N; j++) {
				if(used[j])
					continue;
				int s = station[j];
				double distance = Double.min(distances[ss][s], distances[s][es]);
				if(distance<minDistance) {
					minDistance = distance;
					minDistanceId = j;
				}
			}
			
			used[minDistanceId] = true;
			
			int s = station[minDistanceId];
			solution.add(maxEdgeId, Integer.valueOf(s));
			
			if(distances[ss][s]<distances[s][es])
				maxEdgeId++;
			length+=-distances[ss][es]+distances[ss][s]+distances[s][es];
		}
		
		for(int i=0; i<N; i++)
			station[i] = solution.get(i).intValue();

		return length;
	}
		
}
