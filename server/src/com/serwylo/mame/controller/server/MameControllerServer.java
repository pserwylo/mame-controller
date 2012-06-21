package com.serwylo.mame.controller.server;

import java.net.*;
import java.util.ArrayList;

import com.serwylo.mame.controller.server.events.IInputEventListener;
import com.serwylo.mame.controller.server.events.ServerEvent;
import com.serwylo.mame.controller.server.tcp.TcpServer;
import com.serwylo.mame.controller.server.tcp.TcpServerAppBridge;
import com.serwylo.mame.controller.shared.InputEvent;
import org.apache.commons.cli.*;

/**
 * Listens for requests from clients, and then receives input and passes it to a {@link java.awt.Robot}.
 * Displays the QrCode to connect to the server from the phone.
 * This will stay here as long as no clients are connected. The default behaviour will be to remove the status screen
 * when a client connects. But we should also give the client the option to show it again so multiple clients can 
 * connect. Finally, when the client disconnects, we will show the status again.
 */
public class MameControllerServer implements IInputEventListener
{

	private boolean bluetooth = false;
	private boolean wifi = false;
	private InetAddress ipaddress = null;
	private int port = 57368;
	private boolean showGuiStatus = true;

	private ArrayList<ServerAppBridge> availableServers = new ArrayList<ServerAppBridge>();

	private TcpServerAppBridge tcpBridge;

	public MameControllerServer()
	{
		this.tcpBridge = new TcpServerAppBridge( new TcpServer() );
		this.tcpBridge.getServer().addInputEventListener( this );
		this.availableServers.add( this.tcpBridge );
	}

	public void run( String[] args )
	{
		this.parseArgs( args );

		for ( ServerAppBridge serverBridge : this.availableServers )
		{
			if ( serverBridge.isActive() )
			{
				Server server = serverBridge.getServer();
				if ( server.start() )
				{
					StatusDisplay.add( serverBridge.getStatusDisplay() );
					new Thread( server ).start();
				}
			}
		}
	}

	public void parseArgs( String[] args )
	{
		Options options = new Options();
		options.addOption( "b", "bluetooth", false, "Start bluetooth server" );
		options.addOption( "g", "nogui", false, "Don't show gui status window" );

		for ( ServerAppBridge serverBridge : this.availableServers )
		{
			for ( Option option : serverBridge.getCliOptions() )
			{
				options.addOption( option );
			}
		}

		CommandLine line;
		try
		{
			CommandLineParser parser = new PosixParser();
			line = parser.parse( options, args );
			this.bluetooth = line.hasOption( 'b' );
			this.showGuiStatus = !line.hasOption( 'g' );

			for ( ServerAppBridge serverBridge : this.availableServers )
			{
				serverBridge.parseCommandLine( line );
			}
		}
		catch ( ParseException pe )
		{
			System.err.println( "Error while parsing CLI options:" );
			System.err.println( pe.getMessage() );
			System.exit( 1 );
		}
	}

	@Override
	public void onInputEvent( InputEvent event )
	{
		if ( event.getType() == InputEvent.TYPE_CLOSE )
		{
			System.out.println( "Gracefully closing socket" );
		}
		else
		{
			MameRobot.getInstance().processEvent( event );
		}
	}

	public static void main( String[] args )
	{
		MameControllerServer server = new MameControllerServer();
		server.run( args );
	}

}
