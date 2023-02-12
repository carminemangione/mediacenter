package com.mangione.codingtests.hired;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MaxOfRightOrLeftTest {
	@Test
	public void testEmpty() {
		assertEquals("", new MaxOfRightOrLeft(new long[] {}).getLeftOrRight());
	}

	@Test
	public void testOneNumberReturnsEmpty() {
		assertEquals("", new MaxOfRightOrLeft(new long[] {30}).getLeftOrRight());
	}

	@Test
	public void oneDeepLeftGreater() {
		assertEquals("Left", new MaxOfRightOrLeft(new long[] {30, 10, 1}).getLeftOrRight());
	}

	@Test
	public void oneDeepRightGreater() {
		assertEquals("Right", new MaxOfRightOrLeft(new long[] {30, 1, 10}).getLeftOrRight());
	}

	@Test
	public void equalReturnsEmptyString() {
		assertEquals("", new MaxOfRightOrLeft(new long[] {30, 10, 10}).getLeftOrRight());
	}

	@Test
	public void onlyOneSide() {
		assertEquals("Left", new MaxOfRightOrLeft(new long[] {30, 10}).getLeftOrRight());
	}

	@Test
	public void rightSideLonger() {
		assertEquals("Left", new MaxOfRightOrLeft(new long[] {30, 10}).getLeftOrRight());
	}


}