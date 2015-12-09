package com.germer.markov.chain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MarkovChainTest {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testClosedUniqueChain() {
		WeightedSuffixes wsA = Mockito.mock(WeightedSuffixes.class);
		WeightedSuffixes wsB = Mockito.mock(WeightedSuffixes.class);
		WeightedSuffixes wsC = Mockito.mock(WeightedSuffixes.class);
		WeightedSuffixes wsD = Mockito.mock(WeightedSuffixes.class);
		WeightedSuffixes wsEnd = Mockito.mock(WeightedSuffixes.class);

		when(wsA.getRandom()).thenReturn("alpha");
		when(wsB.getRandom()).thenReturn("beta");
		when(wsC.getRandom()).thenReturn("gamma");
		when(wsD.getRandom()).thenReturn("delta");
		when(wsEnd.getRandom()).thenReturn(null);

		Prefix p = new Prefix(2);
		Prefix pA = p.createNext("alpha");
		Prefix pB = pA.createNext("beta");
		Prefix pC = pB.createNext("gamma");
		Prefix pD = pC.createNext("delta");

		Map<Prefix, WeightedSuffixes> map = new LinkedHashMap<>();

		map.put(p, wsA);
		map.put(pA, wsB);
		map.put(pB, wsC);
		map.put(pC, wsD);
		map.put(pD, wsEnd);

		MarkovChain chain = new MarkovChain(2, " ", map);

		String result = chain.generate(10);

		assertEquals("alpha beta gamma delta", result);
	}

	@Test
	public void testCyclicChain() {
		WeightedSuffixes wsA = Mockito.mock(WeightedSuffixes.class);
		WeightedSuffixes wsB = Mockito.mock(WeightedSuffixes.class);
		WeightedSuffixes wsC = Mockito.mock(WeightedSuffixes.class);
		WeightedSuffixes wsD = Mockito.mock(WeightedSuffixes.class);

		when(wsA.getRandom()).thenReturn("alpha");
		when(wsB.getRandom()).thenReturn("beta");
		when(wsC.getRandom()).thenReturn("alpha");
		when(wsD.getRandom()).thenReturn("beta");

		Prefix p = new Prefix(2);
		Prefix pA = p.createNext("alpha");
		Prefix pB = pA.createNext("beta");
		Prefix pC = pB.createNext("alpha");

		Map<Prefix, WeightedSuffixes> map = new LinkedHashMap<>();

		map.put(p, wsA);
		map.put(pA, wsB);
		map.put(pB, wsC);
		map.put(pC, wsD);

		MarkovChain chain = new MarkovChain(2, " ", map);

		String result = chain.generate(20);

		assertEquals("alpha beta alpha beta alpha beta alpha "
				+ "beta alpha beta alpha beta alpha beta alpha "
				+ "beta alpha beta alpha beta", result);
	}

}
