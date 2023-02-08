package com.mangione.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KnapsackProblem {

	private final int maxWeight;

	public static void main(String[] args) {
		int[] values = new int[] {13, 11, 54};
		int[] weights = new int[] {1, 3, 2};
		System.out.println(new KnapsackProblem(4, values, weights).getMaxWeight());
	}

	public KnapsackProblem(int totalWeight, int[] values, int[] weights) {
		int[][] maxValues = new int[values.length + 1][totalWeight + 1];
		for (int i = 1; i < values.length + 1; i++) {
			for (int weight = 1; weight < totalWeight + 1; weight++)  {
				if (weights[i - 1] <= weight) {
					maxValues[i][weight] = Math.max(values[i - 1] + maxValues[i - 1][weight - weights[i-1]],
							maxValues[i-1][weight]);
				} else {
					maxValues[i][weight] = maxValues[i - 1][weight];
				}
			}
		}
		maxWeight = maxValues[values.length][totalWeight];

		List<String> strings = new ArrayList<>();
		strings.stream().collect(Collectors.joining("\n"));
	}

	public int getMaxWeight() {
		return maxWeight;
	}
} 
