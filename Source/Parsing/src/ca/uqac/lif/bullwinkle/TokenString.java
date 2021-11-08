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
	 * Creates a new token string.
	 * @param tokens An optional list of tokens to add to the string
	 */
	public TokenString(Token ... tokens)
	{
		super();
		for (Token t : tokens)
		{
			add(t);
		}
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
	
	/**
	 * Returns the index of an object in the string occurring after a given
	 * position.
	 * @param o The object to look for
	 * @param start_index The starting position where to look for
	 * @return The index of the object in the string, or -1 if the object
	 * could not be found after the starting position
	 */
	public int indexOf(Object o, int start_index)
	{
		for (int i = start_index; i < size(); i++)
		{
			if (get(i).equals(o))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Determines if a token string is a substring of another token string, and
	 * returns the relative positions of its elements if this is the case. For
	 * instance, given the token string {@code <A> foo <B> <C> bar baz}, the
	 * token string {@code foo <B> bar} is a substring, with its elements
	 * corresponding to the tokens at positions 1, 2, and 4 in the original.
	 * <p>
	 * Note that the method performs a <em>greedy</em> pairing of elements. This
	 * means that each token in the first string is matched with the token at
	 * the earliest possible position in the second string. For instance, the
	 * string {@code foo <B> bar} matched against {@code foo foo <B> <B> bar baz}
	 * will result in the pairing 0, 2, 4 (i.e. the first of the two possible
	 * "foo" is selected, as is the first of the two possible {@code <B>}).
	 * 
	 * @param ts1 The first token string
	 * @param ts2 The token string used as a reference
	 * @return A non-null array of integers if {@code ts1} is a sub-string of
	 * {@code ts2}
	 */
	/*@ null @*/ public static Integer[] match(/*@ non_null @*/ TokenString ts1, /*@ non_null @*/ TokenString ts2)
	{
		if (ts1.size() == 1 && ts1.get(0) instanceof EpsilonTerminalToken)
		{
			// ts1 is epsilon; return an empty integer array
			return new Integer[0];
		}
		if (ts1.size() >= ts2.size())
		{
			// ts1 cannot be a substring of ts2
			return null;
		}
		Integer[] offsets = new Integer[ts1.size()];
		int pos = 0;
		for (int i = 0; i < offsets.length; i++)
		{
			int index = ts2.indexOf(ts1.get(i), pos);
			if (index < 0)
			{
				// No match could be found
				return null;
			}
			offsets[i] = index;
			pos = index + 1;
		}
		return offsets;
	}
}
