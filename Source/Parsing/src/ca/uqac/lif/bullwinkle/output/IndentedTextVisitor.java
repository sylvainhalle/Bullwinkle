/*
  Copyright 2014 Sylvain Hallé
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
package ca.uqac.lif.bullwinkle.output;

import java.util.Stack;

import ca.uqac.lif.bullwinkle.ParseNode;

public class IndentedTextVisitor implements OutputFormatVisitor
{
  private Stack<String> m_indents;
  
  private StringBuilder m_output;
  
  private static final String s_indent = " ";
  
  public IndentedTextVisitor()
  {
    super();
    m_indents = new Stack<String>();
    m_output = new StringBuilder();
    m_indents.push("");
  }

  @Override
  public void visit(ParseNode node)
  {
    String label = node.getValue();
    String current_indent = m_indents.peek() + s_indent;
    if (label == null)
    {
      label = node.getToken();
      m_indents.push(current_indent);
      current_indent = m_indents.peek();
      m_output.append(current_indent).append(label).append("\n");
    }
    else
    {
      // Remove symbols surrounding the name of a rule
      m_output.append(current_indent).append(label).append("\n");
      m_indents.push(current_indent);
    }
  }

  @Override
  public void pop()
  {
    m_indents.pop();
  }

  @Override
  public String toOutputString()
  {
    return m_output.toString();
  }

}
