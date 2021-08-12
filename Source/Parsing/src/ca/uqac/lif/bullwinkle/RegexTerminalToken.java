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
	private static final transient long serialVersionUID = -2430670680001437707L;
	
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
