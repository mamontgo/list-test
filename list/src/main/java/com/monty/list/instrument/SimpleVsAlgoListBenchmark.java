package com.monty.list.instrument;

import java.util.List;
import java.util.concurrent.Callable;

import com.monty.list.AlgoListSort;
import com.monty.list.NumberComparators;
import com.monty.list.SimpleListSort;
import com.monty.list.Utils;

/**
 * Benchmark simple list against algo list
 * We expect the algo list to be an order of magnitude quicker due to the removal of the n^2 problem
 * @author Mark Montgomery
 *
 */
public class SimpleVsAlgoListBenchmark {

	public static void main(String[] args) {
		
		// create a list of 2000 elements of random doubles from 0 to 2000 for benchmark
		List<Number> l = Utils.generateRandomList(2000, 2000);
		
		// perform an equivalence test just to be sure both sorts produce the same result
		System.out.println("Test list result equivilance: " + SimpleListSort.sort(l, NumberComparators.numberAscending).equals(AlgoListSort.sort(l, NumberComparators.numberAscending)));
		
		
		// instrument the simple sort
		TimerInstrument<List<Number>> simpleSortTimer = new TimerInstrument<List<Number>>(new BaseListInstrument<List<Number>>(l) {
			
			public List<Number> call() {
				return SimpleListSort.sort(getList(), NumberComparators.numberAscending);
			}
			
			
		});

		// instrument the algo sort
		TimerInstrument<List<Number>> algoSortTimer = new TimerInstrument<List<Number>>(new BaseListInstrument<List<Number>>(l) {
			
			public List<Number> call() {
				return AlgoListSort.sort(getList(), NumberComparators.numberAscending);
			}
			
			
		});

		try {
			System.out.println("Simple sort time: " + simpleSortTimer.call() + "ms");
			System.out.println("Algo sort time: " + algoSortTimer.call() + "ms");
			
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

