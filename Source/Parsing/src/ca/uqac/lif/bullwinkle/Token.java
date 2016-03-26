package ca.uqac.lif.bullwinkle;

public abstract class Token
{
  /**
   * The token's name
   */
  private String m_name;
  
  /**
   * Whether the matching is sensitive to case. This is a program-wide
   * value
   */
  protected static transient boolean s_caseSensitive = true;
  
  public Token()
  {
    this("");
  }
  
  public Token(String name)
  {
    super();
    setName(name);
  }
  
  public static void setCaseSensitive(boolean b)
  {
    s_caseSensitive = b;
  }
  
  public String getName()
  {
    return m_name;
  }
  
  void setName(final /*@Nullable*/ String name)
  {
  	if (name != null)
  		m_name = name;
  }
  
  @Override
  public String toString()
  {
    return m_name;
  }
  
  @Override
  public int hashCode()
  {
    return m_name.hashCode();
  }
  
  @Override
  public boolean equals(Object o)
  {
  	if (o == null || !(o instanceof Token))
  	{
  		return false;
  	}
  	return equals((Token) o);
  }
  
  protected boolean equals(Token t)
  {
  	return t.getName().compareTo(m_name) == 0;
  }
  
  public abstract boolean matches(final Token tok);
  
  public abstract int match(final String s);
}
