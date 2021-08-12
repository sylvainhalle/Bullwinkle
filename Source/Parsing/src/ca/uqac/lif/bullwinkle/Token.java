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

import java.io.Serializable;

/**
 * An abstract token of the grammar
 * @author Sylvain Hallé
 */
public abstract class Token implements Serializable
{
	/**
	 * Dummy UID
	 */
	private static final transient long serialVersionUID = 1L;

	/**
	 * The token's name
	 */
	private String m_name;

	/**
	 * Whether the matching is sensitive to case. This is a program-wide
	 * value
	 */
	protected static transient boolean s_caseSensitive = true;
	
	/**
	 * Creates a new empty token
	 */
	public Token()
	{
		this("");
	}

	/**
	 * Creates a token with a name
	 * @param name The name of the token
	 */
	public Token(String name)
	{
		super();
		setName(name);
	}

	/**
	 * Sets whether the matching for tokens is case sensitive. This is a
	 * system-wide setting.
	 * @param b {@code true} to make the matching case-sensitive,
	 * {@code false} otherwise
	 */
	public static void setCaseSensitive(boolean b)
	{
		s_caseSensitive = b;
	}

	/**
	 * Gets the name of this token
	 * @return The name
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * Sets the name of this token
	 * @param name The name
	 */
	void setName(final /*@Nullable*/ String name)
	{
		if (name != null)
		{
			m_name = name;
		}
	}

	@Override
	public String toString()
	{
		return m_name;
	}

	@Override
	public int hashCode()
	{
		return m_name.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Token))
		{
			return false;
		}
		return ((Token) o).getName().compareTo(m_name) == 0;
	}

	public abstract boolean matches(final Token tok);

	public abstract int match(final String s);
}
