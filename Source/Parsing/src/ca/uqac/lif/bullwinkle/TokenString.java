package ca.uqac.lif.bullwinkle;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TokenString extends LinkedList<Token>
{
  /**
   * Dummy UID
   */
  transient private static final long serialVersionUID = 1L;
  
  /**
   * Whether this case symbol should remain at the end of the
   * alternatives for a rule
   */
  private boolean m_tryLast = false;
  
  /**
   * Creates a new empty token string
   */
  public TokenString()
  {
    super();
  }
  
  /**
   * Tells whether this element should be tried last when parsing
   * @return True if it should be tried last
   */
  public boolean getTryLast()
  {
	  return m_tryLast;
  }

  /**
   * Tells whether this element should be tried last when parsing
   * @param b Set to true if it should be tried last
   */
  public void setTryLast(boolean b)
  {
	 m_tryLast = b; 
  }
  

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
