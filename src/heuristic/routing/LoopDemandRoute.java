package heuristic.routing;

public class LoopDemandRoute extends DemandRoute<LoopDemandRoute>{

	public LoopDemandRoute(double[][] distances, double[] demands, double capacity) {
		super(distances, demands, capacity);
	}

	protected LoopDemandRoute(double[][] distances, double[] demands,int[] stations,int size, double length, double demand, double capacity) {
		super(distances,demands,stations,size,length,demand, capacity);
	}
	
	@Override
	public void add(int station) {
		double demandChange = demands[station];
		double lengthChange = 0;
		
		if(!isEmpty()) {
			int s1 = getFirst();
			int sn = getLast();
			lengthChange = -distances[s1][sn] + distances[sn][station] + distances[s1][station];
		}
		
		
		add(station, lengthChange, demandChange);
	}


	@Override
	protected void addAll(int n, @SuppressWarnings("hiding") int[] stations) {
		double demandChange = RoutingUtility.calculateDemand(n, stations, demands);
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
		
		addAll(stations, n, lengthChange, demandChange);
	}

	@Override
	public int removeAt(int location) {
		int rs = get(location);

		double demandChange = -1*demands[rs];
		double lengthChange = 0;
		
		if(size==1) {
			lengthChange = 0;
		}
		else {
			
			int sp = get((location-1+size)%size);
			int sn = get((location+1)%size);
			
			lengthChange = -distances[sp][rs]-distances[rs][sn]+distances[sp][sn];
		}
	
		return removeAt(location,lengthChange,demandChange);
	}

	@Override
	public LoopDemandRoute clone() {
		return new LoopDemandRoute(distances, demands, stations, size, length, demand, capacity);
	}

}
