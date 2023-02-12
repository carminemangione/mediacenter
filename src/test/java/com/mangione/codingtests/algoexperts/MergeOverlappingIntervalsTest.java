package com.mangione.codingtests.algoexperts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MergeOverlappingIntervalsTest {
	@Test
	public void fourIntervals() {
		int[][] input = {{1, 2}, {3, 5}, {4, 7}, {6, 8}, {9, 10}};
		int[][] expected =  {{1, 2}, {3, 8}, {9, 10}};
		assertArrayEquals(expected, new MergeOverlappingIntervals(input).getMergedIntervals());
	}

	@Test
	public void oneEncompassingAtTheEnd() {
		int[][] input = {{2, 3}, {4, 5}, {6, 7}, {8, 9}, {1, 10}};
		int[][] expected =  {{1, 10}};
		assertArrayEquals(expected, new MergeOverlappingIntervals(input).getMergedIntervals());
	}


}