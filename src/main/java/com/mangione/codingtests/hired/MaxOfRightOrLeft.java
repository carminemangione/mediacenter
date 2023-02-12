package com.mangione.codingtests.hired;

public class MaxOfRightOrLeft {
	private final String leftOrRight;

	public MaxOfRightOrLeft(long[] tree) {
		long sumLeft = recurseThroughTree(tree, 1);
		long sumRight = recurseThroughTree(tree, 2);
		leftOrRight = sumLeft > sumRight ? "Left" : sumRight > sumLeft ? "Right" : "";
	}

	public String getLeftOrRight() {
		return leftOrRight;
	}

	private long recurseThroughTree(long[] tree, int currentIndex) {
		long sum = 0;
		if (currentIndex < tree.length) {
			if (tree[currentIndex] != -1) {
				sum = tree[currentIndex];

				boolean terminated = false;
				if (currentIndex > 1 && currentIndex < tree.length - 1) {
					if (tree[currentIndex + 1] == -1)
						terminated = true;
					else
						sum += tree[currentIndex + 1];
				}

				sum = checkIfOtherSideTerminatesOrRecurse(tree, currentIndex, sum, terminated);
			}
		}
		return sum;
	}

	private long checkIfOtherSideTerminatesOrRecurse(long[] tree, int currentIndex, long sum, boolean terminated) {
		if (!terminated && currentIndex < tree.length - 2) {
			if (tree[currentIndex + 1] ==  -1) {
				sum += sumRestOfArray(tree, currentIndex + 2);
			}
			else if (tree[currentIndex + 2] == -1) {
				sum += sumRestOfArray(tree, currentIndex + 3);
			}
			sum += recurseThroughTree(tree, currentIndex + 2);
		}
		return sum;
	}

	private long sumRestOfArray(long[] tree, int index) {
		long sum = 0;
		for (int i = index; i < tree.length; i++) {
			sum += tree[i];
		}
		return sum;
	}

}
