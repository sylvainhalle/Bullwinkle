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

import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;

public class ParserTest
{
	BnfParser m_parser;

	@Before
	public void setUp() throws Exception
	{
		m_parser = new BnfParser();
	}

	@Test
	public void simpleInvalidGrammar()
	{
		boolean has_error = false;
		BnfParser parser = new BnfParser();
		try
		{
			parser.setGrammar("A");
		}
		catch (BnfParser.InvalidGrammarException e)
		{
			has_error = true;
		}
		if (!has_error)
		{
			fail("Invalid grammar 'A' should throw an exception when parsed.");
		}
	}

	@Test
	public void simpleValidGrammar()
	{
		boolean has_error = false;
		BnfParser parser = new BnfParser();
		try
		{
			parser.setGrammar("<S> := a | b;");
		}
		catch (BnfParser.InvalidGrammarException e)
		{
			has_error = true;
		}
		if (has_error)
		{
			fail("Valid grammar has thrown an exception when parsed.");
		}
	}

	@Test
	public void simpleValidGrammarFromFile()
	{
		boolean has_error = false;
		BnfParser parser = new BnfParser();
		Scanner grammar = new Scanner(ParserTest.class.getResourceAsStream("data/Grammar-1.bnf"));
		try
		{
			parser.setGrammar(grammar);
		} catch (InvalidGrammarException e)
		{
			has_error = true;
			e.printStackTrace();
		}
		if (has_error)
		{
			fail("Valid grammar has thrown an exception when parsed.");
		}
	}

	@Test
	public void getAlternativesTest()
	{
		boolean has_error = false;
		BnfParser parser = new BnfParser();
		try
		{
			parser.setGrammar("<S> := <a> | b;\n<a> := c;");
		}
		catch (BnfParser.InvalidGrammarException e)
		{
			has_error = true;
		}
		if (has_error)
		{
			fail("Valid grammar has thrown an exception when parsed.");
		}

		List<String> alternatives = parser.getAlternatives("<S>");

		assertTrue(alternatives.get(0).compareTo("<a>") == 0);
		assertTrue(alternatives.get(1).compareTo("b") == 0);
	}

}
