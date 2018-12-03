package heuristic.knapsack;

import java.util.ArrayList;
import java.util.List;

public class KnapsackSolver<T extends KnapsackItem> {

	public double solve(int knapsackSize, int itemId, T[] itemArr) {
		
		if(knapsackSize==0 || itemId==itemArr.length)
			return 0;

		if(itemArr[itemId].getSize()>knapsackSize) {
			return solve(knapsackSize, itemId+1, itemArr);
		}
			
		double withItem = itemArr[itemId].getValue()+solve(knapsackSize-itemArr[itemId].getSize(), itemId+1, itemArr);
		double withoutItem = solve(knapsackSize, itemId+1, itemArr);
		return Double.max(withoutItem ,withItem);
	}

	public double solve(int knapsackSize, int itemId, T[] itemArr, List<T> selected) {
		
		if(knapsackSize==0 || itemId==itemArr.length)
			return 0;

		List<T> selectedWithout = new ArrayList<>();
		double withoutItem = solve(knapsackSize, itemId+1, itemArr, selectedWithout);
		if(itemArr[itemId].getSize()>knapsackSize) {
			selected.addAll(selectedWithout);
			return withoutItem;
		}
		List<T> selectedWith = new ArrayList<>();
		double withItem = itemArr[itemId].getValue()+solve(knapsackSize-itemArr[itemId].getSize(), itemId+1, itemArr, selectedWith);
		if(withItem>withoutItem) {
			selected.addAll(selectedWith);
			selected.add(itemArr[itemId]);
		}
		else
			selected.addAll(selectedWithout);
		
		return Double.max(withoutItem ,withItem);
	}

}
