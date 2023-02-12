package com.mangione.codingtests.algoexperts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindClosestBST {
	private final int closestValue;

	public FindClosestBST(BST bst, int target) {
		closestValue = recurseThroughTreeLookingForClosestValue(bst, target, bst.value);
	}

	static int recurseThroughTreeLookingForClosestValue(BST currentNode, int target, int closestValue) {
		if (currentNode == null)
			return closestValue;
		int currentDistance = Math.abs(target - currentNode.value);
		int previousDistance = Math.abs(target - closestValue);
		int newClosest =  previousDistance > currentDistance ? currentNode.value : closestValue;

		int leftClosest = recurseThroughTreeLookingForClosestValue(currentNode.left, target, newClosest);
		int rightClosest = recurseThroughTreeLookingForClosestValue(currentNode.right, target, newClosest);

		int leftDistance = Math.abs(target - leftClosest);
		int rightDistance = Math.abs(target - rightClosest);
		int newDistance = Math.abs(target - newClosest);

		newClosest = leftDistance < newDistance ? leftClosest : newClosest;
		newClosest = rightDistance < newDistance ? rightClosest: newClosest;
		List<Integer[]> sums = new ArrayList<>();
		sums = sums.stream().sorted((a,b)->a[0].compareTo(b[0])).collect(Collectors.toList());


		return newClosest;
	}

	public int getClosestValue() {
		return closestValue;
	}
}
