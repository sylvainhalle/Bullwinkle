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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.util.EmptyException;

/**
 * Build an object by traversing a parse tree.
 * @author Sylvain Hallé
 *
 * @param <T> The type of the object to be built
 */
public abstract class ParseTreeObjectBuilder<T> implements ParseNodeVisitor
{
	/**
	 * A stack of arbitrary objects. This stack is manipulated
	 * by the various methods that are called when visiting a parse
	 * tree.
	 */
	protected Deque<Object> m_stack;

	/**
	 * The object to be built and returned at the end of the visit of
	 * the parse tree.
	 */
	protected T m_builtObject = null;

	/**
	 * A map that associates non-terminal symbols of the grammar with
	 * specific methods to be called when visiting that symbol in the
	 * traversal of the parse tree.
	 */
	protected final Map<String,MethodAnnotation> m_methods;

	/**
	 * Creates a new object builder
	 */
	public ParseTreeObjectBuilder()
	{
		super();
		m_methods = new HashMap<String,MethodAnnotation>();
		fillMethods(m_methods);
	}

	/**
	 * Build an object from a parse tree
	 * @param tree The parse tree
	 * @return The object
	 * @throws BuildException Generic exception that can be thrown during the
	 *   build process
	 */
	public final synchronized T build(ParseNode tree) throws BuildException
	{
		if (tree == null)
		{
			throw new BuildException("The input tree is null");
		}
		m_stack = new ArrayDeque<Object>();
		try
		{
			preVisit();
			tree.postfixAccept(this);
			m_builtObject = postVisit(m_stack);
			return m_builtObject;
		}
		catch (VisitException e)
		{
			throw new BuildException(e);
		}
	}

	/**
	 * Perform some task before starting the traversal of a parse tree
	 */
	protected synchronized void preVisit()
	{
		// Nothing
	}

	/**
	 * Perform some task after the traversal of a parse tree. By default,
	 * we return the object that is at the top of the stack. Override this
	 * method to perform something else.
	 * @param stack The stack, as it is after traversing the parse tree
	 * @return The object that should be returned to the user
	 */
	@SuppressWarnings("unchecked")
	protected synchronized T postVisit(Deque<Object> stack)
	{
		if (stack.isEmpty())
		{
			return null;
		}
		return (T) stack.peek();
	}

	/**
	 * Retrieves all the methods that have a <tt>@Builda</tt>
	 * annotation in the current class
	 * @param methods A map of methods
	 */
	protected synchronized void fillMethods(Map<String,MethodAnnotation> methods)
	{
		Method[] ms = getClass().getDeclaredMethods();
		for (Method method : ms)
		{
			Builds an = method.getAnnotation(Builds.class);
			if (an != null)
			{
				String non_terminal = an.rule();
				methods.put(non_terminal, new MethodAnnotation(method, an.pop(), an.clean()));
			}
		}
	}

	@Override
	public synchronized void visit(ParseNode node) throws VisitException
	{
		try
		{
			handleNode(node);
		}
		catch (SecurityException e)
		{
			throw new VisitException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new VisitException(e);
		}
		catch (IllegalArgumentException e) 
		{
			throw new VisitException(e);
		}
		catch (InvocationTargetException e) 
		{
			throw new VisitException(e);
		}
	}

	/**
	 * Performs the actual handling of a parse node
	 * @param node The parse node
	 * @throws IllegalAccessException May be thrown when attempting to invoke a method
	 * @throws InvocationTargetException May be thrown when attempting to invoke a method
	 */
	@SuppressWarnings({"squid:S3878"})
	protected void handleNode(ParseNode node) throws IllegalAccessException, InvocationTargetException
	{
		String token_name = node.getToken();
		// Is it a non-terminal symbol?
		if (!token_name.startsWith("<"))
		{
			m_stack.push(token_name);
			return;
		}
		// Is there a stack method that handles this non-terminal?
		if (m_methods.containsKey(token_name))
		{
			MethodAnnotation ma = m_methods.get(token_name);
			if (!ma.pop)
			{
				ma.m.invoke(this, m_stack);
				return;
			}
			List<Object> argument_list = new LinkedList<Object>();
			List<ParseNode> children = node.getChildren();
			for (int i = children.size() - 1; i >= 0; i--)
			{
				ParseNode child = children.get(i);
				Object o = m_stack.pop();
				if (!ma.clean || child.getToken().startsWith("<"))
				{
					argument_list.add(0, o);
				}
			}
			Object[] arguments = argument_list.toArray();
			// We ignore warning S3878 here; the call to invoke
			// *requires* the varargs to be put into an array.
			Object o = ma.m.invoke(this, new Object[]{arguments});
			if (o != null)
			{
				m_stack.push(o);
			}	
		}
	}

	@Override
	public synchronized void pop()
	{
		// Nothing to do. This method is there only to respect the interface
		// of ParseNodeVisitor.
	}

	/**
	 * Exception container to be thrown when attempting to build an object
	 */
	public static class BuildException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;

		public BuildException(String message)
		{
			super(message);
		}

		public BuildException(Throwable t)
		{
			super(t);
		}
	}

	protected static class MethodAnnotation
	{
		Method m;
		boolean pop = false;
		boolean clean = false;

		public MethodAnnotation(Method m, boolean pop, boolean clean)
		{
			this.m = m;
			this.pop = pop;
			this.clean = clean;
		}
	}
}