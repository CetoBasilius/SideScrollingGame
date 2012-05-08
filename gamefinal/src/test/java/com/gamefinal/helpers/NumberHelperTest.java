package com.gamefinal.helpers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberHelperTest {
	@Test
	public void testCountDigits(){

		assertEquals(1,NumberHelper.countDigits(1));
		assertEquals(2,NumberHelper.countDigits(11));

		assertEquals(5,NumberHelper.countDigits(11111));
		assertEquals(5,NumberHelper.countDigits(12345));
	}
}
