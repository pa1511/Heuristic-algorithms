package heuristic.tsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import heuristic.routing.tsp.TSPH1Solver;
import heuristic.routing.tsp.TSPSolution;
import utilities.PStrings;

public class TSPH1Example {
	
	public static void main(String[] args) throws IOException {
		
		for(String file:new String[] {
				"p_1.txt",
				"p_2.txt",
				"p_3.txt",
				"p_4.txt",
				"p_5.txt",
				"p_6.txt",
				"p_7.txt",
				"p_8.txt",
				"p_9.txt",
				"p_10.txt"
				}) {
			double[][] distances = TSPExampleUtility.loadDistanceMatrix(file);		
			long start = System.nanoTime();
			TSPSolution solution = new TSPH1Solver().solve(distances);
			long end = System.nanoTime();
//			System.out.println("File: " + file);
//			System.out.println("Length: " + solution.distance);
			System.out.println("Execution time:  " + PStrings.getRounded(5, (end-start)*1e-6) + " ms\n");
		}

//		for(String file:new String[] {
//				"c_1.txt",
//				"c_2.txt",
//				"c_3.txt",
//				"c_4.txt"
//				}) {
//			double[][] coordinates = TSPExampleUtility.loadCoordinates(file);		
//			double[][] distances = TSPExampleUtility.calculateDistance(coordinates);
//			long start = System.nanoTime();
//			TSPSolution solution = new TSPH1Solver().solve(distances);
//			long end = System.nanoTime();
////			System.out.println("File: " + file);
////			System.out.println("Length: " + solution.distance);
//			System.out.println("Execution time:  " + PStrings.getRounded(5, (end-start)*1e-6) + " ms\n");
//		}

		System.out.println("Best known solution");
		List<String> solutions = Files.readAllLines(Paths.get("data/tsp/s.txt"));
		for(String bestKnown:solutions) {
			System.out.println(bestKnown);
		}
	}
	
}
