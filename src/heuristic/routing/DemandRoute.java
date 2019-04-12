package heuristic.routing;


public abstract class DemandRoute<T extends DemandRoute<T>> extends Route<T> {
	
	protected double[] demands;
	//
	protected double demand = 0;
	public final double capacity;
	
	public DemandRoute(double[][] distances, double[] demands, double capacity) {
		super(distances);
		this.demands = demands;
		this.capacity = capacity;
	}
	
	protected DemandRoute(double[][] distances, double[] demands,int[] stations,int size, double length, double demand, double capacity) {
		super(distances,stations,size,length);
		this.demand = demand;
		this.demands = demands;
		this.capacity = capacity;
	}
	
	@Override
	public abstract DemandRoute<T> clone();

	protected void add(int station, double lengthChange, double demandChange) {
		add(station,lengthChange);
		this.demand+=demandChange;
	}

	protected void addAll(@SuppressWarnings("hiding") int[] stations, double lengthChange, double demandChange) {
		addAll(stations, stations.length, lengthChange, demandChange);
	}

	protected void addAll(@SuppressWarnings("hiding") int[] stations, int n, double lengthChange, double demandChange) {
		addAll(stations, n, lengthChange);
		this.demand+=demandChange;
	}

	protected int removeAt(int i, double lengthChange, double demandChange) {
		this.demand+=demandChange;
		return removeAt(i, lengthChange);
	}

	@Override
	public void removeAll() {
		super.removeAll();
		demand = 0;
	}
	
	public double getDemand() {
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
	
	public double[] getDemands() {
		return demands;
	}
	
}
