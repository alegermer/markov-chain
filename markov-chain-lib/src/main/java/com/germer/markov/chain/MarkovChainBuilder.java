package com.germer.markov.chain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Builder for {@link MarkovChain} instances, offering parameterization of the
 * following behaviors:
 * <ul>
 * <li>Prefix length (number of tokens).</li>
 * <li>Source stream encoding Charset.</li>
 * <li>Token parsing strategy</li>
 * </ul>
 * <p>
 * If no different parameters are given the builder will use default ones.
 * </p>
 * 
 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
 */
public class MarkovChainBuilder {

	/**
	 * Default prefix length ({@value}) if no other specified through
	 * {@link #setPrefixLength}.
	 */
	public static final int DEFAULT_PREFIX_LENGTH = 2;
	/**
	 * Default token parsing strategy (
	 * {@link TokenStrategy#WORD_GLUED_TO_PUNCTUATION}) if no other specified
	 * through {@link #setTokenStrategy}.
	 */
	public static final TokenStrategy DEFAULT_TOKEN_STRATEGY = TokenStrategy.WORD_GLUED_TO_PUNCTUATION;
	/**
	 * Default encoding Charset (system's default) if no other specified through
	 * {@link #setTokenStrategy(TokenStrategy)}.
	 */
	public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

	private int prefixLength;
	private TokenStrategy tokenStrategy;
	private Charset sourceCharset;

	/**
	 * Default {@link MarkovChainBuilder} constructor initializing defaults (
	 * {@link #DEFAULT_PREFIX_LENGTH}, {@link #DEFAULT_TOKEN_STRATEGY},
	 * {@link #DEFAULT_CHARSET}). To build the instance of {@link MarkovChain}
	 * call any of the {@link #build} methods properly.
	 */
	public MarkovChainBuilder() {
		prefixLength = DEFAULT_PREFIX_LENGTH;
		tokenStrategy = DEFAULT_TOKEN_STRATEGY;
		sourceCharset = DEFAULT_CHARSET;
	}

	/**
	 * Sets a specific prefix length for Markov Chain FSM generation, default
	 * value is {@value #DEFAULT_PREFIX_LENGTH}.
	 * 
	 * @param length the prefix length of the {@link MarkovChain} to be built.
	 * @return this {@link MarkovChainBuilder} for method call chaining.
	 * 
	 * @throws IllegalArgumentException if the given length isn't greater then
	 *             0.
	 */
	public MarkovChainBuilder setPrefixLength(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("Prefix lenght must be greater than 0.");
		}
		this.prefixLength = length;

		return this;
	}

	/**
	 * Sets a specific {@link Charset} to be used when reading the
	 * {@link InputStream} during {@link MarkovChain} build. The default one is
	 * retrieved from the system environment.
	 * 
	 * @param charset the source {@link Charset} of the {@link MarkovChain} to
	 *            be built.
	 * @return this {@link MarkovChainBuilder} for method call chaining.
	 * 
	 * @throws NullPointerException if the given charset is null.
	 */
	public MarkovChainBuilder setSourceCharset(Charset charset) {
		if (charset == null) {
			throw new NullPointerException("The specified Charset must be not null.");
		}
		this.sourceCharset = charset;

		return this;
	}

	/**
	 * Sets the token parsing strategy to be used when consuming the
	 * {@link InputStream} during {@link MarkovChain} build. The default one is
	 * {@link TokenStrategy#WORD_GLUED_TO_PUNCTUATION}.
	 * 
	 * @param strategy the desired {@link TokenStrategy} to be used by the
	 *            parser.
	 * @return this {@link MarkovChainBuilder} for method call chaining.
	 * 
	 * @throws NullPointerException if the given strategy is null.
	 */
	public MarkovChainBuilder setTokenStrategy(TokenStrategy strategy) {
		if (strategy == null) {
			throw new NullPointerException("The specified TokenStrategy must be not null.");
		}
		this.tokenStrategy = strategy;

		return this;
	}

	/**
	 * Builds a {@link MarkovChain} instance according to previously defined
	 * parameters (or default ones) from a given text String.
	 * 
	 * @param s the source text to be parsed.
	 * @return the configured, ready-to-be-used {@link MarkovChain} instance.
	 * 
	 * @throws NullPointerException if the given String is null.
	 */
	public MarkovChain build(String s) {
		if (s == null) {
			throw new NullPointerException("The specified String must be not null.");
		}
		return this.build(new ByteArrayInputStream(s.getBytes()));
	}

	/**
	 * Builds a {@link MarkovChain} instance according to previously defined
	 * parameters (or default ones) from a given {@link InputStream}.
	 * 
	 * @param is the source {@link InputStream} aimed to the text to be parsed.
	 * @return the configured, ready-to-be-used {@link MarkovChain} instance.
	 * 
	 * @throws NullPointerException if the given stream is null.
	 */
	public MarkovChain build(InputStream is) {
		if (is == null) {
			throw new NullPointerException("The specified InputStream must be not null.");
		}

		// The state map that will be core of the target MarkovChain.
		Map<Prefix, WeightedSuffixes> stateMap = new LinkedHashMap<>();
		// The current prefix auxiliary for the parser.
		Prefix curPrefix = new Prefix(prefixLength);

		/*
		 * Tokenize the stream using a Scanner and a regex pattern, here the
		 * decision to use Scanner instead of a StreamTokenizer was made in
		 * order to make the algorithm compatible with non-western charsets
		 * (UTF-8, UTF-16).
		 */
		try (Scanner s = new Scanner(is, sourceCharset.name())) {
			while (s.findWithinHorizon(tokenStrategy.getPattern(), 0) != null) {
				curPrefix = consumeToken(stateMap, curPrefix, s.match().group(1));
			}
		}

		curPrefix = consumeToken(stateMap, curPrefix, null);

		return new MarkovChain(prefixLength, tokenStrategy.getSeparator(), stateMap);
	}

	private Prefix consumeToken(Map<Prefix, WeightedSuffixes> stateMap, Prefix curPrefix, String token) {
		/*
		 * Checks if the prefix needs to be added or if it already exists in the
		 * stateMap and bind the received suffix token to it (WeightedSuffix
		 * structure will handle repeated ones properly).
		 */
		WeightedSuffixes suffixes = stateMap.get(curPrefix);

		if (suffixes == null) {
			suffixes = new WeightedSuffixes();
			stateMap.put(curPrefix, suffixes);
		}

		suffixes.add(token);

		return curPrefix.createNext(token);
	}

}
