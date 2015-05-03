package com.monty.list.instrument;

import java.util.concurrent.Callable;

public class TimerInstrument<V> implements Callable<Long> {

	Callable<V> c;

	public TimerInstrument(Callable<V> c) {
		this.c = c;
	}

	public Long call() throws Exception {
		long startTime = System.currentTimeMillis();
		c.call();
		return System.currentTimeMillis() - startTime;
	}
}
