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

/**
 * Terminal token represented by a string.
 * @author Sylvain Hallé
 */
public class StringTerminalToken extends TerminalToken
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = -8815329986927875183L;

	/**
	 * Creates a new empty string terminal token
	 */
	protected StringTerminalToken()
	{
		super();
	}

	/**
	 * Creates a new string terminal token
	 * @param label The string this token should match
	 */
	public StringTerminalToken(String label)
	{
		super(label);
	}

	@Override
	public boolean matches(final Token tok)
	{
		// Anything matches a string
		return tok != null;
	}

	@Override
	public int match(final String s)
	{   
		if (s == null)
		{
			return 0;
		}
		return s.indexOf(' ');
	}
}
