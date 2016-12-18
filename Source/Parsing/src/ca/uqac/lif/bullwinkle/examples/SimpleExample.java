/*
  Copyright 2014-2016 Sylvain Hallé
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

import java.io.File;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.output.GraphvizVisitor;

public class SimpleExample
{
  public static void main(String[] args)
  {
    try
    {
      BnfParser parser = new BnfParser(new File("examples/Simple-Math.bnf"));
      ParseNode node2 = parser.parse("(10 + (3 - 4))");
      GraphvizVisitor visitor = new GraphvizVisitor();
      node2.prefixAccept(visitor);
      System.out.println(visitor.toOutputString());
    }
    catch (Exception e)
    {
      System.err.println("Some error occurred");
      e.printStackTrace();
    }
  }
}
