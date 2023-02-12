package com.mangione.codingtests.algoexperts;

import java.util.Arrays;

public class ArrayOfProducts {
	private final int[] products;

	public ArrayOfProducts(int[] array) {
		long numberOfZeros = Arrays.stream(array)
				.boxed()
				.filter(x->x == 0)
				.count();
		int productOfAllFilterIfOnlyOneZero =
				numberOfZeros > 1 ? 0 :
				Arrays.stream(array)
				.boxed()
				.filter(x -> x != 0)
				.reduce(1, (a, b)->a * b);


		this.products = Arrays.stream(array)
				.boxed()
				.map((x)->x != 0 ?
						numberOfZeros == 1 ? 0 : productOfAllFilterIfOnlyOneZero / x :
						productOfAllFilterIfOnlyOneZero)
				.mapToInt(Integer::intValue)
				.toArray();
	}

	public int[] getProducts() {
		return products;
	}
}
