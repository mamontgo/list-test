package com.monty.list;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AlgoListSortTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AlgoListSortTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AlgoListSortTest.class);
	}

	/**
	 * Test that the random list is sorted correctly in ascending order
	 */
	public void testRandomSimpleSortAsc() {

		List<Number> n = Utils.generateRandomList(20, 100);

		List<Number> sorted = SimpleListSort.sort(n,
				NumberComparators.numberAscending);
		List<Number> algoSorted = AlgoListSort.sort(n,
				NumberComparators.numberAscending);

		assertTrue(sorted.equals(algoSorted));

	}

	/**
	 * Test that the random list is sorted correctly in descending order
	 */
	public void testRandomSimpleSortDesc() {

		List<Number> n = Utils.generateRandomList(20, 100);

		List<Number> sorted = SimpleListSort.sort(n,
				NumberComparators.numberDescending);

		List<Number> algoSorted = AlgoListSort.sort(n,
				NumberComparators.numberDescending);

		assertTrue(sorted.equals(algoSorted));

	}

}
