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

/**
 * A simple class (and descendants) to represent arithmetical
 * expressions. These classes are used by {@link BuildExampleStack} and
 * {@lik BuildExamplePop} to illustrate the use of the
 * {@link ParseTreeObjectBuilder}.
 * @author Sylvain Hallé
 */
public abstract class ArithExp
{
	/**
	 * Object that represents an integer
	 */
	public static class Num extends ArithExp
	{
		int n;

		public Num(int n)
		{
			this.n = n;
		}

		@Override
		public String toString()
		{
			return Integer.toString(n);
		}
	}

	/**
	 * Generic class that represents binary operators, with a left and a right
	 * operand
	 */
	public static abstract class BinaryExp extends ArithExp
	{
		ArithExp left;
		ArithExp right;
		String symbol;

		public BinaryExp(ArithExp left, ArithExp right, String symbol)
		{
			this.left = left;
			this.right = right;
			this.symbol = symbol;
		}

		@Override
		public String toString()
		{
			return "(" + left.toString() + ")" + symbol + "(" + right.toString() + ")"; 
		}
	}

	/**
	 * Addition
	 */
	public static class Add extends BinaryExp
	{
		public Add(ArithExp left, ArithExp right) 
		{
			super(left, right, "+");
		}
	}

	/**
	 * Subtraction
	 */
	public static class Sub extends BinaryExp
	{
		public Sub(ArithExp left, ArithExp right) 
		{
			super(left, right, "-");
		}
	}

	/**
	 * Multiplication
	 */
	public static class Mul extends BinaryExp
	{
		public Mul(ArithExp left, ArithExp right) 
		{
			super(left, right, "×");
		}
	}

	/**
	 * Division
	 */
	public static class Div extends BinaryExp
	{
		public Div(ArithExp left, ArithExp right) 
		{
			super(left, right, "÷");
		}
	}
}