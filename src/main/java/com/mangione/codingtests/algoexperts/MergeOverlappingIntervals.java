package com.mangione.codingtests.algoexperts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MergeOverlappingIntervals {
	private final int[][] mergedIntervals;

	public MergeOverlappingIntervals(int[][] intervals) {
		Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
		List<Integer[]> mergedIntervals = new ArrayList<>();
		Integer[] currentInterval = {intervals[0][0], intervals[0][1]};
		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i][0] <= currentInterval[1]) {
				currentInterval = mergeIntervals(currentInterval, intervals[i]);
			} else if (intervals[i][0] > intervals[i - 1][1]) {
				mergedIntervals.add(new Integer[]{currentInterval[0], currentInterval[1]});
				currentInterval = new Integer[]{intervals[i][0], intervals[i][1]};
			}
		}
		mergedIntervals.add(new Integer[]{currentInterval[0], currentInterval[1]});


		int[][] returnValues = new int[mergedIntervals.size()][2];
		for (int i = 0; i < returnValues.length; i++) {
			Integer[] interval = mergedIntervals.get(i);
			returnValues[i] = new int[]{interval[0], interval[1]};
		}

		this.mergedIntervals = returnValues;
	}

	private Integer[] mergeIntervals(Integer[] currentInterval, int[] interval) {
		return new Integer[]{Math.min(currentInterval[0], interval[0]), Math.max(currentInterval[1], interval[1])};
	}

	public int[][] getMergedIntervals() {
		return mergedIntervals;
	}
}
