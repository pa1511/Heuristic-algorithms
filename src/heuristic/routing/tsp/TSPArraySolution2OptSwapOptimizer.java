package heuristic.routing.tsp;

public class TSPArraySolution2OptSwapOptimizer implements ITSPArrayOptimizer{

	@Override
	public double optimizeImplicit(double[][] distance, int[] station) {
		return optimizeImplicit(distance, station, station.length);
	}
	
	@Override
	public double optimizeImplicit(double[][] distance, int[] station, int N) {
		double totalChange = 0;
		
		
		int diff = 1;
		
		do {
			
			boolean changed = false;
			do {
				
				changed = false;
				
				for(int i=0; i<N-diff;i++) {
					
					int ps = (i-1==-1) ? 0: station[i-1];
					int ss = station[i];
					int es = station[i+diff];
					int ns = (i+diff+1==N) ? 0 : station[i+diff+1];
					
					double change = -distance[ps][ss]-distance[es][ns]+distance[ps][es]+distance[ss][ns];
					
					if(change<0) {
						
						//swap
						int ids = i;
						int ide = i+diff;
						while(ids<ide) {
							int s1 = station[ids];
							int s2 = station[ide];
							//
							station[ide] = s1;
							station[ids] = s2;
							//
							ids++;
							ide--;
						}
						
						//mark change
						changed = true;
						totalChange+=change;
					}
					
				}
				
				if(changed)
					diff = 1;
				else
					diff++;
				
				
			}while(changed);
			
			
		}while(diff<N);
		
		return totalChange;
	}
}
