package com.monty.list;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {

//		List<Number> items = Utils.generateRandomList(15, 100d);
		List<Number> items  = asList(getSimpleItems());
		System.out.println(items);
		
		List<Number> sortedAscThread = ThreadedListSort.sort(items,
				NumberComparators.numberAscending);
		System.out.println(sortedAscThread);

		
		List<Number> sortedAscAlgo = AlgoListSort.sort(items,
				NumberComparators.numberAscending);
		System.out.println(sortedAscAlgo);
		
		List<Number> sortedAsc = SimpleListSort.sort(items,
				NumberComparators.numberAscending);
//		List<Number> sortedDes = SimpleListUtils.sort(items,
//				NumberComparators.numberDescending);

		System.out.println(sortedAsc);
//		System.out.println(sortedDes);

	}
	
	public static double[] getItems() {
		return new double[]{74.26955458548255, 74.4592307530068, 24.6573529243166, 77.68172258058739, 72.44171456204562, 4.114566689950605, 35.604795235677166, 68.88847017814076, 24.545353594003018, 90.88298718105715, 52.965093573122616, 61.67824000125307, 61.98607475061001, 34.66821251418266, 9.228801934620623, 43.73981860432322, 35.06070160928035, 40.43589824311358, 22.851570955906965, 69.06566326841396};
		
	}
	
	public static double[] getSimpleItems() {
		return new double[]{5,4,3,2,1,10,9,8,7,6,15,14,13,12,11,20};
		
	}
	public static List<Number> asList(double[] items){
		ArrayList<Number> a = new ArrayList<Number>();
		for(double d: items){
			a.add(d);
		}
		return a;
	}
	
}
