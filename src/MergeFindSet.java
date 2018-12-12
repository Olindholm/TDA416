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
	
	
	
	public MergeFindSet(int size) {
		this.numberOfSets = size;
		this.set = new int[numberOfSets];
		
		for (int i = 0; i < set.length; i++) {
			set[i] = -1;
		}
	}
	
	
	
	public int find(int elementIndex) {
		if (set[elementIndex] < 0) return elementIndex;
		
		int s = find(set[elementIndex]);
		set[elementIndex] = s;
		
		return s;
	}
	
	
	
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
	
	
	
	public int getNumberOfSets() {
		return numberOfSets;
	}
	
	
	
}
