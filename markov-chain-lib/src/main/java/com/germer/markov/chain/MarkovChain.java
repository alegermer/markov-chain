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
	 * Inner State model class that represents a typical Markov Chain state
	 * node. Its only purpose is to provide a minimalistic model for the
	 * {@link MarkovChain#getStates} informational method, keeping
	 * implementation details of data structures used by the {@link MarkovChain}
	 * itself encapsulated.
	 * 
	 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
	 */
	public class State {
		// All tokens of the prefix.
		private List<String> prefix;
		// Each suffix token.
		private List<String> suffixes;
		// Weight related to each suffix token above.
		private List<Integer> suffixesWeights;

		/**
		 * State constructor used locally, populate the prefix, suffix and
		 * weights lists properly based on given parameters.
		 * 
		 * @param p the {@link Prefix} for the current state node.
		 * @param ws the {@link WeightedSuffixes} for the current state node.
		 */
		private State(Prefix p, WeightedSuffixes ws) {
			this.prefix = Arrays.asList(p.getTokens());
			this.suffixes = ws.getSuffixMap().entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
			this.suffixesWeights = ws.getSuffixMap().entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
		}

		/**
		 * Gets the Markov Chain node prefix as a list of tokens.
		 * 
		 * @return The list of tokens with same size as specified as prefix
		 *         length during build.
		 */
		public List<String> getPrefix() {
			return prefix;
		}

		/**
		 * Gets the Markov Chain node possible suffixes as a list of tokens
		 * where each entry represents a distinct suffix. Related probabilistic
		 * weights can be retrieved through {@link #getSuffixesWeights()} to get
		 * a weight list indexed correlated to the suffix index returned here.
		 * 
		 * @return the list of suffixes for the current prefix.
		 */
		public List<String> getSuffixes() {
			return suffixes;
		}

		/**
		 * Gets the probabilistic weights related to the suffixes, which
		 * correlated list can be retrieved through {@link #getSuffixes()}.
		 * 
		 * @return the list of weight where each entry is related to the one
		 *         with same index in the suffix list.
		 */
		public List<Integer> getSuffixesWeights() {
			return suffixesWeights;
		}
	}

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
		StringBuilder sbResult = new StringBuilder();
		
		// Start from a empty prefix of given length.
		Prefix curPrefix = new Prefix(prefixLength);

		int generatedLen = 0;
		for (; generatedLen < maxTokens; generatedLen++) {

			WeightedSuffixes suffixes = stateMap.get(curPrefix);
			if (suffixes != null) {
				// Get random suffix considering statistical weights.
				String suffix = suffixes.getRandom();
				
				// null suffix represents the final state.
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

	/**
	 * Gets the current {@link MarkovChain} state nodes as a list of
	 * minimalistic instances of {@link State} model. This list of states has
	 * informational purpose only and can be used to fetch details about the
	 * Markov Chain wrapped by the current instance.
	 * 
	 * @return the list of {@link State} representing the full Markov Chain
	 *         model hold by this instance.
	 */
	public List<State> getStates() {
		return stateMap.entrySet().stream().map(entry -> new State(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
}
