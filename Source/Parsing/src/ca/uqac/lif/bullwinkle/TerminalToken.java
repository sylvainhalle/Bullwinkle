/*
  Copyright 2014-2016 Sylvain Hallé
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

/**
 * Terminal token in the grammar.
 * @author Sylvain Hallé
 */
public class TerminalToken extends Token
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = -5734730721366371245L;

	/**
	 * Creates a new empty terminal token
	 */
	protected TerminalToken()
	{
		super();
	}

	/**
	 * Creates a new terminal token with a label
	 * @param label The label
	 */
	public TerminalToken(final String label)
	{
		super(label);
	}

	@Override
	public boolean matches(final Token tok)
	{
		if (tok == null)
		{
			return false;
		}
		if (s_caseSensitive)
		{
			return getName().compareTo(tok.getName()) == 0;
		}
		return getName().compareToIgnoreCase(tok.getName()) == 0;
	}

	@Override
	public int match(final String s)
	{
		String name = getName();
		if (s.length() < name.length())
		{
			return -1;
		}
		if (getName().compareTo(s.substring(0, getName().length())) == 0)
		{
			return getName().length();
		}
		return 0;
	}

	@Override
	public boolean equals(/* @Nullable */ Object o)
	{
		if (o == null)
		{
			return false;
		}
		if (!(o instanceof TerminalToken))
		{
			return false;
		}
		return getName().compareTo(((TerminalToken) o).getName()) == 0;
	}
	
	@Override
	public int hashCode()
	{
		// We call this explicitly, as overriding equals without
		// overriding hashCode is considered bad practice
		return super.hashCode();
	}
	
	
}