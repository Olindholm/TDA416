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

@SuppressWarnings("unchecked")
public class DirectedGraph<E extends Edge> {

	
	
	int nodeCount;
	List<E> edges = new ArrayList<>();

	
	
	/**
	 * Creates a directed Graph with <tt>noOfNodes</tt>
	 * number of nodes. Each node is represented by a
	 * unique node number (<tt>nodeNo</tt>).
	 * 
	 * @param noOfNodes the number of nodes the graph should have.
	 */
	public DirectedGraph(int noOfNodes) {
		this.nodeCount = noOfNodes;
	}
	
	
	
	/**
	 * Adds an edge to the graph connecting two nodes together.
	 * 
	 * This method (and thus graph) does not validate 
	 * the edges to any deep extent. It is thus possible
	 * to add an edge which has the same source and destination.
	 * Be aware, such edges could cause the graph to malfunction.
	 * 
	 * @param e	the edge to add to the graph.
	 * @throws	NullPointerException if the edge <tt>e</tt> is null.
	 */
	public void addEdge(E e) {
		if (e == null) throw new NullPointerException();
		edges.add(e);
	}
	
	
	
	/**
	 * Finds the shortest path from <tt>sourceNodeNo</tt>
	 * to the <tt>destNodeNo</tt> using Dijkstra's algorithm.
	 * 
	 * Be aware, the Dijkstra's algorithm is a greedy algorithm
	 * and does not always find the best/shortest path.
	 * 
	 * 
	 * @param sourceNodeNo	the node number for the source node.
	 * @param destNodeNo	the node number for the destination node.
	 * @return				returns an iterator over the shortest path
	 * 						from the source node to the destination node.
	 * 						If the source and destination node is the same,
	 * 						the iterator will be empty. If a path does not
	 * 						exist this will return null.
	 */
	public Iterator<E> shortestPath(int sourceNodeNo, int destNodeNo) {
		List<E>[] edgesByNode = getEdgesByOrigin();
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
			
			/*
			 * The node we extract will lock it's shortest/cheapest/lightest path.
			 * Thus if the node we've extracted is the destination node.
			 * We've found the shortest path (accomplishable by the algorithm).
			 * 
			 * Then we can "backtrack" to the source node, and collect/sum the
			 * path (edges) and return it as an iterator.
			 */
			if (node.getNodeNo() == destNodeNo) {
				LinkedList<E> path = new LinkedList<>();
				
				while (node.getNodeNo() != sourceNodeNo) {
					path.push(node.getEdge());
					node = ssfNodes[node.getPreviousNodeNo()];
				}
				
				return path.iterator();
			}
			
			
			/*
			 * Otherwise...
			 * For all edges on the extracted node.
			 * Check if the path to the connected nodes are
			 * shorter/cheaper/lighter than the already known
			 * shortest/cheapest/lightest path to that node.
			 * 
			 * If so, save the new and better path.
			 */
			for (E e : edgesByNode[node.getNodeNo()]) {
				DijkstraNode nextNode = ssfNodes[e.getDest()];
				double newNextNodeWeight = node.getWeightToNode() + e.getWeight();
				
				// Null value means no connection yet, equivalent to infinite weight.
				if (nextNode == null || newNextNodeWeight < nextNode.getWeightToNode()) {
					DijkstraNode newNode = new DijkstraNode(e, newNextNodeWeight);
					
					ssfNodes[newNode.getNodeNo()] = newNode;
					nodeQueue.add(newNode);
				}
			}
		}
		
		/*
		 * If no path was found, no path exists.
		 * Thus return null.
		 */
		return null;
	}
	
	
	
	/**
	 * Returns an array of all edges grouped by connected node.
	 * Uses the node numbers (<tt>nodeNo</tt>) as indices for
	 * the connected edges to that node.
	 * 
	 * @return returns an array of Lists containing all edges.
	 */
	private List<E>[] getEdgesByOrigin() {
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
