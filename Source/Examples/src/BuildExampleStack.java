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

import java.io.IOException;
import java.util.Deque;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.Builds;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder.BuildException;

/**
 * In this example, we show how the <tt>@Builds</tt> annotation can be used
 * to identify methods that handle specific non-terminal symbols in a
 * grammar.
 */
public class BuildExampleStack 
{
	public static void main(String[] args) throws InvalidGrammarException, IOException, ParseException, BuildException
	{
		/* We first read a grammar and parse a simple expression */
		BnfParser parser = new BnfParser(BuildExampleStack.class.getResourceAsStream("PN-Math.bnf"));
		ParseNode tree = parser.parse("+ 10 - 3 4");
		
		/* We then create a builder, and ask it to create an ArithExp 
		 * object from the expression */
		MyBuilder builder = new MyBuilder();
		ArithExp exp = builder.build(tree);
		
		/* Just to show that it worked, we ask the resulting object to print
		 * itself. Notice how the way ArithExp objects print themselves is
		 * different from the syntax that was used to create them. */
		System.out.println(exp);
	}

	public static class MyBuilder extends ParseTreeObjectBuilder<ArithExp>
	{
		@Builds(rule = "<add>")
		public void buildAdd(Deque<Object> stack)
		{
			ArithExp e2 = (ArithExp) stack.pop();
			ArithExp e1 = (ArithExp) stack.pop();
			stack.pop(); // symbol
			stack.push(new ArithExp.Add(e1, e2));
		}

		@Builds(rule = "<sub>")
		public void buildSub(Deque<Object> stack)
		{
			ArithExp e2 = (ArithExp) stack.pop();
			ArithExp e1 = (ArithExp) stack.pop();
			stack.pop(); // symbol
			stack.push(new ArithExp.Sub(e1, e2));
		}

		@Builds(rule = "<num>")
		public void buildNum(Deque<Object> stack)
		{
			String s = (String) stack.pop(); // symbol
			stack.push(new ArithExp.Num(Integer.parseInt(s)));
		}
	}
}
