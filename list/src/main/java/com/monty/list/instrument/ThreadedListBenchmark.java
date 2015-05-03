package com.monty.list.instrument;

import java.util.List;
import java.util.concurrent.Callable;

import com.monty.list.NumberComparators;
import com.monty.list.ThreadedListSort;
import com.monty.list.Utils;

/**
 * Benchmark simple list against algo list
 * We expect the algo list to be an order of magnitude quicker due to the removal of the n^2 problem
 * @author Mark Montgomery
 *
 */
public class ThreadedListBenchmark {

	public static void main(String[] args) {
		
		// create a list of 2000 elements of random doubles from 0 to 2000 for benchmark
		List<Number> l = Utils.generateRandomList(10000, 2000);
		
		// instrument the threaded sort
		TimerInstrument<List<Number>> tenThreadsSortTimer = new TimerInstrument<List<Number>>(new BaseListInstrument<List<Number>>(l) {
			
			public List<Number> call() {
				return ThreadedListSort.sort(getList(), NumberComparators.numberAscending);
			}
			
			
		});

		// instrument the algo sort
		TimerInstrument<List<Number>> twentyThreadsSortTimer = new TimerInstrument<List<Number>>(new BaseListInstrument<List<Number>>(l) {
			
			public List<Number> call() {
				return ThreadedListSort.sort(getList(), NumberComparators.numberAscending, 20, 200);
			}
			
			
		});
		
		// instrument the algo sort
		TimerInstrument<List<Number>> twentyFiveThreadsSortTimer = new TimerInstrument<List<Number>>(new BaseListInstrument<List<Number>>(l) {
			
			public List<Number> call() {
				return ThreadedListSort.sort(getList(), NumberComparators.numberAscending, 20, 300);
			}
			
			
		});

		try {
			System.out.println("Ten threads sort time: " + tenThreadsSortTimer.call() + "ms");
			System.out.println("Twenty threads sort time: " + twentyThreadsSortTimer.call() + "ms");
			System.out.println("Twenty threads 500 threshold sort time: " + twentyFiveThreadsSortTimer.call() + "ms");
			
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

