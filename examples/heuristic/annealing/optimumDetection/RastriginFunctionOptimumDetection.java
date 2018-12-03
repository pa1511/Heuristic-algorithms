package heuristic.annealing.optimumDetection;

import java.util.Arrays;
import function.common.benchmark.RastriginFunction;
import function.decorators.DerivativeFunctionWrapper;
import function.decorators.FunctionCallCounterWrapper;
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
import optimization.startSolutionGenerator.RandomStartSolutionGenerator;
import optimization.utility.AlgorithmsPresentationUtility;

public class RastriginFunctionOptimumDetection {

	private RastriginFunctionOptimumDetection() {}
	
	
	public static void main(String[] args) {
		
		double delta = 1;
		
		//Function to optimize
		RastriginFunction function = new RastriginFunction();
		int variableNumber = function.getVariableCount();
		double[] standardSearchMin = function.getStandardSearchMin();
		double[] standardSearchMax = function.getStandardSearchMax();
		FunctionCallCounterWrapper<double[]> wrappedFunction = new FunctionCallCounterWrapper<>(new DerivativeFunctionWrapper(function));		
		

		//Start solution
		DoubleArraySolution startSolution = new RandomStartSolutionGenerator(variableNumber, standardSearchMin, standardSearchMax).generate();
		
		//Decoder
		IDecoder<DoubleArraySolution,double[]> decoder = new PassThroughDoubleDecoder();

		
		//Neighborhood
		double[] deltas = new double[variableNumber];
		Arrays.fill(deltas, delta);
		INeighborGenerator<DoubleArraySolution> neighborhood = new DoubleArrayUnifNeighborhood(deltas);
		
		ITempSchedule tempSchedule = new GeometricTempSchhedule(5000, 0.99, 10000, 10000);
		
		//Optimization
		IOptimizationAlgorithm<DoubleArraySolution> optimizationAlgorithm = 
				new SimulatedAnnealing<double[],DoubleArraySolution>(decoder, neighborhood, startSolution, wrappedFunction, tempSchedule);
				
		TimedOptimizationAlgorithm<DoubleArraySolution> timedOptAlgorithm = new TimedOptimizationAlgorithm<>(optimizationAlgorithm);
		
		//Solution presentation
		DoubleArraySolution solution = timedOptAlgorithm.run();
		AlgorithmsPresentationUtility.printExecutionTime(timedOptAlgorithm.getExecutionTime());
		System.out.println("Solution: "  + solution);
		System.out.println("Value: " + wrappedFunction.applyAsDouble(solution.getValues()));
		System.out.println("Optimum solution: " + Arrays.toString(function.getMinValueCoordinates()));
		AlgorithmsPresentationUtility.printEvaluationCount(wrappedFunction.getEvaluationCount());
	}
	
}
