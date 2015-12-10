package com.germer.markov.web.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.germer.markov.chain.MarkovChain;
import com.germer.markov.chain.MarkovChainBuilder;
import com.germer.markov.web.model.AvailableParameters;
import com.germer.markov.web.model.MarkovChainResult;

/**
 * RESTful API Controller for simple Markov Chain Algorithm text transformation.
 * 
 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
 */
@RestController
public class RestfulController {

	/**
	 * Provides some available parameters to be used in /transform call.
	 * 
	 * @return the current singleton {@link AvailableParameters} model.
	 */
	@RequestMapping(value = "/parameters", method = RequestMethod.GET)
	public AvailableParameters getAvailableParameters() {
		return AvailableParameters.getInstance();
	}

	/**
	 * Applies Markov Chain Algorithm text transformation to a submitted file,
	 * according to given parameters.
	 * 
	 * @param file the {@link MultipartFile} submitted in the POST.
	 * @param prefixLen the prefix length to be used (default 2).
	 * @param maxTokens the token limit for generation when the final state
	 *            isn't achieved before (default 1000).
	 * @param tokenStrategy the token strategy index according to the provided
	 *            through {@link AvailableParameters#getTokenStrategies}.
	 * @return the {@link MarkovChainResult} model.
	 */
	@RequestMapping(value = "/transform", method = RequestMethod.POST)
	public MarkovChainResult handleFileUpload(@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "prefixLen", defaultValue = "2") Integer prefixLen,
			@RequestParam(value = "maxTokens", defaultValue = "1000") Integer maxTokens,
			@RequestParam(value = "tokenStrategy", defaultValue = "0") Integer tokenStrategy) {

		try {
			MarkovChain chain = new MarkovChainBuilder().setPrefixLength(prefixLen)
					.setTokenStrategy(AvailableParameters.getInstance().tokenStrategyByIndex(tokenStrategy))
					.build(file.getInputStream());

			return new MarkovChainResult(chain.generate(maxTokens), chain.getStates());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
