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

/**
 * Terminal token in the grammar.
 * @author Sylvain Hallé
 */
public class TerminalToken extends Token
{
	/**
	 * Dummy UID
	 */
	private static final transient long serialVersionUID = -5734730721366371245L;

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