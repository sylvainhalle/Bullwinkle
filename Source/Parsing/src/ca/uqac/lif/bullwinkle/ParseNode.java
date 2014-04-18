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
package ca.uqac.lif.bullwinkle;

import java.util.LinkedList;
import java.util.List;

public class ParseNode
{
  private LinkedList<ParseNode> m_children = null;
  
  private String m_token = null;
  
  private String m_value = null;
  
  ParseNode()
  {
    super();
    m_children = new LinkedList<ParseNode>();
  }
  
  public List<ParseNode> getChildren()
  {
    LinkedList<ParseNode> nodes = new LinkedList<ParseNode>();
    if (m_children != null)
    {
      nodes.addAll(m_children);
    }
    return nodes;
  }
  
  public String getValue()
  {
    return m_value;
  }
  
  public String getToken()
  {
    return m_token;
  }
  
  void setValue(final String value)
  {
    m_value = value;
  }
  
  void setToken(final String token)
  {
    m_token = token;
  }
  
  void addChild(final ParseNode child)
  {
    m_children.add(child);
  }
  
  @Override
  public String toString()
  {
    return toString("");
  }
  
  private String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append(m_token).append("\n");
    String n_indent = indent + " ";
    for (ParseNode n : m_children)
    {
      out.append(n.toString(n_indent));
    }
    return out.toString();
  }
  
  /**
   * Returns the size of the parse tree
   * @return The number of nodes
   */
  public int getSize()
  {
    int size = 1;
    for (ParseNode node : m_children)
    {
      size += node.getSize();
    }
    return size;
  }
  
  /**
   * Postfix traversal of the parse tree by a visitor
   * @param visitor The visitor
   */
  public void postfixAccept(ParseNodeVisitor visitor)
  {
    for (ParseNode n : m_children)
    {
      n.postfixAccept(visitor);
    }
    visitor.visit(this);
    visitor.pop();
  }
  
  /**
   * Prefix traversal of the parse tree by a visitor
   * @param visitor The visitor
   */
  public void prefixAccept(ParseNodeVisitor visitor)
  {
    visitor.visit(this);
    for (ParseNode n : m_children)
    {
      n.prefixAccept(visitor);
    }
    visitor.pop();
  }
  
}
