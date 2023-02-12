package com.mangione.codingtests.leetcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mangione.codingtests.leetcode.LongestLengthOfSubstringWithoutRepeating;

class LongestLengthOfSubstringWithoutRepeatingTest {
	@Test
	public void emptyStringIsZero() {
		LongestLengthOfSubstringWithoutRepeating length = new LongestLengthOfSubstringWithoutRepeating("");
		Assertions.assertEquals(0, length.lengthOfLongestSubstring());
	}


	@Test
	public void singleCharacterReturnsOne() {
		LongestLengthOfSubstringWithoutRepeating length = new LongestLengthOfSubstringWithoutRepeating(" ");
		Assertions.assertEquals(1, length.lengthOfLongestSubstring());
	}


	@Test
	public void firstSubstringLongest() {
		LongestLengthOfSubstringWithoutRepeating length = new LongestLengthOfSubstringWithoutRepeating("abcabcbb");
		Assertions.assertEquals(3, length.lengthOfLongestSubstring());
	}

	@Test
	public void firstCharacterRepeated() {
		LongestLengthOfSubstringWithoutRepeating length = new LongestLengthOfSubstringWithoutRepeating("bbbbb");
		Assertions.assertEquals(1, length.lengthOfLongestSubstring());
	}

	@Test
	public void longestInMiddle() {
		LongestLengthOfSubstringWithoutRepeating length = new LongestLengthOfSubstringWithoutRepeating("pwwkew");
		Assertions.assertEquals(3, length.lengthOfLongestSubstring());
	}

	@Test
	public void twoUniqueCharacters() {
		LongestLengthOfSubstringWithoutRepeating length = new LongestLengthOfSubstringWithoutRepeating("au");
		Assertions.assertEquals(2, length.lengthOfLongestSubstring());
	}

}