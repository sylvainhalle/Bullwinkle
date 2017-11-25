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
 * Terminal token matching the empty string
 * @author Sylvain Hallé
 */
public class EpsilonTerminalToken extends TerminalToken
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = -7191896043358186569L;
	
	/**
	 * A single static instance of this object
	 */
	public static final transient EpsilonTerminalToken instance = new EpsilonTerminalToken();
	
	protected EpsilonTerminalToken()
	{
		super("ε");
	}

	@Override
	public boolean matches(Token tok)
	{
		return tok instanceof EpsilonTerminalToken;
	}

	@Override
	public int match(String s)
	{
		return 0;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
