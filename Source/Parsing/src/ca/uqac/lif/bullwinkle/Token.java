package ca.uqac.lif.bullwinkle;

public abstract class Token
{
  private String m_name;
  
  Token()
  {
    this("");
  }
  
  Token(String name)
  {
    super();
    setName(name);
  }
  
  public String getName()
  {
    return m_name;
  }
  
  void setName(final String name)
  {
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
  
  public abstract boolean matches(final Token tok);
  
  public abstract int match(final String s);
}
