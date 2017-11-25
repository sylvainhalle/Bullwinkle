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
