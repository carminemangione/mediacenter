package com.mangione.codingtests.algoexperts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NonConstructableChangeTest {
	@Test
	public void noNumbers() {
		NonConstructableChange nonConstructableChange = new NonConstructableChange(new int[]{});
		assertEquals(1, nonConstructableChange.getMinimum());
	}

	@Test
	public void singleNumber() {
		NonConstructableChange nonConstructableChange = new NonConstructableChange(new int[]{1});
		assertEquals(2, nonConstructableChange.getMinimum());
	}

	@Test
	public void baseCase() {
		NonConstructableChange nonConstructableChange = new NonConstructableChange(new int[]{5, 7, 1, 1, 2, 3, 22});
		assertEquals(20, nonConstructableChange.getMinimum());
	}


}