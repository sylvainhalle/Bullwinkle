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

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfRule;
import ca.uqac.lif.bullwinkle.TerminalToken;
import ca.uqac.lif.bullwinkle.TokenString;

public class BnfRuleTest
{

	@Test
	public void ruleWithNonTerminalAndTokens()
	{
		String rule = "<S> := ( <S> );";
		BnfRule brule = null;
		try
		{
			brule = BnfRule.parseRule(rule);
		}
		catch (BnfRule.InvalidRuleException e)
		{
			fail("Valid BNF rule '" + rule + "' has thrown an exception when parsed. " + e);
		}
		if (brule == null)
		{
			fail("Parsing valid BNF rule '" + rule + "' returned null; a non-null value was expected.");
		}
		List<TokenString> alternatives = brule.getAlternatives();
		int size = alternatives.size();
		int expected_size = 1;
		if (size != expected_size)
		{
			fail("Parsing valid BNF rule '" + rule + "' returned " + size + "cases, expected " + expected_size);
		}
		TokenString toks = alternatives.get(0);
		Set<TerminalToken> ter_toks = toks.getTerminalTokens();
		String expected_token = "(";
		if (!ter_toks.contains(new TerminalToken(expected_token)))
		{
			fail("Parsing valid BNF rule '" + rule + "' does not contain expected token '" + expected_token + "'");
		}
	}

}
