package heuristic.routing.tsp;

import java.util.ArrayList;
import java.util.List;


public class TSPSolution{
	
	public final List<TSPSolutionElement> order = new ArrayList<>();
	public double distance = 0.0;
	
	public TSPSolution copy() {
		
		TSPSolution solution = new TSPSolution();
		solution.order.addAll(this.order);
		solution.distance = this.distance;
		return solution;
	}

	
	public void display(boolean simple) {
		if(simple) {
			for(TSPSolutionElement element:order) {
				System.out.print((element.start+1) + " ");
			}
			System.out.println();
		}
		else {
			for(TSPSolutionElement element:order) {
				System.out.println("Start: " + element.start + " End: " + element.end + " Distance: " + element.distance);
			}
		}
		System.out.println("Total distance: " + distance);
	}
	
	public int[] toArray() {
		int[] array = new int[order.size()];
		int i=0;
		for(TSPSolutionElement element:order) {
			array[i] = element.start;
			i++;
		}
		return array;
	}
}