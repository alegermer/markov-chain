package com.germer.markov.web.model;

import com.germer.markov.chain.MarkovChainBuilder;
import com.germer.markov.chain.TokenStrategy;

public class AvailableParameters {

    private final String[]        tokenStrategies      = {"Word glued to punctuation", "Any single character"};
    private final TokenStrategy[] tokenStrategiesEnums = {TokenStrategy.WORD_GLUED_TO_PUNCTUATION, TokenStrategy.ANY_SINGLE_CHARACTER};
    private final int             defaultPrefixLen     = MarkovChainBuilder.DEFAULT_PREFIX_LENGTH;
    private final int             defaultMaxTokens     = 1000;

    private static AvailableParameters instance = new AvailableParameters ();

    private AvailableParameters () {}

    public static AvailableParameters getInstance () {
        return instance;
    }

    public String[] getTokenStrategies () {
        return this.tokenStrategies.clone ();
    }

    public int getDefaultPrefixLen () {
        return defaultPrefixLen;
    }

    public int getDefaultMaxTokens () {
        return defaultMaxTokens;
    }

    public TokenStrategy tokenStrategyByIndex (int index) {
        return tokenStrategiesEnums[index];
    }
}
