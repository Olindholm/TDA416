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

import static java.lang.Integer.signum;

public class SplayWithGet<E extends Comparable<? super E>> extends BinarySearchTree<E> implements CollectionWithGet<E> {
	
	
	
	E foundElement;
	
	
	
	/**
	 * Searches the tree for <tt>element</tt> and returns it.
	 * When successful the <tt>element</tt> is splayed to the top (root) of the tree.
	 * 
	 * If the <tt>element</tt> is not found, null will be returned and
	 * the parent node of where the search terminated will be splayed
	 * to the top (root).
	 * 
	 * @param element	the <tt>element</tt> to search for in the tree.
	 * @return			the <tt>element</tt> found or null if it couldn't be found.
	 * @throws			NullPointerException if parameter <tt>element</tt> is null.
	 */
	@Override
	public E get(E element) {
		if (element == null) throw new NullPointerException();
		foundElement = null;
		
		if (root != null) splayFind(root, element, 0);
		
		return foundElement;
	}
	
	
	
	/**
	 * A recursive method to find, and splay the <tt>element</tt>.
	 * If the element is found, it is stored in <tt>foundElement</tt>.
	 * 
	 * When calling this method initially, it's recommended to let
	 * <tt>e</tt> be <tt>root</tt>, so the whole tree will be searched.
	 * 
	 * Once the <tt>element</tt> is found, or the search is terminated
	 * by a dead end (null). The element node, or parent node of the
	 * where the search is terminated will be splayed to the initial
	 * Entry <tt>e</tt>. E.g. when the initial Entry <tt>e</tt> is
	 * <tt>root</tt> the node will the splayed to the top of the tree (root).
	 * 
	 * @param e			the entry to which to look through.
	 * @param element	the element to look for.
	 * @param depth		the depth of the entry <tt>e</tt> in the tree.
	 * 					The initial entry should have depth of 0.
	 * @return			returns a token determining how the node should be splayed.
	 * 					This is only interesting for the recursive method itself.
	 * 					If this method is used correctly, the last token to be
	 * 					returned should be 0.
	 * 					
	 * 					For more information about this token, see <tt>splay</tt> method.
	 */
	private int splayFind(Entry e, E element, int depth) {
		/*
		 * Token is a multi-purposed variable.
		 * 
		 * Initially, it's used as a comparator to determined
		 * whether or not recursive search should continue.
		 * 
		 * Later it's also used to determine how to splay
		 * the current node.
		 */
		Entry next = null;
		int token = element.compareTo(e.element);
		
		if (token == 0) {
			foundElement = e.element;
			return 0;
		}
		
		/*
		 * If the element was not found, let next
		 * be the left or right node to search.
		 * 
		 * However if the next node is null,
		 * search should be terminated and
		 * this node/element should be splayed
		 * to the top, thus return immediately.
		 */
		next = (token < 0) ? e.left : e.right;
		if (next == null) return 0;
		
		/*
		 * Calculate the splay token (splay operation),
		 * using the recursion of next entry's token splay token,
		 * and the current nodes token.
		 * 
		 * Splay every 2nd entry (when the depth is divisible by 2).
		 * Otherwise, just return token.
		 */
		token = 3 * splayFind(next, element, depth+1) + signum(token);
		return (depth % 2 == 0) ? splay(e, token) : token;
	}
	
	
	
	/**
	 * Replaces entry node e, with another node
	 * further down in the tree given by the token index.
	 * 
	 * This method assumes the splay operation is possible.
	 * I.e. the nodes being splayed and moved around is not null.
	 * 
	 * The splay <tt>token</tt> can take 6 different values,
	 * representing the 6 different splay operations:
	 * token = -4 => zagzag
	 * token = -2 => zigzag
	 * token = -1 => zag
	 * token =  1 => zig
	 * token =  2 => zagzig
	 * token =  4 => zigzig
	 * 
	 * @param e		the parent entry of the node being splayed.
	 * 				One or two steps above, depending on token index.
	 * @param token	the splay operation to perform.
	 * @return		returns a new token, assuming the splay is successful,
	 * 				which always is assumed,  the
	 * 				<b>return token will always be 0</b>.
	 */
	private int splay(Entry e, int token) {
		
		if (token > 0) {
			
			if (token > 2) zigzig(e);		// 4
			else if (token < 2) zig(e);		// 1
			else zagzig(e);					// 2
			
		}
		else {
			
			if (token < -2) zagzag(e);		// -4
			else if (token > -2) zag(e);	// -1
			else zigzag(e);					// -2
			
		}
		
		return 0;
	}
	
	
	
	/**
	 * Performs a zagzag operation to splay z
	 * up to the top (x node), see figure below.
	 * 
	 * <pre>
	 *       x              z
	 *      / \            / \
	 *     y   D    ->    A   y
	 *    / \       ->       / \
	 *   z   C      ->      B   x
	 *  / \                    / \
	 * A   B                  C   D
	 * </pre>
	 * 
	 * @param x the 2 times parent node of the element to be splayed (z).
	 */
	private void zagzag(Entry x) {
		Entry 	y = x.left,
				z = y.left,
				A = z.left,
				B = z.right,
				C = y.right,
				D = x.right;
		
		/*
		 * Switches the position of x and z node
		 * by replacing their respective content (element).
		 */
		E e = x.element;
		x.element = z.element;
		z.element = e;
		
		// Rename the newly switched x and z.
		z = x;
		x = y.left;
		
		// Puzzle the tree back together according to the schematic.
		
		z.left = A;		if (A != null)	A.parent = z;
		z.right = y;					y.parent = z;
		
		y.left = B;		if (B != null)	B.parent = y;
		y.right = x;					x.parent = y;
		
		x.left = C;		if (C != null)	C.parent = x;
		x.right = D;	if (D != null)	D.parent = x;
	}
	
	
	
	/**
	 * Performs a zigzig operation to splay z
	 * up to the top (x node), see figure below.
	 * 
	 * <pre>
	 *   x                      z
	 *  / \                    / \
	 * A   y        ->        y   D
	 *    / \       ->       / \
	 *   B   z      ->      x   C
	 *      / \            / \
	 *     C   D          A   B
	 * </pre>
	 * 
	 * @param x the 2 times parent node of the element to be splayed (z).
	 */
	private void zigzig(Entry x) {
		Entry 	y = x.right,
				z = y.right,
				A = x.left,
				B = y.left,
				C = z.left,
				D = z.right;
		
		/*
		 * Switches the position of x and z node
		 * by replacing their respective content (element).
		 */
		E e = x.element;
		x.element = z.element;
		z.element = e;
		
		// Rename the newly switched x and z.
		z = x;
		x = y.right;
		
		// Puzzle the tree back together according to the schematic.
		
		z.left = y;						y.parent = z;
		z.right = D;	if (D != null)	D.parent = z;
		
		y.left = x;						x.parent = y;
		y.right = C;	if (C != null)	C.parent = y;
		
		x.left = A;		if (A != null)	A.parent = x;
		x.right = B;	if (B != null)	B.parent = x;
	}
	
	
	
	/* Rotera 1 steg i hogervarv, dvs 
		    x'                 y'
		   / \                / \
		  y'  C   -->        A   x'
		 / \                    / \  
		A   B                  B   C
	*/
	private void zag( Entry x ) {
		Entry   y = x.left;
		E    temp = x.element;
		x.element = y.element;
		y.element = temp;
		x.left    = y.left;
		if ( x.left != null )
			 x.left.parent   = x;
		y.left    = y.right;
		y.right   = x.right;
		if ( y.right != null )
			 y.right.parent  = y;
		x.right   = y;
	}
	
	
	
	//   rotateRight
	// ========== ========== ========== ==========
	
	/* Rotera 1 steg i vanstervarv, dvs 
	    x'                 y'
	   / \                / \
	  A   y'  -->        x'  C
	     / \            / \  
	    B   C          A   B   
	*/
	private void zig( Entry x ) {
		Entry  y  = x.right;
		E temp    = x.element;
		x.element = y.element;
		y.element = temp;
		x.right   = y.right;
		if ( x.right != null )
			 x.right.parent  = x;
		y.right   = y.left;
		y.left    = x.left;
		if ( y.left != null )
			 y.left.parent   = y;
		x.left    = y;
	}
	
	
	
	//   rotateLeft
	// ========== ========== ========== ==========
	
	/* Rotera 2 steg i hogervarv, dvs 
	    x'                  z'
	   / \                /   \
	  y'  D   -->        y'    x'
	 / \                / \   / \
	A   z'             A   B C   D
	   / \  
	  B   C  
	*/
	private void zagzig( Entry x ) {
		Entry   y = x.left,
		z = x.left.right;
		E       e = x.element;
		x.element = z.element;
		z.element = e;
		y.right   = z.left;
		if ( y.right != null )
			y.right.parent = y;
		z.left    = z.right;
		z.right   = x.right;
		if ( z.right != null )
			z.right.parent = z;
		x.right   = z;
		z.parent  = x;
	}
	
	
	
	//  doubleRotateRight
	// ========== ========== ========== ==========
	
	/* Rotera 2 steg i vanstervarv, dvs 
	    x'                  z'
	   / \                /   \
	  A   y'   -->       x'    y'
	     / \            / \   / \
	    z   D          A   B C   D
	   / \  
	  B   C  
	*/
	private void zigzag( Entry x ) {
		Entry  y  = x.right,
		z  = x.right.left;
		E      e  = x.element;
		x.element = z.element;
		z.element = e;
		y.left    = z.right;
		if ( y.left != null )
			y.left.parent = y;
		z.right   = z.left;
		z.left    = x.left;
		if ( z.left != null )
			z.left.parent = z;
		x.left    = z;
		z.parent  = x;
	}
	
	
	
}
