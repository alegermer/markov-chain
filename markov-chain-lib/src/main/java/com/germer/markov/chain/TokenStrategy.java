package com.germer.markov.chain;

import java.util.regex.Pattern;

/**
 * Possible token-parsing strategies to be configured through
 * {@link MarkovChainBuilder#setTokenStrategy} when building a
 * {@link MarkovChain} instance. Different strategies will affect the way how
 * the source text will be parsed in a Markov Chain FSM.
 * 
 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
 */
public enum TokenStrategy {
	/**
	 * Source text will be parsed as words and any non-space character will be
	 * considered valid to compose those words (punctuation inclusive). All
	 * space characters will be skipped.
	 * 
	 * <p>
	 * <b>Example:</b>
	 * <code>"This is my source, parse it!" = ["This","is","my","source,","parse","it!"]</code>
	 * </p>
	 */
	WORD_GLUED_TO_PUNCTUATION("\\s*([^\\s]+)", " "),

	/**
	 * Source text will be parsed as if each character (including spaces and
	 * punctuation) is a token.
	 * 
	 * <p>
	 * <b>Example:</b>
	 * <code>"Let's coin words?" = ["L","e","t","'","s"," ", "c","o","i","n"," ","w","o","r","d","s","?"]</code>
	 * </p>
	 */
	ANY_SINGLE_CHARACTER("(?s)(.)", null);

	private final Pattern pattern;

	private final String separator;

	private TokenStrategy(String regex, String separator) {
		this.pattern = Pattern.compile(regex);
		this.separator = separator;
	}

	/**
	 * Gets the {@link Pattern} related to the current strategy. This pattern
	 * will always have its capture-group 1 as a valid token and is already
	 * responsible to skip any needed character until the next valid token.
	 * 
	 * @return the {@link Pattern} instance where capture-group 1 represents a
	 *         token.
	 */
	protected Pattern getPattern() {
		return pattern;
	}

	/**
	 * Gets the String to be used as separator of tokens in a target generated
	 * text through Markov Chain Algorithm.
	 * 
	 * @return the String value to be used as separator.
	 */
	protected String getSeparator() {
		return separator;
	}
}