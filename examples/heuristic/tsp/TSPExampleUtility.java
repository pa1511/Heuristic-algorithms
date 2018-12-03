package heuristic.tsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TSPExampleUtility {

	public static double[][] calculateDistance(double[][] coordinates) {
		
		int N = coordinates.length;
		double[][] distances = new double[N][N];
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				double x1 = coordinates[i][0];
				double y1 = coordinates[i][1];
				double x2 = coordinates[j][0];
				double y2 = coordinates[j][1];
				distances[i][j] = Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
			}
		}
		
		return distances;
	}

	public static double[][] loadCoordinates(String fileName) throws IOException {
		List<String> allLines = Files.readAllLines(Paths.get("data/tsp/"+fileName));
		int count = allLines.size();
		double[][] coord = new double[count][2];
		
		for(int i=0; i<count; i++) {
			String line = allLines.get(i);
			String[] elements = line.split(" +");
			//System.out.println(Arrays.toString(elements));
			coord[i][0] = Double.parseDouble(elements[1]);
			coord[i][1] = Double.parseDouble(elements[2]);
			
		}
		
		return coord;
	}


	public static double[][] loadDistanceMatrix(String fileName) throws IOException {
		List<String> allLines = Files.readAllLines(Paths.get("data/tsp/"+fileName));
		int count = allLines.size();
		double[][] distances = new double[count][count];
		for(int i=0; i<count; i++) {
			String line = allLines.get(i);
			String[] elements = line.split(" +");
			//System.out.println(Arrays.toString(elements));
			for(int j=0; j<count; j++) {
				distances[i][j] = Double.parseDouble(elements[j]);
			}
		}
		return distances;
	}

}
