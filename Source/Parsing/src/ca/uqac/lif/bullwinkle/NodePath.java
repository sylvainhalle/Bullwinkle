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
	private static final long serialVersionUID = 1L;
	
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
