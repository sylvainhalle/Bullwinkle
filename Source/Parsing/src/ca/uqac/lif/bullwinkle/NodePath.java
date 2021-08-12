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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for navigating a parse tree using XPath-like expressions.
 * @author Sylvain Hallé
 */
public final class NodePath implements Serializable
{
	/**
	 * Dummy UID
	 */
	private static final transient long serialVersionUID = 1L;
	
	/**
	 * The pattern used to parse paths
	 */
	protected static transient Pattern s_pattern = Pattern.compile("(.+)(\\[(\\d+)\\]){0,1}");
	
	private NodePath()
	{
		// No-op, won't be called
	}

	/**
	 * Get the first subtree matching a path expression. This actually returns
	 * the first element of {@link #getPath(ParseNode, String)}. What "first"
	 * means is actually the first subtree encountered by the lookup method.
	 * @param n The parse tree to look into
	 * @param path The path expression
	 * @return The first subtree matching the path expression
	 */
	public static ParseNode getPathFirst(ParseNode n, String path)
	{
		List<ParseNode> out = getPath(n, path);
		if (!out.isEmpty())
		{
			return out.get(0);
		}
		return null;
	}

	/**
	 * Get subtrees that match a given path
	 * @param n The parse tree to look into
	 * @param path The path expression
	 * @return A list of subtrees matching the path expression
	 */
	public static List<ParseNode> getPath(ParseNode n, String path)
	{
		String[] path_parts = path.split("\\.");
		List<String> path_list = new LinkedList<String>();
		for (String part : path_parts)
		{
			path_list.add(part);
		}
		// Add a fake root to call recursive function
		ParseNode new_root = new ParseNode();
		new_root.addChild(n);
		return getPath(new_root, path_list);
	}

	protected static List<ParseNode> getPath(ParseNode n, List<String> path)
	{
		List<ParseNode> out = new LinkedList<ParseNode>();
		if (path.isEmpty())
		{
			// Return me
			out.add(n);
			return out;
		}
		if (path.size() == 1 && path.get(0).compareTo("*") == 0)
		{
			// Return all children
			out.addAll(n.getChildren());
			return out;
		}
		String path_el = path.get(0);
		Matcher mat = s_pattern.matcher(path_el);
		if (!mat.find())
		{
			return out; // Error parsing path
		}
		String el_name = mat.group(1);
		String el_card_s = mat.group(3);
		int el_card = 0;
		if (el_card_s != null)
		{
			el_card = Integer.parseInt(el_card_s);
		}
		List<ParseNode> children = n.getChildren();
		int i = 0;
		for (ParseNode child : children)
		{
			if (el_name.compareTo(child.getToken()) == 0)
			{
				if (el_card < 0 || el_card == i)
				{
					// We have the right element
					LinkedList<String> new_path = new LinkedList<String>(path);
					new_path.removeFirst();
					List<ParseNode> new_nodes = getPath(child, new_path);
					out.addAll(new_nodes);
				}
				i++;
			}
		}
		return out;
	}
}
