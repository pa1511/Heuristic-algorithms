package heuristic.tsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import heuristic.routing.tsp.TSPH1Solver;
import heuristic.routing.tsp.TSPSolution;

public class TSPDPExample {
	
	public static void main(String[] args) throws IOException {
		
		for(String file:new String[] {
//				"p_1.txt",
//				"p_2.txt",
//				"p_3.txt",
				"p_4.txt",
//				"p_5.txt",
//				"p_6.txt",
//				"p_7.txt",
//				"p_8.txt",
//				"p_9.txt",
//				"p_10.txt"
				}) {
			double[][] distances = TSPExampleUtility.loadDistanceMatrix(file);		
			long start = System.nanoTime();
			
			DPTSPSolver dptspSolver = new DPTSPSolver(distances);
			int[] solution = dptspSolver.solveTSP();
			
			long end = System.nanoTime();
			System.out.println("File: " + file);
			System.out.println("Solution: " + Arrays.toString(solution));
			System.out.println("Solution length: " + dptspSolver.getBestLength());
			System.out.println("Execution time:  " + (end-start)*1e-6 + " ms\n");
		}


		System.out.println("Best known solution");
		List<String> solutions = Files.readAllLines(Paths.get("data/tsp/s.txt"));
		for(String bestKnown:solutions) {
			System.out.println(bestKnown);
		}
	}

	private static class DPTSPSolver{

		private double bestLength = Double.MAX_VALUE;
		private int[] bestSolution;
		
		private double[][] distances;
		private double minDistance = Double.MAX_VALUE;
		
		
		public DPTSPSolver(double[][] distances) {
			this.distances = distances;
			
			for(int i=0; i<distances.length; i++) {
				for(int j=0; j<distances[i].length;j++) {
					if(i!=j) {
						minDistance = Math.min(minDistance, distances[i][j]);
					}
				}
			}

			
			TSPSolution solution = new TSPH1Solver().solve(distances);
			bestLength = solution.distance;
			bestSolution = solution.toArray();
			System.out.println("Best estimate: " + bestLength); //TODO: remove
		}
		
		public int[] solveTSP() {
			
			int[] solution = new int[distances.length];
			boolean[] visited = new boolean[distances.length];
			//
			solution[0] = 0;
			visited[0] = true;
			solve(1,0,solution,visited);
			//
			return bestSolution;
		}
		
		public double getBestLength() {
			return bestLength;
		}

		private void solve(int position, double cost, int[] solution, boolean[] visited) {
			if(position==solution.length) {
				cost += distances[solution[0]][solution[solution.length-1]];
				if(cost<bestLength) {
					bestLength = cost;
					System.arraycopy(solution, 0, bestSolution, 0, solution.length);
					System.out.println(bestLength);//TODO: remove
				}
			}
			
			
			if(cost>=bestLength || cost+minDistance*(solution.length-position)>=bestLength) {
				return;
			}

			for(int i=0; i<solution.length;i++) {
				//TODO: remove
				if(position==1) {
					System.out.println((i+1)+"/"+solution.length);
				}
				if(!visited[i]) {
					visited[i] = true;
					solution[position] = i;
					solve(position+1, cost+distances[solution[position-1]][i], solution, visited);
					visited[i] = false;
				}
			}
			
		}
	}
	
}
