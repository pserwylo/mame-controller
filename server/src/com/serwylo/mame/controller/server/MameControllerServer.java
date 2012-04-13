package com.serwylo.mame.controller.server;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.intel.bluetooth.BluetoothConsts;
import com.serwylo.mame.controller.shared.Event;
import org.apache.commons.cli.*;

/**
 * Listens for requests from clients, and then receives input and passes it to a {@link java.awt.Robot}.
 * Displays the QrCode to connect to the server from the phone.
 * This will stay here as long as no clients are connected. The default behaviour will be to remove the status screen
 * when a client connects. But we should also give the client the option to show it again so multiple clients can 
 * connect. Finally, when the client disconnects, we will show the status again.
 */
public class MameControllerServer implements DiscoveryListener
{

	private boolean bluetooth = false;
	private boolean wifi = false;
	private InetAddress ipaddress = null;
	private int port = 57368;
	private boolean showGuiStatus = true;

	public InetAddress getIpAddress() { return this.ipaddress; }
	public int getPort() { return this.port; }

	public static void main( String[] args )
	{
		MameControllerServer server = new MameControllerServer();
		server.run( args );
	}

	public void run( String[] args )
	{
		this.parseArgs( args );
		if ( this.bluetooth )
		{
			this.runBluetooth();
		}
		else if ( this.wifi )
		{
			this.runWifi();
		}
	}
	
	public MameControllerServer()
	{
	}

	public void parseArgs( String[] args )
	{
		Options options = new Options();
		options.addOption( "b", "bluetooth", false, "Start bluetooth server" );
		options.addOption( "g", "nogui", false, "Don't show gui status window" );
		options.addOption( "w", "wifi", false, "Start wifi server" );
		options.addOption( "a", "ipaddress", true, "IP Address to bind to (default is to look for one on our own)" );
		options.addOption( "p", "port", true, "Port to bind to (default is 57368)" );

		CommandLine line = null;
		try
		{
			CommandLineParser parser = new PosixParser();
			line = parser.parse( options, args );
			this.bluetooth = line.hasOption( 'b' );
			this.showGuiStatus = !line.hasOption( 'g' );
			this.wifi = line.hasOption( 'w' );
			this.ipaddress = line.hasOption( 'a' ) ? InetAddress.getByName( line.getOptionValue( 'a' ) ) : null;
			this.port = line.hasOption( 'p' ) ? Integer.parseInt( line.getOptionValue( 'p' ) ) : 57368;
		}
		catch ( ParseException pe )
		{
			System.err.println( pe.getMessage() );
			System.exit( 1 );
		}
		catch ( UnknownHostException uhe )
		{
			System.err.println( "Cannot bind to address: " + line.getOptionValue( 'a' ) );
			System.err.println( uhe.getMessage() );
		}
		catch ( NumberFormatException nfe )
		{
			System.err.println( "Cannot bind to port: " + line.getOptionValue( 'p' ) );
			System.err.println( nfe.getMessage() );
		}
	}

	/** Waiting for connection from devices */
	private void runBluetooth()
	{
		// retrieve the local Bluetooth device object
		LocalDevice local = null;

		StreamConnectionNotifier notifier;
		StreamConnection connection = null;

		// setup the server to listen for connection
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable( DiscoveryAgent.GIAC );


		    // Unique UUID for this application
			
			// UUID uuid = new UUID( 80087355 ); // "04c6093b-0000-1000-8000-00805f9b34fb"
			String url = "btspp://localhost:" + BluetoothConsts.SERIAL_PORT_UUID + ";name=MameBluetooth";
			System.out.println( "Opening service at - " + url );
			notifier = (StreamConnectionNotifier)Connector.open( url );
		} 
		catch ( Exception e ) 
		{
			e.printStackTrace();
			return;
		}
       	
		// waiting for connection
		while( true ) 
		{
			try 
			{
				System.out.println("waiting for connection...");
				connection = notifier.acceptAndOpen();
				System.out.println("Received connection...");
				
				Thread processThread = new Thread( new ProcessConnectionThread( connection ) );
				processThread.start();
			} 
			catch ( Exception e ) 
			{
				e.printStackTrace();
				return;
			}
		}
	}

	/**
	 * Infers the address of the server.
	 * Iterates over the network interfaces and their addresses as per (http://stackoverflow.com/a/1062057/1282804)
	 * Will prefer non-loopback addresses over loopback addresses.
	 * Will also prefer earlier addresses rather than later ones (it just seems that network interfaces
	 * like eth0 always seem to come before others such as virtual interfaces).
	 * @return Returns null if no valid address found.
	 * @throws SocketException
	 */
	private InetAddress getIpAddressOfServer() throws SocketException
	{
		InetAddress firstAddress = null;
		InetAddress firstLoopbackAddress = null;
		System.out.println( "Searching for IP addresses of this server...");
		for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); )
		{
			NetworkInterface iface = ifaces.nextElement();
			for ( Enumeration<InetAddress> addresses = iface.getInetAddresses(); addresses.hasMoreElements(); )
			{
				// We could bail after the first valid one is found, but we may as well print out all
				// of the available addresses so that the user can fire up the server again with a specific
				// ip in mind...
				InetAddress address = addresses.nextElement();
				System.out.print( iface.getDisplayName() + ": " + address );
				if ( address instanceof Inet6Address )
				{
					System.out.println( " (IPv6 - IGNORING)" );
				}
				else
				{
					if ( address.isLoopbackAddress() )
					{
						System.out.println( " (loopback)" );
						if ( firstLoopbackAddress == null )
						{
							firstLoopbackAddress = address;
						}
					}
					else if ( !address.isLoopbackAddress() )
					{
						System.out.println( "" );
						if ( firstAddress == null )
						{
							firstAddress = address;
						}
					}
				}
			}
		}

		InetAddress toUse = firstAddress != null ? firstAddress : firstLoopbackAddress;
		System.out.println( "Selecting IP: " + toUse.getHostAddress() );

		return toUse;
	}

	public void runWifi()
	{
		try
		{
			Robot robot = new Robot();

			if ( this.ipaddress == null )
			{
				this.ipaddress = this.getIpAddressOfServer();
			}

			ServerSocket serverSocket = new ServerSocket( this.port, 50, this.ipaddress );
			System.out.println( "Started server at " + this.ipaddress.getHostAddress() + ":" + this.port );

			if ( this.showGuiStatus )
			{
				new StatusDisplay( this );
			}

			System.out.println( "Waiting for client..." );
			Socket clientSocket = serverSocket.accept();
			System.out.println( "Connected to client..." );
			
			BufferedReader input = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
			System.out.println( "Waiting for input from client..." );
			String message = input.readLine();
			
			while ( message != null )
			{
				System.out.println( "Server received: '" + message + "'" );
				Event event = Event.createFromString( message );
				System.out.println( "Event: '" + event + "'" );
				if ( event.getType() == Event.TYPE_CLOSE )
				{
					System.out.println( "Gracefully closing socket" );
					break;
				}
				else
				{
					switch( event.getType() )
					{
					case Event.TYPE_KEY_DOWN:
						robot.keyPress( event.getKeyCode() );
						break;
						
					case Event.TYPE_KEY_UP:
						robot.keyRelease( event.getKeyCode() );
						break;
					}
				}

				System.out.println( "Waiting for input from client..." );
				message = input.readLine();
			}

			System.out.println( "Stopping server" );
			input.close();
			clientSocket.close();
			serverSocket.close();
		}
		catch ( AWTException awte )
		{
			System.err.println( "Error while trying to build robot to control server" );
			System.err.println( awte.getMessage() );
		}
		catch ( BindException be )
		{
			System.err.println( "Failed while binding to " + this.ipaddress + ":" + this.port );
			System.err.println( be.getMessage() );
		}
		catch ( IOException ioe )
		{
			System.err.println( ioe.getMessage() );
		}
		
	}

	@Override
	public void deviceDiscovered( RemoteDevice arg0, DeviceClass arg1 ) 
	{
		System.out.println( "Device discovered" );
	}

	@Override
	public void inquiryCompleted( int arg0 ) 
	{
		System.out.println( "Inquiry completed" );
	}

	@Override
	public void serviceSearchCompleted( int arg0, int arg1 ) 
	{
		System.out.println( "Service search completed" );
	}

	@Override
	public void servicesDiscovered( int arg0, ServiceRecord[] arg1 ) 
	{
		System.out.println( "Services discovered" );
	}
	
}
