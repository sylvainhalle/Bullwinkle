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
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.util.PackageFileReader;

public class ParserTest
{
  BnfParser m_parser;
  
  @Before
  public void setUp() throws Exception
  {
    m_parser = new BnfParser();
  }

  @Test
  public void simpleInvalidGrammar()
  {
    boolean has_error = false;
    BnfParser parser = new BnfParser();
    try
    {
      parser.setGrammar("A");
    }
    catch (BnfParser.InvalidGrammarException e)
    {
      has_error = true;
    }
    if (!has_error)
    {
      fail("Invalid grammar 'A' should throw an exception when parsed.");
    }
  }

  @Test
  public void simpleValidGrammar()
  {
    boolean has_error = false;
    BnfParser parser = new BnfParser();
    try
    {
      parser.setGrammar("<S> := a | b;");
    }
    catch (BnfParser.InvalidGrammarException e)
    {
      has_error = true;
    }
    if (has_error)
    {
      fail("Valid grammar has thrown an exception when parsed.");
    }
  }

  @Test
  public void simpleValidGrammarFromFile()
  {
    boolean has_error = false;
    BnfParser parser = new BnfParser();
    String grammar = PackageFileReader.readPackageFile(ParserTest.class, "data/Grammar-1.bnf");
    try
    {
      parser.setGrammar(grammar);
    } catch (InvalidGrammarException e)
    {
      has_error = true;
      e.printStackTrace();
    }
    if (has_error)
    {
      fail("Valid grammar has thrown an exception when parsed.");
    }
  }

}
