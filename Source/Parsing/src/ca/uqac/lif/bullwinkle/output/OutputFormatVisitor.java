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
package ca.uqac.lif.bullwinkle.output;

import ca.uqac.lif.bullwinkle.ParseNodeVisitor;

/**
 * Traverses a parse tree and converts it into another textual format.
 * @author Sylvain Hallé
 */
public interface OutputFormatVisitor extends ParseNodeVisitor
{
	/**
	 * Gets the string corresponding to the converted parse tree created by
	 * this visitor.
	 * This method must be called <em>after</em> visiting the parse
	 * tree using {@link ca.uqac.lif.bullwinkle.ParseNode#postfixAccept(ParseNodeVisitor) postfixAccept()}
	 * or {@link ca.uqac.lif.bullwinkle.ParseNode#prefixAccept(ParseNodeVisitor) prefixAccept()}
	 * on the root of the parse tree.
	 * @return A string representing the converted parse tree
	 */
	public String toOutputString();
}
