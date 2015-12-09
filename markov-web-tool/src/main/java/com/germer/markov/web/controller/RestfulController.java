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

@RestController
public class RestfulController {

	@RequestMapping(value = "/parameters", method = RequestMethod.GET)
	public AvailableParameters getAvailableParameters() {
		return AvailableParameters.getInstance();
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public MarkovChainResult handleFileUpload(@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "prefixLen", defaultValue = "2") Integer prefixLen,
			@RequestParam(value = "maxTokens", defaultValue = "1000") Integer maxTokens,
			@RequestParam(value = "tokenStrategy", defaultValue = "0") Integer tokenStrategy) {

		try {
			MarkovChain chain = new MarkovChainBuilder().setPrefixLength(prefixLen)
					.setTokenStrategy(AvailableParameters.getInstance().tokenStrategyByIndex(tokenStrategy))
					.build(file.getInputStream());

			return new MarkovChainResult(chain.generate(maxTokens), chain.getMachineStates());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
