package com.mangione.examples;

public class Fibonacci {

	private final long fibonacci;

	public Fibonacci(int i) {
		fibonacci = calculateFibonacci(i);
	}

	public long getValue() {
		return fibonacci;
	}

	private long calculateFibonacci(int i) {
		long[] previousResults = {0, 1};
		long fibonacci = 1;
		for (int j = 1; j  < i; j++) {
			fibonacci = previousResults[0] + previousResults[1];
			previousResults[0] = previousResults[1];
			previousResults[1] = fibonacci;
		}
		return fibonacci;
	}

}
