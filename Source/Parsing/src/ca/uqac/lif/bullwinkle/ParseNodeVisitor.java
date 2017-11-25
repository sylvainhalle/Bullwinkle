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
		private static final long serialVersionUID = 1L;

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
