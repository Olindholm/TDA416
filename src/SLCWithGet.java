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
	
	
	
	/**
	 * Adds an element to the collection.
	 * The element added will be added in sorted order,
	 * with respect to it's natural order. I.e. 
	 * element.compateTo(element) is 0.
	 * 
	 * If two elements have equal natural order, this element
	 * will be sorted before the other element.
	 * 
	 * @param element	the object to add into the list.
	 * @return			true if the object has been added to the list.
	 * @throws			NullPointerException if parameter <tt>element</tt> is null. 
	 */
	@Override
	public boolean add(E element) {
		if (element == null) throw new NullPointerException();
		
		/*
		 * Handle the first special case,
		 * if element is less than the head's element.
		 * If so, add the element first.
		 */
		if (head == null || element.compareTo(head.element) <= 0) return super.add(element);
		
		/*
		 * Once taken care of the only case that
		 * findPrecEntry does not take take into account.
		 * Let it do it's job, and add the element after
		 * the preceding entry.
		 */
		Entry e = findPrecEntry(head, element);
		e.next = new Entry(element, e.next);
		return true;
	}
	
	
	
	/**
	 * Finds the first occurrence of an element
	 * in the collection that is equal to the argument
	 * <tt>e</tt> with respect to its natural order.
	 * I.e. <tt>e.compateTo(element)</tt> is 0.
	 * 
	 * @param element	the object to add into the list.
	 * @return			true if the object has been added to the list.
	 * @throws			NullPointerException if parameter <tt>element</tt> is null. 
	 */
	@Override
	public E get(E element) {
		if (element == null) throw new NullPointerException();
		
		if (head != null) {
			
			/*
			 * Find the last entry e which is less, or equal to
			 * the element which we're looking for.
			 * 
			 * First handle the special case if the element is
			 * less than the element of the head entry.
			 * Then head is the entry e.
			 * Else, let findPrecEntry find the entry.
			 * Let e be the entry followed (next) of the preceding entry.
			 * 
			 * Once found an entry, given it is not null,
			 * make sure it's equal to the element looked for.
			 * If so, return it, otherwise return null (later).
			 * 
			 */
			Entry e = (element.compareTo(head.element) <= 0) ? head : findPrecEntry(head, element).next;
			if (e != null && element.compareTo(e.element) == 0) return e.element;
			
		}
		
		return null;
	}
	
	
	
	/**
	 * Compares an element with the elements
	 * with respect to it's natural ordering in
	 * the linked list to find the preceding entry.
	 * 
	 * This means it does NOT compare elements
	 * with the initial entry <tt>e</tt>.
	 * 
	 * @param e			the initial entry to compare with.
	 * @param element	the element to compare
	 * @return			returns the preceding entry to the elements natural order.
	 */
	private Entry findPrecEntry(Entry e, E element) {
		/*
		 * While the next entry of the current entry is not null and,
		 * the element of the next entry is larger than the element
		 * we're comparing with, step to the next entry.
		 */
		while (e.next != null && element.compareTo(e.next.element) > 0) e = e.next;
		return e;
	}
	
	
	
}
