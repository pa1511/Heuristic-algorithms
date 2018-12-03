package heuristic.greedy.curveFitting;

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
import heuristic.GreedyAlgorithm;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.algorithm.decorator.TimedOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.decoder.PassThroughDoubleDecoder;
import optimization.fittnesEvaluator.FunctionValueFitnessEvaluator;
import optimization.fittnesEvaluator.ThroughOneFitnessEvaluator;
import optimization.fittnesEvaluator.observable.BestObserver;
import optimization.fittnesEvaluator.observable.PerChromosomeObservableFitnessEvaluator;
import optimization.fittnesEvaluator.observable.PrintBestObserver;
import optimization.fittnesEvaluator.observable.PrototypeGraphngBestObserver;
import optimization.fittnesEvaluator.observable.SleepOnBestObserver;
import optimization.solution.DoubleArraySolution;
import optimization.solution.neighborhood.DoubleArrayUnifNeighborhood;
import optimization.solution.neighborhood.INeighborGenerator;
import optimization.stopper.CompositeOptimisationStopper;
import optimization.stopper.FunctionValueStopper;
import optimization.stopper.GenerationNumberEvolutionStopper;
import optimization.stopper.IOptimisationStopper;
import optimization.utility.AlgorithmsPresentationUtility;
import ui.graph.SimpleGraph;

public class LinearSystemParameterDetection {

	private LinearSystemParameterDetection() {}
	
	
	public static void main(String[] args) throws IOException {
		
		double delta = 0.05;
		double acceptableErrorRate = 10e-6;
		int maximumNumberOfGenerations = 1_000_000;
		
		//Function to optimize
		LinearFunction linearFunction = new LinearFunction();
		double[][] systemMatrix = DataSetLoader.loadMatrix(new File(System.getProperty("user.dir"),"data/algorithm-examples/linear-data.txt"));
		FunctionCallCounterWrapper<double[]> function =  new FunctionCallCounterWrapper<double[]>(
				new PrototypeBasedSystemLossFunction(systemMatrix,linearFunction,new SquareErrorFunction()));		
		
		//Start UI
		SimpleGraph graph = new SimpleGraph(4,4);
		graph.addFunction(new DimensionFocusWrapper(linearFunction, 0, new double[1]), Color.BLUE);
		for(double[] row:systemMatrix){
			graph.addPoint(row[0], row[1]);
		}
		graph.display();

		//Start solution
		int variableNumber = linearFunction.getCoefficientCount();
		DoubleArraySolution startSolution = new DoubleArraySolution(new double[variableNumber]);
		
		//Decoder
		IDecoder<DoubleArraySolution,double[]> decoder = new PassThroughDoubleDecoder();

		//Fitness evaluator
		PerChromosomeObservableFitnessEvaluator<DoubleArraySolution> evaluator = new PerChromosomeObservableFitnessEvaluator<>(v->
			 ThroughOneFitnessEvaluator.evaluationMethod.applyAsDouble(
						FunctionValueFitnessEvaluator.evaluationMethod.applyAsDouble(v)
		));
		int sleepTimeInMs = 50;
		evaluator.addObserver(new BestObserver<>(decoder, Arrays.asList(
				new PrintBestObserver<DoubleArraySolution,double[]>(System.out)
				,
				new PrototypeGraphngBestObserver<DoubleArraySolution>(linearFunction, graph),
				//TODO: include to watch step by step progress
				new SleepOnBestObserver<DoubleArraySolution,double[]>(sleepTimeInMs)
		)));

		
		//Optimization stopper
		IOptimisationStopper<DoubleArraySolution> stopper = new CompositeOptimisationStopper<>(Arrays.asList(
			new FunctionValueStopper<>(acceptableErrorRate),
			new GenerationNumberEvolutionStopper<>(maximumNumberOfGenerations)
		));
		
		//Neighborhood
		double[] deltas = new double[variableNumber];
		Arrays.fill(deltas, delta);
		INeighborGenerator<DoubleArraySolution> neighborhood = new DoubleArrayUnifNeighborhood(deltas);
		
		//Optimization
		IOptimizationAlgorithm<DoubleArraySolution> optimizationAlgorithm = 
				new GreedyAlgorithm<double[],DoubleArraySolution>(decoder, neighborhood, evaluator, stopper, function, startSolution);
				
		TimedOptimizationAlgorithm<DoubleArraySolution> timedOptAlgorithm = new TimedOptimizationAlgorithm<>(optimizationAlgorithm);
		
		//Solution presentation
		DoubleArraySolution solution = timedOptAlgorithm.run();
		System.out.println();
		AlgorithmsPresentationUtility.printExecutionTime(timedOptAlgorithm.getExecutionTime());
		System.out.println("Solution: "  + solution);
		System.out.println("Error: " + function.applyAsDouble(solution.getValues()));
		AlgorithmsPresentationUtility.printEvaluationCount(function.getEvaluationCount());
	}
	
}
