package heuristic.routing.tsp;

public class TSPArraySolutionElementSwapOptimizer implements ITSPArrayOptimizer {

	@Override
	public double optimizeImplicit(double[][] distance, int[] station) {
		return optimizeImplicit(distance, station, station.length);
	}
	
//	@Override
//	public double optimizeImplicit(double[][] distance, int[] station, int N) {
//		
//		double totalChange = 0;
//		
//		boolean changed = false;
//		do {
//			changed = false;
//			
//			for(int i=1; i<N; i++) {
//				
//				int ps = (i==1) ? 0 : station[i-2];
//				int ss = station[i-1];
//				int es = station[i];
//				int ns = (i+1==N) ? 0 : station[i+1];
//
//				double change = -distance[ps][ss]-distance[es][ns]+distance[ps][es]+distance[ss][ns];
//				
//				if(change<0) {
//					changed = true;
//					//
//					totalChange+=change;
//					//
//					station[i-1]=es;
//					station[i]=ss;
//				}
//			}
//			
//			
//		}while(changed);
//		
//		return totalChange;
//	}

	@Override
	public double optimizeImplicit(double[][] distance, int[] station, int N) {
		
		double totalChange = 0;
		int diff = 1;
		
		do {
			boolean changed = false;
			do {
				changed = false;
				
				for(int i=0; i<N-diff; i++) {
					
					int ps1 = (i-1==-1) ? 0: station[i-1];
					int ss = station[i];
					int ns1 =station[i+1];
					
					int ps2 = station[i+diff-1];
					int es = station[i+diff];
					int ns2 = (i+diff+1==N) ? 0 : station[i+diff+1];
	
					double change = 0;
					if(ss==ps2 && ns1==es) {
						change = -distance[ps1][ss]-distance[es][ns2]+distance[ps1][es]+distance[ss][ns2];
					}
					else {
						change = -distance[ps1][ss]-distance[ss][ns1]-distance[ps2][es]-distance[es][ns2]
								+distance[ps1][es]+distance[es][ns1]+distance[ps2][ss]+distance[ss][ns2];
					}
					
					if(change<0) {
						changed = true;
						//
						totalChange+=change;
						//
						station[i]=es;
						station[i+diff]=ss;
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
