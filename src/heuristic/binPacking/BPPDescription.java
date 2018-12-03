package heuristic.binPacking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class BPPDescription {
	
	public final double capacity;
	public final double[] weights;
	public final int packetCount;
	
	public BPPDescription(double capacity, double[] wieghts) {
		this.capacity = capacity;
		this.weights = wieghts;
		packetCount = wieghts.length;
	}
	
	
	//===========================================================================================
	
	public static BPPDescription getDescription(File file) throws IOException {
		
		List<String> lines = Files.readAllLines(file.toPath());
		
		double capacity = 0;
		double[] weights = null;
		int p=0;		
		
		for(int i=0,size=lines.size(); i<size;i++) {
			String line = lines.get(i);
			if(line.trim().isEmpty())
				continue;
			if(i==1) {
				capacity = Double.parseDouble(line);
			}
			else if(i==0) {
				int n = Integer.parseInt(line);
				weights = new double[n];
			}
			else {
				weights[p++]=Double.parseDouble(line);
			}
		}
		
		
		return new BPPDescription(capacity, weights);
	}
}
