package com.mangione.codingtests.leetcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mangione.codingtests.leetcode.TwoSum;

class TwoSumTest {
	@Test
	public void twoNumbersAddToTarget() {
		TwoSum twoSum = new TwoSum(new int[]{1, 8}, 9);
		Assertions.assertArrayEquals(new int[] {0, 1}, twoSum.getSolution());
	}

	@Test
	public void fourNumbersFirstAddToTarget() {
		TwoSum twoSum = new TwoSum(new int[]{2,7,11,15}, 9);
		Assertions.assertArrayEquals(new int[] {0, 1}, twoSum.getSolution());
	}

	@Test
	public void threeNumbersAllThreeInSum() {
		TwoSum twoSum = new TwoSum(new int[]{3,2,4}, 6);
		Assertions.assertArrayEquals(new int[] {1, 2}, twoSum.getSolution());
	}

}