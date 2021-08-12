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

import static org.junit.Assert.*;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.BnfRule.InvalidRuleException;
import ca.uqac.lif.bullwinkle.ParseNode;

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
		ParseNode node = parseIt("data/Grammar-0.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 9, node.getSize());
	}

	@Test
	public void testAddCaseValid() throws ParseException, InvalidRuleException
	{
		String expression = "bar";
		BnfParser parser = readGrammar("data/Grammar-0.bnf", "<S>", false);
		parser.addCaseToRule("<S>", "<foo>");
		parser.addRule(BnfRule.parseRule("<foo> := bar"));
		ParseNode node = parser.parse(expression);
		assertNotNull(node);
	}
	
	@Test
	public void testAddCaseInvalid() throws ParseException, InvalidRuleException
	{
		String expression = "bar";
		BnfParser parser = readGrammar("data/Grammar-10.bnf", "<S>", false);
		parser.addCaseToRule("<a>", "<foo>");
		parser.addRule(BnfRule.parseRule("<a> := bar"));
		ParseNode node = parser.parse(expression);
		assertNull(node);
	}
	
	@Test
	public void testAddRule() throws ParseException, InvalidRuleException
	{
		BnfParser parser = readGrammar("data/Grammar-10.bnf", "<S>", false);
		parser.addRule(0, BnfRule.parseRule("<S> := bar"));
		ParseNode node = parser.parse("bar");
		assertNotNull(node);
	}
	
	@Test(expected=InvalidGrammarException.class)
	public void testNulLRules() throws ParseException, InvalidRuleException, InvalidGrammarException
	{
		String s = null;
		BnfParser.getRules(s);
	}
	
	@Test(expected=InvalidGrammarException.class)
	public void testInvalid1() throws ParseException, InvalidRuleException, InvalidGrammarException
	{
		@SuppressWarnings("unused")
		BnfParser parser = new BnfParser(GrammarTests.class.getResourceAsStream("data/Grammar-invalid-1.bnf"));
	}
	
	@Test(expected=InvalidGrammarException.class)
	public void testInvalid2() throws ParseException, InvalidRuleException, InvalidGrammarException
	{
		@SuppressWarnings("unused")
		BnfParser parser = new BnfParser(GrammarTests.class.getResourceAsStream("data/Grammar-invalid-2.bnf"));
	}
	
	@Test(expected=InvalidGrammarException.class)
	public void testInvalid3() throws ParseException, InvalidRuleException, InvalidGrammarException
	{
		@SuppressWarnings("unused")
		BnfParser parser = new BnfParser(GrammarTests.class.getResourceAsStream("data/Grammar-invalid-3.bnf"));
	}
	
	@Test(expected=ParseException.class)
	public void testTooMuchRecursion() throws ParseException, InvalidRuleException, InvalidGrammarException
	{
		BnfParser parser = new BnfParser(GrammarTests.class.getResourceAsStream("data/Grammar-15.bnf"));
		parser.setMaxRecursionSteps(2);
		parser.parse("a a b");
	}

	@Test
	public void testCopy() throws ParseException, InvalidRuleException
	{
		String expression = "bar";
		BnfParser parser = readGrammar("data/Grammar-0.bnf", "<S>", false);
		parser.addCaseToRule("<S>", "<foo>");
		parser.addRule(BnfRule.parseRule("<foo> := bar"));
		BnfParser parser2 = new BnfParser(parser);
		ParseNode node = parser2.parse(expression);
		assertNotNull(node);
	}

	@Test
	public void testToString()
	{
		// A rather dummy test, just to ensure we cover the lines in the
		// toString() method
		BnfParser parser = readGrammar("data/Grammar-0.bnf", "<S>", true);
		String s = parser.toString();
		assertNotNull(s);
		assertTrue(s.length() > 10);
	}
	
	@Test
	public void testInputStream() throws InvalidGrammarException, ParseException
	{
		BnfParser parser = new BnfParser(GrammarTests.class.getResourceAsStream("data/Grammar-0.bnf"));
		ParseNode node = parser.parse("SELECT a FROM t");
		assertNotNull(node);
	}

	@Test
	public void parseGrammarError0()
	{
		String expression = "SELECT";
		parseItNot("data/Grammar-0.bnf", "<S>", expression, false, false);
	}

	@Test
	public void parseGrammar1()
	{
		String expression = "SELECT a FROM t";
		ParseNode node = parseIt("data/Grammar-1.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 9, node.getSize());
	}

	@Test
	public void parseGrammar2a()
	{
		String expression = "SELECT a FROM (t)";
		ParseNode node = parseIt("data/Grammar-1.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 13, node.getSize());
	}

	@Test
	public void parseGrammar2b()
	{
		String expression = "SELECT a FROM (SELECT b FROM t)";
		ParseNode node = parseIt("data/Grammar-1.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 19, node.getSize());
	}

	@Test
	public void parseGrammar3a()
	{
		String expression = "(a) & (a)";
		ParseNode node = parseIt("data/Grammar-9.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 8, node.getSize());
	}

	@Test
	public void parseGrammar3b()
	{
		String expression = "(a) & (a) & (a)";
		ParseNode node = parseIt("data/Grammar-9.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 13, node.getSize());
	}

	@Test
	public void parseGrammar4()
	{
		String expression = "a WHERE b";
		ParseNode node = parseIt("data/Grammar-10.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 5, node.getSize());
	}

	@Test
	public void parseGrammar5()
	{
		String expression = "a WHERE";
		parseItNot("data/Grammar-10.bnf", "<S>", expression, false, false);
	}

	@Test
	public void parseGrammar6()
	{
		String expression = "SELECT 0 AS att FROM 0 AS";
		parseItNot("data/Grammar-11.bnf", "<eml_select>", expression, false, false);
	}

	@Test
	public void parseGrammar7()
	{
		String expression = "(THE TUPLES OF FILE \"a\") WHERE (a) = (0)";
		parseIt("data/Grammar-11.bnf", "<processor>", expression, false, false);
	}

	@Test
	public void parseGrammar8a()
	{
		String expression = "0 FOO";
		parseIt("data/Grammar-14.bnf", "<processor>", expression, false, false);
	}

	@Test
	public void parseGrammar8b()
	{
		String expression = "(0) FOO";
		parseIt("data/Grammar-14.bnf", "<processor>", expression, false, false);
	}

	@Test
	public void parseNumber1()
	{
		String expression = "3.5";
		parseIt("data/Grammar-12.bnf", "<processor>", expression, false, false);
	}

	@Test
	public void parseNumber2()
	{
		String expression = "3";
		parseIt("data/Grammar-12.bnf", "<processor>", expression, false, false);
	}

	@Test
	public void parseGrammarLtlFo1()
	{
		String expression = "G (∃ x ∈ /a/b/c : (x lt y))";
		ParseNode node = parseIt("data/Grammar-2.bnf", "<phi>", expression, false, false);
		checkParseTreeSize(expression, 23, node.getSize());
	}

	@Test
	public void parseGrammarWithEpsilon1()
	{
		String expression = "hello hello";
		ParseNode node = parseIt("data/Grammar-3.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 6, node.getSize());  
	}

	@Test
	public void parseGrammarWithEpsilon2()
	{
		String expression = "hello hello foo";
		ParseNode node = parseIt("data/Grammar-4.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 9, node.getSize());     
	}

	@Test
	public void parseGrammarWithEpsilon3()
	{
		String expression = "[ ]";
		ParseNode node = parseIt("data/Grammar-7.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 5, node.getSize());    
	}

	@Test
	public void parseGrammarWithEpsilon4()
	{
		String expression = "[ ]";
		ParseNode node = parseIt("data/Grammar-8.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 6, node.getSize());   
	}

	@Test
	public void parseGrammarWithEntity1()
	{
		String expression = "a|a";
		ParseNode node = parseIt("data/Grammar-5.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 2, node.getSize());    
	}

	@Test
	public void parseGrammarWithCaptureBlock()
	{
		String expression = "A tomato is a type of fruit";
		ParseNode node = parseIt("data/Grammar-6.bnf", "<S>", expression, false, false);
		checkParseTreeSize(expression, 4, node.getSize());   
	}

	@Test
	public void parseGrammarPartial0()
	{
		String expression = "SELECT <criterion> FROM t";
		ParseNode node = parseIt("data/Grammar-0.bnf", "<S>", expression, false, true);
		checkParseTreeSize(expression, 8, node.getSize());
	}

	@Test
	public void parseGrammarPartial1()
	{
		String expression = "SELECT <foo> FROM t";
		parseItNot("data/Grammar-0.bnf", "<S>", expression, false, true);
	}

	@Test
	public void parseGrammarPartial2()
	{
		String expression = "<A> <B> c";
		parseIt("data/Grammar-13.bnf", "<S>", expression, false, true);
	}

	@Test
	public void parseGrammarPartial3()
	{
		String expression = "foo <B> c";
		parseIt("data/Grammar-13.bnf", "<S>", expression, false, true);
	}

	@Test
	public void parseGrammarPartial4()
	{
		String expression = "foo <Z> d c";
		parseIt("data/Grammar-13.bnf", "<S>", expression, false, true);
	}

	@Test
	public void parseGrammarPartial4b()
	{
		String expression = "foo <Y> d c";
		parseItNot("data/Grammar-13.bnf", "<S>", expression, false, true);
	}

	@Test
	public void parseGrammarPartial4c()
	{
		String expression = "foo <B> d c";
		parseItNot("data/Grammar-13.bnf", "<S>", expression, false, true);
	}

	@Test
	public void parseGrammarPartial5()
	{
		String expression = "foo 0 d c";
		parseIt("data/Grammar-13.bnf", "<S>", expression, false, true);
	}
	
	@Test
	public void parseGrammarDebug() throws ParseException
	{
		String expression = "a WHERE";
		BnfParser parser = readGrammar("data/Grammar-10.bnf", "<S>", true);
		DummyLogger dl = new DummyLogger();
		parser.setDebugMode(true, dl);
		parser.parse(expression);
		assertTrue(dl.hasLogged());
	}

	private static void checkParseTreeSize(String expression, int expected, int size)
	{
		if (size != expected)
		{
			fail("Incorrect parsing of expression '" + expression + "': expected a parse tree of size " + expected + ", got " + size);
		}    
	}

	private ParseNode parseIt(String grammar_filename, String start_symbol, String expression, boolean debug_mode, boolean partial_parsing)
	{
		BnfParser parser = readGrammar(grammar_filename, start_symbol, debug_mode);
		parser.setPartialParsing(partial_parsing);
		ParseNode node = shouldParseAndNotNull(expression, parser);
		return node;
	}

	private void parseItNot(String grammar_filename, String start_symbol, String expression, boolean debug_mode, boolean partial_parsing)
	{
		BnfParser parser = readGrammar(grammar_filename, start_symbol, debug_mode);
		shouldNotParse(expression, parser);
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

	/**
	 * Attempts to parse an expression with the given parser, and
	 * expects the parsing not to raise an exception or to return null.
	 * @param expression The expression to parse
	 * @param parser The parser to use
	 */
	private void shouldNotParse(final String expression, final BnfParser parser)
	{
		ParseNode node = null;
		try
		{
			node = parser.parse(expression);      
		}
		catch (BnfParser.ParseException e)
		{
		}
		if (node != null)
		{
			fail("The parsing of " + expression + " should have failed");
		}
	}

	public static BnfParser readGrammar(final String filename, final String start_rule, boolean debug_mode)
	{
		BnfParser parser = new BnfParser();
		parser.setDebugMode(debug_mode);
		try
		{
			Scanner grammar = new Scanner(GrammarTests.class.getResourceAsStream(filename));
			//String grammar = PackageFileReader.readPackageFile(GrammarTests.class, filename);
			parser.setGrammar(grammar);
		}
		catch (BnfParser.InvalidGrammarException e)
		{
			fail("Error parsing grammar file " + filename + ": " + e);
		}
		parser.setStartRule(start_rule);
		return parser;
	}
	
	/**
	 * A dummy logger; just checks that it has been called at least once
	 */
	protected static class DummyLogger extends Logger
	{
		boolean m_hasLogged = false;
		
		protected DummyLogger()
		{
			super("ca.uqac.lif.bullwinkle.GrammarTests.DummyLogger", null);
		}
		
		@Override
		public void log(Level l, String m)
		{
			m_hasLogged = true;
		}
		
		@Override
		public void log(Level l, String m, Object param1)
		{
			m_hasLogged = true;
		}
		
		public boolean hasLogged()
		{
			return m_hasLogged;
		}
		
	}
}
