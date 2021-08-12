/* MIT License
 *
 * Copyright 2014-2021 Sylvain Hallé
 *
 * Laboratoire d'informatique formelle
 * Université du Québec à Chicoutimi, Canada
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
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
