package com.monty.list;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * 
 * Splits a list sort into parts and processes with multiple threads
 * 
 * Two types of thread jobs exist
 * 1) CompareSplit
 * Takes a list of unsorted items and nominates an element to be compared.  The first element in the list is always nominated as it is an unsorted list
 * and it is as likely as any element to weighted with less than and greater than elements than any other element.
 * This sorts the list into 3 items of equals, less than and greater than.
 * The items are inserted into a repository list collecting the split parts with the greater than and less than placed in an unsorted state
 * while the equals part is added as a sorted state.
 * Each time a split completes the repository list is iterated with the unsorted elements either split again depending on the threshold set or
 * sorted.
 *  
 * 2) Sort
 * Takes a list of elements and sorts them based on the comparator provided.
 * 
 * 
 * @author Mark Montgomery
 *
 * @param <T>
 */
class ThreadedListSorter<T> {

	
	private int sortingThreshold;

	private ExecutorService executorService;
	private Comparator<? super T> comparator;
	private LinkedList<SortedList<T>> results;

	public ThreadedListSorter(ExecutorService executorService,
			Comparator<? super T> comparator, int threshold) {
		super();
		this.executorService = executorService;
		this.results = new LinkedList<SortedList<T>>();
		this.comparator = comparator;
		this.sortingThreshold = threshold;
	}

	public int getSortingThreshold() {
		return sortingThreshold;
	}

	/**
	 * Root public sort function
	 * 
	 * @return sorted list
	 */
	public List<T> sort(List<T> unsorted) {
		SortedList<T> rootlist = new SortedList<T>(SortedStatus.BEING_SPLIT);
		this.results.add(rootlist);

		CompareSplit<T> c = new CompareSplit<T>(executorService, comparator,
				rootlist, this, unsorted.subList(1, unsorted.size()),
				unsorted.get(0));
		c.compareSplit();

		try {
			executorService.awaitTermination(1000000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		LinkedList<T> result = new LinkedList<T>();
		for (SortedList<T> s : this.results) {
			result.addAll(s.getList());
		}
		return result;
	}

	private void splitUnsorted(SortedList<T> unsorted) {
		CompareSplit<T> c = new CompareSplit<T>(executorService, comparator,
				unsorted, this);
		c.compareSplit();
	}

	private void sortUnsorted(SortedList<T> unsorted) {
		executorService.execute(new SortRunnable<T>(this.comparator, unsorted,
				this));
	}

	protected void updateSorted(SortedList<T> current,
			LinkedList<T> sortedResult) {
		synchronized (this.results) {

			int index = this.results.indexOf(current);

			this.results.set(index, new SortedList<T>(sortedResult,
					SortedStatus.SORTED));
		}

	}

	/**
	 * Replace current SortedList object with the result from CompareResults
	 * items
	 * 
	 * @param current
	 * @param results
	 */
	protected void updateCompareSplit(SortedList<T> current,
			CompareResults<T> compareResults) {
		synchronized (this.results) {

			int index = this.results.indexOf(current);

			this.results.set(index,
					new SortedList<T>(compareResults.getGreaterThan(),
							SortedStatus.UNSORTED));
			this.results.add(index, new SortedList<T>(
					compareResults.getEqual(), SortedStatus.SORTED));
			this.results.add(index,
					new SortedList<T>(compareResults.getLessThan(),
							SortedStatus.UNSORTED));
			
			executorService.execute(new ProcessUnsortedRunnable<T>(this));
		}

	}

	protected void processUnsorted() {
		synchronized (this.results) {
			boolean allSorted = true;
			for (SortedList<T> s : this.results) {
				if (s.getSorted() == SortedStatus.UNSORTED) {
					if (s.getList().size() > sortingThreshold) {
						allSorted = false;
						s.setSorted(SortedStatus.BEING_SPLIT);
						splitUnsorted(s);
					} else {
						s.setSorted(SortedStatus.BEING_SORTED);
						sortUnsorted(s);
					}
				} else if (s.getSorted() == SortedStatus.BEING_SPLIT){
					allSorted = false;
				}
			}
			if (allSorted) {
				executorService.shutdown();
			}
		}
	}

	private static class ProcessUnsortedRunnable<T> implements Runnable {
		
		private ThreadedListSorter<T> caller;
		
		public ProcessUnsortedRunnable(ThreadedListSorter<T>  caller) {
			this.caller = caller;
		}
		public void run() {
			caller.processUnsorted();
		}

	}

	private static class CompareSplit<T> {

		private CompareResults<T> results;
		private ExecutorService executorService;
		private Comparator<? super T> comparator;
		private SortedList<T> parent;
		private ThreadedListSorter<T> caller;
		private List<T> unsorted;
		private T item;
		private int parts;
		private int partsProcessed;

		public CompareSplit(ExecutorService executorService,
				Comparator<? super T> comparator, SortedList<T> parent,
				ThreadedListSorter<T> caller, List<T> unsorted, T item) {
			this(executorService, comparator, parent, caller);
			this.item = item;
			this.unsorted = unsorted;
		}

		public CompareSplit(ExecutorService executorService,
				Comparator<? super T> comparator, SortedList<T> parent,
				ThreadedListSorter<T> caller) {

			this.executorService = executorService;
			this.comparator = comparator;
			this.parent = parent;
			this.caller = caller;

			this.results = new CompareResults<T>();
			this.partsProcessed = 0;
		}

		public void compareSplit() {
			// not benefit in alg list sorting for small lists
			if (item == null) {
				item = parent.getList().get(0);
			}
			this.results.getEqual().add(item);

			if (unsorted == null) {
				unsorted = parent.getList().subList(1, parent.getList().size());
			}

			parts = Math
					.floorDiv(unsorted.size(), caller.getSortingThreshold());

			for (int i = 0; i < parts; i++) {
				final int start = i * caller.getSortingThreshold();
				List<T> data;

				if (i == parts - 1) {
					data = unsorted.subList(start, unsorted.size());
				} else {
					data = unsorted.subList(start,
							start + caller.getSortingThreshold());
				}
				executorService.execute(new CompareRunnable<T>(this, data,
						item, comparator));
			}
		}

		public synchronized void merge(CompareResults<T> merge) {
			results.merge(merge);
			partsProcessed++;
			if (partsProcessed == parts) {
				caller.updateCompareSplit(parent, results);
			}
		}

	}

	private static class CompareResults<T> {

		private LinkedList<T> lessThan;
		private LinkedList<T> equal;
		private LinkedList<T> greaterThan;

		public CompareResults() {
			lessThan = new LinkedList<T>();
			equal = new LinkedList<T>();
			greaterThan = new LinkedList<T>();
		}

		public void call(List<T> items, T item, Comparator<? super T> c) {

			for (T comparable : items) {

				int result = c.compare(comparable, item);
				if (result < 0) {
					lessThan.add(comparable);
				} else if (result > 0) {
					greaterThan.add(comparable);
				} else {
					equal.add(comparable);
				}
			}
		}

		public synchronized CompareResults<T> merge(CompareResults<T> merge) {
			equal.addAll(merge.getEqual());
			lessThan.addAll(merge.getLessThan());
			greaterThan.addAll(merge.getGreaterThan());
			return this;
		}

		public LinkedList<T> getLessThan() {
			return lessThan;
		}

		public LinkedList<T> getEqual() {
			return equal;
		}

		public LinkedList<T> getGreaterThan() {
			return greaterThan;
		}
	}

	private static class SortRunnable<T> implements Runnable {

		private Comparator<? super T> c;
		private SortedList<T> parent;
		private ThreadedListSorter<T> caller;

		public SortRunnable(Comparator<? super T> c, SortedList<T> parent,
				ThreadedListSorter<T> caller) {
			this.c = c;
			this.parent = parent;
			this.caller = caller;
		}

		public void run() {
			caller.updateSorted(parent,
					(LinkedList<T>) AlgoListSort.sort(parent.getList(), c));
		}

	}

	private static class CompareRunnable<T> implements Runnable {

		private List<T> l;
		private CompareSplit<T> parentResult;
		private T item;
		private Comparator<? super T> c;

		public CompareRunnable(CompareSplit<T> result, List<T> l, T item,
				Comparator<? super T> c) {
			this.l = l;
			this.parentResult = result;
			this.item = item;
			this.c = c;
		}

		public void run() {
			CompareResults<T> result = new CompareResults<T>();
			result.call(l, item, c);
			parentResult.merge(result);

		}

	}

	public enum SortedStatus {
		SORTED, BEING_SPLIT, BEING_SORTED, UNSORTED
	};

	private static class SortedList<T> {
		private LinkedList<T> list;
		private SortedStatus isSorted;

		public SortedList(SortedStatus isSorted) {
			super();
			this.isSorted = isSorted;
		}

		public SortedList(LinkedList<T> list, SortedStatus isSorted) {
			super();
			this.list = list;
			this.isSorted = isSorted;
		}

		public LinkedList<T> getList() {
			return list;
		}

		public SortedStatus getSorted() {
			return isSorted;
		}

		public void setSorted(SortedStatus isSorted) {
			this.isSorted = isSorted;
		}

	}
}
