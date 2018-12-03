package heuristic.binPacking;

import java.util.Arrays;
import java.util.PrimitiveIterator.OfInt;
import java.util.Random;

public class Bin1D {
	
	private double capacity;
	private double occupiedCapacity = 0;
	private int[] elements; 
	private int size = 0;
	
	public Bin1D(double capacity) {
		this(4,capacity);
	}
	
	public Bin1D(int elementsSize, double capacity) {
		elements = new int[elementsSize];
		this.capacity = capacity;
	}
	
	public void add(int packet, double volume) {
		
		if(size==elements.length) {
			@SuppressWarnings("hiding")
			int[] elements = new int[this.elements.length*2];
			System.arraycopy(this.elements, 0, elements, 0, size);
			this.elements = elements;
		}
		
		elements[size++]=packet;
		occupiedCapacity+=volume;
	}
	
	public int remove(int i, BPPDescription description) {
		int packet = elements[i];
		System.arraycopy(elements, i+1, elements, i, size-i-1);		
		size--;
		this.occupiedCapacity-=description.weights[packet];		
		return packet;
	}
	
	public int removeLast(BPPDescription description) {
		int packet = elements[--size];
		this.occupiedCapacity-=description.weights[packet];
		return packet;
	}
	
	public int remove(Random random, BPPDescription description) {
		return remove(random.nextInt(size), description);
	}

	public int get(int i) {
		return elements[i];
	}
	
	public int get(Random random) {
		return get(random.nextInt(size));
	}
	
	public void clear() {
		size = 0;
		occupiedCapacity = 0;
	}
		
	public double getCapacity() {
		return capacity;
	}
	
	public double getOccupiedCapacity() {
		return occupiedCapacity;
	}

	public int size() {
		return size;
	}
	
	public final int[] toArray() {
		return Arrays.copyOf(elements, size);
	}
	
	/**
	 * Be careful when using this array. 
	 * It is a reference to the internal storage.
	 */
	public final int[] getArray() {
		return elements;
	}
	
	public boolean isEmpty() {
		return size==0;
	}
	
	@Override
	public Bin1D clone() {
		Bin1D clone = new Bin1D(elements.length, capacity);
		clone.occupiedCapacity = occupiedCapacity;
		System.arraycopy(elements, 0, clone.elements, 0, size);
		clone.size = size;
		return clone;
	}
	
	public void copy(Bin1D other) {
		
		if(this==other)
			return;
		
		if(other.size>this.elements.length) {
			this.elements = new int[other.elements.length];
		}
		
		System.arraycopy(other.elements, 0, this.elements, 0, other.size);
		this.size = other.size;
		this.occupiedCapacity = other.occupiedCapacity;
		this.capacity = other.capacity;
	}
	

	public OfInt iterator() {
		return new OfInt() {
			
			private int current = 0;
			
			@Override
			public boolean hasNext() {
				return current<size;
			}
			
			@Override
			public int nextInt() {
				return get(current++);
			}
		};
	}
	
	
	@Override
	public String toString() {
		return Arrays.toString(toArray()) + " " + occupiedCapacity+"/"+capacity;
	}

	
}
