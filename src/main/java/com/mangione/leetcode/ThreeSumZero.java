package com.mangione.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ThreeSumZero {
	private final List<List<Integer>> sums;

	public ThreeSumZero(int[] input) {
		sums = new ArrayList<>();
		Set<Integer> seenRoots = new HashSet<>();
		for (int firstIndex= 0; firstIndex < input.length; firstIndex++) {
			if (!seenRoots.contains(input[firstIndex])) {
				secondIndex(input, sums, firstIndex);
				seenRoots.add(input[firstIndex]);
			}
		}
		sums.sort(Comparator.comparingInt((List<Integer> o) -> o.get(0))
				.thenComparingInt(o -> o.get(1)).thenComparingInt(o -> o.get(2)));
	}

	private void secondIndex(int[] input, List<List<Integer>> lists, int firstIndex) {
		for (int secondIndex= 0; secondIndex < input.length; secondIndex++) {
			if (secondIndex != firstIndex) {
				thirdIndex(input, lists, firstIndex, secondIndex);
			}
		}
	}

	private void thirdIndex(int[] input, List<List<Integer>> lists, int firstIndex, int secondIndex) {
		for (int thirdIndex = secondIndex + 1; thirdIndex < input.length; thirdIndex++) {
			if (thirdIndex != firstIndex) {
				findZeroSum(input, firstIndex, secondIndex, thirdIndex, lists);
			}
		}
	}

	private void findZeroSum(int[] input, int firstIndex, int secondIndex, int thirdIndex, List<List<Integer>> solutions) {
		int sum = input[firstIndex] + input[secondIndex] + input[thirdIndex];
		if (sum == 0) {
			List<Integer> newLine = Arrays.asList(input[firstIndex], input[secondIndex], input[thirdIndex]);
			newLine.sort(Integer::compare);
			if (!solutions.contains(newLine))
				solutions.add(newLine);
		}
	}

	public List<List<Integer>> threeSum() {
		return sums;
	}
}
