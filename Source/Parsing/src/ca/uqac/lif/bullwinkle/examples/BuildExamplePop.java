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

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.Builds;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder.BuildException;

public class BuildExamplePop 
{
	public static void main(String[] args) throws InvalidGrammarException, IOException, ParseException, BuildException
	{
		BnfParser parser = new BnfParser(BuildExamplePop.class.getResourceAsStream("Simple-Math.bnf"));
		ParseNode tree = parser.parse("10 + (6 - 4)");
		MyBuilder builder = new MyBuilder();
		ArithExp exp = builder.build(tree);
		System.out.println(exp);
	}

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

		@Builds(rule = "<num>", pop = true)
		public ArithExp buildNum(Object ... parts)
		{
			return new ArithExp.Num(Integer.parseInt((String) parts[0]));
		}
	}
}
