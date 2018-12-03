package heuristic.routing.tsp;

public class TSPSolutionElement{
	
	public int start;
	public int end;
	public double distance;

	public TSPSolutionElement(int start, int end, double distance) {
		this.start = start;
		this.end = end;
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return "["+start+","+end+"]:"+distance;
	}
}