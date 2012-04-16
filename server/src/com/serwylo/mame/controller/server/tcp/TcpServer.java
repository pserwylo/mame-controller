package com.serwylo.mame.controller.server.tcp;

import com.serwylo.mame.controller.server.Server;
import com.serwylo.mame.controller.shared.Event;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * Based on design from http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html (thanks :)
 */
public class TcpServer extends Server
{

	public static final int DEFAULT_PORT = 57368;

	protected InetAddress ipAddress;
	protected int port = DEFAULT_PORT;
	protected ServerSocket socket;

	protected boolean hasStopped = false;

	public TcpServer()
	{
		this( null, 0 );
	}

	public TcpServer( InetAddress addressToBind, int portToBind )
	{
		if ( addressToBind == null )
		{
			try
			{
				addressToBind = this.getIpAddressOfServer();
			}
			catch ( SocketException uhe )
			{
				// Cannot really continue...
				throw new RuntimeException( "Cannot start TCP server, unable to find IP address of server." );
			}
		}

		if ( port <= 0 )
		{
			port = DEFAULT_PORT;
		}

		this.ipAddress = addressToBind;
		this.port = portToBind;
	}

	public InetAddress getIpAddress()
	{
		return this.ipAddress;
	}

	public void setIpAddress( InetAddress address )
	{
		try
		{
			address = this.getIpAddressOfServer();
		}
		catch ( SocketException uhe )
		{
			// Cannot really continue...
			throw new RuntimeException( "Cannot start TCP server, unable to find IP address of server." );
		}
		this.ipAddress = address;
	}

	public int getPort()
	{
		return this.port;
	}

	public void setPort( int port )
	{
		this.port = port;
	}

	/**
	 * Simply tries to open a {@link java.net.ServerSocket} and returns false if it fails.
	 * The port and IP address it binds to were specified at construction.
	 */
	@Override
	public boolean start()
	{
		boolean success = false;
		try
		{
			if ( this.ipAddress == null )
			{
				try
				{
					this.ipAddress = this.getIpAddressOfServer();
				}
				catch ( SocketException se )
				{
					System.err.println( "Error getting default IP address" );
					System.err.println( se.getMessage() );
				}
			}

			this.socket = new ServerSocket( this.port, 50, this.ipAddress );
			System.out.println( "Started server at " + this.ipAddress.getHostAddress() + ":" + this.port );
			success = true;
		}
		catch ( BindException be )
		{
			System.err.println( "Failed while binding to " + this.ipAddress + ":" + this.port );
			System.err.println( be.getMessage() );
		}
		catch ( IOException ioe )
		{
			System.err.println( "Failed opening server socket at " + this.ipAddress + ":" + this.port );
			System.err.println( ioe.getMessage() );
		}

		return success;
	}

	@Override
	public void stop()
	{
		this.hasStopped = true;
		try
		{
			this.socket.close();
		}
		catch ( IOException ioe )
		{
			System.err.println( "Error while closing socket." );
			System.err.println( ioe.getMessage() );
		}

	}

	@Override
	public void run()
	{
		while ( !this.hasStopped )
		{
			try
			{
				System.out.println( "Waiting for client..." );
				Socket clientSocket = this.socket.accept();
				new Thread( new TcpClientWorker( this, clientSocket ) ).start();
			}
			catch ( IOException ioe )
			{
				System.err.println( ioe.getMessage() );
			}
		}
	}

	/**
	 * Infers the address of the server.
	 * Iterates over the netework interfaces and their addresses as per (http://stackoverflow.com/a/1062057/1282804)
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

	/**
	 * For the client worker threads to notify the server, who in turn will notify any listeners.
	 * This is required because the client worker thread doesn't know who is listening to the server.
	 * @param event Event from the client worker thread.
	 */
	public void receiveEvent( Event event )
	{
		this.dispatchEvent( event );
	}
}
