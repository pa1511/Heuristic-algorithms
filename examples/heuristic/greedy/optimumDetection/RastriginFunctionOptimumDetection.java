package heuristic.greedy.optimumDetection;

import java.util.Arrays;
import function.common.benchmark.RastriginFunction;
import function.decorators.DerivativeFunctionWrapper;
import function.decorators.FunctionCallCounterWrapper;
import heuristic.GreedyAlgorithm;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.algorithm.decorator.TimedOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.decoder.PassThroughDoubleDecoder;
import optimization.fittnesEvaluator.FunctionValueFitnessEvaluator;
import optimization.fittnesEvaluator.ThroughOneFitnessEvaluator;
import optimization.fittnesEvaluator.observable.Best2DUnitGraphngObserver;
import optimization.fittnesEvaluator.observable.BestObserver;
import optimization.fittnesEvaluator.observable.PerChromosomeObservableFitnessEvaluator;
import optimization.fittnesEvaluator.observable.PrintBestObserver;
import optimization.fittnesEvaluator.observable.SleepOnBestObserver;
import optimization.solution.DoubleArraySolution;
import optimization.solution.neighborhood.DoubleArrayUnifNeighborhood;
import optimization.solution.neighborhood.INeighborGenerator;
import optimization.startSolutionGenerator.RandomStartSolutionGenerator;
import optimization.stopper.CompositeOptimisationStopper;
import optimization.stopper.FunctionValueStopper;
import optimization.stopper.GenerationNumberEvolutionStopper;
import optimization.stopper.IOptimisationStopper;
import optimization.utility.AlgorithmsPresentationUtility;
import ui.graph.SimpleGraph;

public class RastriginFunctionOptimumDetection {

	private RastriginFunctionOptimumDetection() {}
	
	
	public static void main(String[] args) {
		
		double acceptableErrorRate = 10e-6;
		int maximumNumberOfGenerations = 500_000_000;
		double delta = 1;
		
		//Function to optimize
		RastriginFunction function = new RastriginFunction();
		int variableNumber = function.getVariableCount();
		double[] standardSearchMin = function.getStandardSearchMin();
		double[] standardSearchMax = function.getStandardSearchMax();
		FunctionCallCounterWrapper<double[]> wrappedFunction = new FunctionCallCounterWrapper<double[]>(new DerivativeFunctionWrapper(function));		
		
		//Start UI
		SimpleGraph graph = new SimpleGraph(10,10);
		graph.display();

		//Start solution
		DoubleArraySolution startSolution = new RandomStartSolutionGenerator(variableNumber, standardSearchMin, standardSearchMax).generate();
		
		//Decoder
		IDecoder<DoubleArraySolution,double[]> decoder = new PassThroughDoubleDecoder();

		//Fitness evaluator
		PerChromosomeObservableFitnessEvaluator<DoubleArraySolution> evaluator = new PerChromosomeObservableFitnessEvaluator<>(v->
			 ThroughOneFitnessEvaluator.evaluationMethod.applyAsDouble(
						FunctionValueFitnessEvaluator.evaluationMethod.applyAsDouble(v)
		));
		int sleepTimeInMs = 50;
		evaluator.addObserver(new BestObserver<>(decoder, Arrays.asList(
				new PrintBestObserver<DoubleArraySolution,double[]>(System.out),
				new Best2DUnitGraphngObserver<DoubleArraySolution>(graph),
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
				new GreedyAlgorithm<double[],DoubleArraySolution>(decoder, neighborhood, evaluator, stopper, wrappedFunction, startSolution);
				
		TimedOptimizationAlgorithm<DoubleArraySolution> timedOptAlgorithm = new TimedOptimizationAlgorithm<>(optimizationAlgorithm);
		
		//Solution presentation
		DoubleArraySolution solution = timedOptAlgorithm.run();
		System.out.println();
		AlgorithmsPresentationUtility.printExecutionTime(timedOptAlgorithm.getExecutionTime());
		System.out.println("Solution: "  + solution);
		System.out.println("Value: " + wrappedFunction.applyAsDouble(solution.getValues()));
		System.out.println("Optimum solution: " + Arrays.toString(function.getMinValueCoordinates()));
		AlgorithmsPresentationUtility.printEvaluationCount(wrappedFunction.getEvaluationCount());
	}
	
}
