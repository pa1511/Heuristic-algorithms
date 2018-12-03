package heuristic.routing;

public class ImplicitLoopDemandRoute extends DemandRoute<ImplicitLoopDemandRoute>{

	public ImplicitLoopDemandRoute(double[][] distances, int[] demands) {
		super(distances, demands);
	}

	protected ImplicitLoopDemandRoute(double[][] distances, int[] demands,int[] stations,int size, double length, int demand) {
		super(distances,demands,stations,size,length,demand);
	}
	
	@Override
	public void add(int station) {
		int demandChange = demands[station];
		double lengthChange;
		
		if(isEmpty()) {
			lengthChange = distances[0][station]+distances[station][0];
		}
		else {
			int sn = getLast();
			lengthChange = -distances[sn][0]+distances[sn][station]+distances[station][0];
		}		
		
		add(station, lengthChange, demandChange);
	}

	@Override
	protected void addAll(int n, @SuppressWarnings("hiding") int[] stations) {
		if(n==0)
			return;
		
		int demandChange = RoutingUtility.calculateDemand(n, stations, demands);
		double lengthChange;
		
		if(isEmpty()) {
			lengthChange = RoutingUtility.calculateImplicitLoopLength(stations, n, distances);
		}
		else {
			int ls = getLast();
			lengthChange = RoutingUtility.calculateChainLength(stations, n, distances)-distances[ls][0]
					+distances[ls][stations[0]]+distances[stations[n-1]][0];
		}
		
		addAll(stations, n, lengthChange, demandChange);
	}

	@Override
	public int removeAt(int location) {
		int rs = get(location);
		
		int demandChange = -1*demands[rs];
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

		return removeAt(location, lengthChange, demandChange);
	}

	@Override
	public ImplicitLoopDemandRoute clone() {
		return new ImplicitLoopDemandRoute(distances, demands, stations, size, length, demand);
	}


}
