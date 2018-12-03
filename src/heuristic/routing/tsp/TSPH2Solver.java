package heuristic.routing.tsp;

public class TSPH2Solver {
	
	private boolean shouldOptimize = true;
	
	public TSPSolution solve(double[][] distances) {
		
		int N = distances[0].length;
		TSPSolution solution = new TSPSolution();
		
		double distance = distances[0][0];
		solution.order.add(new TSPSolutionElement(0, 0, distance));
		solution.distance+=distance;
		boolean[] used = new boolean[N];
		used[0] = true;
		
		//run solver
		solution = solve(distances, N, solution, used);
		
		//Optimizing
		if(shouldOptimize)
			solution = optimize(distances, N, solution,0);
								
		return solution;
	}

	private TSPSolution solve(double[][] distances, int N, TSPSolution solution, boolean[] used) {
		
		double distance;
		for(int i=0; i<N; i++) {
			
			double min = Double.MAX_VALUE;
			int minLinkIndex = -1;
			int minNodeIndex = -1;
			
			for(int j=0; j<N; j++) {
				
				if(used[j])
					continue;
			
				for(int linkIndex =0,linkCount = solution.order.size();linkIndex<linkCount;linkIndex++) {
					TSPSolutionElement link = solution.order.get(linkIndex);
					distance = solution.distance - link.distance + distances[link.start][j] + distances[j][link.end];
					if(min>distance) {
						min = distance;
						minLinkIndex = linkIndex;
						minNodeIndex = j;
					}
				}
			
			}
			
			if(minLinkIndex!=-1 && minNodeIndex!=-1) {
				TSPSolutionElement link = solution.order.remove(minLinkIndex);
				solution.order.add(minLinkIndex, new TSPSolutionElement(link.start, minNodeIndex, distances[link.start][minNodeIndex]));
				solution.order.add(minLinkIndex+1,new TSPSolutionElement(minNodeIndex, link.end, distances[minNodeIndex][link.end]));
				solution.distance = min;
				used[minNodeIndex] = true;
			}
			
		}
		
		return solution;
	}
	
	//TODO: move this to an optimization class
	private TSPSolution optimize(double[][] distances, int N, TSPSolution solution,int startElementIndex) {
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

	public void shouldOptimize(@SuppressWarnings("hiding") boolean shouldOptimize) {
		this.shouldOptimize = shouldOptimize;
	}
}
