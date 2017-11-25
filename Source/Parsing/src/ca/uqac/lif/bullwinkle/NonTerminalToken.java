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

public class NonTerminalToken extends Token
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = -6214246017840941018L;

	/**
	 * The left-hand side symbol used to mark a non-terminal token
	 * in a grammar rule
	 */
	public static final transient String s_leftSymbol = "<";

	/**
	 * The right-hand side symbol used to mark a non-terminal token
	 * in a grammar rule
	 */
	public static final transient String s_rightSymbol = ">";

	public NonTerminalToken()
	{
		super();
	}

	public NonTerminalToken(String s)
	{
		super(s);
	}

	@Override
	public boolean matches(final Token tok)
	{
		return false;
	}

	@Override
	public int match(final String s)
	{
		return 0;
	}
}
