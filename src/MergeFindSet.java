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

public class MergeFindSet {
	
	
	
	int[] set;
	int numberOfSets;
	
	
	
	/**
	 * Creates <tt>size</tt> number of disjoint sets.
	 * 
	 * @param size	the number of disjoint sets to create.
	 */
	public MergeFindSet(int size) {
		this.numberOfSets = size;
		this.set = new int[numberOfSets];
		
		for (int i = 0; i < set.length; i++) {
			set[i] = -1;
		}
	}
	
	
	
	/**
	 * 
	 * Finds the root element of the set which <tt>elementIndex</tt>
	 * belongs to, and returns the index of that root element.
	 * 
	 * @param elementIndex	element to look for in the disjointed sets.
	 * @return				returns the index of the root element in the set.
	 */
	public int find(int elementIndex) {
		if (set[elementIndex] < 0) return elementIndex;
		
		int s = find(set[elementIndex]);
		set[elementIndex] = s;
		
		return s;
	}
	
	
	
	/**
	 * Merges two disjointed sets into one.
	 * 
	 * It is safe to join index elements
	 * which are not root element indices.
	 * 
	 * It is safe to merge the same set with itself.
	 * Though it will result in no change.
	 * 
	 * @param firstRoot		one of the root index to merge.
	 * @param secondRoot	the other of the root index to merge.
	 */
	public boolean merge(int firstRoot, int secondRoot) {
		firstRoot = find(firstRoot);
		secondRoot = find(secondRoot);
		
		if (firstRoot == secondRoot) return false;
		
		int firstRootSize = set[firstRoot];
		int secondRootSize = set[secondRoot];
		
		if (firstRootSize <= secondRootSize) {
			set[firstRoot] = firstRootSize + secondRootSize;
			set[secondRoot] = firstRoot;
		}
		else {
			set[firstRoot] = secondRoot;
			set[secondRoot] = firstRootSize + secondRootSize;
		}
		
		numberOfSets--;
		return true;
	}
	
	
	
	/**
	 * Returns the number of disjoint sets.
	 * 
	 * @return the number of disjoint sets.
	 */
	public int getNumberOfSets() {
		return numberOfSets;
	}
	
	
	
}
