package heuristic.routing;

public class LoopRoute extends Route<LoopRoute>{

	public LoopRoute(double[][] distances) {
		super(distances);
	}

	protected LoopRoute(double[][] distances,int[] stations,int size, double length) {
		super(distances,stations,size,length);
	}
	
	@Override
	public void add(int station) {
		double lengthChange = 0;
		
		if(!isEmpty()) {
			int s1 = getFirst();
			int sn = getLast();
			lengthChange = -distances[s1][sn] + distances[sn][station] + distances[s1][station];
		}
		
		
		add(station, lengthChange);
	}


	@Override
	protected void addAll(int n,@SuppressWarnings("hiding") int[] stations) {
		double lengthChange;
		if(isEmpty()) {
			lengthChange = RoutingUtility.calculateLoopLength(stations, n, distances);
		}
		else {
			int fs = getFirst();
			int ls = getLast();
			lengthChange = RoutingUtility.calculateChainLength(stations, n, distances)-distances[ls][fs]
					+distances[ls][stations[0]]+distances[stations[n-1]][fs];
		}
		
		addAll(stations, n, lengthChange);
	}
	
	@Override
	public int removeAt(int location) {		
		int rs = get(location);

		double lengthChange = 0;
		
		if(size==1) {
			lengthChange = 0;
		}
		else {
			
			int sp = get((location-1+size)%size);
			int sn = get((location+1)%size);
			
			lengthChange = -distances[sp][rs]-distances[rs][sn]+distances[sp][sn];
		}
	
		return removeAt(location,lengthChange);
	}

	@Override
	public LoopRoute clone() {
		return new LoopRoute(distances, stations, size, length);
	}

}
