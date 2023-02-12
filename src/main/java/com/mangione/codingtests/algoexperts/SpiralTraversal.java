package com.mangione.codingtests.algoexperts;

import java.util.ArrayList;
import java.util.List;

public class SpiralTraversal {

	private final List<Integer> spiralUnwind;

	public SpiralTraversal(int[][] input) {
		spiralUnwind = spiralTraverse(input);

	}

	public static List<Integer> spiralTraverse(int[][] array) {
		List<Integer> answer = new ArrayList<>();
		int currentMinRow = 0;
		int currentMaxRow = array.length;
		int currentMinCol = 0;
		int currentMaxCol = array[0].length;

		while (currentMaxRow - currentMinRow > 0) {
			for (int i = currentMinCol; i < currentMaxCol; i++) {
				answer.add(array[currentMinRow][i]);
			}

			currentMinRow++;
			if (currentMaxRow - currentMinRow == 0)
				break;

			for (int i = currentMinRow; i < currentMaxRow; i++) {
				answer.add(array[i][currentMaxCol - 1]);
			}
			currentMaxCol--;
			if (currentMaxCol - currentMinCol == 0)
				break;

			for (int i = currentMaxCol - 1; i >= currentMinCol; i--) {
				answer.add(array[currentMaxRow - 1][i]);
			}
			currentMaxRow--;
			if (currentMaxRow - currentMinRow == 0)
				break;

			for (int i = currentMaxRow - 1; i >= currentMinRow; i--) {
				answer.add(array[i][currentMinCol]);
			}
			currentMinCol++;
			if (currentMaxCol - currentMinCol == 0)
				break;
		}
		// Write your code here.
		return answer;
	}

	public List<Integer> getSpiralUnwind() {
		return spiralUnwind;
	}
}
