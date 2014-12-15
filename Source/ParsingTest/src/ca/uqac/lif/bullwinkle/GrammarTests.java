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
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.util.FileReadWrite;


public class GrammarTests
{

  @Before
  public void setUp() throws Exception
  {
    // Nothing to do
  }

  @Test
  public void parseGrammar0()
  {
    String expression = "SELECT a FROM t";
    ParseNode node = parseIt("data/Grammar-0.bnf", "<S>", expression, false);
    int size = node.getSize();
    int expected_size = 9;
    if (size != expected_size)
    {
      fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected_size + ", got " + size);
    }
  }
  
  @Test
  public void parseGrammar1()
  {
    String expression = "SELECT a FROM t";
    ParseNode node = parseIt("data/Grammar-1.bnf", "<S>", expression, false);
    int size = node.getSize();
    int expected_size = 9;
    if (size != expected_size)
    {
      fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected_size + ", got " + size);
    }
  }
  
  @Test
  public void parseGrammar2a()
  {
    String expression = "SELECT a FROM (t)";
    ParseNode node = parseIt("data/Grammar-1.bnf", "<S>", expression, false);
    int size = node.getSize();
    int expected_size = 13;
    if (size != expected_size)
    {
      fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected_size + ", got " + size);
    }
  }
  
  @Test
  public void parseGrammar2b()
  {
    String expression = "SELECT a FROM (SELECT b FROM t)";
    ParseNode node = parseIt("data/Grammar-1.bnf", "<S>", expression, false);
    int size = node.getSize();
    int expected_size = 19;
    if (size != expected_size)
    {
      fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected_size + ", got " + size);
    }
  }
  
  @Test
  public void parseGrammarLtlFo1()
  {
    String expression = "G (∃ x ∈ /a/b/c : (x lt y))";
    ParseNode node = parseIt("data/Grammar-2.bnf", "<phi>", expression, false);
    int size = node.getSize();
    int expected_size = 23;
    if (size != expected_size)
    {
      fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected_size + ", got " + size);
    }
  }
  
  @Test
  public void parseGrammarWithEpsilon1()
  {
    String expression = "hello hello";
    ParseNode node = parseIt("data/Grammar-3.bnf", "<S>", expression, false);
    int size = node.getSize();
    int expected_size = 6;
    if (size != expected_size)
    {
      fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected_size + ", got " + size);
    }	  
  }
  
  @Test
  public void parseGrammarWithEpsilon2()
  {
    String expression = "hello hello foo";
    ParseNode node = parseIt("data/Grammar-4.bnf", "<S>", expression, false);
    int size = node.getSize();
    int expected_size = 9;
    if (size != expected_size)
    {
      fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected_size + ", got " + size);
    }     
  }
  
  @Test
  public void parseGrammarWithEntity1()
  {
    String expression = "a|a";
    ParseNode node = parseIt("data/Grammar-5.bnf", "<S>", expression, false);
    int size = node.getSize();
    int expected_size = 2;
    if (size != expected_size)
    {
      fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected_size + ", got " + size);
    }     
  }
  
  private ParseNode parseIt(String grammar_filename, String start_symbol, String expression, boolean debug_mode)
  {
    BnfParser parser = readGrammar(grammar_filename, start_symbol, debug_mode);
    ParseNode node = shouldParseAndNotNull(expression, parser);
    return node;
  }
  
  private ParseNode shouldParseAndNotNull(final String expression, final BnfParser parser)
  {
    ParseNode node = shouldParse(expression, parser);
    if (node == null)
    {
      fail("Parsing '" + expression + "' returned null; a non-null result was expected");
    }
    return node;
  }
  
  /**
   * Attempts to parse an expression with the given parser, and
   * expects the parsing not to raise any exception.
   * @param expression The expression to parse
   * @param parser The parser to use
   * @return The parse node if any, null if expression could not parse
   */
  private ParseNode shouldParse(final String expression, final BnfParser parser)
  {
    ParseNode node = null;
    try
    {
      node = parser.parse(expression);      
    }
    catch (BnfParser.ParseException e)
    {
      fail("Parsing '" + expression + "' threw exception " + e);
    }
    return node;
  }

  private BnfParser readGrammar(final String filename, final String start_rule, boolean debug_mode)
  {
    BnfParser parser = new BnfParser();
    parser.setDebugMode(debug_mode);
    try
    {
      String grammar = FileReadWrite.readFile(filename);
      parser.setGrammar(grammar);
    }
    catch (BnfParser.InvalidGrammarException e)
    {
      fail("Error parsing grammar file " + filename + ": " + e);
    }
    catch (IOException e)
    {
      fail("I/O Exception when reading " + filename + ": " + e);
    }
    parser.setStartRule(start_rule);
    return parser;
  }
}
