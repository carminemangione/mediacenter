package com.mangione.examples;

public class SimpleString {
	public static void main(String[] args) {
		System.out.println(new SimpleString().makeOutWord("<<>>", "poop"));
	}

	public String makeOutWord(String out, String word) {
	  String opening = out.substring(0, 1);
	  int closing = out.lastIndexOf(opening);
	  return out.substring(0, closing) + word + out.substring(closing + 1);
	}

}
