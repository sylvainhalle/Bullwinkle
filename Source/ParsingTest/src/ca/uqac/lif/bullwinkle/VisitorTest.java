/*
  Copyright 2014-2017 Sylvain Hallé
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
