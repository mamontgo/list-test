package com.monty.list;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SimpleListSort {

	/**
	 * Sorts list and returns new sorted list
	 * 
	 * @param l
	 * @param c
	 * @return
	 */
	public static <T> LinkedList<T> sort(List<T> l, Comparator<? super T> c) {
		return sortPart(l, c, l.size());
	}

	/**
	 * Will sort a first n number of elements in a list
	 * 
	 * 
	 * @param l
	 *            unsorted list
	 * @param c
	 *            comparator
	 * @param iterations
	 *            the number of elements to be sorted
	 * @return n elements in sorted list
	 */
	public static <T> LinkedList<T> sortPart(List<T> l, Comparator<? super T> c,
			int iterations) {

		LinkedList<T> items = new LinkedList<T>();

		boolean added = false;

		if (l.size() > 0) {
			items.add(l.get(0));
			for (int i = 1; i <= (l.size() < iterations ? l.size() : iterations) - 1; i++) {

				added = false;

				for (int x = 0; x <= items.size() - 1; x++) {
					if (c.compare(items.get(x), l.get(i)) >= 0) {
						items.add(x, l.get(i));
						added = true;
						break;
					}
				}

				if (!added) {
					items.addLast(l.get(i));
				}

			}
		}

		return items;

	}
	
	
	
	
}
