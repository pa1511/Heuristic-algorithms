package heuristic.coins;

import java.util.Arrays;

public class CoinsProblem {

	
	
	public static void main(String[] args) {
	
		int[] coinValues = new int[] {1,2,5};
		
		int value = 14;
		int coinCount = getMinCoinsForValue(value,coinValues);
		System.out.println("Min coins for value " + value + " is: " + coinCount);
		
	}

	private static int getMinCoinsForValue(int value, int[] coinValues) {
		
		int[] coinCount = new int[value+1];
		Arrays.fill(coinCount, Integer.MAX_VALUE);
		coinCount[0] = 0;
		
		Arrays.sort(coinValues);

		for(int i=0; i<coinCount.length; i++) {
			for(int coinValue:coinValues) {
				
				if(coinValue>i)
					break;
				
				if(coinCount[i-coinValue]<coinCount[i])
					coinCount[i] = coinCount[i-coinValue]+1;
			}
		}
		
		return coinCount[value];
	}
	
	
}
