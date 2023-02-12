package com.mangione.codingtests.algoexperts;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpiralTraversalTest {
	@Test
	public void testOneNumber() {
		int[][] input = {{1}};
		int[] answer = {1};

		assertEquals(Arrays.stream(answer).boxed().collect(Collectors.toList()),
				new SpiralTraversal(input).getSpiralUnwind());
	}


	@Test
	public void testFourByFour() {
		int[][] input = {{1, 2, 3, 4}, {12, 13, 14, 5}, {11, 16, 15, 6}, {10, 9, 8, 7}};
		int[] answer = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

		assertEquals(Arrays.stream(answer).boxed().collect(Collectors.toList()),
				new SpiralTraversal(input).getSpiralUnwind());
	}

	@Test
	public void testSixByFour() {
		int[][] input = {
				{27, 12, 35, 26},
				{25, 21, 94, 11},
				{19, 96, 43, 56},
				{55, 36, 10, 18},
				{96, 83, 31, 94},
				{93, 11, 90, 16}};
		int[] answer = {27, 12, 35, 26, 11, 56, 18, 94, 16, 90, 11, 93, 96, 55, 19, 25, 21, 94, 43, 10, 31, 83, 36, 96};

		assertEquals(Arrays.stream(answer).boxed().collect(Collectors.toList()),
				new SpiralTraversal(input).getSpiralUnwind());
	}

	@Test
	public void testThreeByFour() {
		int[][] input = {
				{1, 2, 3, 4},
				{10, 11, 12, 5},
				{9, 8, 7, 6}
		};
		int[] answer = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

		assertEquals(Arrays.stream(answer).boxed().collect(Collectors.toList()),
				new SpiralTraversal(input).getSpiralUnwind());
	}


}