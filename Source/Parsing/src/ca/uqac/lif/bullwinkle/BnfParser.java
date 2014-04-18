/*
  Copyright 2014 Sylvain Hallé
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import ca.uqac.lif.bullwinkle.BnfRule.InvalidRuleException;
import ca.uqac.lif.util.EmptyException;
import ca.uqac.lif.util.FileReadWrite;
import ca.uqac.lif.util.MutableString;

public class BnfParser
{
  private LinkedList<BnfRule> m_rules;
  
  private BnfRule m_startRule;
  
  private static final String s_CRLF = System.getProperty("line.separator");
  
  public static int s_maxRecursionSteps = 50;
  
  public BnfParser()
  {
    super();
    m_startRule = null;
    m_rules = new LinkedList<BnfRule>();
  }
  
  public BnfParser(File f) throws IOException, InvalidGrammarException
  {
    this();
    String contents = FileReadWrite.readFile(f);
    setGrammar(contents);
  }
  
  public static void setMaxRecursionSteps(int steps)
  {
    if (steps > 0)
    {
      s_maxRecursionSteps = steps;
    }
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
  
  public void setGrammar(String grammar) throws InvalidGrammarException
  {
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
          m_rules.add(new_rule);
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
  }
  
  public ParseNode getParseTree(final String input) throws ParseException
  {
    return null;
  }
  
  public void addRule(final BnfRule rule)
  {
    m_rules.add(rule);
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
  
  public ParseNode parse(final String input) throws ParseException
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
  
  private ParseNode parse(final BnfRule rule, MutableString input, int level) throws ParseException
  {
    if (level > s_maxRecursionSteps)
    {
      //throw new ParseException("Maximum number of recursion steps reached. If the input string is indeed valid, try increasing the limit.");
      return null;
    }
    ParseNode out_node = null;
    MutableString n_input = new MutableString(input);
    boolean wrong_symbol = true;
    for (TokenString alt : rule.getAlternatives())
    {
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
            child.setToken(input_tok.toString());
            out_node.addChild(child);
          }
          else
          {
            // Rule expects a token, token in string does not match: NO MATCH
            wrong_symbol = true;
            out_node = null;
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
            break;
          }
          out_node.addChild(child);
        }
      }
      if (!wrong_symbol || n_input.isEmpty())
      {
        // We succeeded in parsing the complete string: done
        break;
      }
    }
    int chars_consumed = input.length() - n_input.length();
    if (chars_consumed == 0)
    {
      // We did not consume anything: fail
      return null;
    }
    input.truncateSubstring(chars_consumed);
    if (level == 0 && !input.isEmpty())
    {
      // The top-level rule must parse the complete string
      return null;
    }

    return out_node;
  }
  
  public TokenString tokenize(final String s)
  {
    TokenString out = new TokenString();
    Set<TerminalToken> tokens = getTerminalTokens();
    String in_s = new String(s);
    boolean change = true;
    while (!in_s.isEmpty() && change)
    {
      change = false;
      int min_index_of_known_token = in_s.length();
      in_s = in_s.trim();
      for (TerminalToken ttok : tokens)
      {
        // For each known token, check if it is present at the start of
        // the current string
        String to_look = ttok.toString();
        int index = in_s.indexOf(to_look);
        min_index_of_known_token = Math.min(index, min_index_of_known_token);
        if (index != 0)
        {
          // No: move on to next token
          continue;
        }
        // Yes: remove it from start of string and add it to the token string
        change = true;
        in_s = in_s.substring(to_look.length());
        TerminalToken to_add = new TerminalToken(to_look);
        out.add(to_add);
        break;
      }
      if (!change)
      {
        // The string does not correspond to the start of any known token
        // We deduce a new token from the substring up to the first occurrence
        // of a known token
        String deduced_token = in_s.substring(0, min_index_of_known_token);
        TerminalToken to_add = new TerminalToken(deduced_token);
        out.add(to_add);
        in_s = in_s.substring(min_index_of_known_token);
      }
    }
    return out;
  }
  
  private BnfRule getRule(final Token tok)
  {
    for (BnfRule rule : m_rules)
    {
      NonTerminalToken lhs = rule.getLeftHandSide();
      if (lhs.toString().compareTo(tok.toString()) == 0)
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
