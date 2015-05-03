package com.monty.list;

import java.util.Comparator;

public class NumberComparators {

	public static Comparator<Number> numberAscending = new Comparator<Number>() {

		public int compare(Number o1, Number o2) {
			if(o1.doubleValue()>o2.doubleValue()) return 1;
			if(o1.doubleValue()<o2.doubleValue()) return -1;
			return 0;			
		}
	};
	
	public static Comparator<Number> numberDescending = new Comparator<Number>() {

		public int compare(Number o1, Number o2) {
			if(o1.doubleValue()>o2.doubleValue()) return -1;
			if(o1.doubleValue()<o2.doubleValue()) return 1;
			return 0;			
		}
	};


}
