package com.mangione.codingtests.leetcode;

public class TwoSum {
	private final int[] solution;
	public TwoSum(int[] input, int target) {
		solution = new int[2];
		boolean solved = false;
		for (int firstNumber = 0; firstNumber < input.length && !solved; firstNumber++) {
			solution[0] = firstNumber;
			for (int secondNumber = firstNumber + 1; secondNumber < input.length && !solved; secondNumber++) {
				if (input[firstNumber] + input[secondNumber] == target) {
					solution[1] = secondNumber;
					solved = true;
				}
			}
		}
	}

	public int[] getSolution() {
		return solution;
	}



}
