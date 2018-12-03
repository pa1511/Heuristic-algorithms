package heuristic.routing;

public class ImplicitLoopRoute extends Route<ImplicitLoopRoute>{

	public ImplicitLoopRoute(double[][] distances) {
		super(distances);
	}

	protected ImplicitLoopRoute(double[][] distances, int[] stations,int size, double length) {
		super(distances,stations,size,length);
	}
	
	@Override
	public void add(int station) {
		double lengthChange;
		
		if(isEmpty()) {
			lengthChange = distances[0][station]+distances[station][0];
		}
		else {
			int sn = getLast();
			lengthChange = -distances[sn][0]+distances[sn][station]+distances[station][0];
		}		
		
		add(station, lengthChange);
	}

	@Override
	protected void addAll(int n, @SuppressWarnings("hiding") int[] stations) {
		double lengthChange;
		
		if(isEmpty()) {
			lengthChange = RoutingUtility.calculateImplicitLoopLength(stations, n, distances);
		}
		else {
			int ls = getLast();
			lengthChange = RoutingUtility.calculateChainLength(stations, n, distances)-distances[ls][0]
					+distances[ls][stations[0]]+distances[stations[n-1]][0];
		}
		
		addAll(stations, n, lengthChange);
	}

	@Override
	public int removeAt(int location) {
		int rs = get(location);
		
		double lengthChange = 0;
		
		if(location==0) {
			if(size==1) {
				lengthChange = -distances[0][rs]-distances[rs][0];
			}
			else {
				int ns = get(location+1);
				lengthChange = -distances[0][rs]-distances[rs][ns]+distances[0][ns];
			}
		}
		else if(location==size-1) {
			int ps = get(location-1);
			lengthChange = -distances[ps][rs]-distances[rs][0]+distances[ps][0];
		}
		else {
			int ps = get(location-1);
			int ns = get(location+1);
			
			lengthChange = -distances[ps][rs]-distances[rs][ns]+distances[ps][ns];
		}

		return removeAt(location, lengthChange);
	}

	@Override
	public ImplicitLoopRoute clone() {
		return new ImplicitLoopRoute(distances, stations, size, length);
	}


}
