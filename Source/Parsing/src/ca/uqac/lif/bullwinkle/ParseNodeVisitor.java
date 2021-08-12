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

import ca.uqac.lif.util.EmptyException;

/**
 * Implementation of the <em>Visitor</em> design pattern for a parse tree.
 * @author Sylvain Hallé
 */
public interface ParseNodeVisitor
{
	/**
	 * Method called when entering a new node in the parse tree
	 * @param node The parse node
	 * @throws VisitException Thrown if something wrong happens
	 */
	public void visit(ParseNode node) throws VisitException;

	/**
	 * Method called when leaving a node in the parse tree
	 */
	public void pop();

	/**
	 * Exception container that can be thrown when visiting a parse node
	 */
	public static class VisitException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final transient long serialVersionUID = 1L;

		public VisitException(String message)
		{
			super(message);
		}

		public VisitException(Throwable t)
		{
			super(t);
		}  
	}
}
