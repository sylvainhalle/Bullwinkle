/*
  Copyright 2014-2017 Sylvain Hallé
  Laboratoire d'informatique formelle
  Université du Québec à Chicoutimi, Canada

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
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
	private static final long serialVersionUID = 1L;

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
