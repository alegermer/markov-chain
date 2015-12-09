package com.germer.markov.chain;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

public class MarkovChainBuilderTest {

	@Test(expected = IllegalArgumentException.class)
	public void testZeroPrefixLength() {
		new MarkovChainBuilder().setPrefixLength(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNegativePrefixLength() {
		new MarkovChainBuilder().setPrefixLength(-1);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullCharset() {
		new MarkovChainBuilder().setSourceCharset(null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullTokenStrategy() {
		new MarkovChainBuilder().setTokenStrategy(null);
	}
	
	
	@Test(expected = NullPointerException.class)
	public void testNullInputStream(){
		new MarkovChainBuilder().build((InputStream) null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullInputString(){
		new MarkovChainBuilder().build((String) null);
	}
	
	@Test
	public void testEmptyInputString() {
		MarkovChain chain = new MarkovChainBuilder().build("");
		
		String result = chain.generate(1000);
		
		assertEquals("",result);
	}
	
	@Test
	public void testFewWordsRequested(){
		MarkovChain chain = new MarkovChainBuilder().build("this is just a sample");
		
		String resultNeg = chain.generate(-1);
		String result0 = chain.generate(0);
		String result1 = chain.generate(1);
		String result2 = chain.generate(2);
		
		assertEquals("", resultNeg);
		assertEquals("", result0);
		assertEquals("this", result1);
		assertEquals("this is", result2);
	}
	
	@Test
	public void testHugePrefixLength(){
		InputStream is = this.getClass()
				.getResourceAsStream("/the-beatitudes.txt");
		
		MarkovChain chain = new MarkovChainBuilder().setPrefixLength(1000).build(is);
		
		String result = chain.generate(1000);
		
		assertEquals("Blessed are the poor in spirit, for theirs is the kingdom "
				+ "of heaven. Blessed are those who mourn, for they will be "
				+ "comforted. Blessed are the meek, for they will inherit the "
				+ "earth. Blessed are those who hunger and thirst for "
				+ "righteousness, for they will be filled. Blessed are the "
				+ "merciful, for they will be shown mercy. Blessed are the "
				+ "pure in heart, for they will see God. Blessed are the "
				+ "peacemakers, for they will be called sons of God.", result);
	}

	@Test
	public void testUTF8source() {
		InputStream is = this.getClass()
				.getResourceAsStream("/sample-russian.txt");

		MarkovChain chain = new MarkovChainBuilder()
				.setSourceCharset(Charset.forName("UTF-8")).build(is);

		String result = chain.generate(1000);
		
		assertEquals("На берегу пустынных волн Стоял он, дум великих полн, "
				+ "И вдаль глядел. Пред ним широко Река неслася; бедный чёлн По "
				+ "ней стремился одиноко. По мшистым, топким берегам Чернели избы "
				+ "здесь и там, Приют убогого чухонца; И лес, неведомый лучам В "
				+ "тумане спрятанного солнца, Кругом шумел.", result);
	}

}
