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

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.Builds;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder.BuildException;

/**
 * In this example, we show the use of the "pop" and "clean" options of the
 * <tt>@Builds</tt> annotation.
 * <ul>
 * <li>The "pop" option avoids us from popping objects from the object stack
 * manually; rather, the method can expect to receive the proper number of
 * objects in an array</li>
 * <li>The "clean" option removes from the argument array all elements that
 * match <em>terminal</em> tokens in the corresponding grammar rule. In the
 * object builder for this example, this removes all parentheses and symbols
 * and keeps only the operands of an operator.</li>
 * </ul>  
 */
public class BuildExamplePop 
{
	public static void main(String[] args) throws InvalidGrammarException, IOException, ParseException, BuildException
	{
		/* We first read a grammar and parse a simple expression */
		BnfParser parser = new BnfParser(BuildExamplePop.class.getResourceAsStream("Simple-Math.bnf"));
		ParseNode tree = parser.parse("(10 ÷ (2 × 3)) + (6 - 4)");
		
		/* We then create a builder, and ask it to create an ArithExp 
		 * object from the expression */
		MyBuilder builder = new MyBuilder();
		ArithExp exp = builder.build(tree);
		
		/* Just to show that it worked, we ask the resulting object to print
		 * itself. There are superfluous parentheses, due to the behaviour of
		 * the toString() method of ArithExp. */
		System.out.println(exp);
	}

	/**
	 * An object builder that creates ArithExp objects from the rules of the
	 * grammar. Notice how we can parse arithmetical expressions using 6 lines of
	 * BNF grammar, and about 15 lines for this builder. 
	 */
	public static class MyBuilder extends ParseTreeObjectBuilder<ArithExp>
	{
		@Builds(rule = "<add>", pop = true, clean = true)
		public ArithExp buildAdd(Object ... parts)
		{
			return new ArithExp.Add((ArithExp) parts[0], (ArithExp) parts[1]);
		}

		@Builds(rule = "<sub>", pop = true, clean = true)
		public ArithExp buildSub(Object ... parts)
		{
			return new ArithExp.Sub((ArithExp) parts[0], (ArithExp) parts[1]);
		}
		
		@Builds(rule = "<mul>", pop = true, clean = true)
		public ArithExp buildMul(Object ... parts)
		{
			return new ArithExp.Mul((ArithExp) parts[0], (ArithExp) parts[1]);
		}
		
		@Builds(rule = "<div>", pop = true, clean = true)
		public ArithExp buildDiv(Object ... parts)
		{
			return new ArithExp.Div((ArithExp) parts[0], (ArithExp) parts[1]);
		}

		@Builds(rule = "<num>", pop = true)
		public ArithExp buildNum(Object ... parts)
		{
			return new ArithExp.Num(Integer.parseInt((String) parts[0]));
		}
	}
}
