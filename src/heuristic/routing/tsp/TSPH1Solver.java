package heuristic.routing.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TSPH1Solver {
	
	//TODO: improve code-quality when you are done experimenting 
	private boolean shouldOptimize = false;
	
	public TSPSolution solve(double[][] distances) {
		return solve(distances, IntStream.range(0, distances.length).toArray());
	}
	
	public TSPSolution solve(double[][] distances, int[] elements) {
		
		if(elements.length<=3) {
			TSPSolution solution = new TSPSolution();
			for(int i=0; i<elements.length;i++) {
				int elem1 = elements[i];
				int elem2 = elements[(i+1)%elements.length];
				solution.order.add(new TSPSolutionElement(elem1, elem2, distances[elem1][elem2]));
				solution.distance+=distances[elem1][elem2];
			}
			return solution;
		}
		
		int N = elements.length;
		TSPSolution solution = new TSPSolution();
		
		int startElem = elements[0];
		double distance = distances[startElem][startElem];
		solution.order.add(new TSPSolutionElement(startElem, startElem, distance));
		solution.distance+=distance;

		//run solver
		solution = solve(distances, elements, N, solution,1);
		
		//extra remove-insert round to try and optimize
		if(shouldOptimize) {
			solution = optimize(distances, N, solution,0);
						
			optimize2(distances, solution);
		
			optimize3(distances, solution);			
		}		
		
		return solution;
	}

	//TODO: move this to an optimization class
	private void optimize3(double[][] distances, TSPSolution solution) {
		int limit = solution.order.size();
		for(int i=0; i<limit-2; i++) {
			TSPSolutionElement element1 = solution.order.get(i);
			TSPSolutionElement elementM = solution.order.get(i+1);
			TSPSolutionElement element2 = solution.order.get(i+2);

			double newDistance = solution.distance-element1.distance-element2.distance
					+distances[element1.start][element2.start]+distances[element1.end][element2.end];
			
			if(newDistance<solution.distance) {
				solution.distance = newDistance;
				//
				int t = element1.end;
				element1.end = element2.start;
				element2.start = t;
				//
				element1.distance = distances[element1.start][element1.end];
				element2.distance = distances[element2.start][element2.end];
				//
				t = elementM.start;
				elementM.start = elementM.end;
				elementM.end = t;
			}
			
		}
	}

	//TODO: move this to an optimization class
	private void optimize2(double[][] distances, TSPSolution solution) {
		for(int r =0; r<10; r++) {
			int limit = solution.order.size();
			for(int i=0; i<limit-3; i++) {
				for(int j=i+3;j<limit;j++) {
					
					TSPSolutionElement element1 = solution.order.get(i);
					TSPSolutionElement element1F = solution.order.get(i+1);
					TSPSolutionElement element2 = solution.order.get(j);
					TSPSolutionElement element2B = solution.order.get(j-1);
					
					
					double e1d = distances[element1.start][element2.start];
					double e1fd = distances[element2.start][element1F.end];
					double e2d = distances[element1.end][element2.end];
					double e2bd = distances[element2B.start][element1.end];
					
					double newDistance = solution.distance
							-element1.distance-element2.distance-element1F.distance-element2B.distance
							+e1d+e1fd
							+e2d+e2bd;
					
					if(newDistance<solution.distance) {
						solution.distance = newDistance;
						//
						int t = element1.end;
						element1.end = element2.start;
						element2.start = t;
						//
						element1.distance = e1d;
						element2.distance = e2d;
						//
						element1F.start = element1.end;
						element1F.distance = e1fd;
						//
						element2B.end = element2.start;
						element2B.distance = e2bd;
					}
				}
			}
		}
	}

	//TODO: move this to an optimization class
	private TSPSolution optimize(double[][] distances,int N, TSPSolution solution,int startElementIndex) {
		double distance;
		for(int i=startElementIndex; i<N;i++) {
			TSPSolutionElement link = solution.order.remove(i);
			
			int linkCount = solution.order.size();
			TSPSolutionElement before = solution.order.get((i-1+linkCount)%linkCount);
			
			before.end = link.end;
			distance = before.distance;
			before.distance = distances[before.start][before.end];
			
			solution.distance += -link.distance - distance + before.distance;
			
			int id = link.start;
			
			double min = Double.MAX_VALUE;
			int minIndex = -1;
			for(int linkIndex =0;linkIndex<linkCount;linkIndex++) {
				link = solution.order.get(linkIndex);
				distance = solution.distance - link.distance + distances[link.start][id] + distances[id][link.end];
				if(min>distance) {
					min = distance;
					minIndex = linkIndex;
				}
			}
			
			link = solution.order.remove(minIndex);
			solution.order.add(minIndex, new TSPSolutionElement(link.start, id, distances[link.start][id]));
			solution.order.add(minIndex+1,new TSPSolutionElement(id, link.end, distances[id][link.end]));
			solution.distance = min;
			
		}
		
		return solution;
	}

	private TSPSolution solve(double[][] distances, int[] elements,int N, TSPSolution solution,int startElementIndex) {
		double distance;
		for(int i=startElementIndex; i<N; i++) {
			
			int element = elements[i];
			
			double min = Double.MAX_VALUE;
			int minIndex = -1;
			List<Integer> minIndexes = new ArrayList<>();
			
			for(int linkIndex =0,linkCount = solution.order.size();linkIndex<linkCount;linkIndex++) {
				TSPSolutionElement link = solution.order.get(linkIndex);
				distance = solution.distance - link.distance + distances[link.start][element] + distances[element][link.end];
				if(Math.abs(min-distance)<1e-8) {
					minIndexes.add(Integer.valueOf(linkIndex));
				}
				if(min>distance) {
					min = distance;
					minIndex = linkIndex;
					minIndexes.clear();
					minIndexes.add(Integer.valueOf(minIndex));
				}
			}
			
			if(minIndexes.size()==1) {
				TSPSolutionElement link = solution.order.remove(minIndex);
				solution.order.add(minIndex, new TSPSolutionElement(link.start, element, distances[link.start][element]));
				solution.order.add(minIndex+1,new TSPSolutionElement(element, link.end, distances[element][link.end]));
				solution.distance = min;
			}
			else {
				
				TSPSolution original = solution.copy();
				
				TSPSolution best = null;
				
				for(Integer minId:minIndexes) {
					minIndex = minId.intValue();
					solution = original.copy();
					
					TSPSolutionElement link = solution.order.remove(minIndex);
					solution.order.add(minIndex, new TSPSolutionElement(link.start, element, distances[link.start][element]));
					solution.order.add(minIndex+1,new TSPSolutionElement(element, link.end, distances[element][link.end]));
					solution.distance = min;
					
					solution = solve(distances, elements, N, solution, i+1);
					if(best == null || best.distance>solution.distance) {
						best = solution;
					}
				}
				
				return best;
			}
			
		}
		
		return solution;
	}
	
	public void shouldOptimize(@SuppressWarnings("hiding") boolean shouldOptimize) {
		this.shouldOptimize = shouldOptimize;
	}
}
