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
