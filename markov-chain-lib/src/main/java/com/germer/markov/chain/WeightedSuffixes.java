package com.germer.markov.chain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Markov Chain suffix data-structure used internally in the package. This class
 * is basically a wrapper of a Suffix (String token) to Weight (Number of
 * occurrences) Map. Suffixes must be added through {@link #add} method
 * (repeated values will be merged for a best memory usage) and a pseudo-random
 * random suffix selection is provided by {@link #getRandom} method.
 * 
 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
 */
class WeightedSuffixes {

	// suffix -> weight map keeping track of order for informational purposes
	private Map<String, Integer> suffixMap = new LinkedHashMap<>();
	// first suffix cached for performance reasons.
	private String firstSuffix = null;
	// total weight (count of amount of suffix added).
	private int totalWeight = 0;

	/**
	 * The pseudo-random generator used by instances to select suffixes among
	 * those present in the suffixMap. This is not a security-sensitive random
	 * solution and neither presents great efficiency in multi-threaded
	 * environments. If anything more cryptographic secure or thread-safe
	 * robustness is required this can be change to better alternatives.
	 */
	private static final Random RANDOM = new Random();

	/**
	 * Adds a new suffix to the collection, analyzing if its a repeated
	 * occurrence to keep statistical weight data in a memory-usage optimized
	 * way. The added suffixes can be after retrieved randomly through
	 * {@link #getRandom} method.
	 * 
	 * @param suffix the suffix to be added as a String-token.
	 */
	public void add(String suffix) {
		// Add (or replace) suffix to the map incrementing its weight.
		Integer weight = getSuffixMap().getOrDefault(suffix, 0) + 1;
		getSuffixMap().put(suffix, weight);
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
		return RANDOM.nextInt(getTotalWeight());
	}

	/**
	 * Gets one of the suffixes hold by this instance using pseudo-random
	 * probabilistic weighted by the current weights (statistics for occurrences
	 * collected during {@link #add} method calls).
	 * 
	 * @return a pseudo-randomly selected suffix properly considering
	 *         statistical likelihood weights.
	 */
	public String getRandom() {

		String randomSuffix = null;

		if (getSuffixMap().size() == 1) {
			randomSuffix = firstSuffix;
		} else if (getSuffixMap().size() > 1) {
			int randomIndex = generateRandomIndex();

			for (Entry<String, Integer> suffix : getSuffixMap().entrySet()) {
				/*
				 * Subtracting the suffix weight from the generated index will
				 * specify that we have the expected suffix to be returned when
				 * the result goes negative.
				 */
				randomIndex -= suffix.getValue();

				if (randomIndex < 0) {
					randomSuffix = suffix.getKey();
					break;
				}
			}
		}

		return randomSuffix;
	}

	/**
	 * Gets the suffix map wrapped by this {@link WeightedSuffixes} instance.
	 * Basically a map of suffix (String) to its weights (Integer).
	 * 
	 * @return the suffix map instance hold by this instance.
	 */
	Map<String, Integer> getSuffixMap() {
		return suffixMap;
	}

	/**
	 * Gets the total weight hold by this {@link WeightedSuffixes} instance.
	 * Basically this would be the total sum of all the weights related to the
	 * suffixes in this scope.
	 * 
	 * @return the total weight hold by this instance.
	 */
	int getTotalWeight() {
		return totalWeight;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer> suffix : getSuffixMap().entrySet()) {
			sb.append(suffix.getValue()).append("x ").append(suffix.getKey()).append(" ");
		}
		return sb.toString();
	}
}