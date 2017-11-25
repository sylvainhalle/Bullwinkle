/*
  Copyright 2014-2016 Sylvain Hallé
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
package ca.uqac.lif.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class MutableStringTest
{
	@Test
	public void testEmpty()
	{
		MutableString ms1 = new MutableString();
		MutableString ms2 = new MutableString();
		assertTrue(ms1.equals(ms2));
		assertTrue(ms1.isEmpty());
	}
	
	@Test
	public void testClear()
	{
		MutableString ms1 = new MutableString("abc");
		ms1.clear();
		assertEquals("", ms1.toString());
		assertEquals(0, ms1.length());
	}
	
	@Test
	public void testCopy()
	{
		MutableString ms1 = new MutableString("abc");
		MutableString ms2 = new MutableString(ms1);
		assertTrue(ms1.equals(ms2));
	}
	
	@Test
	public void testLength()
	{
		MutableString ms1 = new MutableString("abc");
		assertEquals(3, ms1.length());
	}
	
	@Test
	public void testEqualsNull()
	{
		MutableString ms1 = new MutableString("abc");
		assertFalse(ms1.equals(null));
	}
	
	@Test
	public void testEqualsNonString()
	{
		MutableString ms1 = new MutableString("abc");
		assertFalse(ms1.equals("abc"));
	}
	
	@Test
	public void testSplitEmpty()
	{
		MutableString ms1 = new MutableString();
		MutableString[] parts = ms1.split(",");
		assertEquals(1, parts.length);
		assertEquals("", parts[0].toString());
	}
	
	@Test
	public void testSplitNoChar()
	{
		MutableString ms1 = new MutableString("abc");
		MutableString[] parts = ms1.split(",");
		assertEquals(1, parts.length);
		assertEquals("abc", parts[0].toString());
	}
	
	@Test
	public void testSplit()
	{
		MutableString ms1 = new MutableString("abc,def,ghi");
		MutableString[] parts = ms1.split(",");
		assertEquals(3, parts.length);
		assertEquals("abc", parts[0].toString());
		assertEquals("def", parts[1].toString());
		assertEquals("ghi", parts[2].toString());
	}
	
	@Test
	public void testIndexOf1()
	{
		MutableString ms1 = new MutableString("abc,def,ghi");
		assertEquals(2, ms1.indexOf("c,"));
	}
	
	@Test
	public void testIndexOf2()
	{
		MutableString ms1 = new MutableString("abc,def,ghi");
		assertEquals(7, ms1.indexOf(",", 5));
	}
	
	@Test
	public void testSubstring1()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		MutableString ms2 = ms1.substring(3);
		assertEquals("abcdefghi", ms1.toString());
		assertEquals("defghi", ms2.toString());
	}
	
	@Test
	public void testSubstring2()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		MutableString ms2 = ms1.substring(3, 6);
		assertEquals("abcdefghi", ms1.toString());
		assertEquals("def", ms2.toString());
		assertFalse(ms2.isEmpty());
	}
	
	@Test
	public void testTruncate1()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		MutableString ms2 = ms1.truncateSubstring(3);
		assertEquals("defghi", ms1.toString());
		assertEquals("abc", ms2.toString());
	}
	
	@Test
	public void testTruncate2()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		MutableString ms2 = ms1.truncateSubstring(3, 6);
		assertEquals("abcghi", ms1.toString());
		assertEquals("def", ms2.toString());
		assertFalse(ms2.isEmpty());
	}
	
	@Test
	public void testReplaceAll()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		ms1.replaceAll("cd", "CD");
		assertEquals("abCDefghi", ms1.toString());
	}
	
	@Test
	public void testStartsWith()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		assertTrue(ms1.startsWith("abc"));
	}
	
	@Test
	public void testStartsWithIgnoreCase()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		assertTrue(ms1.startsWithIgnoreCase("ABC"));
	}
	
	@Test
	public void testEndsWith()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		assertTrue(ms1.endsWith("hi"));
	}
	
	@Test
	public void testEndsWithIgnoreCase()
	{
		MutableString ms1 = new MutableString("abcdefghi");
		assertTrue(ms1.endsWithIgnoreCase("HI"));
	}
	
	@Test
	public void testTrim()
	{
		MutableString ms1 = new MutableString("  foo ");
		ms1.trim();
		assertEquals("foo", ms1.toString());
	}
}
