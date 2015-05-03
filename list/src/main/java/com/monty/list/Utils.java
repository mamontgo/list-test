package com.monty.list;

import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static List<Number> generateRandomList(int size, double maxrange){
		
		List<Number> array = new ArrayList<Number>(size);
		for(int i=0;i<size;i++){
			array.add(Math.random() * maxrange);
		}
		return array;
	}
}
