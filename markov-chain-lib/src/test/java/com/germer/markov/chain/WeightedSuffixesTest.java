package com.germer.markov.chain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class WeightedSuffixesTest {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMockingRandomGeneration() {
		WeightedSuffixes ws = Mockito.spy(new WeightedSuffixes());

		// The returnVals array represent the mocked Random index generation
		// bound to the boundaries of each token.
		Answer<Integer> answer = new Answer<Integer>() {

			int count = 0;
			int[] returnVals = {0, 3, 4, 6, 7, 8, 9};

			@Override
			public Integer answer(InvocationOnMock invocation)
					throws Throwable {
				return returnVals[count++];
			}
		};
		doAnswer(answer).when(ws).generateRandomIndex();

		// Adds 4 x alpha, 3 x beta, 2 x gamma, 1 x delta
		ws.add("alpha");
		ws.add("beta");
		ws.add("gamma");
		ws.add("delta");
		ws.add("alpha");
		ws.add("beta");
		ws.add("gamma");
		ws.add("alpha");
		ws.add("beta");
		ws.add("alpha");

		assertEquals(10, ws.getTotalWeight());
		assertEquals("alpha", ws.getRandom());
		assertEquals("alpha", ws.getRandom());
		assertEquals("beta", ws.getRandom());
		assertEquals("beta", ws.getRandom());
		assertEquals("gamma", ws.getRandom());
		assertEquals("gamma", ws.getRandom());
		assertEquals("delta", ws.getRandom());

	}

}
