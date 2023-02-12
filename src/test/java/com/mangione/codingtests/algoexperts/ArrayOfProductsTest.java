package com.mangione.codingtests.algoexperts;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ArrayOfProductsTest {
	@Test
	public void firstTwoElementsIsZero() {
		int[] array = {0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		int[] expected = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		assertArrayEquals(expected, new ArrayOfProducts(array).getProducts());
	}

	@Test
	public void firstElementsZero() {
		int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		int[] expected = {362880, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		assertArrayEquals(expected, new ArrayOfProducts(array).getProducts());
	}

	public int[][] mergeOverlappingIntervals(int[][] intervals) {
		// Write your code here.

		Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
		return new int[][] {};
	}

}