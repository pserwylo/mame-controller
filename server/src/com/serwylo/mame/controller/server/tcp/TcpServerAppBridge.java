package com.serwylo.mame.controller.server.tcp;

import com.serwylo.mame.controller.server.Server;
import com.serwylo.mame.controller.server.ServerAppBridge;
import com.serwylo.mame.controller.server.StatusDisplay;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TcpServerAppBridge extends ServerAppBridge
{

	private static final String NAME = "Wifi";

	private TcpStatusDisplay statusDisplay;

	private TcpServer server;

	public TcpServerAppBridge( TcpServer server )
	{
		this.server = server;
		this.statusDisplay = new TcpStatusDisplay( this.server );
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public Server getServer()
	{
		return this.server;
	}

	@Override
	public StatusDisplay getStatusDisplay()
	{
		return this.statusDisplay;
	}

	@Override
	public ArrayList<Option> getCliOptions()
	{
		ArrayList<Option> options = new ArrayList<Option>( 10 );
		options.add( new Option( "w", "wifi", false, "Start wifi server" ) );
		options.add( new Option("a", "ipaddress", true, "IP Address to bind to (default is to look for one on our own)" ) );
		options.add( new Option( "p", "port", true, "Port to bind to (default is " + TcpServer.DEFAULT_PORT + ")" ) );
		return options;
	}

	@Override
	public void parseCommandLine( CommandLine commandLine )
	{
		try
		{
			this.isActive = commandLine.hasOption( 'w' );

			InetAddress address = commandLine.hasOption( 'a' ) ? InetAddress.getByName( commandLine.getOptionValue( 'a' ) ) : null;
			this.server.setIpAddress( address );

			Integer port = commandLine.hasOption( 'p' ) ? Integer.parseInt( commandLine.getOptionValue( 'p' ) ) : TcpServer.DEFAULT_PORT;
			this.server.setPort( port );
		}
		catch ( UnknownHostException uhe )
		{
			System.err.println( "Invalid address: " + commandLine.getOptionValue( 'a' ) );
			System.err.println( uhe.getMessage() );
		}
		catch ( NumberFormatException nfe )
		{
			System.err.println( "Invalid port: " + commandLine.getOptionValue( 'p' ) );
			System.err.println( nfe.getMessage() );
		}

		this.statusDisplay.init();
	}
}
