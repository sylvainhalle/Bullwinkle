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
package ca.uqac.lif.bullwinkle.examples;

import java.io.IOException;
import java.util.Stack;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.Builds;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder.BuildException;

public class BuildExampleStack 
{
	public static void main(String[] args) throws InvalidGrammarException, IOException, ParseException, BuildException
	{
		BnfParser parser = new BnfParser(BuildExampleStack.class.getResourceAsStream("PN-Math.bnf"));
		ParseNode tree = parser.parse("+ 10 - 3 4");
		MyBuilder builder = new MyBuilder();
		ArithExp exp = builder.build(tree);
		System.out.println(exp);
	}

	public static class MyBuilder extends ParseTreeObjectBuilder<ArithExp>
	{
		@Builds(rule = "<add>")
		public void buildAdd(Stack<Object> stack)
		{
			ArithExp e2 = (ArithExp) stack.pop();
			ArithExp e1 = (ArithExp) stack.pop();
			stack.pop(); // symbol
			stack.push(new ArithExp.Add(e1, e2));
		}

		@Builds(rule = "<sub>")
		public void buildSub(Stack<Object> stack)
		{
			ArithExp e2 = (ArithExp) stack.pop();
			ArithExp e1 = (ArithExp) stack.pop();
			stack.pop(); // symbol
			stack.push(new ArithExp.Sub(e1, e2));
		}

		@Builds(rule = "<num>")
		public void buildNum(Stack<Object> stack)
		{
			String s = (String) stack.pop(); // symbol
			stack.push(new ArithExp.Num(Integer.parseInt(s)));
		}
	}
}
