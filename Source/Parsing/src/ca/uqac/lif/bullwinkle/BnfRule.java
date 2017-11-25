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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.uqac.lif.util.EmptyException;

/**
 * Implementation of a single rule of a BNF grammar
 */
public class BnfRule implements Serializable
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 579372107761975753L;

	/**
	 * A list of token strings that for all the possible cases of that rule
	 */
	private List<TokenString> m_alternatives;

	/**
	 * The left-hand side of the rule. Since we deal with BNF grammars, this
	 * left-hand side must be a single non-terminal symbol.
	 */
	private NonTerminalToken m_leftHandSide;

	/**
	 * Creates a new empty BNF rule
	 */
	BnfRule()
	{
		super();
		m_alternatives = new ArrayList<TokenString>();
	}

	/**
	 * Creates a BNF rule out of a string
	 * @param input The string that contains a BNF rule. This string must follow
	 *   the syntactical restrictions described in the README
	 * @return A BNF rule if the parsing succeeded
	 * @throws BnfRule.InvalidRuleException Thrown if the parsing could not be
	 *   done correctly
	 */
	public static BnfRule parseRule(String input) throws BnfRule.InvalidRuleException
	{
		BnfRule out = new BnfRule();
		String[] lr = input.split("\\s*:=\\s*");
		if (lr.length != 2)
		{
			throw new InvalidRuleException("Cannot find left- and right-hand side of BNF rule");
		}
		assert lr.length == 2;
		String lhs = lr[0].trim();
		out.setLeftHandSide(new NonTerminalToken(lhs));
		if (lr[1].startsWith("^"))
		{
			// This is a regex line
			String regex = unescapeString(lr[1]);
			// Remove semicolon
			TokenString alternative_to_add = new TokenString();
			Token to_add = new RegexTerminalToken(regex);
			alternative_to_add.add(to_add);
			out.addAlternative(alternative_to_add);
		}
		else
		{
			// Anything but a regex line
			String[] parts = lr[1].split("\\s+\\|\\|\\s+");
			String[] normal_alternatives = parts[0].split("\\s+\\|\\s+");
			String[] sticky_alternatives = new String[0];
			if (parts.length > 1)
			{
				sticky_alternatives = parts[1].split("\\s+\\|\\s+");
			}
			if (normal_alternatives.length == 0 && sticky_alternatives.length == 0)
			{
				throw new InvalidRuleException("Right-hand side of BNF rule is empty");
			}
			processAlternatives(out, normal_alternatives, false);
			processAlternatives(out, sticky_alternatives, true);
		}
		return out;
	}
	
	private static void processAlternatives(BnfRule out, String[] alternatives, boolean sticky) throws BnfRule.InvalidRuleException
	{
		for (String alt : alternatives)
		{
			TokenString alternative_to_add = new TokenString();
			alternative_to_add.setTryLast(sticky);
			String[] words = alt.split(" ");
			if (words.length == 0)
			{
				throw new InvalidRuleException("Alternative of BNF rule is empty");
			}
			assert words.length > 0;
			for (String word : words)
			{
				String trimmed_word = word.trim();
				if (trimmed_word.contains("<") && !trimmed_word.startsWith("<"))
				{
					throw new InvalidRuleException("The expression '" + 
							trimmed_word + "' contains tokens that are not separated by spaces");
				}
				if (trimmed_word.startsWith("<"))
				{
					// This is a non-terminal symbol
					Token to_add = new NonTerminalToken(trimmed_word);
					alternative_to_add.add(to_add);
				}
				else if (trimmed_word.compareTo("\uCEB5") == 0 || trimmed_word.compareTo("\u03B5") == 0)
				{
					// There are two "lowercase epsilon" code points in Unicode; check for both
					Token to_add = new EpsilonTerminalToken();
					alternative_to_add.add(to_add);
				}
				else
				{
					if (trimmed_word.isEmpty())
					{
						throw new InvalidRuleException("Trying to create an empty terminal token"); 
					}
					// This is a literal token
					trimmed_word = unescapeString(trimmed_word);
					alternative_to_add.add(new TerminalToken(trimmed_word));
				}
			}
			out.addAlternative(alternative_to_add);
		}

	}

	/**
	 * Sets the left-hand side of the rule
	 * @param t The non-terminal token that will be used for the
	 *   left-hand side of the rule
	 */
	void setLeftHandSide(final NonTerminalToken t)
	{
		m_leftHandSide = t;
	}

	/**
	 * Adds an alternative to the rule
	 * @param ts The alternative to add
	 */
	void addAlternative(/* @NonNull */ final TokenString ts)
	{
		m_alternatives.add(ts);
	}

	/**
	 * Adds an alternative to the rule, and puts it in a specific position
	 * @param index The position to put the new alternative
	 * @param ts The alternative to add
	 */
	void addAlternative(int index, /* @NonNull */ final TokenString ts)
	{
		m_alternatives.add(index, ts);
	}

	/**
	 * Retrieves the list of all the alternatives that this rule defines
	 * @return A list of alternatives, each of which is a string of tokens
	 *   (either terminal or non-terminal)
	 */
	public List<TokenString> getAlternatives()
	{
		List<TokenString> ordered_list = new ArrayList<TokenString>();
		List<TokenString> last_elements = new ArrayList<TokenString>();
		for (TokenString ts : m_alternatives)
		{
			if (ts.getTryLast())
			{
				last_elements.add(ts);
			}
			else
			{
				ordered_list.add(ts);
			}
		}
		ordered_list.addAll(last_elements);
		return ordered_list;
	}
	
	/**
	 * Retrieves the left-hand side symbol of the rule
	 * @return The left-hand side symbol
	 */
	public NonTerminalToken getLeftHandSide()
	{
		return m_leftHandSide;
	}

	/**
	 * Interprets UTF-8 escaped characters and converts them back into
	 * a UTF-8 string. The solution used here (going through a
	 * <tt>Property</tt> object) can be found on
	 * <a href="http://stackoverflow.com/a/24046962">StackOverflow</a>.
	 * It has the advantage of not relying on (yet another) external
	 * library (as the accepted solution does) just for using a single
	 * method. 
	 * @param s The input string
	 * @return The converted (unescaped) string
	 */
	protected static String unescapeString(String s)
	{
		// We want only the unicode characters to be resolved;
		// double all other backslashes
		String new_s = s.replaceAll("\\\\([^u])", "\\\\\\\\$1");
		Properties p = new Properties();
		try
		{
			p.load(new StringReader("key=" + new_s));
		}
		catch (IOException e)
		{
			Logger.getAnonymousLogger().log(Level.WARNING, "", e);
		}
		return p.getProperty("key");
	}

	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append(m_leftHandSide).append(" := ");
		boolean first = true;
		for (TokenString alt : m_alternatives)
		{
			if (!first)
			{
				out.append(" | ");
			}
			first = false;
			out.append(alt);
		}
		return out.toString();
	}

	public Set<TerminalToken> getTerminalTokens()
	{
		Set<TerminalToken> out = new HashSet<TerminalToken>();
		for (TokenString ts : m_alternatives)
		{
			out.addAll(ts.getTerminalTokens());
		}
		return out;
	}

	/**
	 * Adds a collection of alternatives to the rule
	 * @param alternatives The alternatives to add
	 */
	public void addAlternatives(Collection<TokenString> alternatives)
	{
		m_alternatives.addAll(alternatives);
	}

	/**
	 * Adds a collection of alternatives to the rule at a specific
	 * position in the list
	 * @param position The position in the rule list where it is to
	 *   be added
	 * @param alternatives The alternatives to add
	 */
	public void addAlternatives(int position, Collection<TokenString> alternatives)
	{
		for (TokenString alt : alternatives)
		{
			m_alternatives.add(position, alt);
		}
	}

	/**
	 * Exception thrown when the parsing of a rule form a string fails
	 */
	public static class InvalidRuleException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;

		public InvalidRuleException(String message)
		{
			super(message);
		}
	}
}
