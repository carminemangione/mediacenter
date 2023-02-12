package com.mangione.codingtests.algoexperts;

import java.util.Arrays;

public class NonConstructableChange {
	private int minimum;

	public NonConstructableChange(int[] coins) {
		boolean changeMade = true;
		minimum = 0;
		int[] sortedCoins = Arrays.stream(coins).boxed().sorted().mapToInt(x->x).toArray();
		for (int currentMinimum = 1; changeMade; currentMinimum++) {
			int changeRemaining = currentMinimum;
			for (int i = sortedCoins.length - 1; i >= 0; i--) {
				if (sortedCoins[i] <= changeRemaining) {
					changeRemaining -= sortedCoins[i];
				}

				if (i == 0 && changeRemaining != 0) {
					changeMade = false;
					minimum = currentMinimum;
				}
			}
		}
	}

	public int getMinimum() {
		return minimum;
	}
}
