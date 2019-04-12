package heuristic.routing.cvrp.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import function.ID1Function;
import heuristic.VariableNeighborhoodDescent;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPOptimize;
import heuristic.routing.cvrp.neighborhood.ChangeStationRoute;
import heuristic.routing.cvrp.neighborhood.MergeRoutes;
import heuristic.routing.cvrp.neighborhood.SwapStations;
import optimization.decoder.IDecoder;
import optimization.decoder.PassThroughDecoder;
import optimization.fittnesEvaluator.FunctionValueFitnessEvaluator;
import optimization.fittnesEvaluator.IFitnessEvaluator;
import optimization.fittnesEvaluator.NegateFitnessEvaluator;
import optimization.solution.neighborhood.INeighborhood;
import optimization.solution.neighborhood.selection.INeighborSelection;
import optimization.solution.neighborhood.selection.SelectBestImprovingNeighbor;

public class VNDCVRPOptimization implements ICVRPOptimize {

	@Override
	public DemandRoutesSolution optimize(CVRPDescription description, DemandRoutesSolution routes) {
		
		DemandRoutesSolution startSolution = routes;
		
		// Decoded
		IDecoder<DemandRoutesSolution, DemandRoutesSolution> decoder = new PassThroughDecoder<>();
		
		// Evaluator
		IFitnessEvaluator<DemandRoutesSolution> evaluator = new NegateFitnessEvaluator<>(
				new FunctionValueFitnessEvaluator<>());
		
		//Function
		ID1Function<DemandRoutesSolution> function = (s)->s.length;

		//Neighborhood Selection
		INeighborSelection<DemandRoutesSolution> neighborhoodSelection = new SelectBestImprovingNeighbor<>();
		
		//Neighborhoods
		Function<DemandRoutesSolution,Predicate<DemandRoutesSolution>> neighborAcceptanceTest = element -> neighbor -> neighbor.length<element.length;
		INeighborhood<DemandRoutesSolution> neighborhood1 = new ChangeStationRoute(description, neighborAcceptanceTest);
		INeighborhood<DemandRoutesSolution> neighborhood2 = new MergeRoutes(description, neighborAcceptanceTest);
		INeighborhood<DemandRoutesSolution> neighborhood3 = new SwapStations(description, neighborAcceptanceTest);
				
				
		List<INeighborhood<DemandRoutesSolution>> neghborhoods = Arrays.asList(
				neighborhood1, neighborhood2,neighborhood3 
		);
		
		
		//Optimization algorithm
		VariableNeighborhoodDescent<DemandRoutesSolution, DemandRoutesSolution> optimization = new VariableNeighborhoodDescent<>(
				startSolution, decoder, neghborhoods, false, evaluator, neighborhoodSelection, function);

		DemandRoutesSolution solution = optimization.run();
		
		//remove empty routes
		solution.removeEmptyRoutes();
		
		return solution;
	}

}
