package com.monty.list;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedListSort {

	private static final int THREADS = 10;
	
	private static final int THRESHOLD = 200;

	public static <T> List<T> sort(List<T> l, Comparator<? super T> c) {
		return ThreadedListSort.sort(l, c, THREADS, THRESHOLD);
	}
	
	public static <T> List<T> sort(List<T> l, Comparator<? super T> c,
			int threads, int threshold) {
		if (l.size() <= 10) {
			return SimpleListSort.sort(l, c);
		} else if (l.size() <= 200) {
			return AlgoListSort.sort(l, c);
		}

		// enough elements to make it worth splitting into thread ops
		// create a
		final ExecutorService executorService = Executors
				.newFixedThreadPool(THREADS);

		ThreadedListSorter<T> sorter = new ThreadedListSorter<T>(executorService, c, threshold);
		return sorter.sort(l);
	}

}
