package com.gamefinal.helpers;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringHelperTest {

	@Test
	public void testCountOccurrences(){
		assertEquals(1,StringHelper.countOccurrences("Hola!",'!'));
		assertEquals(2,StringHelper.countOccurrences("Hola!!",'!'));
		assertEquals(10,StringHelper.countOccurrences("Hola!!!!!!!!!!",'!'));
		assertEquals(0,StringHelper.countOccurrences("Hola!",'#'));
		assertEquals(2,StringHelper.countOccurrences("Hola, como estas?",' '));
	}
}
