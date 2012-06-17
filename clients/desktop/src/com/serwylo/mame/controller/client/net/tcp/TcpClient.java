package com.serwylo.mame.controller.client.net.tcp;

import com.badlogic.gdx.Gdx;
import com.serwylo.mame.controller.client.net.ConnectionEvent;
import com.serwylo.mame.controller.client.net.NetworkClient;
import com.serwylo.mame.controller.shared.InputEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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
	public String toString()
	{
		if ( this.ipAddress != null && this.port > 0 )
		{
			return "Wifi Server [" + this.ipAddress.getHostAddress() + ":" + this.port + "]";
		}
		else if ( this.hostAndPort != null )
		{
			return "Wifi Server [" + this.hostAndPort + "]";
		}
		else
		{
			return "Wifi Server [Disconnected]";
		}
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

	public boolean open( final InetAddress address, final int port )
	{
		new Thread( new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					TcpClient.this.socket = new Socket();
					SocketAddress socketAddress = new InetSocketAddress( address.getHostAddress(), port );
					TcpClient.this.socket.connect( socketAddress, 3000 );
					TcpClient.this.output = new PrintWriter( TcpClient.this.socket.getOutputStream(), true );
					TcpClient.this.input = new BufferedReader( new InputStreamReader( TcpClient.this.socket.getInputStream() ) );
					TcpClient.this.isConnected = true;
					TcpClient.this.notifyListeners( ConnectionEvent.createConnectedEvent( TcpClient.this ) );
				}
				catch( IOException ioe )
				{
					Gdx.app.error( "Tcp Network", ioe.getMessage() );
					TcpClient.this.notifyListeners( ConnectionEvent.createConnectionFailedEvent( TcpClient.this, ioe ) );
				}

			}

		}).start();
		return true;
	}
	
	public void sendEvent( InputEvent event )
	{
		System.err.println( "Sending to server: '" + event.toString() + "'" );
		this.output.write( event.toString() );
		this.output.flush();
	}
	
	public void close()
	{
		this.isConnected = false;
		try
		{
			if ( this.output != null )
			{
				this.sendEvent( InputEvent.createCloseEvent() );
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
