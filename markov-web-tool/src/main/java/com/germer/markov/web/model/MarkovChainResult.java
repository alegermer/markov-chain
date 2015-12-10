package com.germer.markov.web.model;

import java.util.List;

import com.germer.markov.chain.MarkovChain;

/**
 * Response model of a Markov Chain transformation request. This model is meant
 * to be serialized as JSON by its public default getters.
 * 
 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
 */
public class MarkovChainResult {

	private final String content;
	private final List<MarkovChain.State> states;

	public MarkovChainResult(String content, List<MarkovChain.State> states) {
		this.content = content;
		this.states = states;
	}

	public String getContent() {
		return content;
	}

	public List<MarkovChain.State> getStates() {
		return states;
	}
}
