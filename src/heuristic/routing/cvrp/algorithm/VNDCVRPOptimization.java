package heuristic.routing.cvrp.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import function.ID1Function;
import heuristic.VariableNeighborhoodDescent;
import heuristic.routing.DemandRoute;
import heuristic.routing.cvrp.CVRPDescription;
import heuristic.routing.cvrp.CVRPUtility;
import heuristic.routing.cvrp.DemandRoutesSolution;
import heuristic.routing.cvrp.ICVRPOptimize;
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
		INeighborhood<DemandRoutesSolution> neighborhood1 = new INeighborhood<DemandRoutesSolution>() {
			
			@Override
			public void neighborhood(DemandRoutesSolution element, List<DemandRoutesSolution> neighborhood) {
				
				for(int cr=0,size=element.routes.size();cr<size;cr++) {
					DemandRoute originalRoute = element.routes.get(cr);
					for(int cs=0; cs<originalRoute.size();cs++) {
						for(int nr=0; nr<size;nr++) {
							if(cr==nr)
								continue;
							
							int station = originalRoute.get(cs);
							
							if(element.routes.get(nr).getDemand()+description.demand[station]>description.capacity)
								continue;

							
							DemandRoutesSolution neighbor = element.clone();
							DemandRoute route1 = neighbor.get(cr);
							DemandRoute route2 = neighbor.get(nr);
							
							double originalLength = route1.getLength()+route2.getLength();
														
							station = route1.get(cs);
							route1.removeAt(cs);
							route2.add(station);
																					
							CVRPUtility.optimizeRoute(route1);
							CVRPUtility.optimizeRoute(route2);		
							
							double newLength = route1.getLength()+route2.getLength();

							if(newLength<originalLength) {
								neighbor.length+=-originalLength+newLength;
								neighborhood.add(neighbor);
							}
						}
					}
				}
				
			}
		};
		
		INeighborhood<DemandRoutesSolution> neighborhood2 = new INeighborhood<DemandRoutesSolution>() {
			
			@Override
			public void neighborhood(DemandRoutesSolution element, List<DemandRoutesSolution> neighborhood) {

				for(int cr=0,size=element.routes.size(); cr<size; cr++) {
					for(int nr=cr+1; nr<size; nr++) {
						
						if(element.get(cr).isEmpty() || element.get(nr).isEmpty())
							continue;
						
						if(element.get(cr).getDemand()+element.get(nr).getDemand()>description.capacity)
							continue;
						
							
						DemandRoutesSolution neighbor = element.clone();
						DemandRoute route1 = neighbor.get(cr);
						DemandRoute route2 = neighbor.get(nr);

						double originalLength = route1.getLength()+route2.getLength();

						route1.addAll(route2);
						route2.removeAll();
						
						CVRPUtility.optimizeRoute(route1);
						
						double newLength = route1.getLength()+route2.getLength();
						
						if(newLength<originalLength) {
							neighbor.length+=-originalLength+newLength;
							neighborhood.add(neighbor);
						}
					}
				}
				
			}
		};

		INeighborhood<DemandRoutesSolution> neighborhood3 = new INeighborhood<DemandRoutesSolution>() {
			
			@Override
			public void neighborhood(DemandRoutesSolution element, List<DemandRoutesSolution> neighborhood) {
				
				for(int cr=0,size=element.routes.size();cr<size;cr++) {
					DemandRoute originalRoute1 = element.routes.get(cr);
					for(int cs=0,sizeR1=originalRoute1.size(); cs<sizeR1;cs++) {
						for(int nr=0; nr<size;nr++) {
							if(cr==nr)
								continue;
							DemandRoute originalRoute2 = element.routes.get(nr);
							for(int ns=0,sizeR2=originalRoute2.size(); ns<sizeR2;ns++) {
								
								int s1 = originalRoute1.get(cs);
								int s2 = originalRoute2.get(ns);
								double[] demands = description.demand;
								
								if(originalRoute1.getDemand()-demands[s1]+demands[s2]>description.capacity ||
										originalRoute2.getDemand()-demands[s2]+demands[s1]>description.capacity)
									continue;
								
								
								DemandRoutesSolution neighbor = element.clone();
								DemandRoute route1 = neighbor.get(cr);
								DemandRoute route2 = neighbor.get(nr);
								
								double originalLength = route1.getLength()+route2.getLength();
								
								s1 = route1.removeAt(cs);
								s2 = route2.removeAt(ns);
								
								route1.add(s2);
								route2.add(s1);
																						
								CVRPUtility.optimizeRoute(route1);
								CVRPUtility.optimizeRoute(route2);		
								
								double newLength = route1.getLength()+route2.getLength();

								if(newLength<originalLength) {
									neighbor.length+=-originalLength+newLength;
									neighborhood.add(neighbor);
								}

							}							
						}
					}
				}
				
			}
		};

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
