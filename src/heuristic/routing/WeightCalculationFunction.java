package heuristic.routing;

@FunctionalInterface
public interface WeightCalculationFunction{
	public double calculate(double x1, double y1, double x2, double y2);
}