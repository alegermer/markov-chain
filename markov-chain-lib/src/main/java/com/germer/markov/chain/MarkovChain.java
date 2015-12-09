package com.germer.markov.chain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Markov Chain Finite State Machine representation ready to generate
 * pseudo-random natural language text according to parameters set and source
 * text specified during its build. Instances of this class can only be created
 * through the {@link MarkovChainBuilder}.
 * 
 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
 */
public class MarkovChain {

	// The state map representing current FSM.
	private Map<Prefix, WeightedSuffixes> stateMap;
	// The prefix length used during the stateMap built.
	private int prefixLength;
	// The expected token-separator when generating pseudo-random text.
	private String separator;

	/**
	 * Package-private constructor, instances should be created through
	 * {@link MarkovChainBuilder}.
	 * 
	 * @param prefixLength the prefix length used during the FSM build.
	 * @param separator the expected token-separator when generating
	 *            pseudo-random text.
	 * @param stateMap the Map representation of the Markov Chain FSM.
	 */
	MarkovChain(int prefixLength, String separator, Map<Prefix, WeightedSuffixes> stateMap) {
		this.prefixLength = prefixLength;
		this.separator = separator;
		this.stateMap = stateMap;
	}

	/**
	 * Generates pseudo-random text from the Markov Chain FSM represented by
	 * this current instance.
	 * 
	 * @param maxTokens the token size limit for the result generated text (in
	 *            case of FSM final state isn't achieved before this limit).
	 * @return The pseudo-random generated text.
	 */
	public String generate(int maxTokens) {
		Prefix curPrefix = new Prefix(prefixLength);
		StringBuilder sbResult = new StringBuilder();

		int generatedLen = 0;
		for (; generatedLen < maxTokens; generatedLen++) {

			WeightedSuffixes suffixes = stateMap.get(curPrefix);
			if (suffixes != null) {
				String suffix = suffixes.getRandom();
				if (suffix != null) {
					if (separator != null && generatedLen != 0) {
						sbResult.append(separator);
					}
					sbResult.append(suffix);
					curPrefix.shiftLeft(suffix);
					continue;
				}
			}
			break;
		}

		return sbResult.toString();
	}

	public class State {
		List<String> prefix;
		List<String> suffixes;
		List<Integer> suffixesWeights;

		State(Prefix p, WeightedSuffixes ws) {
			this.prefix = Arrays.asList(p.getTokens());
			this.suffixes = ws.suffixMap.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList());
			this.suffixesWeights = ws.suffixMap.entrySet().stream().map(entry -> entry.getValue())
					.collect(Collectors.toList());
		}

		public List<String> getPrefix() {
			return prefix;
		}

		public List<String> getSuffixes() {
			return suffixes;
		}

		public List<Integer> getSuffixesWeights() {
			return suffixesWeights;
		}
	}

	public List<State> getMachineStates() {
		return stateMap.entrySet().stream().map(entry -> new State(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
}
