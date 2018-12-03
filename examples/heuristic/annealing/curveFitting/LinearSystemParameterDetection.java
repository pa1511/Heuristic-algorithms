package heuristic.annealing.curveFitting;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import dataset.handeling.DataSetLoader;
import function.decorators.DimensionFocusWrapper;
import function.decorators.FunctionCallCounterWrapper;
import function.error.PrototypeBasedSystemLossFunction;
import function.error.SquareErrorFunction;
import function.prototype.LinearFunction;
import heuristic.annealing.SimulatedAnnealing;
import heuristic.annealing.schedule.GeometricTempSchhedule;
import heuristic.annealing.schedule.ITempSchedule;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.algorithm.decorator.TimedOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.decoder.PassThroughDoubleDecoder;
import optimization.solution.DoubleArraySolution;
import optimization.solution.neighborhood.DoubleArrayUnifNeighborhood;
import optimization.solution.neighborhood.INeighborGenerator;
import optimization.utility.AlgorithmsPresentationUtility;
import ui.graph.SimpleGraph;

public class LinearSystemParameterDetection {

	private LinearSystemParameterDetection() {}
	
	
	public static void main(String[] args) throws IOException {
		
		//Function to optimize
		LinearFunction linearFunction = new LinearFunction();
		double[][] systemMatrix = DataSetLoader.loadMatrix(new File(System.getProperty("user.dir"),"data/algorithm-examples/linear-data.txt"));
		FunctionCallCounterWrapper<double[]> function =  new FunctionCallCounterWrapper<double[]>(
				new PrototypeBasedSystemLossFunction(systemMatrix,linearFunction,new SquareErrorFunction()));		
		
		//Start solution
		int variableNumber = linearFunction.getCoefficientCount();
		DoubleArraySolution startSolution = new DoubleArraySolution(new double[variableNumber]);
		
		//Decoder
		IDecoder<DoubleArraySolution,double[]> decoder = new PassThroughDoubleDecoder();
		
		//Neighborhood
		double delta = 0.05;
		double[] deltas = new double[variableNumber];
		Arrays.fill(deltas, delta);
		INeighborGenerator<DoubleArraySolution> neighborhood = new DoubleArrayUnifNeighborhood(deltas);
		
		//Temperature schedule
		ITempSchedule tempSchedule = new GeometricTempSchhedule(10, 0.9, 100, 5000);
		
		//Optimization
		IOptimizationAlgorithm<DoubleArraySolution> optimizationAlgorithm = 
				new SimulatedAnnealing<double[],DoubleArraySolution>(decoder, neighborhood, startSolution, function, tempSchedule);
				
		TimedOptimizationAlgorithm<DoubleArraySolution> timedOptAlgorithm = new TimedOptimizationAlgorithm<>(optimizationAlgorithm);
		
		//Solution presentation
		DoubleArraySolution solution = timedOptAlgorithm.run();
		AlgorithmsPresentationUtility.printExecutionTime(timedOptAlgorithm.getExecutionTime());
		System.out.println("Solution: "  + solution);
		System.out.println("Error: " + function.applyAsDouble(solution.getValues()));
		AlgorithmsPresentationUtility.printEvaluationCount(function.getEvaluationCount());
		
		//Start UI
		SimpleGraph graph = new SimpleGraph(4,4);
		graph.addFunction(new DimensionFocusWrapper(linearFunction, 0, new double[1]), Color.BLUE);
		for(double[] row:systemMatrix){
			graph.addPoint(row[0], row[1]);
		}
		graph.display();

	}
	
}
