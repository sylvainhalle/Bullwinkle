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
