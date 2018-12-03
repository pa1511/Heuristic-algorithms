package heuristic.routing;


public abstract class DemandRoute<T extends DemandRoute<T>> extends Route<T> {
	
	protected int[] demands;
	//
	protected int demand = 0;
	
	public DemandRoute(double[][] distances, int[] demands) {
		super(distances);
		this.demands = demands;
	}
	
	protected DemandRoute(double[][] distances, int[] demands,int[] stations,int size, double length, int demand) {
		super(distances,stations,size,length);
		this.demand = demand;
		this.demands = demands;
	}
	
	@Override
	public abstract DemandRoute<T> clone();

	protected void add(int station, double lengthChange, int demandChange) {
		add(station,lengthChange);
		this.demand+=demandChange;
	}

	protected void addAll(@SuppressWarnings("hiding") int[] stations, double lengthChange, int demandChange) {
		addAll(stations, stations.length, lengthChange, demandChange);
	}

	protected void addAll(@SuppressWarnings("hiding") int[] stations, int n, double lengthChange, int demandChange) {
		addAll(stations, n, lengthChange);
		this.demand+=demandChange;
	}

	protected int removeAt(int i, double lengthChange, int demandChange) {
		this.demand+=demandChange;
		return removeAt(i, lengthChange);
	}

	@Override
	public void removeAll() {
		super.removeAll();
		demand = 0;
	}
	
	public int getDemand() {
		return demand;
	}
	
	public void setDemand(int demand) {
		this.demand = demand;
	}
	
	@Override
	public void copy(Route<T> other) {
		super.copy(other);
		if(other instanceof DemandRoute) {
			this.demand = ((DemandRoute<T>)other).demand;
			this.demands = ((DemandRoute<T>) other).demands;
		}
	}
	
	public int[] getDemands() {
		return demands;
	}
	
}
