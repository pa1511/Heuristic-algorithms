package heuristic.routing;

public class ChainRoute extends Route<ChainRoute> {

	public ChainRoute(double[][] distances) {
		super(distances);
	}

	protected ChainRoute(double[][] distances,int[] stations,int size, double length) {
		super(distances,stations,size,length);
	}
	
	@Override
	public void add(int station) {
		if(size>0)
			add(station, distances[getLast()][station]);
		else
			add(station, 0);
	}

	@Override
	protected void addAll(int n, @SuppressWarnings("hiding") int[] stations) {
		double lengthChange = RoutingUtility.calculateChainLength(stations, n, distances);
		if(!isEmpty()) {
			lengthChange+=distances[getLast()][stations[0]];
		}
		addAll(stations, n, lengthChange);
		
	}	
	@Override
	public int removeAt(int location) {
		int rs = get(location);
		
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
		
		return removeAt(location, lengthChange);
	}

	@Override
	public ChainRoute clone() {
		return new ChainRoute(distances, stations, size, length);
	}

}
