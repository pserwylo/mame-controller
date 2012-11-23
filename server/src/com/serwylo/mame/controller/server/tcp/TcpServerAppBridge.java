package com.serwylo.mame.controller.server.tcp;

import com.serwylo.mame.controller.server.Server;
import com.serwylo.mame.controller.server.ServerAppBridge;
import com.serwylo.mame.controller.server.StatusDisplay;
import com.serwylo.mame.controller.server.utils.PropertiesParser;
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
	public void parseConfig( PropertiesParser config )
	{
		String addressString = null;
		Integer port = null;
		try
		{
			this.isActive = config.getBoolean( 'w' );

			addressString = config.getString( 'a', null );
			InetAddress address = addressString != null ? InetAddress.getByName( addressString ) : null;
			this.server.setIpAddress( address );

			port = config.getInt( 'p', TcpServer.DEFAULT_PORT );
			this.server.setPort( port );
		}
		catch ( UnknownHostException uhe )
		{
			System.err.println( "Invalid address: " + addressString );
			System.err.println( uhe.getMessage() );
		}
		catch ( NumberFormatException nfe )
		{
			System.err.println( "Invalid port: " + port );
			System.err.println( nfe.getMessage() );
		}

		this.statusDisplay.init();
	}
}
