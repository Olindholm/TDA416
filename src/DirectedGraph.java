/**
 * 
 * Copyright 2018 (C) Osvald Lindholm
 * 
 * This file is the result of the laborations provided
 * for the Datastrukturer och Algoritmer (TDA416)
 * course at Chalmers. The laborations can be found at:
 * http://www.cse.chalmers.se/edu/course/tda416/
 *
 */

import java.util.*;

public class DirectedGraph<E extends Edge> {

	
	
	int nodeCount;
	List<E> edges = new ArrayList<>();

	
	
	public DirectedGraph(int noOfNodes) {
		this.nodeCount = noOfNodes;
	}
	
	
	
	public void addEdge(E e) {
		if (e == null) throw new NullPointerException();
		edges.add(e);
	}
	
	
	
	public Iterator<E> shortestPath(int from, int to) {
		return null;
	}
	
	
	
	/**
	 * Spans a minimum spanning tree (MST)
	 * using Kruskal's algorithm.
	 * 
	 * @return returns an iterator over the MST.
	 */
	public Iterator<E> minimumSpanningTree() {
		Set<E> mst = new HashSet<>();
		MergeFindSet cc = new MergeFindSet(nodeCount);
		PriorityQueue<E> eque = new PriorityQueue<>(edges);
		
		while (cc.getNumberOfSets() > 1 && !eque.isEmpty()) {
			E e = eque.poll();
			
			if ( cc.merge(e.from, e.to) ) mst.add(e);
		}
		
		return mst.iterator();
	}
	
	

}
