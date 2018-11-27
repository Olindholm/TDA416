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
public class SLCWithGet<E extends Comparable<? super E>> extends LinkedCollection<E> implements CollectionWithGet<E> {
	
	
	
	@Override
	public boolean add(E element) {
		if (element == null) throw new NullPointerException();
		
		if (head == null || element.compareTo(head.element) <= 0) return super.add(element);
		
		Entry e = findPrecEntry(head, element);
		e.next = new Entry(element, e.next);
		return true;
	}
	
	
	
	@Override
	public E get(E element) {
		if (element == null) throw new NullPointerException();
		
		if (head != null) {
			
			Entry e = (element.compareTo(head.element) <= 0) ? head : findPrecEntry(head, element).next;
			if (e != null && element.compareTo(e.element) == 0) return e.element;
			
		}
		
		return null;
	}
	
	
	
	private Entry findPrecEntry(Entry e, E element) {
		while (e.next != null && element.compareTo(e.next.element) > 0) e = e.next;
		return e;
	}
	
	
	
}
