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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * An ordered sequence of tokens
 * @author Sylvain Hallé
 */
public class TokenString extends LinkedList<Token>
{
	/**
	 * Dummy UID
	 */
	private static final transient long serialVersionUID = 1L;

	/**
	 * Whether this case symbol should remain at the end of the
	 * alternatives for a rule
	 */
	private boolean m_tryLast = false;

	/**
	 * Creates a new empty token string
	 */
	public TokenString()
	{
		super();
	}

	/**
	 * Tells whether this element should be tried last when parsing
	 * @return True if it should be tried last
	 */
	public boolean getTryLast()
	{
		return m_tryLast;
	}

	/**
	 * Tells whether this element should be tried last when parsing
	 * @param b Set to true if it should be tried last
	 */
	public void setTryLast(boolean b)
	{
		m_tryLast = b; 
	}

	/**
	 * Creates a copy of this token string
	 * @return The copy
	 */
	public final TokenString getCopy()
	{
		TokenString out = new TokenString();
		out.addAll(this);
		return out;
	}

	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (Token t: this)
		{
			if (!first)
				out.append(" ");
			first = false;
			out.append(t);
		}
		return out.toString();
	}

	/**
	 * Gets the set of all terminal tokens that appear in this string
	 * @return The set of tokens
	 */
	Set<TerminalToken> getTerminalTokens()
	{
		Set<TerminalToken> out = new HashSet<TerminalToken>();
		for (Token t : this)
		{
			if (t instanceof TerminalToken)
				out.add((TerminalToken) t);
		}
		return out;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof TokenString))
		{
			return false;
		}
		TokenString rt = (TokenString) o;
		if (rt.size() != size())
		{
			return false;
		}
		for (int i = 0; i < size(); i++)
		{
			Token t1 = get(i);
			Token t2 = rt.get(i);
			if (!t1.equals(t2))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return size();
	}
}
