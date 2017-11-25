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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class CliTest
{
	@Test
	public void testCli1()
	{
		String[] args = {};
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, null, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_ARGUMENTS, retcode);
	}
	
	@Test
	public void testCli2()
	{
		String[] args = {"-foo"};
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, null, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_ARGUMENTS, retcode);
	}
	
	@Test
	public void testCli3()
	{
		String[] args = {"Grammar-0.bnf"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_PARSE, retcode);
	}
	
	@Test
	public void testCliXml()
	{
		String[] args = {"--format", "xml", "Grammar-0.bnf"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT foo FROM bar".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_OK, retcode);
		String s = new String(baos_stdout.toByteArray());
		assertFalse(s.isEmpty());
		assertTrue(s.contains("<token>"));
	}
	
	@Test
	public void testCliTxt()
	{
		String[] args = {"--format", "txt", "Grammar-0.bnf"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT foo FROM bar".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_OK, retcode);
		String s = new String(baos_stdout.toByteArray());
		assertFalse(s.isEmpty());
		assertTrue(s.contains("SELECT"));
	}
	
	@Test
	public void testCliDot()
	{
		String[] args = {"--format", "dot", "Grammar-0.bnf"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT foo FROM bar".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_OK, retcode);
		String s = new String(baos_stdout.toByteArray());
		assertFalse(s.isEmpty());
		assertTrue(s.contains("digraph"));
	}
	
	@Test
	public void testCliFoo()
	{
		String[] args = {"--format", "foo", "Grammar-0.bnf"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT foo FROM bar".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_ARGUMENTS, retcode);
	}
	
	@Test
	public void testCliFromFile()
	{
		String[] args = {"--format", "dot", "Grammar-0.bnf", "TextToParse.txt"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT foo FROM bar".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_OK, retcode);
	}
	
	@Test
	public void testCliFromNonexistentFile()
	{
		String[] args = {"--format", "dot", "Grammar-0.bnf", "doesnotexist.txt"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT foo FROM bar".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_IO, retcode);
	}
	
	@Test
	public void testCli5()
	{
		String[] args = {"nonexistentfile.bnf"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT foo FROM bar".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_IO, retcode);
	}
	
	@Test
	public void testCli6()
	{
		String[] args = {"--format", "dot", "Grammar-0.bnf"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_PARSE, retcode);
	}
	
	@Test
	public void testInvalidGrammar()
	{
		String[] args = {"Grammar-invalid-1.bnf"};
		ByteArrayInputStream bais_stdin = new ByteArrayInputStream("SELECT".getBytes());
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, bais_stdin, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_GRAMMAR, retcode);
	}
	
	@Test
	public void testCliVersion()
	{
		String[] args = {"--version"};
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, null, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_OK, retcode);
	}
	
	@Test
	public void testCliHelp()
	{
		String[] args = {"--help", "--verbosity", "0"};
		ByteArrayOutputStream baos_stdout = new ByteArrayOutputStream();
		ByteArrayOutputStream baos_stderr = new ByteArrayOutputStream();
		int retcode = BullwinkleCli.doMain(args, null, new PrintStream(baos_stdout), new PrintStream(baos_stderr));
		assertEquals(BullwinkleCli.ERR_OK, retcode);
	}
}
