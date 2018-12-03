package heuristic.routing.cvrp.algorithm;

import java.util.Arrays;
import java.util.Random;

import function.ID1Function;
import function.IFunction;
import heuristic.GreedyAlgorithm;
import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPOptimize;
import optimization.algorithm.IOptimizationAlgorithm;
import optimization.decoder.IDecoder;
import optimization.decoder.PassThroughDecoder;
import optimization.fittnesEvaluator.FunctionValueFitnessEvaluator;
import optimization.fittnesEvaluator.IFitnessEvaluator;
import optimization.fittnesEvaluator.ThroughOneFitnessEvaluator;
import optimization.solution.neighborhood.CompositeNeighborhood;
import optimization.solution.neighborhood.INeighborGenerator;
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
					error+=500*Math.max(0, route.getDemand()-description.capacity);
				}
								
				return error+solutionRoutes.length;
			}

		};
		
		// Neighborhood
		Random random = RNGProvider.getRandom();		
		CompositeNeighborhood<DemandRoutesSolution> compositeNeighborhood = new CompositeNeighborhood<>(Arrays.asList(
				new INeighborGenerator<DemandRoutesSolution>() {
					
					@Override
					public void randomNeighbor(DemandRoutesSolution element, DemandRoutesSolution neighbor) {
						
						DemandRoute route1 = neighbor.getNonEmpty(random);
						DemandRoute route2 = neighbor.getNonEmpty(random);

						if(route1==route2) {
							return;
						}
																
						double route1Length = route1.getLength();
						double route2Length = route2.getLength();						
						
						int s1 = route1.removeAt(random);
						int s2 = route2.removeAt(random);

						route1.add(s2);
						route2.add(s1);
						
						//If the produced solution is not valid we don't want to loose our time on it
						if(route1.getDemand()<=description.capacity && route2.getDemand()<=description.capacity) {
							CVRPUtility.optimizeRoute(route1);
							CVRPUtility.optimizeRoute(route2);
						}
												
						neighbor.length += -route1Length-route2Length+route1.getLength()+route2.getLength();
					}

					
				}
				,new INeighborGenerator<DemandRoutesSolution>() {
					
					@Override
					public void randomNeighbor(DemandRoutesSolution element, DemandRoutesSolution neighbor) {
						
						DemandRoute route1 = neighbor.get(random);
						DemandRoute route2 = neighbor.getNonEmpty(random);
						
						if(route1==route2) {
							return;
						}
												
						double route1Length = route1.getLength();
						double route2Length = route2.getLength();						
						
						int s2 = route2.removeAt(random); 
						route1.add(s2);
						
						//If the produced solution is not valid we don't want to loose our time on it
						if(route1.getDemand()<=description.capacity && route2.getDemand()<=description.capacity) {
							CVRPUtility.optimizeRoute(route1);
							CVRPUtility.optimizeRoute(route2);
						}
						
						neighbor.length += -route1Length-route2Length+route1.getLength()+route2.getLength();
					}
				}
				,new INeighborGenerator<DemandRoutesSolution>() {
					
					@Override
					public void randomNeighbor(DemandRoutesSolution element, DemandRoutesSolution neighbor) {				
						DemandRoute route1 = neighbor.getNonEmpty(random);
						DemandRoute route2 = neighbor.getNonEmpty(random);
												
						if(route1==route2 || route1.getDemand()+route2.getDemand()>description.capacity) {
							return;
						}

						double route1Length = route1.getLength();
						double route2Length = route2.getLength();

						route2.addAll(route1);
						route1.removeAll();;

						CVRPUtility.optimizeRoute(route2);
						
						neighbor.length += -route1Length-route2Length+route1.getLength()+route2.getLength();
					}
				}
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
