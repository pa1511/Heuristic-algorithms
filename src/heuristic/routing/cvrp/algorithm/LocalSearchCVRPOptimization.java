package heuristic.routing.cvrp.algorithm;

import java.util.Arrays;
import java.util.Random;

import function.ID1Function;
import function.IFunction;
import heuristic.GreedyAlgorithm;
import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPOptimize;
import heuristic.routing.cvrp.neighborhoodGenerators.ChangeStationRouteNG;
import heuristic.routing.cvrp.neighborhoodGenerators.MergeRouteNG;
import heuristic.routing.cvrp.neighborhoodGenerators.SwapStationsNG;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.decoder.PassThroughDecoder;
import optimization.fittnesEvaluator.FunctionValueFitnessEvaluator;
import optimization.fittnesEvaluator.IFitnessEvaluator;
import optimization.fittnesEvaluator.ThroughOneFitnessEvaluator;
import optimization.solution.neighborhood.CompositeNeighborhood;
import optimization.solution.neighborhood.RepeatNeighborhood;
import optimization.stopper.CompositeOptimisationStopper;
import optimization.stopper.GenerationNumberEvolutionStopper;
import optimization.stopper.IOptimisationStopper;
import optimization.stopper.StagnationStopper;
import utilities.random.RNGProvider;

public class LocalSearchCVRPOptimization implements ICVRPOptimize{

	@Override
	public DemandRoutesSolution optimize(CVRPDescription description, DemandRoutesSolution routes) {
				
		//Parameters
		double repetitionChance = 0.1;
		int maximumGenerationNumber = 1_000_000;
		int maximumStagnation = 8_000;
		
		// Start solution
		//Adding a few more routes for flexibility
		DemandRoutesSolution startSolution = routes;

		// Decoded
		IDecoder<DemandRoutesSolution, DemandRoutesSolution> decoder = new PassThroughDecoder<>();

		// Evaluator
		IFitnessEvaluator<DemandRoutesSolution> evaluator = new ThroughOneFitnessEvaluator<>(
				new FunctionValueFitnessEvaluator<>());

		//Stopper
		IOptimisationStopper<DemandRoutesSolution> stopper = new CompositeOptimisationStopper<>(Arrays.asList(
				new GenerationNumberEvolutionStopper<>(maximumGenerationNumber),
				new StagnationStopper<>(maximumStagnation)
		));
		
		IFunction<DemandRoutesSolution> function = new ID1Function<DemandRoutesSolution>() {
			
			@Override
			public double applyAsDouble(DemandRoutesSolution solutionRoutes) {
				double error = 0;
				for(DemandRoute route:solutionRoutes) {
					if(route.isEmpty())
						continue;
					error+=500*Math.max(0, route.getDemand()-route.capacity);
				}
								
				return error+solutionRoutes.length;
			}

		};
		
		// Neighborhood
		Random random = RNGProvider.getRandom();		
		CompositeNeighborhood<DemandRoutesSolution> compositeNeighborhood = new CompositeNeighborhood<>(Arrays.asList(
				new SwapStationsNG(random, description)
				,new ChangeStationRouteNG(random, description)
				,new MergeRouteNG(description, random)
				));
		RepeatNeighborhood<DemandRoutesSolution> neighborhood = new RepeatNeighborhood<>(repetitionChance,
				compositeNeighborhood);

		//Optimization algorithm
		IOptimizationAlgorithm<DemandRoutesSolution> optimizationAlgorithm = new GreedyAlgorithm<DemandRoutesSolution, DemandRoutesSolution>(
				decoder, neighborhood, evaluator, stopper, function, startSolution);

		//Run optimization
		DemandRoutesSolution solution = optimizationAlgorithm.run();
		
		//remove empty routes
		solution.removeEmptyRoutes();
		
		return solution;
	}


}
