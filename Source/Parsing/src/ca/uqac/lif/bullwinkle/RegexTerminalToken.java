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

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Terminal symbol in the grammar defined by a regular expression. 
 * @author Sylvain Hallé
 */
public class RegexTerminalToken extends TerminalToken
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = -2430670680001437707L;
	
	/**
	 * The pattern used to perform the matching
	 */
	private transient Pattern m_pattern;

	/**
	 * Creates a new empty terminal token
	 */
	protected RegexTerminalToken()
	{
		super();
	}

	/**
	 * Creates a new terminal token
	 * @param label The regular expression that matches this token
	 */
	public RegexTerminalToken(final String label)
	{
		super(label);
	}

	@Override
	public void setName(final String s)
	{
		super.setName(s);
		m_pattern = Pattern.compile(s);
	}

	@Override
	public boolean matches(final Token tok)
	{
		String contents = tok.getName();
		Matcher matcher = m_pattern.matcher(contents);
		return matcher.matches();
	}

	@Override
	public int match(final String s)
	{
		Matcher matcher = m_pattern.matcher(s);
		if (matcher.find())
		{
			return matcher.end();
		}
		return -1;
	}

	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append(getName());
		return out.toString();
	}

	/**
	 * Returns the content of each capture block in the regex,
	 * matched against the input string 
	 * @param s The input string
	 * @return A list of strings, each of which is the content
	 * of a capture block
	 */
	public List<String> getCaptureBlocks(final String s)
	{
		List<String> out = new LinkedList<String>();
		Matcher matcher = m_pattern.matcher(s);
		if (matcher.find())
		{
			for (int i = 1; i <= matcher.groupCount(); i++)
			{
				String group_match = matcher.group(i);
				out.add(group_match);
			}
		}
		return out;
	}
	
	@Override
	public int hashCode()
	{
		return m_pattern.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof RegexTerminalToken))
		{
			return false;
		}
		RegexTerminalToken rt = (RegexTerminalToken) o;
		return m_pattern.toString().compareTo(rt.m_pattern.toString()) == 0;
	}
}
