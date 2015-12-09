package com.germer.markov.chain;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

class WeightedSuffixes {

	LinkedHashMap<String, Integer> suffixMap = new LinkedHashMap<>();
	String firstSuffix = null;
	int totalWeight = 0;

	/**
	 * The pseudo-random generator used by instances to select suffixes among
	 * those present in the suffixMap. This is not a security-sensitive random
	 * solution and neither presents great efficiency in multi-threaded
	 * environments. If anything more cryptographic secure or thread-safe
	 * robustness is required this can be change to better alternatives.
	 */
	private static final Random RANDOM = new Random();

	public void add(String suffix) {
		// Add (or replace) suffix to the map incrementing its weight.
		Integer weight = suffixMap.getOrDefault(suffix, 0) + 1;
		suffixMap.put(suffix, weight);
		totalWeight++;

		/*
		 * Store first suffix added in a dedicated attribute for performance
		 * optimization in case of single option.
		 */
		if (firstSuffix == null) {
			firstSuffix = suffix;
		}
	}

	/**
	 * Generates a random index bounded to the current suffixes total weight.
	 * The main purpose of this abstraction is to leave random number generation
	 * implementation details isolated and make easier testing and simulating
	 * results when needed.
	 * 
	 * @return a random index bound to the current {@link #totalWeight} (from 0
	 *         to totalWeight - 1).
	 */
	int generateRandomIndex() {
		return RANDOM.nextInt(totalWeight);
	}

	public String getRandom() {

		String randomSuffix = null;

		if (suffixMap.size() == 1) {
			randomSuffix = firstSuffix;
		} else if (suffixMap.size() > 1) {
			int randomIndex = generateRandomIndex();

			for (Entry<String, Integer> suffix : suffixMap.entrySet()) {
				// subtracting the suffix weight from the generated index will
				// specify that we have the expected suffix to be returned when
				// the result goes negative.
				randomIndex -= suffix.getValue();

				if (randomIndex < 0) {
					randomSuffix = suffix.getKey();
					break;
				}
			}
		}

		return randomSuffix;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer> suffix : suffixMap.entrySet()) {
			sb.append(suffix.getValue()).append("x ").append(suffix.getKey())
					.append(" ");
		}
		return sb.toString();
	}
}