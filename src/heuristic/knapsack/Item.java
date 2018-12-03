package heuristic.knapsack;

public class Item implements KnapsackItem{
	
	public final int size;
	public final double value;
	
	public Item(int size, double value) {
		this.size = size;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "["+size+","+value+"]";
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public double getValue() {
		return value;
	}
	
}