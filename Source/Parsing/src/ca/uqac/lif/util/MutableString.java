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
package ca.uqac.lif.util;

/**
 * A mutable String object. This object behaves in almost the same way as
 * a regular <tt>String</tt>, except that its contents can be changed.
 * For example, while method {@link String#replaceAll(String, String)} in
 * the class <tt>String</tt> creates a <em>new</em> string where the
 * replacements are made, method {@link #replaceAll(String, String)} in
 * <tt>MutableString</tt> modifies the current object.
 *  
 * @author Sylvain Hallé
 */
public class MutableString
{
	/**
	 * The underlying string
	 */
	protected String m_string;

	/**
	 * Creates a new empty mutable string
	 */
	public MutableString()
	{
		this("");
	}

	/**
	 * Creates a new mutable string from an existing string object
	 * @param s The string
	 */
	public MutableString(String s)
	{
		super();
		m_string = s;
	}

	/**
	 * Creates a new mutable string from another mutable string
	 * @param s The mutable string
	 */
	public MutableString(MutableString s)
	{
		this(s.toString());
	}

	/**
	 * Clears the contents of this mutable string
	 */
	public void clear()
	{
		m_string = "";
	}

	/**
	 * Returns the position of the first occurrence of a substring within
	 * the current string
	 * @see String#indexOf(String)
	 * @param s The string to look for
	 * @return The position
	 */
	public int indexOf(String s)
	{
		return m_string.indexOf(s);
	}

	/**
	 * Returns the position of the first occurrence of a substring within
	 * the current string, starting at some index
	 * @see String#indexOf(String, int)
	 * @param s The string to look for
	 * @param fromIndex The starting position
	 * @return The position
	 */
	public int indexOf(String s, int fromIndex)
	{
		return m_string.indexOf(s, fromIndex);
	}

	/**
	 * Trims a string of its leading and trailing whitespace characters
	 * @see String#trim()
	 */
	public void trim()
	{
		m_string = m_string.trim();
	}

	/**
	 * Gets the length of the string
	 * @see String#length()
	 * @return The length
	 */
	public int length()
	{
		return m_string.length();
	}

	/**
	 * Splits a mutable string into parts according to a separator expression
	 * @see String#split(String)
	 * @param regex The regex used to separate the string
	 * @return An array of mutable strings, one for each part
	 */
	public MutableString[] split(String regex)
	{
		String[] splitted = m_string.split(regex);
		MutableString[] out = new MutableString[splitted.length];
		for (int i = 0; i < splitted.length; i++)
		{
			out[i] = new MutableString(splitted[i]);
		}
		return out;
	}

	/**
	 * Truncates the current string. This effectively takes out a part of
	 * the current substring.
	 * @param begin The start position
	 * @param end The end position
	 * @return A new instance of the mutable string, keeping only the
	 * characters between <tt>begin</tt> and <tt>end</tt>. Note that this
	 * also <em>removes</em> this substring from the current object.
	 */
	public MutableString truncateSubstring(int begin, int end)
	{
		String out = m_string.substring(begin, end);
		m_string = m_string.substring(0, begin) + m_string.substring(end);
		return new MutableString(out);
	}

	/**
	 * Truncates the current substring
	 * @param begin The start position
	 * @return  A new instance of the mutable string, keeping only the
	 * characters between <tt>begin</tt> and the end of the string.
	 * Note that this
	 * also <em>removes</em> this substring from the current object.
	 */
	public MutableString truncateSubstring(int begin)
	{
		String out = m_string.substring(0, begin);
		m_string = m_string.substring(begin);
		return new MutableString(out);
	}

	/**
	 * Gets a substring of the current string. Contrary to
	 * {@link #truncateSubstring(int, int)}, this does not modify the current
	 * object. 
	 * @see String#substring(int, int)
	 * @param start The start position
	 * @param end The end position
	 * @return A new instance of the mutable string, keeping only the
	 * characters between <tt>begin</tt> and <tt>end</tt>.
	 */
	public MutableString substring(int start, int end)
	{
		return new MutableString(m_string.substring(start, end));
	}

	/**
	 * Gets a substring of the current string. Contrary to
	 * {@link #truncateSubstring(int)}, this does not modify the current
	 * object. 
	 * @see String#substring(int, int)
	 * @param start The start position
	 * @return A new instance of the mutable string, keeping only the
	 * characters between <tt>begin</tt> and the end of the string.
	 */
	public MutableString substring(int start)
	{
		return new MutableString(m_string.substring(start));
	}

	/**
	 * Checks if a string starts with another string
	 * @see String#startsWith(String)
	 * @param s The string to look for
	 * @return {@code true} if the current object starts with <tt>s</tt>,
	 * {@code false} otherwise
	 */
	public boolean startsWith(String s)
	{
		return m_string.startsWith(s);
	}

	/**
	 * Checks if a string starts with another string, ignoring upper/lowercase
	 * @param s The string to look for
	 * @return {@code true} if the current object starts with <tt>s</tt>,
	 * {@code false} otherwise
	 */
	public boolean startsWithIgnoreCase(String s)
	{
		return m_string.toLowerCase().startsWith(s.toLowerCase());
	}

	/**
	 * Checks if a string ends with another string
	 * @see String#endsWith(String)
	 * @param s The string to look for
	 * @return {@code true} if the current object ends with <tt>s</tt>,
	 * {@code false} otherwise
	 */
	public boolean endsWith(String s)
	{
		return m_string.endsWith(s);
	}

	/**
	 * Checks if a string ends with another string, ignoring upper/lowercase
	 * @param s The string to look for
	 * @return {@code true} if the current object ends with <tt>s</tt>,
	 * {@code false} otherwise
	 */
	public boolean endsWithIgnoreCase(String s)
	{
		return m_string.toLowerCase().endsWith(s.toLowerCase());
	}

	/**
	 * Checks if a string contains the same character sequence as this
	 * mutable string object 
	 * @see String#compareTo(String)
	 * @param s The string to compare to
	 * @return {@code true} if <tt>s</tt> and the current object contain the
	 * same character sequence, {@code false} otherwise
	 */
	public boolean is(String s)
	{
		return m_string.compareTo(s) == 0;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof MutableString))
		{
			return false;
		}
		return is(((MutableString) o).toString());
	}
	
	@Override
	public int hashCode()
	{
		return m_string.hashCode();
	}

	/**
	 * Checks if the current mutable string is empty
	 * @see String#isEmpty()
	 * @return {@code true} if the string is empty, {@code false} otherwise
	 */
	public boolean isEmpty()
	{
		return m_string.isEmpty();
	}

	/**
	 * Replaces all occurrences of a pattern by another string in the current
	 * mutable string object
	 * @see String#replaceAll(String, String)
	 * @param regex The pattern to look for
	 * @param replacement The replacement string
	 */
	public void replaceAll(String regex, String replacement)
	{
		m_string = m_string.replaceAll(regex, replacement);
	}

	@Override
	public String toString()
	{
		return m_string;
	}
}