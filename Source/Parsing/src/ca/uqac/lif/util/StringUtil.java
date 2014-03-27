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
package ca.uqac.lif.util;

/**
 * A fully static class, this contains helper methods for working with strings.
 *
 * @author Joseph Lenton
 */
public final class StringUtil
{
  private static final char DEFAULT_TRIM_WHITESPACE = ' ';

  /**
   * No Constructor.
   */
  private StringUtil() { }

  /**
   * Concatonates all the strings given together into one long string.
   * @param strings An array of strings, cannot be null and cannot contain null.
   * @return A string made by concatonating all the elements of strings together.
   */
  public static String stringConcat(String ... strings)
  {
    /* Add up the total length of the strings, this is a small optimization
     * for when were working with lots of long strings. */
    int length = 0;
    for (int i = 0; i < strings.length; i++) {
      length += strings[i].length();
    }

    // append all strings together
    final StringBuilder concatString = new StringBuilder(length);
    for (int i = 0; i < strings.length; i++) {
      concatString.append(strings[i]);
    }

    return concatString.toString();
  }

  /**
   * Trims spaces from the left side of the string and returns the result.
   * @param string The string to trim.
   * @return A string with all spaces removed from the left side.
   */
  public static String trimLeft(String string)
  {
    return trimLeft( string, DEFAULT_TRIM_WHITESPACE );
  }

  /**
   * Trims the character given from the given string and returns the result.
   * @param string The string to trim, cannot be null.
   * @param trimChar The character to trim from the left of the given string.
   * @return A string with the given character trimmed from the string given.
   */
  public static String trimLeft(final String string, final char trimChar)
  {
    final int stringLength = string.length();
    int i;

    for (i = 0; i < stringLength && string.charAt(i) == trimChar; i++) {
      /* increment i until it is at the location of the first char that
       * does not match the trimChar given. */
    }

    if (i == 0) {
      return string;
    } else {
      return string.substring(i);
    }
  }

  /**
   * Trims spaces from the right side of the string given and returns the
   * result.
   * @param string The string to trim, cannot be null.
   * @return A string with all whitespace trimmed from the right side of it.
   */
  public static String trimRight(final String string)
  {
    return trimRight(string, DEFAULT_TRIM_WHITESPACE);
  }

  /**
   * Trims the character given from the right side of the string given. The
   * result of this trimming is then returned.
   * @param string The string to trim, cannot be null.
   * @param trimChar The character to trim from the right side of the given string.
   * @return The result of trimming the character given from the right side of the given string.
   */
  public static String trimRight(final String string, final char trimChar)
  {
    final int lastChar = string.length() - 1;
    int i;

    for (i = lastChar; i >= 0 && string.charAt(i) == trimChar; i--) {
      /* Decrement i until it is equal to the first char that does not
       * match the trimChar given. */
    }

    if (i < lastChar) {
      // the +1 is so we include the char at i
      return string.substring(0, i+1);
    } else {
      return string;
    }
  }

  /**
   * Trims the character given from both left and right of the string given.
   * For trimming whitespace you can simply use the String classes trim method.
   * @param string The string to trim characters from, cannot be null.
   * @param trimChar The character to trim from either side of the given string.
   * @return A string with the given characters trimmed from either side.
   */
  public static String trim(final String string, final char trimChar)
  {
    return trimLeft(trimRight(string, trimChar), trimChar);
  }
}