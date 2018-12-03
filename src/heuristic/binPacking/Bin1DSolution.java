package heuristic.binPacking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import iterators.GenericIterator;
import optimization.solution.SingleObjectiveSolution;

public class Bin1DSolution extends SingleObjectiveSolution<Bin1DSolution> implements Iterable<Bin1D>{

	public final List<Bin1D> bins;
	public int activeBins = 0;

	public Bin1DSolution() {
		this(new ArrayList<>());
	}
	
	public Bin1DSolution(List<Bin1D> bins) {
		this.bins = bins;
		for(Bin1D bin:bins) {
			if(!bin.isEmpty()) {
				activeBins++;
			}
		}
	}
	
	public Bin1D get(int i) {
		return bins.get(i);
	}

	public Bin1D get(Random random) {
		return get(random.nextInt(bins.size()));
	}
	
	public Bin1D getNonEmpty(Random random) {
		while(true) {
			Bin1D route = get(random);
			if(!route.isEmpty())
				return route;
		}		
	}
	
	public void removeEmptyBins() {
		for(int i=0;i<bins.size();i++) {
			if(bins.get(i).isEmpty()) {
				bins.remove(i);
				i--;
			}
		}
	}

	public void add(Bin1D bin) {
		bins.add(bin);
		if(!bin.isEmpty())
			activeBins++;
	}
	
	public void remove(Bin1D bin) {
		bins.remove(bin);
		if(!bin.isEmpty())
			activeBins--;
	}
	
	//========================================================================================
	
	@Override
	public void copy(Bin1DSolution other) {
		
		int N = this.bins.size();
		for(int i=0,size=other.bins.size(); i<size;i++) {
			Bin1D bin1 = other.bins.get(i);
			if(i<N) {
				Bin1D bin2 = this.bins.get(i);
				bin2.copy(bin1);
			}
			else {
				this.bins.add(bin1.clone());
			}
		}
		this.activeBins = other.activeBins;
	}

	@Override
	public Bin1DSolution clone() {
		Bin1DSolution other = new Bin1DSolution();		
		for(Bin1D bin:bins) {
			other.add(bin.clone());
		}
		other.activeBins = this.activeBins;
		return other;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		int i=1;
		for (Bin1D bin : bins) {
			stringBuilder.append("Bin ").append(i).append(" : ").append(bin.toString()).append("\n");
			i++;
		}
		stringBuilder.setLength(stringBuilder.length()-1);
		
		return stringBuilder.toString();
	}

	//================================================================================================
	
	@Override
	public Iterator<Bin1D> iterator() {
		return new GenericIterator<>(bins.size(), bins::get);
	}

}