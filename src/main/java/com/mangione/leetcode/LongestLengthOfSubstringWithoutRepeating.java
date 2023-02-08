package com.mangione.leetcode;

import java.util.HashSet;
import java.util.Set;

public class LongestLengthOfSubstringWithoutRepeating {
	private final int longestLength;

	LongestLengthOfSubstringWithoutRepeating(String s) {
		int currentMax = s.length() > 0 ? 1 : 0;
		Set<Character> deadEndCharacters = new HashSet<>();
		for (int firstCharacter = 0; firstCharacter < s.length(); firstCharacter++) {
			currentMax = Math.max( currentMax, fromCurrentCharacter(s, firstCharacter, deadEndCharacters));
		}
		longestLength = currentMax;
	}

	private int fromCurrentCharacter(String s, int firstCharacter, Set<Character> deadEndCharacters) {
		Set<Character> seen = new HashSet<>();
		seen.add(s.charAt(firstCharacter));
		int currentLength = 1;
		for (int i = firstCharacter + 1; i < s.length(); i++) {
			char nextCharacter = s.charAt(i);
			if (!seen.contains(nextCharacter) && !deadEndCharacters.contains(nextCharacter)) {
				currentLength += 1;
				seen.add(nextCharacter);
			} else {
				deadEndCharacters.add(nextCharacter);
				break;
			}
		}
		return currentLength;
	}

	public int lengthOfLongestSubstring() {
		return longestLength;
	}

}
