/*
  Copyright 2014 Sylvain Hallé
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTerminalToken extends TerminalToken
{
  public RegexTerminalToken(final String label)
  {
    super(label);
  }

  private Pattern m_pattern;
  
  @Override
  public void setName(final String s)
  {
    super.setName(s);
    m_pattern = Pattern.compile(s);
  }

  @Override
  public boolean matches(final Token tok)
  {
    String contents = tok.getName();
    Matcher matcher = m_pattern.matcher(contents);
    return matcher.matches();
  }

  @Override
  public int match(final String s)
  {
    Matcher matcher = m_pattern.matcher(s);
    if (matcher.find())
    {
      return matcher.end();
    }
    return -1;
  }
  
  @Override
  public String toString()
  {
    StringBuilder out = new StringBuilder();
    out.append(getName());
    return out.toString();
  }

}
