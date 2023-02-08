package com.mangione.examples;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FibonacciTest {
	@Test
	public void zero() {
		Fibonacci fibonacci = new Fibonacci(0);
		Assertions.assertEquals(0, fibonacci.getValue());
	}
	@Test
	public void one() {
		Fibonacci fibonacci = new Fibonacci(1);
		Assertions.assertEquals(1, fibonacci.getValue());
	}

	@Test
	public void two() {
		Fibonacci fibonacci = new Fibonacci(2);
		Assertions.assertEquals(1, fibonacci.getValue());
	}

	@Test
	public void three() {
		Fibonacci fibonacci = new Fibonacci(3);
		Assertions.assertEquals(2, fibonacci.getValue());
	}

	@Test
	public void four() {
		Fibonacci fibonacci = new Fibonacci(4);
		Assertions.assertEquals(3, fibonacci.getValue());
	}

	@Test
	public void fortySeven() {
		Fibonacci fibonacci = new Fibonacci(47);
		Assertions.assertEquals(2971215073L, fibonacci.getValue());
	}

}