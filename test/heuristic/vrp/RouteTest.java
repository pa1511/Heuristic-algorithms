package heuristic.vrp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import heuristic.routing.ChainDemandRoute;
import heuristic.routing.ImplicitLoopDemandRoute;
import heuristic.routing.LoopDemandRoute;

public class RouteTest {

	
	private static final double DELTA = 1e-3;
	private static final double capacity = 1000;

	@Test
	public void chainRoutTestAddAll() {
		//Define
		int N = 10;
		double[][] distances = new double[N][N];
		for(int i=0; i<distances.length; i++) {
			for(int j=0; j<distances.length; j++) {
				distances[i][j]=Math.abs(i-j);
			}
		}
		double[] demands = new double[N];
		Arrays.fill(demands, 1);
		
		//Change
		ChainDemandRoute route1 = new ChainDemandRoute(distances,demands,capacity);
		ChainDemandRoute route2 = new ChainDemandRoute(distances,demands,capacity);
		
		route1.addAll(new int[] {1,2});
		route2.addAll(new int[] {3,4});

		route1.addAll(route2);
		
		assertArrayEquals(new int[] {1,2,3,4}, route1.toArray());
		assertEquals(3, route1.getLength(),1e-6);
		assertEquals(4, route1.getDemand(),DELTA);
		System.out.println("Route: " + route1.toString());
	}
	
	@Test
	public void chainRoutTest() {
		
		//Define
		int N = 10;
		double[][] distances = new double[N][N];
		for(int i=0; i<distances.length; i++) {
			for(int j=0; j<distances.length; j++) {
				distances[i][j]=Math.abs(i-j);
			}
		}
		double[] demands = new double[N];
		Arrays.fill(demands, 1);
		
		//Change
		ChainDemandRoute route = new ChainDemandRoute(distances,demands,capacity);
		route.add(1);
		route.add(2);
		route.add(3);
		
		//Check
		assertArrayEquals(new int[] {1,2,3}, route.toArray());
		assertEquals(2, route.getLength(),1e-6);
		assertEquals(3, route.getDemand(),DELTA);
		System.out.println("Route: " + route.toString());
		
		//Change
		route.removeAt(1);
		
		//Check
		assertEquals(2, route.getLength(),1e-6);
		assertEquals(2, route.getDemand(),DELTA);
		assertArrayEquals(new int[] {1,3}, route.toArray());
		System.out.println("Route: " + route.toString());
		
		//Change
		route.addAll(new int[] {4,5,6});
		
		//Check
		assertEquals(5, route.getLength(),1e-6);
		assertEquals(5, route.getDemand(),DELTA);
		assertArrayEquals(new int[] {1,3,4,5,6}, route.toArray());
		System.out.println("Route: " + route.toString());
	
		//Change
		ChainDemandRoute clone = route.clone();
		
		//Check
		assertEquals(route.size(), clone.size());
		assertEquals(route.getDemand(), clone.getDemand(),DELTA);
		assertEquals(route.getLength(), clone.getLength(),DELTA);
		assertArrayEquals(route.getArray(), clone.getArray());
		
		//Change
		clone.removeAt(0);
		
		//Check
		assertEquals(3, clone.getLength(),1e-6);
		assertEquals(4, clone.getDemand(),DELTA);
		assertArrayEquals(new int[] {3,4,5,6}, clone.toArray());
		System.out.println("Route: " + clone.toString());
		
		//Change
		route.copy(clone);
		
		//Check
		assertEquals(clone.size(), route.size());
		assertEquals(clone.getDemand(), route.getDemand(),DELTA);
		assertEquals(clone.getLength(), route.getLength(),DELTA);
		assertArrayEquals(clone.getArray(), route.getArray());
	}
	
	@Test
	public void loopRoutTestAddAll() {
		
		//Define
		int N = 10;
		double[][] distances = new double[N][N];
		for(int i=0; i<distances.length; i++) {
			for(int j=0; j<distances.length; j++) {
				distances[i][j]=Math.abs(i-j);
			}
		}
		double[] demands = new double[N];
		Arrays.fill(demands, 1);
		
		//Change
		LoopDemandRoute route1 = new LoopDemandRoute(distances,demands,capacity);
		LoopDemandRoute route2 = new LoopDemandRoute(distances,demands,capacity);
		
		route1.addAll(new int[] {1,2});
		route2.addAll(new int[] {3,4});

		route1.addAll(route2);
		
		assertArrayEquals(new int[] {1,2,3,4}, route1.toArray());
		assertEquals(6, route1.getLength(),1e-6);
		assertEquals(4, route1.getDemand(),DELTA);
		System.out.println("Route: " + route1.toString());
	}

	@Test
	public void loopRoutTest() {
		
		//Define
		int N = 10;
		double[][] distances = new double[N][N];
		for(int i=0; i<distances.length; i++) {
			for(int j=0; j<distances.length; j++) {
				distances[i][j]=Math.abs(i-j);
			}
		}
		double[] demands = new double[N];
		Arrays.fill(demands, 1);
		
		//Change
		LoopDemandRoute route = new LoopDemandRoute(distances,demands,capacity);
		route.add(1);
		route.add(2);
		route.add(3);
		
		//Check
		assertArrayEquals(new int[] {1,2,3}, route.toArray());
		assertEquals(4, route.getLength(),1e-6);
		assertEquals(3, route.getDemand(),DELTA);
		System.out.println("Route: " + route.toString());
		
		//Change
		route.removeAt(1);
		
		//Check
		assertEquals(4, route.getLength(),1e-6);
		assertEquals(2, route.getDemand(),DELTA);
		assertArrayEquals(new int[] {1,3}, route.toArray());
		System.out.println("Route: " + route.toString());
		
		//Change
		route.addAll(new int[] {4,5,6});
		
		//Check
		assertEquals(10, route.getLength(),1e-6);
		assertEquals(5, route.getDemand(),DELTA);
		assertArrayEquals(new int[] {1,3,4,5,6}, route.toArray());
		System.out.println("Route: " + route.toString());
	
		//Change
		LoopDemandRoute clone = route.clone();
		
		//Check
		assertEquals(route.size(), clone.size());
		assertEquals(route.getDemand(), clone.getDemand(),DELTA);
		assertEquals(route.getLength(), clone.getLength(),DELTA);
		assertArrayEquals(route.getArray(), clone.getArray());
		
		//Change
		clone.removeAt(0);
		
		//Check
		assertEquals(6, clone.getLength(),1e-6);
		assertEquals(4, clone.getDemand(),DELTA);
		assertArrayEquals(new int[] {3,4,5,6}, clone.toArray());
		System.out.println("Route: " + clone.toString());
		
		//Change
		route.copy(clone);
		
		//Check
		assertEquals(clone.size(), route.size());
		assertEquals(clone.getDemand(), route.getDemand(),DELTA);
		assertEquals(clone.getLength(), route.getLength(),DELTA);
		assertArrayEquals(clone.getArray(), route.getArray());
	}
	
	@Test
	public void implicitLoopRoutTestAddAll() {
		
		//Define
		int N = 10;
		double[][] distances = new double[N][N];
		for(int i=0; i<distances.length; i++) {
			for(int j=0; j<distances.length; j++) {
				distances[i][j]=Math.abs(i-j);
			}
		}
		double[] demands = new double[N];
		Arrays.fill(demands, 1);
		
		//Change
		ImplicitLoopDemandRoute route1 = new ImplicitLoopDemandRoute(distances,demands,capacity);
		ImplicitLoopDemandRoute route2 = new ImplicitLoopDemandRoute(distances,demands,capacity);
		
		route1.addAll(new int[] {1,2});
		route2.addAll(new int[] {3,4});

		route1.addAll(route2);
		
		assertArrayEquals(new int[] {1,2,3,4}, route1.toArray());
		assertEquals(8, route1.getLength(),1e-6);
		assertEquals(4, route1.getDemand(),DELTA);
		System.out.println("Route: " + route1.toString());
	}

	@Test
	public void implicitLoopRoutTest() {
		
		//Define
		int N = 10;
		double[][] distances = new double[N][N];
		for(int i=0; i<distances.length; i++) {
			for(int j=0; j<distances.length; j++) {
				distances[i][j]=Math.abs(i-j);
			}
		}
		double[] demands = new double[N];
		Arrays.fill(demands, 1);
		
		//Change
		ImplicitLoopDemandRoute route = new ImplicitLoopDemandRoute(distances,demands,capacity);
		route.add(1);
		route.add(2);
		route.add(3);
		
		//Check
		assertArrayEquals(new int[] {1,2,3}, route.toArray());
		assertEquals(6, route.getLength(),1e-6);
		assertEquals(3, route.getDemand(),DELTA);
		System.out.println("Route: " + route.toString());
		
		//Change
		route.removeAt(1);
		
		//Check
		assertEquals(6, route.getLength(),1e-6);
		assertEquals(2, route.getDemand(),DELTA);
		assertArrayEquals(new int[] {1,3}, route.toArray());
		System.out.println("Route: " + route.toString());
		
		//Change
		route.addAll(new int[] {4,5,6});
		
		//Check
		assertEquals(12, route.getLength(),1e-6);
		assertEquals(5, route.getDemand(),DELTA);
		assertArrayEquals(new int[] {1,3,4,5,6}, route.toArray());
		System.out.println("Route: " + route.toString());
	
		//Change
		ImplicitLoopDemandRoute clone = route.clone();
		
		//Check
		assertEquals(route.size(), clone.size());
		assertEquals(route.getDemand(), clone.getDemand(),DELTA);
		assertEquals(route.getLength(), clone.getLength(),DELTA);
		assertArrayEquals(route.getArray(), clone.getArray());
		
		//Change
		clone.removeAt(0);
		
		//Check
		assertEquals(12, clone.getLength(),1e-6);
		assertEquals(4, clone.getDemand(),DELTA);
		assertArrayEquals(new int[] {3,4,5,6}, clone.toArray());
		System.out.println("Route: " + clone.toString());
		
		//Change
		route.copy(clone);
		
		//Check
		assertEquals(clone.size(), route.size());
		assertEquals(clone.getDemand(), route.getDemand(),DELTA);
		assertEquals(clone.getLength(), route.getLength(),DELTA);
		assertArrayEquals(clone.getArray(), route.getArray());
	}

}
