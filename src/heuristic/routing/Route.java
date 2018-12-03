package heuristic.routing;

import java.util.Arrays;
import java.util.Random;

public abstract class Route<T extends Route<T>> {
	
	protected double[][] distances;
	//
	protected int[] stations;
	protected int size = 0;
	protected double length;
	
	public Route(double[][] distances) {
		stations = new int[4];
		this.distances = distances;
	}
	
	protected Route(double[][] distances,int[] stations,int size, double length) {
		this.size = size;
		this.stations = Arrays.copyOf(stations, stations.length);
		this.length = length;
		this.distances = distances;
	}
	
	public abstract void add(int station);
	public abstract int removeAt(int i);
	protected abstract void addAll(int n, @SuppressWarnings("hiding") int[] stations);
	
	@Override
	public abstract Route<T> clone();

	protected final void add(int station, double lengthChange) {
		if(size==stations.length) {
			int newLength = stations.length*2;
			expandStationsArray(newLength);
		}
		
		stations[size++]=station;
		this.length+=lengthChange;
	}

	public final void addAll(@SuppressWarnings("hiding") int[] stations) {
		addAll(stations.length, stations);
	}

	public final void addAll(T route) {
		addAll(route.size, route.stations);
	}

	protected final void addAll(@SuppressWarnings("hiding") int[] stations, double lengthChange) {
		addAll(stations, stations.length, lengthChange);
	}

	protected final void addAll(@SuppressWarnings("hiding") int[] stations, int n, double lengthChange) {
		if(size+n>=this.stations.length) {
			//Determine new length
			int newLength = this.stations.length*2;
			while(newLength<size+n)
				newLength*=2;

			//Expand stations list
			expandStationsArray(newLength);
		}
		System.arraycopy(stations, 0, this.stations, size, n);
		size+=n;
		this.length+=lengthChange;
	}

	protected final int removeAt(int i, double lengthChange) {
		this.length+=lengthChange;
		int ret = stations[i];
		System.arraycopy(stations, i+1, stations, i, size-i-1);		
		size--;
		return ret;
	}
	
	public int removeAt(Random random) {
		return removeAt(random.nextInt(size));
	}
	
	
	public void removeAll() {
		size = 0;
		length = 0;
	}
	
	public final int get(int i) {
		return stations[i];
	}
	
	public final int get(Random random) {
		return stations[random.nextInt(size)];
	}
	
	public final int getFirst() {
		return stations[0];
	}
	
	public final int getLast() {
		return stations[size-1];
	}
	
	public final int size() {
		return size;
	}
	
	public final boolean isEmpty() {
		return size==0;
	}
	
	public final void flip() {
		
		int ssId = 0;
		int esId = size-1;
		
		while(ssId<esId) {
			int ss = stations[ssId];
			int es = stations[esId];
			//
			stations[esId] = ss;
			stations[ssId] = es;
			//
			ssId++;
			esId--;
		}
		
	}

	public final int[] toArray() {
		return Arrays.copyOf(stations, size);
	}
	
	/**
	 * Be careful when using this array. 
	 * It is a reference to the internal storage.
	 */
	public final int[] getArray() {
		return stations;
	}

	public final double getLength() {
		return length;
	}
		
	public final void setLength(double length) {
		this.length = length;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(Arrays.copyOf(stations, size));
	}

	public void copy(Route<T> other) {
		this.size = other.size;
		if(this.stations.length<other.size) {
			expandStationsArray(other.stations.length);
		}
		System.arraycopy(other.stations, 0, stations, 0, other.size);
		this.length = other.length;
		this.distances = other.distances;
	}
	
	public double[][] getDistances() {
		return distances;
	}
		
	private void expandStationsArray(int newLength) {
		int[] expandedStations = new int[newLength];
		System.arraycopy(stations, 0, expandedStations, 0, stations.length);
		stations = expandedStations;
	}

	
}
