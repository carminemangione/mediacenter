package com.mangione.codingtests.leetcode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mangione.codingtests.leetcode.ThreeSumZero;

class ThreeSumTest {
	@Test
	public void threeNumbersNoTriplet() {
		int[] input = {1, 2, 3};
		ThreeSumZero solution = new ThreeSumZero(input);
		Assertions.assertTrue(solution.threeSum().isEmpty());
	}

	@Test
	public void threeNumbersTriplet() {
		int[] input = {1, 2, -3};
		ThreeSumZero solution = new ThreeSumZero(input);
		List<Integer> expected = solution.threeSum().get(0);
		List<Integer> actual = List.of(new Integer[]{-3, 1, 2});
		assertEquals(expected, actual);
	}

	@Test
	public void fourNumbersOutOfOrder() {
		int[] input = {1, 4, 2, -5};
		ThreeSumZero solution = new ThreeSumZero(input);
		List<Integer> actusl = solution.threeSum().get(0);
		List<Integer> expected = List.of(new Integer[]{-5, 1, 4});
		assertEquals(actusl, expected);
	}

	@Test
	public void fiveNumbersThirdOutOfOrder() {
		int[] input = {1, 4, 2, 1, -5};
		ThreeSumZero solution = new ThreeSumZero(input);
		List<Integer> expected = solution.threeSum().get(0);
		List<Integer> actual = List.of(new Integer[]{-5, 1, 4});
		assertEquals(expected, actual);
	}

	@Test
	public void fiveNumbersSecondOutOfOrder() {
		int[] input = {1, 1, 4, 1, -5};
		ThreeSumZero solution = new ThreeSumZero(input);
		List<Integer> expected = solution.threeSum().get(0);
		List<Integer> actual = List.of(new Integer[]{-5, 1, 4});
		assertEquals(expected, actual);
	}

	@Test
	public void fiveNumbersFirstOutOfOrder() {
		int[] input = {2, 1, 4, 1, -5};
		ThreeSumZero solution = new ThreeSumZero(input);
		List<Integer> expected = solution.threeSum().get(0);
		List<Integer> actual = List.of(new Integer[]{-5, 1, 4});
		assertEquals(expected, actual);
	}

	@Test
	public void twoAnswersDontRepeat() {
		int[] input = {-1, 0, 1, 2, -1, -4};
		ThreeSumZero solution = new ThreeSumZero(input);
		List<List<Integer>> expected = Arrays.asList(List.of(new Integer[]{-1, -1, 2}),
				List.of(new Integer[]{-1, 0, 1}));
		assertEquals(expected.size(), solution.threeSum().size());
		for (List<Integer> integers : expected) {
			Assertions.assertTrue(solution.threeSum().contains(integers));
		}
	}

	@Test
	public void wrongOutput() {
		// input: [34,55,79,28,46,33,2,48,31,-3,84,71,52,-3,93,15,21,-43,57,-6,86,56,94,74,83,-14,28,-66,46,-49,62,-11,43,65,77,12,47,61,26,1,13,29,55,-82,76,26,15,-29,36,-29,10,-70,69,17,49]
		// expected: [[-82,-11,93],[-82,13,69],[-82,17,65],[-82,21,61],[-82,26,56],[-82,33,49],[-82,34,48],[-82,36,46],[-70,-14,84],[-70,-6,76],[-70,1,69],[-70,13,57],[-70,15,55],[-70,21,49],[-70,34,36],[-66,-11,77],[-66,-3,69],[-66,1,65],[-66,10,56],[-66,17,49],[-49,-6,55],[-49,-3,52],[-49,1,48],[-49,2,47],[-49,13,36],[-49,15,34],[-49,21,28],[-43,-14,57],[-43,-6,49],[-43,-3,46],[-43,10,33],[-43,12,31],[-43,15,28],[-43,17,26],[-29,-14,43],[-29,1,28],[-29,12,17],[-14,-3,17],[-14,1,13],[-14,2,12],[-11,-6,17],[-11,1,10],[-3,1,2]]
		int[] input = {34,55,79,28,46,33,2,48,31,-3,84,71,52,-3,93,15,21,-43,57,-6,86,56,94,74,83,-14,28,-66,46,-49,62,-11,43,65,77,12,47,61,26,1,13,29,55,-82,76,26,15,-29,36,-29,10,-70,69,17,49};
		ThreeSumZero solution = new ThreeSumZero(input);

		int[][] expected = {{-82,-11,93},{-82,13,69},{-82,17,65},{-82,21,61},{-82,26,56},{-82,33,49},{-82,34,48},{-82,36,46},{-70,-14,84},{-70,-6,76},{-70,1,69},{-70,13,57},{-70,15,55},{-70,21,49},{-70,34,36},{-66,-11,77},{-66,-3,69},{-66,1,65},{-66,10,56},{-66,17,49},{-49,-6,55},{-49,-3,52},{-49,1,48},{-49,2,47},{-49,13,36},{-49,15,34},{-49,21,28},{-43,-14,57},{-43,-6,49},{-43,-3,46},{-43,10,33},{-43,12,31},{-43,15,28},{-43,17,26},{-29,-14,43},{-29,1,28},{-29,12,17},{-14,-3,17},{-14,1,13},{-14,2,12},{-11,-6,17},{-11,1,10},{-3,1,2}};

		List<List<Integer>> answers = solution.threeSum();
		assertEquals(expected.length, answers.size());
	}
}