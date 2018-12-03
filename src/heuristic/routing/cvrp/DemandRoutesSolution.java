package heuristic.routing.cvrp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import heuristic.routing.DemandRoute;
import heuristic.routing.Route;
import heuristic.routing.RoutingUtility;
import iterators.GenericIterator;
import optimization.solution.SingleObjectiveSolution;

public class DemandRoutesSolution extends SingleObjectiveSolution<DemandRoutesSolution> implements Iterable<DemandRoute>{

	public final List<DemandRoute> routes;
	public double length = 0;

	public DemandRoutesSolution() {
		this(new ArrayList<>());
	}
	
	public DemandRoutesSolution(List<DemandRoute> routes) {
		this.routes = routes;
		this.length = RoutingUtility.getTotalLengthOf(routes);
	}
	
	public DemandRoute get(int i) {
		return routes.get(i);
	}

	public DemandRoute get(Random random) {
		return get(random.nextInt(routes.size()));
	}
	
	public DemandRoute getNonEmpty(Random random) {
		while(true) {
			DemandRoute route = get(random);
			if(!route.isEmpty())
				return route;
		}		
	}
	
	public void removeEmptyRoutes() {
		for(int i=0;i<routes.size();i++) {
			if(routes.get(i).isEmpty()) {
				routes.remove(i);
				i--;
			}
		}
	}

	public void add(DemandRoute route) {
		routes.add(route);
		length+=route.getLength();
	}
	
	public void remove(DemandRoute route) {
		if(routes.remove(route))
			length+=route.getLength();
	}

	public double getLength() {
		return length;
	}
	
	public void setLength(double length) {
		this.length = length;
	}
	
	//========================================================================================
	
	@SuppressWarnings("unchecked")
	@Override
	public void copy(DemandRoutesSolution other) {
		int thisRouteCount=this.routes.size();
		int otherRouteCount=other.routes.size();
		
		if(otherRouteCount>=thisRouteCount) {
			for(int i=0; i<thisRouteCount;i++) {
				this.routes.get(i).copy(other.routes.get(i));
			}
			for(int i=thisRouteCount; i<otherRouteCount;i++) {
				this.routes.add(other.routes.get(i).clone());
			}
		}
		else {
			for(int i=0; i<otherRouteCount;i++) {
				this.routes.get(i).copy(other.routes.get(i));
			}
			for(int i=otherRouteCount; i<thisRouteCount; i++) {
				this.routes.remove(this.routes.size());
			}
		}
		this.length = other.length;
	}

	@Override
	public DemandRoutesSolution clone() {
		DemandRoutesSolution other = new DemandRoutesSolution();		
		for(DemandRoute route:routes) {
			other.add(route.clone());
		}
		return other;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		int i=1;
		for (Route route : routes) {
			stringBuilder.append("Route ").append(i).append(" : ").append(Arrays.toString(route.toArray())).append("\n");
			i++;
		}
		stringBuilder.setLength(stringBuilder.length()-1);
		
		return stringBuilder.toString();
	}

	//================================================================================================
	
	@Override
	public Iterator<DemandRoute> iterator() {
		return new GenericIterator<>(routes.size(), routes::get);
	}

}