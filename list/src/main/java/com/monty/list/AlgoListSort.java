package com.monty.list;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class AlgoListSort {
	/**
	 * Sorts list and returns new sorted list
	 * 
	 * @param l
	 * @param c
	 * @return
	 */
	public static <T> List<T> sort(List<T> l, Comparator<? super T> c) {

		// not benefit in alg list sorting for small lists
		if (l.size() <= 10) {
			return SimpleListSort.sort(l, c);
		}

		// sorted first 10 items and them resort to alg sorting
		LinkedList<T> items = SimpleListSort.sortPart(l, c, 10);

		for (int i = 10; i < l.size(); i++) {

			int start = 0;
			int end = items.size() - 1;
			final T currentCheckItem = l.get(i);
			
			if (c.compare(currentCheckItem, items.getLast()) >= 0) {
				items.addLast(currentCheckItem);
			} else if (c.compare(currentCheckItem, items.getFirst()) <= 0) {
				items.addFirst(currentCheckItem);
			} else {

				while (true) {

					final int checkpos = start + Math.floorDiv(end - start, 2);
					final T currentListItem = items.get(checkpos);

					final int compareState = c.compare(currentCheckItem,
							currentListItem);

					if (compareState > 0) {

						if (checkpos < end
								&& c.compare(currentCheckItem,
										items.get(checkpos + 1)) <= 0) {
							items.add(checkpos+1, currentCheckItem);
							break;
						}
						start = checkpos;

					} else if (compareState < 0) {

						if (checkpos > start
								&& c.compare(currentCheckItem,
										items.get(checkpos - 1)) >= 0) {
							items.add(checkpos, currentCheckItem);
							break;
						}
						end = checkpos;

					} else {
						items.add(checkpos, currentCheckItem);
						break;
					}
				}
			}
		}

		return items;
	}

}
