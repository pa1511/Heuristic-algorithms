package heuristic.tsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import heuristic.routing.LoopRoute;
import heuristic.routing.Route;

public class TSPHEdgeStat {
	
	public static void main(String[] args) throws IOException {
		
		String[] problems = new String[] {
//				"p_1",
				"p_3",
//				"p_4",
//				"p_6",
				"p_8",
				"p_9",
//				"p_10"
				};
		
		int[][] bestSolutions = new int[][] {
//				{0, 12, 1, 14, 8, 4, 6, 2, 11, 13, 9, 7, 5, 3, 10},
				{0, 2, 3, 4, 1},
//				{0, 7, 37, 30, 43, 17, 6, 27, 5, 36, 18, 26, 16, 42, 29, 35, 45, 32, 19, 46, 20, 31, 38, 47, 4, 41, 23, 9, 44, 34, 3, 25, 1, 28, 33, 40, 15, 21, 2, 22, 13, 24, 12, 10, 11, 14, 39, 8},
//				{0, 24, 23, 22, 25, 21, 20, 16, 17, 19, 18, 15, 10, 11, 12, 14, 13, 9, 8, 7, 6, 4, 5, 3, 2, 1},
				{0, 4, 2, 3, 1, 6, 5},
				{0, 8, 9, 2, 5, 4, 1, 7, 6, 3},
//				{0, 27, 5, 11, 8, 4, 25, 28, 2, 1, 19, 9, 3, 14, 17, 16, 13, 21, 10, 18, 24, 6, 22, 26, 7, 23, 15, 12, 20}
		};
		
		
		for(int i=0; i<problems.length;i++) {
			String file = problems[i]+".txt";
			//
			double[][] distances = TSPExampleUtility.loadDistanceMatrix(file);		
			//
			Route route = new LoopRoute(distances);
			route.addAll(bestSolutions[i]);
			//
			Edge[] edges = new Edge[distances.length*(distances.length-1)/2];
			int e = 0;
			for(int j=0; j<distances.length;j++) {
				for(int k=j+1;k<distances.length;k++) {
					edges[e++]=new Edge(j, k, distances[j][k]);
				}
			}
			Arrays.sort(edges, (e1,e2)->{
				return Double.compare(e1.distance, e2.distance);
			});
			
			int thirdMax = -1;
			int secondMax = -1;
			int maxPosition = -1;
			//
			for(int j=0,size=route.size(); j<size;j++) {
				int ss = route.get(j);
				int es = (j+1==size) ? route.get(0) : route.get(j+1);
				
				if(ss>es) {
					int swap = ss;
					ss = es;
					es = swap;
				}
				
				for(int k=0; k<edges.length;k++) {
					Edge edge = edges[k];
					if(edge.start==ss && edge.end==es) {
						if(k>maxPosition) {
							maxPosition=k;
						}
						else if(k>secondMax) {
							secondMax=k;
						}
						else if(k>thirdMax) {
							thirdMax=k;
						}
						break;
					}
				}
				
			}
			double r1 = ((double)++maxPosition)/((double)edges.length)* 100;
			double r2 = ((double)++secondMax)/((double)edges.length)* 100;
			double r3 = ((double)++thirdMax)/((double)edges.length)* 100;
			System.out.println("Problem:"+problems[i]+"-->Length:" + route.getLength()+"-->"+r1 + "%|"+r2+"%|"+r3+"%");
			//
			//TODO:
			int limit = (int)(edges.length*0.8+1);
			int[] edgeCount = new int[distances.length];
			Edge[] solution = new Edge[distances.length];
			double res = solve(0,0,limit,edgeCount,solution,edges);
			System.out.println("Length: " + res);
			
			
			
			
		}

		//
		System.out.println();
		System.out.println("Best known solution");
		int i=0;
		for(String bestKnown:Files.readAllLines(Paths.get("data/tsp/s.txt"))) {
			if(bestKnown.startsWith(problems[i])) {
				System.out.println(bestKnown);
				i=(i+1)%problems.length;
			}
		}

	}
	
	/**
	 * The solution it returns is not good, but the distance should be ok
	 */
	private static double solve(int eP, int dP, int limit, int[] edgeCount, Edge[] solution, Edge[] edges) {
		
		if(dP==limit) {
			if(Arrays.stream(edgeCount).allMatch(i->i==2))
				return 0;
			
			return 1e5;//large penalty constant
		}
		
		Edge edge = edges[dP];
		int ss = edge.start;
		int es = edge.end;
		
		double d2 = solve(eP, dP+1, limit, edgeCount, solution, edges);		
		
		if(edgeCount[ss]+1<=2 && edgeCount[es]+1<=2) {
			edgeCount[ss]++;
			edgeCount[es]++;
			//solution[eP] = edge;
			double d1 = edge.distance+solve(eP+1, dP+1, limit, edgeCount, solution, edges); 
			edgeCount[ss]--;
			edgeCount[es]--;
			//solution[eP] = null;
			
			if(d1<d2)
				return d1;
		}
		
		
		return d2;
	}

	private static class Edge {
		
		private int start;
		private int end;
		private double distance;

		public Edge(int start, int end, double distance) {
			this.start = start;
			this.end = end;
			this.distance = distance;
		}
		
		@Override
		public String toString() {
			return "["+start+","+end+"]->"+distance;
		}
	}
}
