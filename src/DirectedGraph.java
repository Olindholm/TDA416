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

import java.lang.reflect.Array;
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
	
	
	
	public Iterator<E> shortestPath(int sourceNodeNo, int destNodeNo) {
		LinkedList<E>[] edgesByNode = getEdgesByOrigin();
		DijkstraNode[] ssfNodes = (DijkstraNode[]) Array.newInstance(DijkstraNode.class, this.nodeCount);
		
		PriorityQueue<DijkstraNode> nodeQueue = new PriorityQueue<>();
		
		/*
		 * Creates an initial "dummy" Dijkstra's node for the source node.
		 * Each node Dijkstra's node contains the edge which brought them
		 * to this node (nodeNo). However the initial one, does not have
		 * this edge, as it is the starting point. Thus a "dummy" is needed.
		 */
		DijkstraNode dummshiet = new DijkstraDummyNode(sourceNodeNo);
		ssfNodes[sourceNodeNo] = dummshiet;
		nodeQueue.add(dummshiet);
		
		while (!nodeQueue.isEmpty()) {
			DijkstraNode node = nodeQueue.poll();
			
			if (node.getNodeNo() == destNodeNo) {
				LinkedList<E> path = new LinkedList<>();
				
				while (node.getNodeNo() != sourceNodeNo) {
					path.push(node.getEdge());
					node = ssfNodes[node.getPreviousNodeNo()];
				}
				
				return path.iterator();
			}
			
			for (E e : edgesByNode[node.getNodeNo()]) {
				DijkstraNode nextNode = ssfNodes[e.getDest()];
				double newNextNodeWeight = node.getWeightToNode() + e.getWeight();
				
				if (nextNode == null || newNextNodeWeight < nextNode.getWeightToNode()) {
					DijkstraNode newNode = new DijkstraNode(e, newNextNodeWeight);
					
					ssfNodes[newNode.getNodeNo()] = newNode;
					nodeQueue.add(newNode);
				}
			}
		}
		
		return null;
	}
	
	private LinkedList<E>[] getEdgesByOrigin() {
		LinkedList<E>[] edgesByNode = new LinkedList[this.nodeCount];
		
		for (int i = 0; i < edgesByNode.length; i++) {
			edgesByNode[i] = new LinkedList<>();
		}
		
		for (E e : edges) {
			edgesByNode[e.from].add(e);
		}
		
		return edgesByNode;
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
	
	
	
	private class DijkstraDummyNode extends DijkstraNode {
		
		int nodeNo;
		
		public DijkstraDummyNode(int nodeNo) {
			super(null, 0);
			this.nodeNo = nodeNo;
		}
		
		@Override
		public int getNodeNo() {
			return this.nodeNo;
		}
		
	}


	public class DijkstraNode implements Comparable<DijkstraNode> {
		
		
		
		E edge;
		double weightToNode;
		
		
		
		public DijkstraNode(E edge, double weightToNode) {
			this.edge = edge;
			this.weightToNode = weightToNode;
		}
		
		
		
		public E getEdge() {
			return edge;
		}
		
		
		
		public double getWeightToNode() {
			return weightToNode;
		}
		
		
		
		public int getPreviousNodeNo() {
			return edge.from;
		}
		
		
		
		public int getNodeNo() {
			return edge.to;
		}
		
		
		
		@Override
		public int compareTo(DijkstraNode node) {
			return (int) Math.signum(this.getWeightToNode() - node.getWeightToNode());
		}
		
		
		
	}
}
