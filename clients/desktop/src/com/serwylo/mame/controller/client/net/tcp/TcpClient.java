package com.serwylo.mame.controller.client.net.tcp;

import com.serwylo.mame.controller.client.net.NetworkClient;
import com.serwylo.mame.controller.shared.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Connects over a TCP/IP socket to the server. Could be using wifi, or perhaps 3G or some other
 * technology, but obviously the latency would increase to unbearable levels if slower connections
 * were employed. At any rate, we just presume we are connected to some sort of network.
 */
public class TcpClient extends NetworkClient
{

	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;

	private static TcpClient instance = new TcpClient();

	private String hostAndPort;
	private InetAddress ipAddress;
	private int port;

	private TcpClient()
	{
		
	}

	public static TcpClient create()
	{
		return TcpClient.create( null );
	}

	public static TcpClient create( String connectionString )
	{
		TcpClient client = new TcpClient();
		client.hostAndPort = connectionString;
		return client;
	}

	@Override
	public boolean open()
	{
		if ( ipAddress != null )
		{
			return this.open( ipAddress, port );
		}
		else
		{
			return this.open( this.hostAndPort );
		}
	}

	/**
	 * Connects using a string which looks like: "hostname:port" (e.g. "192.168.0.1:5555").
	 * This is the type of string which will be encoded in the QR code from the server.
	 * Really just parses
	 * @param hostAndPort
	 * @return true on success
	 */
	public boolean open( String hostAndPort )
	{
		try
		{
			int colonIndex = hostAndPort.indexOf( ':' );
			String addressString = hostAndPort.substring( 0, colonIndex );
			String portString = hostAndPort.substring( colonIndex + 1 );

			InetAddress address = InetAddress.getByName( addressString );
			int port = Integer.parseInt( portString );

			return this.open( address, port );
		}
		catch ( Exception e )
		{
			System.err.println( "Cannot connect to server at '" + hostAndPort + "'." );
			System.err.println( "Expected syntax 'host:port' (e.g. '192.168.0.1:5555')" );
			return false;
		}
	}

	public boolean open( InetAddress address, int port )
	{
		try
		{
			this.socket = new Socket( address.getHostAddress(), port );
			this.output = new PrintWriter( this.socket.getOutputStream(), true );
			this.input = new BufferedReader( new InputStreamReader( this.socket.getInputStream() ) );
			return true;
		}
		catch( IOException ioe )
		{
			System.err.println( ioe.getMessage() );
			ioe.printStackTrace();
			return false;
		}
	}
	
	public void sendEvent( Event event )
	{
		System.err.println( "Sending to server: '" + event.toString() + "'" );
		this.output.write( event.toString() );
		this.output.flush();
	}
	
	public void close()
	{
		try
		{
			if ( this.output != null )
			{
				this.sendEvent( Event.createCloseEvent() );
				this.output.close();
			}
			
			if ( this.input != null )
			{
				this.input.close();
			}
			
			if ( this.socket != null )
			{
				this.socket.close();
			}
		}
		catch( IOException ioe )
		{
			System.err.println( ioe.getMessage() );
			ioe.printStackTrace();
		}
	}
	
}
