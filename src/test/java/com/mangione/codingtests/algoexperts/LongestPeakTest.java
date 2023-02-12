package com.mangione.codingtests.algoexperts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestPeakTest {
	@Test
	public void repeatInMiddleLongestFollowing() {
		int[] array = {1, 2, 3, 3, 4, 0, 10};
		assertEquals(3, new LongestPeak(array).getLongestPeak());
	}


}