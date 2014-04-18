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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ca.uqac.lif.util.EmptyException;

public class BnfRule
{
  private List<TokenString> m_alternatives;
  
  private NonTerminalToken m_leftHandSide;
  
  BnfRule()
  {
    super();
    m_alternatives = new LinkedList<TokenString>();
  }
  
  public static BnfRule parseRule(String input) throws BnfRule.InvalidRuleException
  {
    BnfRule out = new BnfRule();
    String[] lr = input.split("\\s*:=\\s*");
    if (lr.length != 2)
    {
      throw new InvalidRuleException("Cannot find left- and right-hand side of BNF rule");
    }
    assert(lr.length == 2);
    String lhs = lr[0].trim();
    out.setLeftHandSide(new NonTerminalToken(lhs));
    String[] alternatives = lr[1].split("\\s+\\|\\s+");
    if (alternatives.length == 0)
    {
      throw new InvalidRuleException("Right-hand side of BNF rule is empty");
    }
    assert(alternatives.length > 0);
    for (String alt : alternatives)
    {
      TokenString alternative_to_add = new TokenString();
      String[] words = alt.split(" ");
      if (words.length == 0)
      {
        throw new InvalidRuleException("Alternative of BNF rule is empty");
      }
      assert(words.length > 0);
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
          //trimmed_word = trimmed_word.substring(1, trimmed_word.length() - 1);
          Token to_add = new NonTerminalToken(trimmed_word);
          alternative_to_add.add(to_add);
        }
        else if (trimmed_word.startsWith("^"))
        {
          // This is a regex symbol
          Token to_add = new RegexTerminalToken(trimmed_word);
          alternative_to_add.add(to_add);
        }
        else
        {
          if (trimmed_word.isEmpty())
          {
            throw new InvalidRuleException("Trying to create an empty terminal token"); 
          }
          // This is a literal token
          alternative_to_add.add(new TerminalToken(trimmed_word));
        }
      }
      out.addAlternative(alternative_to_add);
    }
    return out;
  }
  
  void setLeftHandSide(final NonTerminalToken t)
  {
    m_leftHandSide = t;
  }
  
  void addAlternative(final TokenString ts)
  {
    m_alternatives.add(ts);
  }
  
  List<TokenString> getAlternatives()
  {
    return m_alternatives;
  }
  
  NonTerminalToken getLeftHandSide()
  {
    return m_leftHandSide;
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
  
  public TokenString tokenize(String s)
  {
    TokenString out = new TokenString();
    // TODO
    return out;
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
