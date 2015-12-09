package com.germer.markov.chain;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrefixTest {

	private static final String[] EXPECTED_P = {null, null};
	private static final String[] EXPECTED_PA = {null, "alpha"};
	private static final String[] EXPECTED_PB = {"alpha", "beta"};
	private static final String[] EXPECTED_PC = {"beta", "gamma"};
	private static final String[] EXPECTED_PD = {"gamma", "delta"};

	@Test
	public void testInstantiation() {
		Prefix p2 = new Prefix(2);
		Prefix p3 = new Prefix(3);

		assertEquals(2, p2.getTokens().length);
		assertNull(p2.getTokens()[0]);
		assertNull(p2.getTokens()[1]);

		assertEquals(3, p3.getTokens().length);
		assertNull(p3.getTokens()[0]);
		assertNull(p3.getTokens()[1]);
		assertNull(p3.getTokens()[2]);
	}

	@Test
	public void testNextPrefix() {
		Prefix p = new Prefix(2);
		Prefix pa = p.createNext("alpha");
		Prefix pb = pa.createNext("beta");
		Prefix pc = pb.createNext("gamma");
		Prefix pd = pc.createNext("delta");

		assertArrayEquals(EXPECTED_P, p.getTokens());
		assertArrayEquals(EXPECTED_PA, pa.getTokens());
		assertArrayEquals(EXPECTED_PB, pb.getTokens());
		assertArrayEquals(EXPECTED_PC, pc.getTokens());
		assertArrayEquals(EXPECTED_PD, pd.getTokens());
	}

	@Test
	public void testShiftLeft() {
		Prefix p = new Prefix(2);
		assertArrayEquals(EXPECTED_P, p.getTokens());
		
		p.shiftLeft("alpha");
		assertArrayEquals(EXPECTED_PA, p.getTokens());
		
		p.shiftLeft("beta");
		assertArrayEquals(EXPECTED_PB, p.getTokens());
		
		p.shiftLeft("gamma");
		assertArrayEquals(EXPECTED_PC, p.getTokens());
		
		p.shiftLeft("delta");
		assertArrayEquals(EXPECTED_PD, p.getTokens());
	}

}
