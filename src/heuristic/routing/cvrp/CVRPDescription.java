package heuristic.routing.cvrp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import heuristic.routing.EdgeWeightType;
import heuristic.routing.WeightCalculationFunction;

public class CVRPDescription {

	public final int dimension;
	private final double[] capacity;
	public final double[][] locations;
	public final double[] demand;
	public final double[][] distance;

	public CVRPDescription(int dim, double[] cap, double[][] locations, double[] demands, EdgeWeightType edgeWeightType) {
		this.dimension = dim;
		this.capacity = cap;
		this.locations = locations;
		this.demand = demands;
		
		distance = new double[dim][dim];
		WeightCalculationFunction calculationFunction = edgeWeightType.getCalculationFunction();
		for(int i=0; i<dim; i++) {
			for(int j=i+1;j<dim; j++) {
				distance[i][j]=distance[j][i]=calculationFunction.calculate(locations[i][0], locations[i][1], locations[j][0], locations[j][1]);
			}
		}
	}
	
	public double getCapacity(int i) {
		if(i>=capacity.length)
			return capacity[0];
		return capacity[i];
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Capacity").append(Arrays.toString(this.capacity)).append("\n");
		stringBuilder.append("Dimension").append(this.dimension).append("\n");
		//
		stringBuilder.append("Locations:").append("\n");
		for(double[] location:locations)
			stringBuilder.append(Arrays.toString(location)).append("\n");
		//
		stringBuilder.append("Demands:").append("\n");
		stringBuilder.append(Arrays.toString(demand)).append("\n");
		
		return stringBuilder.toString();
	}

	//======================================================================================================================
	
	private static enum ReadState {
		GENERAL_INFO, NODE_COORDINATE, NODE_DEMANDS;
	}

	@SuppressWarnings("null")
	public static CVRPDescription getDescription(String fileName) throws IOException {
		
		int dim = 0;
		double[] cap = null;
		EdgeWeightType edgeWeightType = null;
		double[][] locations = null;
		double[] demands = null;
		
		
		ReadState state = ReadState.GENERAL_INFO;
		
		List<String> lines = Files.readAllLines(Paths.get(fileName));		
		while(!lines.isEmpty()) {
			String line = lines.remove(0);
			if(line.startsWith("NAME") || line.startsWith("COMMENT") || line.startsWith("TYPE"))
				continue;
			
			if(line.startsWith("DIMENSION")) {
				dim = Integer.parseInt(line.replace("DIMENSION :", "").trim());
				locations = new double[dim][2];
				demands = new double[dim];
			}
			else if(line.startsWith("EDGE_WEIGHT_TYPE")) {
				line = line.replace("EDGE_WEIGHT_TYPE :", "").trim();
				edgeWeightType = EdgeWeightType.getWeightType(line);
			}
			else if(line.startsWith("CAPACITY")){
				double capacity = Double.parseDouble(line.replace("CAPACITY :", "").trim());
				cap = new double[] {capacity};
			}
			else if(line.startsWith("NODE_COORD_SECTION")) {
				state = ReadState.NODE_COORDINATE;
			}
			else if(line.startsWith("DEMAND_SECTION")) {
				state = ReadState.NODE_DEMANDS;
			}
			else if(line.startsWith("DEPOT_SECTION")){
				break;
			}
			else {
				if(state==ReadState.NODE_COORDINATE) {
					String[] data = line.split(" ");
					int id = Integer.parseInt(data[0].trim())-1;
					double x = Double.parseDouble(data[1].trim());
					double y = Double.parseDouble(data[2].trim());
					locations[id][0] = x;
					locations[id][1] = y;
				}
				else if(state==ReadState.NODE_DEMANDS) {
					String[] data = line.split(" ");
					int id = Integer.parseInt(data[0].trim())-1;
					double demand = Double.parseDouble(data[1].trim());
					demands[id] = demand;
				}
			}
			
		}
		
		
		return new CVRPDescription(dim,cap,locations,demands,edgeWeightType);
	}
		
}
