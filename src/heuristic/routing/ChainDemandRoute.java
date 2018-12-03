package heuristic.routing;

public class ChainDemandRoute extends DemandRoute<ChainDemandRoute> {

	public ChainDemandRoute(double[][] distances, int[] demands) {
		super(distances, demands);
	}

	protected ChainDemandRoute(double[][] distances, int[] demands,int[] stations,int size, double length, int demand) {
		super(distances,demands,stations,size,length,demand);
	}
	
	@Override
	public void add(int station) {
		if(size>0)
			add(station, distances[getLast()][station], demands[station]);
		else
			add(station, 0, demands[station]);
	}

	@Override
	protected void addAll(int n, @SuppressWarnings("hiding") int[] stations) {
		int demandChange = RoutingUtility.calculateDemand(n, stations, demands);
		double lengthChange = RoutingUtility.calculateChainLength(stations, n, distances);
		if(!isEmpty()) {
			lengthChange+=distances[getLast()][stations[0]];
		}
		addAll(stations, n, lengthChange, demandChange);
	}

	
	@Override
	public int removeAt(int location) {
		int rs = get(location);
		
		int demandChange = -1*demands[rs];
		double lengthChange = 0;
		
		if(location==0) {
			if(size!=1) {
				lengthChange = -distances[rs][get(location+1)];
			}
		}
		else if(location==size-1){
			lengthChange = -distances[get(location-1)][rs];
		}
		else {
			int s1 = get(location-1);
			int s2 = get(location+1);
			lengthChange = -distances[s1][rs]-distances[rs][s2]+distances[s1][s2];
		}
		
		return removeAt(location, lengthChange, demandChange);
	}

	@Override
	public ChainDemandRoute clone() {
		return new ChainDemandRoute(distances, demands, stations, size, length, demand);
	}


}
