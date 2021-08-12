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

import java.util.Deque;

import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder.BuildException;

public class ObjectBuilderTest
{
	@Test
	public void testBuilder() throws InvalidGrammarException, ParseException, BuildException
	{
		BnfParser parser = new BnfParser(ObjectBuilderTest.class.getResourceAsStream("data/DummyObject.bnf"));
		ParseNode tree = parser.parse("+ 3 3");
		DummyBuilder db = new DummyBuilder();
		DummyObject dob = db.build(tree);
		assertNotNull(dob);
		assertTrue(dob instanceof Add);
	}
	
	@Test
	public void testBuilderPop() throws InvalidGrammarException, ParseException, BuildException
	{
		BnfParser parser = new BnfParser(ObjectBuilderTest.class.getResourceAsStream("data/DummyObject.bnf"));
		ParseNode tree = parser.parse("+ 3 3");
		DummyBuilderPop db = new DummyBuilderPop();
		DummyObject dob = db.build(tree);
		assertNotNull(dob);
		assertTrue(dob instanceof Add);
	}
	
	@Test(expected=BuildException.class)
	public void testBuilderInvalid1() throws InvalidGrammarException, ParseException, BuildException
	{
		// Will cause an IllegalArgumentException
		BnfParser parser = new BnfParser(ObjectBuilderTest.class.getResourceAsStream("data/DummyObject.bnf"));
		ParseNode tree = parser.parse("+ 3 3");
		InvalidBuilder db = new InvalidBuilder();
		db.build(tree);
	}
	
	@Test(expected=BuildException.class)
	public void testBuilderInvalid2() throws InvalidGrammarException, ParseException, BuildException
	{
		// Will cause an IllegalAccessException
		BnfParser parser = new BnfParser(ObjectBuilderTest.class.getResourceAsStream("data/DummyObject.bnf"));
		ParseNode tree = parser.parse("- 3 3");
		InvalidBuilder db = new InvalidBuilder();
		db.build(tree);
	}
	
	public static class DummyBuilder extends ParseTreeObjectBuilder<DummyObject>
	{
		@Builds(rule="<add>")
		public void handle(Deque<Object> q)
		{
			DummyObject o2 = (DummyObject) q.pop(); // o2
			DummyObject o1 = (DummyObject) q.pop(); // o1
			q.pop(); // symbol
			Add a = new Add();
			a.left = o1;
			a.right = o2;
			q.push(a);
		}
		
		@Builds(rule="<num>")
		public void handleNum(Deque<Object> q)
		{
			int n = Integer.parseInt((String) q.pop());
			Num new_n = new Num();
			new_n.n = n;
			q.push(new_n);
		}
	}
	
	public static class DummyBuilderPop extends ParseTreeObjectBuilder<DummyObject>
	{
		@Builds(rule="<add>", pop=true)
		public Add handle(Object ... parts)
		{
			assertEquals(3, parts.length);
			Add a = new Add();
			a.right = (DummyObject) parts[2];
			a.left = (DummyObject) parts[1];
			return a;
		}
		
		@Builds(rule="<num>", pop=true)
		public Num handleNum(Object ... parts)
		{
			assertEquals(1, parts.length);
			int n = Integer.parseInt((String) parts[0]);
			Num new_n = new Num();
			new_n.n = n;
			return new_n;
		}
	}
	
	public static class InvalidBuilder extends ParseTreeObjectBuilder<DummyObject>
	{
		@Builds(rule="<add>")
		public void handle()
		{
			// Will throw an exception: incorrect arguments
		}
		
		@Builds(rule="<sub>")
		private void handleSub(Deque<Object> q)
		{
			// Will throw an exception: method is private
			DummyObject o2 = (DummyObject) q.pop(); // o2
			DummyObject o1 = (DummyObject) q.pop(); // o1
			q.pop(); // symbol
			Add a = new Add();
			a.left = o1;
			a.right = o2;
			q.push(a);
		}
		
		@Builds(rule="<num>")
		public void handleNum(Deque<Object> q)
		{
			int n = Integer.parseInt((String) q.pop());
			Num new_n = new Num();
			new_n.n = n;
			q.push(new_n);
		}
	}
	
	public static class DummyObject
	{
		
	}
	
	public static class Add extends DummyObject
	{
		DummyObject left;
		DummyObject right;
	}
	
	public static class Num extends DummyObject
	{
		int n;
	}
}
