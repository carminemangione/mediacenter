package com.mangione.codingtests.leetcode;

public class MedianOfTwoSortedArrays {
	private final double median;

	public MedianOfTwoSortedArrays(int[] first, int[] second) {
		int[] mergedLists = mergeLists(first, second);
		if (mergedLists.length % 2 == 1) {
			median = mergedLists[mergedLists.length / 2];
		} else {
			int middle = mergedLists.length / 2;
			median = ((double) mergedLists[middle] + mergedLists[middle - 1]) / 2.0;
		}
	}

	public double getMedian() {
		return median;
	}

	private int[] mergeLists(int[] first, int[] second) {
		int[] merged = new int[first.length + second.length];
		int firstIndex = 0;
		int secondIndex = 0;
		for (int i = 0; i < merged.length; i++) {
			if (secondIndex == second.length) {
				merged[i] = first[firstIndex];
				firstIndex++;
			} else if (firstIndex == first.length) {
				merged[i] = second[secondIndex];
				secondIndex++;
			} else if (first[firstIndex] <= second[secondIndex]) {
				merged[i] = first[firstIndex];
				firstIndex++;
			} else {
				merged[i] = second[secondIndex];
				secondIndex++;
			}
		}
		return merged;
	}
}
