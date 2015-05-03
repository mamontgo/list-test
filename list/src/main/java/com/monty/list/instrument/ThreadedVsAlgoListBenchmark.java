package com.monty.list.instrument;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import com.monty.list.AlgoListSort;
import com.monty.list.NumberComparators;
import com.monty.list.ThreadedListSort;
import com.monty.list.Utils;

/**
 * Benchmark threaded list against algo list
 * We expect the threaded list to be an order of magnitude quicker due concurrent processing
 * 
 * Benchmarked against the java collections sort that proves to be super efficient and difficult to beat
 * even when using a bunch of threads
 * 
 * Perhaps the Threaded sort would prevail if it relied on the collections sort instead of the current algo sort being used.
 * A job for another day perhaps
 * 
 * @author Mark Montgomery
 *
 */
public class ThreadedVsAlgoListBenchmark {

	public static void main(String[] args) {
		
		// create a list of 2000 elements of random doubles from 0 to 2000 for benchmark
		List<Number> l = Utils.generateRandomList(20000, 2000);
		
		// instrument the threaded sort
		TimerInstrument<List<Number>> threadedSortTimer = new TimerInstrument<List<Number>>(new BaseListInstrument<List<Number>>(l) {
			
			public List<Number> call() {
				return ThreadedListSort.sort(getList(), NumberComparators.numberAscending, 20, 200);
			}
			
			
		});

		// instrument the algo sort
		TimerInstrument<List<Number>> algoSortTimer = new TimerInstrument<List<Number>>(new BaseListInstrument<List<Number>>(l) {
			
			public List<Number> call() {
				return AlgoListSort.sort(getList(), NumberComparators.numberAscending);
			}
			
			
		});
		
		// instrument the java collections sort
		TimerInstrument<List<Number>> javaSortTimer = new TimerInstrument<List<Number>>(new BaseListInstrument<List<Number>>(l) {
			
			public List<Number> call() {
				Collections.sort(getList(), NumberComparators.numberAscending);
				return null;
			}
			
			
		});

		try {
			System.out.println("Threaded sort time: " + threadedSortTimer.call() + "ms");
			System.out.println("Algo sort time: " + algoSortTimer.call() + "ms");
			System.out.println("Java collections package sort time: " + javaSortTimer.call() + "ms");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private abstract static class BaseListInstrument<V> implements Callable<V> {
		
		private List<Number> list;
		
		public BaseListInstrument(List<Number> l){
			this.list = l;
		}
		
		protected List<Number> getList() {
			return list;
		}
		
		
	}
}

