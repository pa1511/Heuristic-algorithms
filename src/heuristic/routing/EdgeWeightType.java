package heuristic.routing;

public enum EdgeWeightType {
	EUC_2D((x1,y1,x2,y2)->Math.round(Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2))));
	
	private final WeightCalculationFunction f;

	private EdgeWeightType(WeightCalculationFunction f) {
		this.f = f;
	}
	
	public WeightCalculationFunction getCalculationFunction() {
		return f;
	}

	public static EdgeWeightType getWeightType(String line) {
		if(line.matches("EUC_2D"))
			return EdgeWeightType.EUC_2D;
		
		throw new IllegalArgumentException("Unknown weight type requested");
	}
}
