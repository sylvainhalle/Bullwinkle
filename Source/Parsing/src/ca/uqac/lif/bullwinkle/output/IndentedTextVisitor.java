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

package ca.uqac.lif.bullwinkle.output;

import java.util.ArrayDeque;
import java.util.Deque;

import ca.uqac.lif.bullwinkle.ParseNode;

/**
 * Renders a parse tree into a sequence of indented lines of text. For
 * example, the following parse tree:
 * <pre>
 *   &lt;S&gt;
 *  /   \
 * foo  &lt;A&gt;
 *       |
 *      bar
 * </pre>
 * will be rendered as
 * <pre>
 * &lt;S&gt;
 *  foo
 *  &lt;A&gt;
 *   bar
 * </pre>
 * This can be an easy way to use Bullwinkle as an external program to
 * perform the parsing, and to pass a machine-readable parse tree to another
 * program.
 * 
 * @author Sylvain Hallé
 */
public class IndentedTextVisitor implements OutputFormatVisitor
{
	/**
	 * A stack of string, keeping the indents to apply to each line of text
	 */
	private Deque<String> m_indents;

	/**
	 * A string builder where the output is progressively built
	 */
	private StringBuilder m_output;

	/**
	 * The amount of spaces to apply to each successive indent level
	 */
	private static final String s_indent = " ";

	/**
	 * Creates a new IndentedTextVisitor
	 */
	public IndentedTextVisitor()
	{
		super();
		m_indents = new ArrayDeque<String>();
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
