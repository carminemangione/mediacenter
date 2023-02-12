package com.mangione.codingtests.algoexperts;

public class LongestPeak {
	private final int longestPeak;

	public LongestPeak(int[] array) {
		boolean positiveSlope;
		int longestPeak = 0;
		for (int i = 1; i < array.length - longestPeak; i++) {

			// are we starting with a positive slope
			positiveSlope = array[i] > array[i - 1];
			if (positiveSlope) {
				int currentPeakLength = 2;
				int cur = i + 1;

				while (positiveSlope && cur < array.length) {
					positiveSlope = array[cur] > array[cur - 1];
					if (positiveSlope) {
						currentPeakLength++;
						cur++;
					} else if (array[cur] == array[cur - 1]) {
						currentPeakLength = 0;
					}
				}

				if (cur == array.length && array[array.length - 1] > array[array.length - 2]) {
					longestPeak = 0;
					break;
				}

				// travel down
				while (!positiveSlope && cur < array.length && currentPeakLength !=0) {
					positiveSlope = array[cur] >= array[cur - 1];
					if (!positiveSlope ) {
						currentPeakLength++;
						cur++;
					}
				}
				longestPeak = Math.max(longestPeak, currentPeakLength);
			}
		}
		this.longestPeak = longestPeak;
	}

	public int getLongestPeak() {
		return longestPeak;
	}
}
