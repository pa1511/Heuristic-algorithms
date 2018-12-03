package heuristic.tsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import heuristic.routing.tsp.TSPArraySolution2OptSwapOptimizer;
import heuristic.routing.tsp.TSPArraySolutionElementSwapOptimizer;
import heuristic.routing.tsp.TSPH3ArraySolver;
import utilities.PStrings;

public class TSPH3Example {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		for(String file:new String[] {
//				"p_1.txt",
				"p_2.txt",
//				"p_3.txt",
//				"p_4.txt",
				"p_5.txt",
//				"p_6.txt",
				"p_7.txt",
//				"p_8.txt",
//				"p_9.txt",
//				"p_10.txt"
				}) {
			double[][] distances = TSPExampleUtility.loadDistanceMatrix(file);		
			//
			int[] solution = IntStream.range(1, distances.length).toArray();
			long start = System.nanoTime();
			double length =	new TSPH3ArraySolver().solveImplicit(distances,solution);
			length += new TSPArraySolutionElementSwapOptimizer().optimizeImplicit(distances, solution);
			length += new TSPArraySolution2OptSwapOptimizer().optimizeImplicit(distances, solution);
			long end = System.nanoTime();
			System.out.println("File: " + file);
			System.out.println("Solution: " + Arrays.toString(solution));
			System.out.println("Length: " + length);
//			System.out.println("Execution time:  " + PStrings.getRounded(5, (end-start)*1e-6) + " ms\n");
//
//			System.out.println(length);
//			System.out.println(PStrings.getRounded(5, (end-start)*1e-6));

		
		}

//		for(String file:new String[] {
//				"c_1.txt",
//				"c_2.txt",
//				"c_3.txt",
//				"c_4.txt"
//				}) {
//			double[][] coordinates = TSPExampleUtility.loadCoordinates(file);		
//			double[][] distances = TSPExampleUtility.calculateDistance(coordinates);
//			
//			int[] solution = IntStream.range(1, distances.length).toArray();
//			long start = System.nanoTime();
//			double length =	new TSPH3ArraySolver().solveImplicit(distances,solution);
//			length += new TSPArraySolutionNeighborSwapOptimizer().optimizeImplicit(distances, solution);
//			long end = System.nanoTime();
//			System.out.println("File: " + file);
//			System.out.println("Solution: " + Arrays.toString(solution));
//			System.out.println("Length: " + length);
//			System.out.println("Execution time:  " + PStrings.getRounded(5, (end-start)*1e-6) + " ms\n");
//			
//		}

		System.out.println("Best known solution");
		List<String> solutions = Files.readAllLines(Paths.get("data/tsp/s.txt"));
		for(String bestKnown:solutions) {
			System.out.println(bestKnown);
		}
	}
	
}
