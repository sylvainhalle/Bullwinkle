/*
    LIF utils, a collection of useful classes
    Copyright (C) 2015-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackageFileReader
{
	private PackageFileReader()
	{
	    throw new IllegalAccessError("Utility class");
	}
	
	public static String readPackageFile(Class<?> c, String path)
	{
		InputStream in = c.getResourceAsStream(path);
		String out;
		try
		{
			out = readPackageFile(in);
		}
		catch (IOException e)
		{
			Logger.getAnonymousLogger().log(Level.WARNING, "", e);
			return null;
		}
		return out;
	}

	/**
	 * Reads a file and puts its contents in a string
	 * @param in The input stream to read
	 * @return The file's contents, and empty string if the file
	 * does not exist
	 * @throws IOException If the reading fails
	 */
	public static String readPackageFile(InputStream in) throws IOException
	{
		if (in == null)
		{
			throw new IOException();
		}
		java.util.Scanner scanner = null;
		StringBuilder out = new StringBuilder();
		try
		{
			scanner = new java.util.Scanner(in, "UTF-8");
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				out.append(line).append(System.getProperty("line.separator"));
			}
		}
		finally
		{
			if (scanner != null)
				scanner.close();
		}
		return out.toString();
	}
}
