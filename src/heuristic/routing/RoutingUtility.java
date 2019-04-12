package heuristic.routing;

import java.util.List;

public class RoutingUtility {

	/**
	 * Calculates the length for the given array of stations. </br>
	 * stations = [s1,s2,s3,...,sn]
	 * length = distance[s1][s2]+distance[s2][s3]+...+distance[sn-1][sn]
	 */
	public static double calculateChainLength(int[] stations, int n, double[][] distances) {
		
		double length = 0;
		
		for(int i=0; i<n-1; i++) {
			int s1 = stations[i];
			int s2 = stations[i+1];
			length += distances[s1][s2]; 
		}
				
		return length;
	}

	/**
	 * Calculates the length for the given array of stations. </br>
	 * stations = [s1,s2,s3,...,sn]
	 * length = distance[s1][s2]+distance[s2][s3]+...+distance[sn-1][sn]+distance[s1][sn]
	 */
	public static double calculateLoopLength(int[] stations, int n, double[][] distances) {
				
		double length = calculateChainLength(stations, n, distances);
		length+=distances[stations[0]][stations[n-1]];
		
		return length;
	}

	/**
	 * Calculates the length for the given array of stations. </br>
	 * stations = [s1,s2,s3,...,sn]
	 * length = distance[0][s1]+distance[s1][s2]+distance[s2][s3]+...+distance[sn-1][sn]+distance[sn][0]
	 */
	public static double calculateImplicitLoopLength(int[] stations, int n, double[][] distances) {		
		
		if(n==0)
			return 0;
		
		double length = calculateChainLength(stations, n, distances);
		length+=distances[0][stations[0]];
		length+=distances[stations[n-1]][0];
		return length;
	}

	
	/**
	 * Calculates the length for the given array of stations. </br>
	 * stations = [s1,s2,s3,...,sn]
	 * length = distance[s1][s2]+distance[s2][s3]+...+distance[sn-1][sn]
	 */
	public static double calculateChainLength(int[] stations, double[][] distances) {
		return calculateChainLength(stations,stations.length,distances);
	}

	/**
	 * Calculates the length for the given array of stations. </br>
	 * stations = [s1,s2,s3,...,sn]
	 * length = distance[s1][s2]+distance[s2][s3]+...+distance[sn-1][sn]+distance[s1][sn]
	 */
	public static double calculateLoopLength(int[] stations, double[][] distances) {		
		return calculateLoopLength(stations,stations.length,distances);
	}

	/**
	 * Calculates the length for the given array of stations. </br>
	 * stations = [s1,s2,s3,...,sn]
	 * length = distance[0][s1]+distance[s1][s2]+distance[s2][s3]+...+distance[sn-1][sn]+distance[sn][0]
	 */
	public static double calculateImplicitLoopLength(int[] stations, double[][] distances) {
		return calculateImplicitLoopLength(stations, stations.length, distances);
	}
	
	//=====================================================================================================

	public static double calculateDemand(int[] stations, double[] demands) {
		return calculateDemand(stations.length, stations, demands);
	}

	public static double calculateDemand(int n, int[] stations, double[] demands) {
		int demand = 0;
		for(int i=0; i<n; i++) {
			int station = stations[i];
			demand+=demands[station];
		}
		return demand;
	}

	//==========================================================================================================
	
	public static double getTotalLengthOf(List<? extends Route> routes) {
		double length = 0;
		for(Route route:routes)
			length+=route.getLength();
		return length;
	}
}
