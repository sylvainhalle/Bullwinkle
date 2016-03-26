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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ca.uqac.lif.bullwinkle.BnfRule.InvalidRuleException;
import ca.uqac.lif.util.EmptyException;
import ca.uqac.lif.util.FileReadWrite;
import ca.uqac.lif.util.MutableString;

public class BnfParser
{
	private LinkedList<BnfRule> m_rules;

	private BnfRule m_startRule;

	private static transient final String s_CRLF = System.getProperty("line.separator");

	private transient boolean m_debugMode = false;

	public static int s_maxRecursionSteps = 50;

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

	public BnfParser(File f) throws IOException, InvalidGrammarException
	{
		this();
		String contents = FileReadWrite.readFile(f);
		setGrammar(contents);
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

	public static void setMaxRecursionSteps(int steps)
	{
		if (steps > 0)
		{
			s_maxRecursionSteps = steps;
		}
	}

	public void setDebugMode(boolean b)
	{
		m_debugMode = b;
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

	public void addCaseToRule(int index, String rule_name, String case_string)
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
	
	public void addCaseToRule(String rule_name, String case_string)
	{
		addCaseToRule(0, rule_name, case_string);
	}

	protected BnfRule getRule(String rule_name)
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

	public void setGrammar(String grammar) throws InvalidGrammarException
	{
		List<BnfRule> rules = getRules(grammar);
		addRules(rules);
	}

	public static List<BnfRule> getRules(String grammar) throws InvalidGrammarException
	{
		List<BnfRule> rules = new LinkedList<BnfRule>();
		if (grammar == null)
		{
			throw new InvalidGrammarException("Null argument given");
		}
		String[] lines = grammar.split(s_CRLF);
		String current_rule = "";
		for (String line : lines)
		{
			// Remove comments and empty lines
			int index = line.indexOf("#");
			if (index >= 0)
			{
				line = line.substring(0, index);
			}
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				continue;
			}
			current_rule += " " + line;
			if (current_rule.endsWith(";"))
			{
				// We have a complete rule
				try
				{
					// Remove semi-colon
					current_rule = current_rule.trim();
					BnfRule new_rule = BnfRule.parseRule(current_rule.substring(0, current_rule.length() - 1));
					rules.add(new_rule);
				}
				catch (InvalidRuleException e)
				{
					throw new InvalidGrammarException(e.toString());
				}
				current_rule = "";
			}
		}
		if (!current_rule.isEmpty())
		{
			throw new InvalidGrammarException("Error parsing rule " + current_rule);
		}
		return rules;
	}

	public ParseNode getParseTree(final String input) throws ParseException
	{
		return null;
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

	public void setStartRule(final String tokenName)
	{
		NonTerminalToken ntok = new NonTerminalToken(tokenName);
		setStartRule(ntok);
	}

	public void setStartRule(final NonTerminalToken token)
	{
		m_startRule = getRule(token);
	}

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
		ParseNode out = parse(m_startRule, n_input, 0);
		return out;
	}

	private /*@Nullable*/ ParseNode parse(final BnfRule rule, MutableString input, int level) throws ParseException
	{
		if (level > s_maxRecursionSteps)
		{
			//throw new ParseException("Maximum number of recursion steps reached. If the input string is indeed valid, try increasing the limit.");
			log("Maximum number of recursion steps reached", level);
			return null;
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
					// Non-terminal token: recursively try to parse it
					BnfRule new_rule = getRule(alt_tok);
					if (new_rule == null)
					{
						// No rule found for non-terminal symbol:
						// there is an error in the grammar
						throw new ParseException("Cannot find rule for token " + alt_tok);

					}
					ParseNode child = parse(new_rule, n_input, level + 1);
					if (child == null)
					{
						// Parsing failed
						wrong_symbol = true;
						out_node = null;
						log("FAILED parsing input " + input + " with rule " + rule, level);
						break;
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
			System.err.println(out);
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
		private static final long serialVersionUID = 1L;

		public InvalidGrammarException(final String message)
		{
			super(message);
		}
	}

	public static class ParseException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 2L;

		public ParseException(final String message)
		{
			super(message);
		}
	}
}
