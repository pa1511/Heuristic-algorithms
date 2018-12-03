package heuristic.knapsack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import heuristic.knapsack.Item;
import heuristic.knapsack.KnapsackSolver;

public class KnapsackProblemExample {

	
	private KnapsackProblemExample() {}
	
	
	public static void main(String[] args) throws IOException {
		
		//Read problem definition
		List<String> itemDefs = Files.readAllLines(Paths.get("data/knapsack/p_2.txt"));
		final int knapsackSize = Integer.parseInt(itemDefs.remove(0));
		
		List<Item> items = itemDefs.stream().map(itemDef->{
			String[] values = itemDef.split(" ");
			return new Item(Integer.parseInt(values[0]), Double.parseDouble(values[1]));
		}).collect(Collectors.toList());
		Item[] itemArr = items.toArray(new Item[items.size()]);
		
		System.out.println("Knapsack size: " + knapsackSize);
		System.out.println("Items: " + Arrays.toString(itemArr));
		
		//Solving knapsack problem
		long start = System.nanoTime();
		double maxValue = new KnapsackSolver<Item>().solve(knapsackSize, 0,itemArr);
		long end = System.nanoTime();
		System.out.println("Time: " + (end-start)*1e-6 + " ms");
		
		//Presenting solution
		System.out.println("\nMax knapsack value: " + maxValue);
	}
}
