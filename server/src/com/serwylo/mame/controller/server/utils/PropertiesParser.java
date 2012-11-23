package com.serwylo.mame.controller.server.utils;

import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;


/**
 * Stores values from a config file (specified with the --config-file or -c argument) and also directly from command
 * line arguments. The command line arguments will overwrite config file arguments.
 */
public class PropertiesParser
{

	private HashMap<String, String> keysValues = new HashMap<String, String>();

	private Options options;
	private String[] args;

	private boolean showOutput = true;

	public PropertiesParser( Options options, String[] args )
	{
		this.options = options;
		this.args = args;
	}

	public PropertiesParser setShowOutput( boolean value )
	{
		this.showOutput = value;
		return this;
	}

	public String getString( char key, String defaultValue )
	{
		return this.keysValues.containsKey( String.valueOf( key ) ) ? this.keysValues.get( String.valueOf( key ) ) : defaultValue;
	}

	public int getInt( char key, int defaultValue )
	{
		return this.keysValues.containsKey( String.valueOf( key ) ) ? Integer.parseInt( this.keysValues.get( String.valueOf( key ) ) ) : defaultValue;
	}

	public boolean getBoolean( char key )
	{
		return this.keysValues.containsKey( String.valueOf( key ) ) && this.keysValues.get( String.valueOf( key ) ) != null;
	}

	public PropertiesParser parse() throws IOException, ParseException
	{
		CommandLine commandLine = new PosixParser().parse( this.options, this.args );
		if ( commandLine.hasOption( 'c' ) )
		{
			Properties properties = new Properties();
			String propertiesFile = commandLine.getOptionValue( 'c' );
			properties.load( new FileInputStream( propertiesFile ) );

			// If the property is in a config file, store its value first, then allow it to be overwritten by commandline
			// options...
			for ( Option option : (Collection<Option>)this.options.getOptions() )
			{
				if ( option.getOpt().equals( "c" ) )
				{
					continue;
				}

				boolean isBoolean = !( option.hasArg() || option.hasArgs() );
				if ( isBoolean )
				{
					String value = properties.getProperty( option.getLongOpt() );
					if ( value != null )
					{
						this.setValue( option, value, "from config file '" + propertiesFile + "'" );
					}
				}
				else
				{
					boolean hasValue = properties.containsKey( option.getLongOpt() );

					if ( hasValue )
					{
						this.setValue( option, (String)properties.get( option.getLongOpt() ), "from config file" );
					}
				}
			}
		}

		// If the property is in a config file, store its value first, then allow it to be overwritten by commandline
		// options...
		for ( Option option : commandLine.getOptions() )
		{
			if ( option.getOpt().equals( "c" ) )
			{
				continue;
			}

			boolean isBoolean = !( option.hasArg() || option.hasArgs() );
			boolean inConfig = this.keysValues.containsKey( option.getOpt() );

			if ( commandLine.hasOption( option.getOpt() ) )
			{
				String fromWhere = inConfig
					? "overwriting value in config with value from command line argument"
					: "from command line argument";

				if ( isBoolean )
				{
					this.setValue( option, true, fromWhere );
				}
				else
				{
					this.setValue( option, option.getValue(), fromWhere );;
				}

			}
		}

		return this;
	}

	private void setValue( Option option, boolean isPresent, String fromWhere )
	{
		setValue(  option, isPresent ? "" : null, fromWhere );
	}

	/**
	 * If we have been told to show output, then we will dump info about the option which is being set.
	 * @param option The option which we are currently parsing.
	 * @param value Value the option is being set to, or null if a boolean where it is not present.
	 * @param fromWhere Descriptive string such as "from config file" or "overwriting value in config with value from command line argument".
	 * @see   PropertiesParser#setShowOutput(boolean)
	 */
	private void setValue( Option option, String value, String fromWhere )
	{
		this.keysValues.put(  option.getOpt(), value );

		if ( this.showOutput )
		{
			StringBuilder sb = new StringBuilder();
			if ( option.hasArg() || option.hasArgs() )
			{
				sb.append( "Setting config option '" );
				sb.append( option.getLongOpt() );
				sb.append( "' to '" );
				sb.append( value );
				sb.append( "'" );
			}
			else
			{
				sb.append( "Turning " );
				sb.append( value == null ? "off" : "on" );
				sb.append( " option '" );
				sb.append( option.getLongOpt() );
				sb.append( "'" );
			}

			if ( fromWhere != null )
			{
				sb.append( " (" );
				sb.append( fromWhere );
				sb.append( ")" );
			}

			System.out.println( sb.toString() );
		}
	}
}
