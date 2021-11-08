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

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.bullwinkle.ParseNodeVisitor.VisitException;

/**
 * A node in the parse tree created by the parser.
 * @author Sylvain Hallé
 */
public class ParseNode
{
	/**
	 * A list of children of this parse node
	 */
	private ArrayList<ParseNode> m_children = null;

	/**
	 * The grammar token represented by this parse node
	 */
	private String m_token = null;

	/**
	 * The value (if any) represented by this parse node
	 */
	private String m_value = null;

	/**
	 * Creates an empty parse node
	 */
	ParseNode()
	{
		super();
		m_children = new ArrayList<ParseNode>();
	}

	/**
	 * Creates a parse node for a grammar token
	 * @param token The token
	 */
	ParseNode(String token)
	{
		this();
		setToken(token);
	}

	/**
	 * Gets the children of this parse node. This method returns a
	 * <em>new</em> list instance, and not the internal list the parse
	 * node uses to store its children.
	 * @return The list of children
	 */
	public List<ParseNode> getChildren()
	{
		if (m_children != null)
		{
			ArrayList<ParseNode> nodes = new ArrayList<ParseNode>(m_children.size());
			nodes.addAll(m_children);
			return nodes;
		}
		return new ArrayList<ParseNode>(0);
	}

	/**
	 * Gets the value of this parse node
	 * @return The value
	 */
	public String getValue()
	{
		return m_value;
	}

	/**
	 * Gets the token name associated to this parse node
	 * @return The token name
	 */
	public String getToken()
	{
		return m_token;
	}

	/**
	 * Sets the value for this parse node
	 * @param value The value
	 */
	void setValue(final String value)
	{
		m_value = value;
	}

	/**
	 * Sets the token name for this parse node
	 * @param token The token name
	 */
	void setToken(final String token)
	{
		m_token = token;
	}

	/**
	 * Adds a child to this parse node
	 * @param child A child
	 */
	void addChild(final ParseNode child)
	{
		m_children.add(child);
	}

	@Override
	public String toString()
	{
		return toString("");
	}

	/**
	 * Produces a string rendition of the contents of this parse node
	 * @param indent The indent to add to every line of the output
	 * @return The contents of this parse node as a string
	 */
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
	 * @throws VisitException Generic exception that can be thrown during the
	 *   traversal
	 */
	public void postfixAccept(ParseNodeVisitor visitor) throws VisitException
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
	 * @throws VisitException Generic exception that can be thrown during the
	 *   traversal
	 */
	public void prefixAccept(ParseNodeVisitor visitor) throws VisitException
	{
		visitor.visit(this);
		for (ParseNode n : m_children)
		{
			n.prefixAccept(visitor);
		}
		visitor.pop();
	}
	
	/**
	 * Creates a deep copy of the parse node and all its children.
	 * @return A copy of the parse node
	 */
	public ParseNode duplicate()
	{
		ParseNode new_n = new ParseNode(m_token);
		new_n.setValue(m_value);
		for (ParseNode child : m_children)
		{
			new_n.addChild(child.duplicate());
		}
		return new_n;
	}
	
	/**
	 * Deletes a child node at a given position.
	 * @param index The position of the child node to delete.
	 */
	public void deleteChild(int index)
	{
		m_children.remove(index);
	}
}
