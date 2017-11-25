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
