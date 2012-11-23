package com.serwylo.mame.controller.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import com.serwylo.mame.controller.server.events.*;
import com.serwylo.mame.controller.server.tcp.TcpServer;
import com.serwylo.mame.controller.server.tcp.TcpServerAppBridge;
import com.serwylo.mame.controller.server.utils.MameProperties;
import com.serwylo.mame.controller.server.utils.PropertiesParser;
import com.serwylo.mame.controller.shared.InputEvent;
import org.apache.commons.cli.*;

import javax.swing.*;

/**
 * Listens for requests from clients, and then receives input and passes it to a {@link java.awt.Robot}.
 * Displays the QrCode to connect to the server from the phone.
 * This will stay here as long as no clients are connected. The default behaviour will be to remove the status screen
 * when a client connects. But we should also give the client the option to show it again so multiple clients can
 * connect. Finally, when the client disconnects, we will show the status again.
 */
public class MameControllerServer implements IInputEventListener, IServerEventListener, IClientEventListener
{

	private ArrayList<ServerAppBridge> availableServers = new ArrayList<ServerAppBridge>();
	private ExecManager execManager;

	public MameControllerServer()
	{
		TcpServerAppBridge tcpBridge = new TcpServerAppBridge( new TcpServer() );
		tcpBridge.getServer().addInputEventListener( this );
		tcpBridge.getServer().addServerEventListener( this );
		this.availableServers.add( tcpBridge );
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
		options.addOption( "c", "config-file", true, "Pass the options in via a config file, rather than individual command line args" );
		options.addOption( "g", "status-gui", false, "Show the status in a GUI window in the bottom right of the screen" );
		options.addOption( "s", "status-output", true, "Path to (png) file to dump status window to" );
		options.addOption( "x", "exec", true, "The executable command to run once clients are connected" );
		options.addOption( "t", "wait-time", true, "Amount of time to wait after the first client is connected before running exec" );

		for ( ServerAppBridge serverBridge : this.availableServers )
		{
			for ( Option option : serverBridge.getCliOptions() )
			{
				options.addOption( option );
			}
		}

		try
		{
			PropertiesParser config = new PropertiesParser( options, args ).parse();

			// Make these properties available globally...
			MameProperties.setup(
				config.getBoolean( 'g' ),
				config.getString( 's', null ),
				config.getString( 'x', null ),
				config.getInt( 't', 0 )
			);

			for ( ServerAppBridge serverBridge : this.availableServers )
			{
				serverBridge.parseConfig( config );
			}

			this.execManager = ExecManager.create( MameProperties.getInstance().getExecutable() );
		}
		catch ( Exception e )
		{
			System.err.println( "Error while parsing CLI options:" );
			System.err.println( e.getMessage() );
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

	/**
	 * Look for client connect/disconnect events and tell the execManager to do the appropriate thing in response.
	 * @param event Mainly we are interested in the {@link ServerEvent#TYPE_NEW_CLIENT} event, where we can then listen
	 *              to this client for its own events.
	 */
	@Override
	public void onServerEvent( ServerEvent event )
	{
		if ( event.getType() == ServerEvent.TYPE_NEW_CLIENT )
		{
			event.getClient().addClientEventListener( this );
		}
	}

	@Override
	public void onClientEvent( ClientEvent event )
	{
		if ( event.getType() == ClientEvent.TYPE_CLIENT_CONNECTED )
		{
			if ( !this.execManager.isRunning() )
			{
				if ( MameProperties.getInstance().getWaitTime() > 0 )
				{
					System.out.println( "Waiting " + MameProperties.getInstance().getWaitTime() + " milliseconds before running exe..." );
					Timer timer = new Timer( MameProperties.getInstance().getWaitTime(), new ActionListener()
					{
						@Override
						public void actionPerformed( ActionEvent actionEvent )
						{
							execManager.start();
						}
					});
					timer.setRepeats( false );
					timer.start();
				}
				else
				{
					this.execManager.start();
				}
			}
		}
		else if ( event.getType() == ClientEvent.TYPE_CLIENT_DISCONNECTED )
		{
			if ( this.execManager.isRunning() )
			{
				this.execManager.stop();
			}
		}
	}

	public static void main( String[] args )
	{
		MameControllerServer server = new MameControllerServer();
		server.run( args );
	}

}
