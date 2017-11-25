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
	private static final long serialVersionUID = 1L;

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
