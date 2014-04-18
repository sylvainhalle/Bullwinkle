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

public class TerminalToken extends Token
{
  public TerminalToken(final String label)
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
    if (getName().compareTo(tok.getName()) == 0)
    {
      return true;
    }
    return false;
  }

  @Override
  public int match(final String s)
  {
    String name = getName();
    if (s.length() < name.length())
    {
      return -1;
    }
    if (getName().compareTo(s.substring(0, getName().length())) == 0)
    {
      return getName().length();
    }
    return 0;
  }

  public boolean equals(Object o)
  {
    if (o == null)
    {
      return false;
    }
    if (!(o instanceof TerminalToken))
    {
      return false;
    }
    return equals((TerminalToken) o);
  }

  private boolean equals(TerminalToken o)
  {
    if (o == null)
    {
      return false;
    }
    return getName().compareTo(o.getName()) == 0;
  }
}