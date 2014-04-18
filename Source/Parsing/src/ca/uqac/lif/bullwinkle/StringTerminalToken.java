package ca.uqac.lif.bullwinkle;

public class StringTerminalToken extends TerminalToken
{
  public StringTerminalToken(String label)
  {
    super(label);
  }

  @Override
  public boolean matches(final Token tok)
  {   
    if (tok == null)
    {
      return false;
    }
    // Anything matches a string
    return true;
  }
  
  @Override
  public int match(final String s)
  {   
    if (s == null)
    {
      return 0;
    }
    // Our parser 
    return s.indexOf(" ");
  }
}
