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

//import static org.junit.Assert.*;

//import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

public class PathTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void test1() throws ParseException
	{
		BnfParser parser = GrammarTests.readGrammar("data/Grammar-0.bnf", "<S>", false);
		String expression = "SELECT a FROM t";
		ParseNode pn = parser.parse(expression);
		String path = "<S>.<selection>.<criterion>";
		/*List<ParseNode> result =*/ NodePath.getPath(pn, path);
		//fail("Not yet implemented");
	}

	@Test
	public void test2() throws ParseException
	{
		BnfParser parser = GrammarTests.readGrammar("data/Grammar-0.bnf", "<S>", false);
		String expression = "SELECT a FROM t";
		ParseNode pn = parser.parse(expression);
		String path = "<S>.<selection>.*";
		/*List<ParseNode> result =*/ NodePath.getPath(pn, path);
		//fail("Not yet implemented");
	}

}
