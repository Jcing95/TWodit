package de.jcing.util;

import java.util.LinkedList;

public class ListArray<A> {
	
	private LinkedList<A[]> elements;
	
	public ListArray() {
		elements = new LinkedList<>();
	}
	
	@SuppressWarnings("unchecked")
	public ListArray(A[]...elements) {
		this();
		for(A[] a : elements) {
			this.elements.add(a);
		}
	}
	
	public A get(int index) {
		int reminding = index;
		for(A[] a : elements) {
			if(a.length > reminding) {
				return a[reminding];
			} else {
				reminding -= a.length;
			}
		}
		throw new IndexOutOfBoundsException();
	}
	
	@SuppressWarnings("unchecked")
	public void add(A...elements) {
		this.elements.add(elements);
	}
	
	public int size() {
		int size = 0;
		for(A[] a : elements)
			size += a.length;
		return size;
	}
}
