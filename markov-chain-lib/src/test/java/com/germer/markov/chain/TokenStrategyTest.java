package com.germer.markov.chain;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.junit.Test;

public class TokenStrategyTest {

	private static final String TARGET_SOURCE = "This is my source, parse it!";
	private static final String[] EXPECTED_WORD_GLUED_PUNC = { "This", "is", "my", "source,", "parse", "it!" };
	private static final String[] EXPECTED_ANY_SINGLE_CHAR = { "T", "h", "i", "s", " ", "i", "s", " ", "m", "y", " ",
			"s", "o", "u", "r", "c", "e", ",", " ", "p", "a", "r", "s", "e", " ", "i", "t", "!" };

	private String[] parse(Pattern p, String target) {
		List<String> tokens = new ArrayList<>();

		try (Scanner s = new Scanner(target)) {
			while (s.findWithinHorizon(p, 0) != null) {
				tokens.add(s.match().group(1));
			}
		}

		return tokens.toArray(new String[0]);
	}

	@Test
	public void testWordGluedToPunctuationPattern() {
		Pattern p = TokenStrategy.WORD_GLUED_TO_PUNCTUATION.getPattern();
		assertArrayEquals(EXPECTED_WORD_GLUED_PUNC, parse(p, TARGET_SOURCE));
	}

	@Test
	public void testAnySingleCharacterPattern() {
		Pattern p = TokenStrategy.ANY_SINGLE_CHARACTER.getPattern();
		assertArrayEquals(EXPECTED_ANY_SINGLE_CHAR, parse(p, TARGET_SOURCE));
	}

}
