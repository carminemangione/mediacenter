package com.mangione.leetcode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MedianOfTwoSortedArraysTest {
	@Test
	public void secondArrayLongerOddNumberOfElements() {
		int[] first = {1, 3};
		int[] second = {2};
		MedianOfTwoSortedArrays medianOfTwoSortedArrays = new MedianOfTwoSortedArrays(first, second);
		assertEquals(2, medianOfTwoSortedArrays.getMedian());
	}

	@Test
	public void firstArrayLongerOddNumberOfElements() {
		int[] first = {2};
		int[] second = {1,3};
		MedianOfTwoSortedArrays medianOfTwoSortedArrays = new MedianOfTwoSortedArrays(first, second);
		assertEquals(2, medianOfTwoSortedArrays.getMedian());
	}

	@Test
	public void evenNumberOfElementsTakesMean() {
		int[] first = {2,3};
		int[] second = {1,4};
		MedianOfTwoSortedArrays medianOfTwoSortedArrays = new MedianOfTwoSortedArrays(first, second);
		assertEquals(2.5, medianOfTwoSortedArrays.getMedian());

	}

	@Test
	public void oneElementReturnsFirst() {
		int[] first = {2};
		int[] second = {};
		MedianOfTwoSortedArrays medianOfTwoSortedArrays = new MedianOfTwoSortedArrays(first, second);
		assertEquals(2, medianOfTwoSortedArrays.getMedian());
	}


}