/* MIT License
 *
 * Copyright 2014-2021 Sylvain Hallé
 *
 * Laboratoire d'informatique formelle
 * Université du Québec à Chicoutimi, Canada
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package ca.uqac.lif.bullwinkle;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.bullwinkle.TerminalToken;
import ca.uqac.lif.bullwinkle.TokenString;

/**
 * Unit tests for {@link TokenString}.
 */
public class TokenStringTest
{
	protected static final NonTerminalToken A = new NonTerminalToken("A");
	protected static final NonTerminalToken B = new NonTerminalToken("B");
	protected static final NonTerminalToken C = new NonTerminalToken("C");
	protected static final TerminalToken foo = new StringTerminalToken("foo");
	protected static final TerminalToken bar = new StringTerminalToken("bar");
	protected static final TerminalToken baz = new StringTerminalToken("baz");
	
	@Test
	public void testMatch1()
	{
		TokenString ts1 = new TokenString(foo, B, bar);
		TokenString ts2 = new TokenString(A, foo, B, C, bar, baz);
		Integer[] offsets = TokenString.match(ts1, ts2);
		assertNotNull(offsets);
		assertEquals(3, offsets.length);
		assertEquals(1, offsets[0].intValue());
		assertEquals(2, offsets[1].intValue());
		assertEquals(4, offsets[2].intValue());
	}
	
	@Test
	public void testMatch2()
	{
		TokenString ts1 = new TokenString(foo, B, bar);
		TokenString ts2 = new TokenString(foo, foo, B, B, bar, baz);
		Integer[] offsets = TokenString.match(ts1, ts2);
		assertNotNull(offsets);
		assertEquals(3, offsets.length);
		assertEquals(0, offsets[0].intValue());
		assertEquals(2, offsets[1].intValue());
		assertEquals(4, offsets[2].intValue());
	}
	
	@Test
	public void testMatch3()
	{
		TokenString ts1 = new TokenString(foo, bar, B);
		TokenString ts2 = new TokenString(A, foo, B, C, bar, baz);
		Integer[] offsets = TokenString.match(ts1, ts2);
		assertNull(offsets);
	}
	
	@Test
	public void testMatch4()
	{
		TokenString ts1 = new TokenString(new EpsilonTerminalToken());
		TokenString ts2 = new TokenString(A, foo, B, C, bar, baz);
		Integer[] offsets = TokenString.match(ts1, ts2);
		assertNotNull(offsets);
		assertEquals(0, offsets.length);
	}
	
	@Test
	public void testMatch5()
	{
		TokenString ts1 = new TokenString(A, foo, B, C, bar, baz);
		Integer[] offsets = TokenString.match(ts1, ts1);
		assertNull(offsets);
	}
}
