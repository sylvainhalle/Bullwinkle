/* MIT License
 * 
 * Copyright 2014-2021 Sylvain Hallé
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

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.uqac.lif.bullwinkle.BnfRule.InvalidRuleException;
import ca.uqac.lif.util.EmptyException;
import ca.uqac.lif.util.MutableString;

/**
 * A parser reads a string according to a BNF grammar and produces a
 * parse tree
 * 
 * @author Sylvain Hallé
 */
public class BnfParser implements Serializable
{
	/**
	 * Dummy UID
	 */
	private static final transient long serialVersionUID = 1L;

	/**
	 * The list of parsing rules
	 */
	private LinkedList<BnfRule> m_rules;

	/**
	 * The start rule to be used for the parsing
	 */
	private BnfRule m_startRule;

	/**
	 * Whether the parser is set on debug mode
	 */
	private transient boolean m_debugMode = false;

	/**
	 * Where debug info should be printed
	 */
	private transient Logger m_debugOut = Logger.getAnonymousLogger(); 

	/**
	 * Maximum number of recursion steps when parsing a string.
	 * Parsing will stop immediately when reaching this depth.
	 * This is an upper bound that prevents the parser from entering
	 * an infinite recursion.
	 */
	private transient int m_maxRecursionSteps = 50;

	/**
	 * Sets whether partial parsing is enabled
	 */
	private boolean m_partialParsing = false;

	/**
	 * Creates a new empty parser with no grammar
	 */
	public BnfParser()
	{
		super();
		m_startRule = null;
		m_rules = new LinkedList<BnfRule>();
	}

	/**
	 * Creates a new parser by copying the rules from another parser
	 * @param parser The parser to copy the rules from
	 */
	public BnfParser(BnfParser parser)
	{
		super();
		m_rules = new LinkedList<BnfRule>();
		m_rules.addAll(parser.m_rules);
		m_startRule = parser.m_startRule;
	}

	/**
	 * Creates a new parser by reading its grammar from the contents of
	 * some input stream
	 * @param is The input stream to read from
	 * @throws InvalidGrammarException Thrown if the grammar to read is invalid
	 */
	public BnfParser(InputStream is) throws InvalidGrammarException
	{
		this();
		Scanner s = new Scanner(is);
		setGrammar(s);
	}

	/**
	 * Instructs the parser to perform partial parsing. In partial parsing,
	 * a string can contain instances of non-terminal tokens. For example,
	 * given the rules
	 * <pre>
	 * &lt;S&gt; := &lt;A&gt; b
	 * &lt;A&gt; := foo | bar
	 * </pre>
	 * With partial parsing, the string <tt>&lt;A&gt; b</tt> will parse.
	 * In this case, note that the resulting parse tree can have non-terminal
	 * tokens as leaves.
	 * @param b Set to true to enable partial parsing
	 */
	public void setPartialParsing(boolean b)
	{
		m_partialParsing = b;
	}

	/**
	 * Whether the matching is sensitive to case. This is a program-wide
	 * value
	 * @param b True if parsing is case-sensitive, false otherwise
	 */
	public static void setCaseSensitive(boolean b)
	{
		Token.setCaseSensitive(b);
	}

	/**
	 * Sets the maximum number of recursion steps that the parsing will use.
	 * This setting is there to avoid infinite loops in the parsing
	 * @param steps The maximum number of recursion steps. Must be positive.
	 */
	public void setMaxRecursionSteps(int steps)
	{
		if (steps > 0)
		{
			m_maxRecursionSteps = steps;
		}
	}

	/**
	 * Sets the parser into "debug mode". This will print information messages
	 * about the status of the parsing on the standard error stream
	 * @param b Set to <code>true</code> to enable debug mode
	 */
	public void setDebugMode(boolean b)
	{
		m_debugMode = b;
	}

	/**
	 * Sets the parser into "debug mode". This will print information messages
	 * about the status of the parsing in some print stream.
	 * @param b Set to <code>true</code> to enable debug mode
	 * @param out A logger to log the information
	 */
	public void setDebugMode(boolean b, /*@ non_null @*/ Logger out)
	{
		m_debugMode = b;
		m_debugOut = out;
	}
	
	/**
	 * Retrieves the start rule of the grammar associated to this parser.
	 * @return The start rule
	 */
	/*@ pure @*/ public BnfRule getStartRule()
	{
		return m_startRule;
	}

	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		for (BnfRule rule : m_rules)
		{
			out.append(rule).append(";\n");
		}
		return out.toString();
	}

	/**
	 * Adds a new case to an existing rule
	 * @param index The location in the list of cases where to put the
	 *   new case. Use 0 to put the new case at the beginning.
	 * @param rule_name The name of the rule
	 * @param case_string The case to add
	 */
	public void addCaseToRule(int index, /*@ non_null @*/ String rule_name, /*@ non_null @*/ String case_string)
	{
		BnfRule rule = getRule(rule_name);
		if (rule == null)
		{
			return;
		}
		NonTerminalToken ntok = new NonTerminalToken(case_string);
		TokenString ts = new TokenString();
		ts.add(ntok);
		rule.addAlternative(index, ts);
	}

	/**
	 * Adds a new case to an existing rule
	 * @param rule_name The name of the rule
	 * @param case_string The case to add
	 */
	public void addCaseToRule(/*@ non_null @*/ String rule_name, /*@ non_null @*/ String case_string)
	{
		addCaseToRule(0, rule_name, case_string);
	}

	/**
	 * Gets the rule instance with given name
	 * @param rule_name The name of the rule
	 * @return The rule, or <tt>null</tt> if no rule exists with given name
	 */
	/*@ non_null @*/ public BnfRule getRule(/*@ non_null @*/ String rule_name)
	{
		for (BnfRule rule : m_rules)
		{
			String lhs = rule.getLeftHandSide().getName();
			if (rule_name.compareTo(lhs) == 0)
			{
				return rule;
			}
		}
		return null;
	}

	/**
	 * Sets the parser's grammar from a string
	 * @param grammar The string containing the grammar to be used
	 * @throws InvalidGrammarException Thrown if the grammar string is
	 *   invalid
	 */
	public void setGrammar(String grammar) throws InvalidGrammarException
	{
		List<BnfRule> rules = getRules(grammar);
		addRules(rules);
	}
	
	/**
	 * Sets the parser's grammar from a scanner
	 * @param scanner A scanner containing the grammar to be used
	 * @throws InvalidGrammarException Thrown if the grammar string is
	 *   invalid
	 */
	public void setGrammar(Scanner scanner) throws InvalidGrammarException
	{
		List<BnfRule> rules = getRules(scanner);
		addRules(rules);
	}


	/**
	 * Converts a string into a list of grammar rules
	 * @param grammar The string containing the grammar to be used
	 * @return A list of grammar rules
	 * @throws InvalidGrammarException Thrown if the grammar string is
	 *   invalid
	 */
	public static List<BnfRule> getRules(String grammar) throws InvalidGrammarException
	{
		if (grammar == null)
		{
			throw new InvalidGrammarException("Null argument given");
		}
		Scanner s = new Scanner(grammar);
		return getRules(s);
	}

	/**
	 * Converts a string source into a list of grammar rules
	 * @param scanner A scanner open on a string containing the
	 *  grammar to be used
	 * @return A list of grammar rules
	 * @throws InvalidGrammarException Thrown if the grammar string is
	 *   invalid
	 */
	public static List<BnfRule> getRules(Scanner scanner) throws InvalidGrammarException
	{
		List<BnfRule> rules = new LinkedList<BnfRule>();
		StringBuilder current_rule_builder = new StringBuilder();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			// Remove comments and empty lines
			int index = line.indexOf('#');
			if (index >= 0)
			{
				line = line.substring(0, index);
			}
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				continue;
			}
			current_rule_builder.append(" ").append(line);
			if (line.endsWith(";"))
			{
				// We have a complete rule
				String current_rule = current_rule_builder.toString();
				try
				{
					// Remove semi-colon
					current_rule = current_rule.trim();
					BnfRule new_rule = BnfRule.parseRule(current_rule.substring(0, current_rule.length() - 1));
					rules.add(new_rule);
				}
				catch (InvalidRuleException e)
				{
					scanner.close();
					throw new InvalidGrammarException(e);
				}
				current_rule_builder.setLength(0);
			}
		}
		scanner.close();
		if (!current_rule_builder.toString().isEmpty())
		{
			throw new InvalidGrammarException("Error parsing rule " + current_rule_builder.toString());
		}
		return rules;
	}
	
	/**
	 * Returns the list of alternative rules
	 * @param rule_name The rule you need the alternatives
	 * @return a list of strings representing the alternatives
	 */
	public /*@NotNull*/ List<String> getAlternatives(String rule_name)
	{
		for (BnfRule rule : m_rules)
	    {
	      String lhs = rule.getLeftHandSide().getName();
	      if (rule_name.compareTo(lhs) == 0)
	      {
	    	List<String> alternatives = new ArrayList<String>();
	        for(TokenString alt : rule.getAlternatives())
	        {
	        	alternatives.add(alt.toString());
	        }
	        return alternatives;
	      }
	    }
	    return new ArrayList<String>(0);
	}

	/**
	 * Adds a rule to the parser at a specific position.
	 * If a rule with the same left-hand side
	 * already exists in the parser, the case of the rule passed as an argument
	 * will be added to the cases of the existing rule.
	 * @param position The position where the alternatives to this rule will be added 
	 * @param rule The rule to add
	 */
	public void addRule(final int position, final BnfRule rule)
	{
		NonTerminalToken r_left = rule.getLeftHandSide();
		for (BnfRule in_rule : m_rules)
		{
			NonTerminalToken in_left = in_rule.getLeftHandSide();
			if (r_left.equals(in_left))
			{
				in_rule.addAlternatives(position, rule.getAlternatives());
				break;
			}
		}
		// No rule with the same LHS was found
		m_rules.add(rule);
	}

	/**
	 * Adds a rule to the parser. If a rule with the same left-hand side
	 * already exists in the parser, the case of the rule passed as an argument
	 * will be added to the cases of the existing rule. 
	 * @param rule The rule to add
	 */
	public void addRule(final BnfRule rule)
	{
		NonTerminalToken r_left = rule.getLeftHandSide();
		for (BnfRule in_rule : m_rules)
		{
			NonTerminalToken in_left = in_rule.getLeftHandSide();
			if (r_left.equals(in_left))
			{
				in_rule.addAlternatives(rule.getAlternatives());
				break;
			}
		}
		// No rule with the same LHS was found
		m_rules.add(rule);
	}

	/**
	 * Adds a collection of rules to the parser 
	 * @param rules The rules to add
	 */
	public void addRules(final Collection<BnfRule> rules)
	{
		for (BnfRule rule : rules)
		{
			addRule(rule);
		}
	}

	/**
	 * Sets the start rule to be used for the parsing
	 * @param tokenName The name of the non-terminal to be used. It must
	 *   be defined in the grammar, otherwise a <code>NullPointerException</code>
	 *   will be thrown when attempting to parse a string.
	 */
	public void setStartRule(final String tokenName)
	{
		NonTerminalToken ntok = new NonTerminalToken(tokenName);
		setStartRule(ntok);
	}

	/**
	 * Sets the start rule to be used for the parsing
	 * @param token The non-terminal to be used. It must
	 *   be defined in the grammar, otherwise a <code>NullPointerException</code>
	 *   will be thrown when attempting to parse a string.
	 */
	public void setStartRule(final NonTerminalToken token)
	{
		m_startRule = getRule(token);
	}

	/**
	 * Parse a string
	 * @param input The string to parse
	 * @return The root of the resulting parsing tree
	 * @throws ParseException Thrown if the string does not follow the grammar
	 */
	public /*@NonNull*/ ParseNode parse(final String input) throws ParseException
	{
		MutableString n_input = new MutableString(input);
		if (m_startRule == null)
		{
			if (m_rules.isEmpty())
			{
				throw new ParseException("No start rule could be found");
			}
			// If no start rule was specified, take first rule of the list as default
			m_startRule = m_rules.peekFirst();
		}
		return parse(m_startRule, n_input, 0);
	}

	private /*@Nullable*/ ParseNode parse(final BnfRule rule, MutableString input, int level) throws ParseException
	{
		if (level > m_maxRecursionSteps)
		{
			throw new ParseException("Maximum number of recursion steps reached. If the input string is indeed valid, try increasing the limit.");
		}
		ParseNode out_node = null;
		MutableString n_input = new MutableString(input);
		boolean wrong_symbol = true;
		boolean read_epsilon = false;
		log("Considering input '" + input + "' with rule " + rule, level);
		for (TokenString alt : rule.getAlternatives())
		{
			log("Alternative " + alt, level);
			out_node = new ParseNode();
			NonTerminalToken left_hand_side = rule.getLeftHandSide();
			out_node.setToken(left_hand_side.toString());
			out_node.setValue(left_hand_side.toString());
			TokenString new_alt = alt.getCopy();
			Iterator<Token> alt_it = new_alt.iterator();
			n_input = new MutableString(input);
			wrong_symbol = false;
			while (alt_it.hasNext() && !wrong_symbol)
			{
				n_input.trim();
				Token alt_tok = alt_it.next();
				if (alt_tok instanceof TerminalToken)
				{
					if (alt_tok instanceof EpsilonTerminalToken)
					{
						// Epsilon always works
						ParseNode child = new ParseNode();
						child.setToken("");
						out_node.addChild(child);       
						read_epsilon = true;
						break;
					}
					if (n_input.isEmpty())
					{
						// Rule expects a token, string has no more: NO MATCH
						wrong_symbol = true;
						break;
					}
					int match_prefix_size = alt_tok.match(n_input.toString());
					if (match_prefix_size > 0)
					{
						ParseNode child = new ParseNode();
						MutableString input_tok = n_input.truncateSubstring(0, match_prefix_size);
						if (alt_tok instanceof RegexTerminalToken)
						{
							// In the case of a regex, create children with each capture block
							child = appendRegexChildren(child, (RegexTerminalToken) alt_tok, input_tok);
						}
						child.setToken(input_tok.toString());
						out_node.addChild(child);
					}
					else
					{
						// Rule expects a token, token in string does not match: NO MATCH
						wrong_symbol = true;
						out_node = null;
						log("FAILED parsing with case " + new_alt, level);
						break;
					}
				}
				else
				{
					ParseNode child = null;
					// Non-terminal token: recursively try to parse it
					String alt_tok_string = alt_tok.toString();
					if (m_partialParsing && n_input.startsWith(alt_tok_string))
					{
						n_input.truncateSubstring(0, alt_tok_string.length());
						child = new ParseNode(alt_tok_string);
					}
					else
					{
						BnfRule new_rule = getRule(alt_tok);
						if (new_rule == null)
						{
							// No rule found for non-terminal symbol:
							// there is an error in the grammar
							throw new ParseException("Cannot find rule for token " + alt_tok);

						}
						child = parse(new_rule, n_input, level + 1);
						if (child == null)
						{
							// Parsing failed
							wrong_symbol = true;
							out_node = null;
							log("FAILED parsing input " + input + " with rule " + rule, level);
							break;
						}
					}
					out_node.addChild(child);
				}
			}
			if (!wrong_symbol)
			{
				if (!alt_it.hasNext())
				{
					// We succeeded in parsing the complete string: done
					if (level > 0 || (level == 0 && n_input.toString().trim().length() == 0))
					{
						break;
					}
				}
				else
				{
					// The rule expects more symbols, but there are none
					// left in the input; set wrong_symbol back to true to
					// force exploring the next alternative
					wrong_symbol = true;
					n_input = new MutableString(input);
					log("No symbols left in input; will explore next alternative", level);
					break;
				}
			}
		}
		int chars_consumed = input.length() - n_input.length();
		if (wrong_symbol)
		{
			// We did not consume anything, and the symbol was not epsilon: fail
			log("FAILED: expected more symbols with rule " + rule, level);
			return null;    	
		}
		if (chars_consumed == 0 && !read_epsilon)
		{
			// We did not consume anything, and the symbol was not epsilon: fail
			log("FAILED: did not consume anything of " + input + " with rule " + rule, level);
			return null;
		}
		input.truncateSubstring(chars_consumed);
		if (level == 0 && !input.isEmpty())
		{
			// The top-level rule must parse the complete string
			log("FAILED: The top-level rule must parse the complete string", level);
			return null;
		}
		return out_node;
	}

	private BnfRule getRule(/* @NonNull */ final Token tok)
	{
		if (tok == null)
		{
			return null;
		}
		for (BnfRule rule : m_rules)
		{
			NonTerminalToken lhs = rule.getLeftHandSide();
			if (lhs != null && lhs.toString().compareTo(tok.toString()) == 0)
			{
				return rule;
			}
		}
		return null;
	}

	public Set<TerminalToken> getTerminalTokens()
	{
		Set<TerminalToken> out = new HashSet<TerminalToken>();
		for (BnfRule rule : m_rules)
		{
			out.addAll(rule.getTerminalTokens());
		}
		return out;
	}

	private void log(String message, int level)
	{
		StringBuilder out = new StringBuilder();
		if (m_debugMode)
		{
			for (int i = 0; i < level; i++)
				out.append("  ");
			out.append(message);
			m_debugOut.log(Level.INFO, "{0}", out.toString());
		}
	}

	/**
	 * In the case where the parsing matches a regex terminal node, creates
	 * children to the parse node representing the contents of each capture
	 * block in the regex, if any.
	 * @param node The parse node
	 * @param tok The terminal token that matches the string
	 * @param s The string that was matched
	 * @return The input node, to which children may have been appended 
	 */
	protected static ParseNode appendRegexChildren(ParseNode node, RegexTerminalToken tok, MutableString s)
	{
		List<String> blocks = tok.getCaptureBlocks(s.toString());
		for (String block : blocks)
		{
			ParseNode pn = new CaptureBlockParseNode(block);
			node.addChild(pn);
		}
		return node;
	}

	public static class InvalidGrammarException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final transient long serialVersionUID = 1L;

		public InvalidGrammarException(final String message)
		{
			super(message);
		}
		
		public InvalidGrammarException(Throwable t)
		{
			super(t);
		}
	}

	public static class ParseException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final transient long serialVersionUID = 2L;

		public ParseException(final String message)
		{
			super(message);
		}
	}
}