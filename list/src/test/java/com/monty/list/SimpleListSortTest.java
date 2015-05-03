package com.monty.list;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class SimpleListSortTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SimpleListSortTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SimpleListSortTest.class );
    }

    /**
     * Test that the random list is sorted correctly
     * in ascending order
     */
    public void testRandomSimpleSortAsc()
    {
    	
    	
    	List<Number> n = Utils.generateRandomList(20, 100);
    	
    	List<Number> sorted = SimpleListSort.sort(n, NumberComparators.numberAscending);
    	
    	for(int i = 1; i<sorted.size()-1; i++){
            assertTrue( sorted.get(i).doubleValue() >= sorted.get(i-1).doubleValue() );
            assertTrue( sorted.get(i).doubleValue() <= sorted.get(i+1).doubleValue() );    		
    	}
    	
    }
    
    
    /**
     * Test that the random list is sorted correctly
     * in descending order
     */
    public void testRandomSimpleSortDesc()
    {
    	
    	
    	List<Number> n = Utils.generateRandomList(20, 100);
    	
    	List<Number> sorted = SimpleListSort.sort(n, NumberComparators.numberDescending);
    	
    	for(int i = 1; i<sorted.size()-1; i++){
            assertTrue( sorted.get(i).doubleValue() <= sorted.get(i-1).doubleValue() );
            assertTrue( sorted.get(i).doubleValue() >= sorted.get(i+1).doubleValue() );    		
    	}
    	
    }

}
