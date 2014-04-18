package ca.uqac.lif.bullwinkle;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TokenString extends LinkedList<Token>
{
  /**
   * Dummy UID
   */
  private static final long serialVersionUID = 1L;

  public final TokenString getCopy()
  {
    TokenString out = new TokenString();
    out.addAll(this);
    return out;
  }
  
  @Override
  public String toString()
  {
    StringBuilder out = new StringBuilder();
    boolean first = true;
    for (Token t: this)
    {
      if (!first)
        out.append(" ");
      first = false;
      out.append(t);
    }
    return out.toString();
  }
  
  Set<TerminalToken> getTerminalTokens()
  {
    Set<TerminalToken> out = new HashSet<TerminalToken>();
    for (Token t : this)
    {
      if (t instanceof TerminalToken)
      out.add((TerminalToken) t);
    }
    return out;
  }

}
