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
package ca.uqac.lif.bullwinkle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.output.GraphvizVisitor;
import ca.uqac.lif.bullwinkle.output.IndentedTextVisitor;
import ca.uqac.lif.bullwinkle.output.OutputFormatVisitor;
import ca.uqac.lif.bullwinkle.output.XmlVisitor;
import ca.uqac.lif.util.CliParser;
import ca.uqac.lif.util.CliParser.ArgumentMap;

public class BullwinkleCli
{

	/**
	 * Return codes
	 */
	public static final int ERR_OK = 0;
	public static final int ERR_PARSE = 2;
	public static final int ERR_IO = 3;
	public static final int ERR_ARGUMENTS = 4;
	public static final int ERR_RUNTIME = 6;
	public static final int ERR_GRAMMAR = 7;
	public static final int ERR_INPUT = 9;

	/**
	 * Build string to identify versions
	 */
	protected static final String VERSION_STRING = BullwinkleCli.class.getPackage().getImplementationVersion();

	private BullwinkleCli()
	{
		throw new IllegalAccessError("Main class");
	}

	/**
	 * Main loop
	 * @param args The command-line arguments
	 */
	@SuppressWarnings({"squid:S106", "squid:S1166"})
	public static void main(String[] args)
	{
		// Setup parameters
		int verbosity = 1;
		String output_format = "xml", grammar_filename = null, filename_to_parse = null;

		// Parse command line arguments
		CliParser cli_parser = setupOptions();
		ArgumentMap c_line = setupCommandLine(cli_parser, args);
		if (c_line == null)
		{
			// oops, something went wrong
			System.err.println("Error parsing command-line arguments");
			System.exit(ERR_ARGUMENTS);
		}
		assert c_line != null;
		if (c_line.hasOption("verbosity"))
		{
			verbosity = Integer.parseInt(c_line.getOptionValue("verbosity"));
		}
		if (verbosity > 0)
		{
			showHeader();
		}
		if (c_line.hasOption("version"))
		{
			System.err.println("(C) 2014-2016 Sylvain Hallé et al., Université du Québec à Chicoutimi");
			System.err.println("This program comes with ABSOLUTELY NO WARRANTY.");
			System.err.println("This is a free software, and you are welcome to redistribute it");
			System.err.println("under certain conditions. See the file LICENSE-2.0 for details.\n");
			System.exit(ERR_OK);
		}
		if (c_line.hasOption("help"))
		{
			showUsage(cli_parser);
			System.exit(ERR_OK);
		}
		if (c_line.hasOption("format"))
		{
			output_format = c_line.getOptionValue("f");
		}
		// Get grammar file
		List<String> remaining_args = c_line.getOthers();
		if (remaining_args.isEmpty())
		{
			System.err.println("ERROR: no grammar file specified");
			System.exit(ERR_ARGUMENTS);
		}
		grammar_filename = remaining_args.get(0);
		// Get file to parse, if any
		if (remaining_args.size() >= 2)
		{
			filename_to_parse = remaining_args.get(1);
		}

		// Read grammar file
		BnfParser parser = null;
		try
		{
			parser = new BnfParser(new File(grammar_filename));
		}
		catch (InvalidGrammarException e)
		{
			System.err.println("ERROR: invalid grammar");
			System.exit(ERR_GRAMMAR);
		}
		catch (IOException e)
		{
			System.err.println("ERROR reading grammar " + grammar_filename);
			System.exit(ERR_IO);
		}
		assert parser != null;

		// Read input file
		Scanner scanner = null;
		if (filename_to_parse == null)
		{
			// Read from stdin
			scanner = new Scanner(System.in);
		}
		else
		{
			// Read from file
			try
			{
				scanner = new Scanner(new File(filename_to_parse));
			} 
			catch (FileNotFoundException e)
			{
				System.err.println("ERROR reading input\n");
				System.exit(ERR_IO);
			}
		}
		StringBuilder input_file = new StringBuilder();
		if (scanner == null)
		{
			System.err.println("ERROR reading input\n");
			System.exit(ERR_IO);
		}
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			input_file.append(line).append("\n");
		}
		scanner.close();
		String file_contents = input_file.toString();

		// Parse contents of file
		ParseNode p_node = null;
		try
		{
			p_node = parser.parse(file_contents);
		}
		catch (ca.uqac.lif.bullwinkle.BnfParser.ParseException e)
		{
			System.err.println("ERROR parsing input\n");
			e.printStackTrace();
			System.exit(ERR_PARSE);
		}
		if (p_node == null)
		{
			System.err.println("ERROR parsing input\n");
			System.exit(ERR_PARSE);
		}
		assert p_node != null;

		// Output parse node to desired format
		PrintStream output = System.out;
		OutputFormatVisitor out_vis = null;
		if (output_format.compareToIgnoreCase("xml") == 0)
		{
			// Output to XML
			out_vis = new XmlVisitor();
		}
		else if (output_format.compareToIgnoreCase("dot") == 0)
		{
			// Output to DOT
			out_vis = new GraphvizVisitor();
		}
		else if (output_format.compareToIgnoreCase("txt") == 0)
		{
			// Output to indented plain text
			out_vis = new IndentedTextVisitor();
		}
		else if (output_format.compareToIgnoreCase("json") == 0)
		{
			// Output to JSON
		}
		if (out_vis == null)
		{
			System.err.println("ERROR: unknown output format " + output_format);
			System.exit(ERR_ARGUMENTS);
		}
		assert out_vis != null;
		p_node.prefixAccept(out_vis);
		output.print(out_vis.toOutputString());

		// Terminate without error
		System.exit(ERR_OK);
	}

	/**
	 * Sets up the options for the command line parser
	 * @return The options
	 */
	private static CliParser setupOptions()
	{
		CliParser cli_parser = new CliParser();
		cli_parser.addArgument(new CliParser.Argument()
		.withShortName("h")
		.withLongName("help")
		.withDescription("Display command line usage"));
		cli_parser.addArgument(new CliParser.Argument()
		.withShortName("f")
		.withLongName("format")
		.withArgument("x")
		.withDescription("Output parse tree in format x (dot, xml, txt or json). Default: xml"));
		cli_parser.addArgument(new CliParser.Argument()
		.withLongName("verbosity")
		.withArgument("x")
		.withDescription("Verbose messages with level x"));
		cli_parser.addArgument(new CliParser.Argument()
		.withLongName("version")
		.withDescription("Show version number"));
		return cli_parser;
	}

	/**
	 * Show the command's usage
	 * @param cli_parser The command-line parser
	 */
	@SuppressWarnings("squid:S106")
	private static void showUsage(CliParser cli_parser)
	{
		cli_parser.printHelp("java -jar BullwinkleParser.jar [options] grammar [inputfile]", System.err);
	}

	/**
	 * Sets up the command line parser
	 * @param args The command line arguments passed to the class' {@link main}
	 * method
	 * @param options The command line options to be used by the parser
	 * @return The object that parsed the command line parameters
	 */
	private static ArgumentMap setupCommandLine(CliParser cli_parser, String[] cli)
	{
		return cli_parser.parse(cli);
	}

	@SuppressWarnings("squid:S106")
	private static void showHeader()
	{
		System.err.println("Bullwinkle " + VERSION_STRING + ", a LL(k) parser");
	}

	public static String getVersionString()
	{
		return VERSION_STRING;
	}

}
