package com.germer.markov.chain;

import static java.util.Objects.deepEquals;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Markov Chain prefix data-structure used internally in the package. This class
 * is basically a wrapper for a string-token array representing the prefix,
 * offering some helper methods and required structure to be used by
 * Collections.
 * 
 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
 */
class Prefix {

	// prefix is nothing more than a array of tokens.
	private String[] tokens;

	/**
	 * Constructs a {@link Prefix} instance for a given length with null tokens
	 * (non-word representation).
	 * 
	 * @param prefixLength the length of the new prefix.
	 */
	Prefix(int prefixLength) {
		tokens = new String[prefixLength];
	}

	/**
	 * Creates a new {@link Prefix} from the current instance as a left-shift of
	 * the current prefix tokens array, dropping the left-most token and adding
	 * the given token as right-most. The current instance remains untouched.
	 * 
	 * @param token the token to be assumed as the right-most in the new
	 *            {@link Prefix} created.
	 * @return the new {@link Prefix} instance.
	 */
	Prefix createNext(String token) {
		int len = tokens.length;
		Prefix next = new Prefix(len);
		System.arraycopy(tokens, 1, next.tokens, 0, len - 1);
		next.tokens[len - 1] = token;
		return next;
	};

	/**
	 * Shifts left the current prefix tokens array, dropping the left-most token
	 * and adding the given token as right-most, keeping the array length
	 * unchanged.
	 * 
	 * @param token the token to be put as the new right-most.
	 */
	void shiftLeft(String token) {
		System.arraycopy(tokens, 1, tokens, 0, tokens.length - 1);
		this.tokens[tokens.length - 1] = token;
	}

	/**
	 * Gets a copy of the token-array wrapped by this {@link Prefix} instance.
	 * 
	 * @return the copy of the token-array representing this prefix.
	 */
	String[] getTokens() {
		return tokens.clone();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Prefix && deepEquals(this.tokens, ((Prefix) o).tokens));
	}

	@Override
	public int hashCode() {
		return ArrayUtils.hashCode(tokens);
	}

	@Override
	public String toString() {
		return String.join(" ", this.tokens);
	}
}