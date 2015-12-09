package com.germer.markov.chain;

import static java.util.Objects.deepEquals;

import org.apache.commons.lang3.ArrayUtils;

class Prefix {

	private String[] tokens;

	Prefix(int prefixLength) {
		tokens = new String[prefixLength];
	}

	Prefix createNext(String token) {
		int len = tokens.length;
		Prefix next = new Prefix(len);
		System.arraycopy(tokens, 1, next.tokens, 0, len - 1);
		next.tokens[len - 1] = token;
		return next;
	};

	void shiftLeft(String token) {
		System.arraycopy(tokens, 1, tokens, 0, tokens.length - 1);
		this.tokens[tokens.length - 1] = token;
	}
	
	String[] getTokens(){
		return tokens.clone();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Prefix
				&& deepEquals(this.tokens, ((Prefix) o).tokens));
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