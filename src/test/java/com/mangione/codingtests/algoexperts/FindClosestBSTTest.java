package com.mangione.codingtests.algoexperts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class FindClosestBSTTest {

//	{"id": "10", "left": "5", "right": "15", "value": 10},
//	{"id": "15", "left": "13", "right": "22", "value": 15},
//	{"id": "22", "left": null, "right": null, "value": 22},
//	{"id": "13", "left": null, "right": "14", "value": 13},
//	{"id": "14", "left": null, "right": null, "value": 14},
//	{"id": "5", "left": "2", "right": "5-2", "value": 5},
//	{"id": "5-2", "left": null, "right": null, "value": 5},
//	{"id": "2", "left": "1", "right": null, "value": 2},
//	{"id": "1", "left": null, "right": null, "value": 1}
	@Test
	public void threeDeep() {
		BST right = new BST(15, new BST(13, null, new BST(14, null, null)),
				new BST(22, null, null));
		BST left = new BST(5, new BST(2, new BST(1, null, null), null),
				new BST(5, null, null));
		BST bst = new BST(10, left, right);
		assertEquals(13, new FindClosestBST(bst, 12).getClosestValue());
	}

	@Test
	public void reduce() {
		int[] numbers = {5, 1, 4};
		for (int number : numbers) {
			
		}
		assertEquals(11, Arrays.stream(numbers).reduce(Integer::sum).getAsInt());

	}

}

