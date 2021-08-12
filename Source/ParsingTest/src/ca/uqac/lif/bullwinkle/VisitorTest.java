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

import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseNodeVisitor.VisitException;
import ca.uqac.lif.bullwinkle.output.GraphvizVisitor;
import ca.uqac.lif.bullwinkle.output.IndentedTextVisitor;
import ca.uqac.lif.bullwinkle.output.XmlVisitor;

public class VisitorTest
{
	@Test
	public void testPrefix() throws VisitException
	{
		ParseNode root = new ParseNode("foo");
		{
			ParseNode c = new ParseNode("bar");
			{
				ParseNode d = new ParseNode("0");
				c.addChild(d);
			}
			{
				ParseNode d = new ParseNode("1");
				c.addChild(d);
			}
			root.addChild(c);
		}
		PrintVisitor v = new PrintVisitor();
		root.prefixAccept(v);
		String obtained = v.getString();
		assertEquals("foo,bar,0,pop,1,pop,pop,pop,", obtained);
	}
	
	@Test
	public void testPostfix() throws VisitException
	{
		ParseNode root = new ParseNode("foo");
		{
			ParseNode c = new ParseNode("bar");
			{
				ParseNode d = new ParseNode("0");
				c.addChild(d);
			}
			{
				ParseNode d = new ParseNode("1");
				c.addChild(d);
			}
			root.addChild(c);
		}
		PrintVisitor v = new PrintVisitor();
		root.postfixAccept(v);
		String obtained = v.getString();
		assertEquals("0,pop,1,pop,bar,pop,foo,pop,", obtained);
	}
	
	@Test
	public void testText() throws InvalidGrammarException, ParseException, VisitException
	{
		String expression = "SELECT a FROM t";
		BnfParser parser = new BnfParser(VisitorTest.class.getResourceAsStream("data/Grammar-0.bnf"));
		ParseNode node = parser.parse(expression);
		IndentedTextVisitor visitor = new IndentedTextVisitor();
		node.postfixAccept(visitor);
		String output = visitor.toOutputString();
		assertNotNull(output);
	}
	
	@Test
	public void testGraphviz() throws InvalidGrammarException, ParseException, VisitException
	{
		String expression = "SELECT a FROM t";
		BnfParser parser = new BnfParser(VisitorTest.class.getResourceAsStream("data/Grammar-0.bnf"));
		ParseNode node = parser.parse(expression);
		GraphvizVisitor visitor = new GraphvizVisitor();
		node.postfixAccept(visitor);
		String output = visitor.toOutputString();
		assertNotNull(output);
	}
	
	@Test
	public void testXml() throws InvalidGrammarException, ParseException, VisitException
	{
		String expression = "SELECT a FROM t";
		BnfParser parser = new BnfParser(VisitorTest.class.getResourceAsStream("data/Grammar-0.bnf"));
		ParseNode node = parser.parse(expression);
		XmlVisitor visitor = new XmlVisitor();
		visitor.setTokenElementName("tok");
		visitor.setTopElementName("top");
		node.postfixAccept(visitor);
		String output = visitor.toOutputString();
		assertNotNull(output);
		assertTrue(output.contains("<top>"));
		assertTrue(output.contains("<tok>"));
	}
	
	public static class PrintVisitor implements ParseNodeVisitor
	{
		StringBuilder m_builder = new StringBuilder();
		
		public String getString()
		{
			return m_builder.toString();
		}

		@Override
		public void visit(ParseNode node) throws VisitException
		{
			m_builder.append(node.getToken()).append(",");
			
		}

		@Override
		public void pop()
		{
			m_builder.append("pop,");
		}
	}
}
