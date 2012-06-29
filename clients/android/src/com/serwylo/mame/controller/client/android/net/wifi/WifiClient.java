package com.serwylo.mame.controller.client.android.net.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.serwylo.mame.controller.client.android.net.ConnectionEvent;
import com.serwylo.mame.controller.client.android.net.NetworkClient;
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
public class WifiClient extends NetworkClient
{

	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;

	private static WifiClient instance = new WifiClient();

	private String ipAddressString;
	private InetAddress ipAddress;
	private int port;

	private WifiClient()
	{
		
	}

	public static WifiClient create( String ipAddress, int port )
	{
		WifiClient client = new WifiClient();
		client.ipAddressString = ipAddress;
		client.port = port;
		return client;
	}

	@Override
	public String toString()
	{
		String desc;
		if ( this.ipAddressString != null && this.port > 0 )
		{
			desc = this.ipAddressString + ":" + this.port;
		}
		else if ( this.ipAddress != null && this.port > 0 )
		{
			desc = this.ipAddress.getHostAddress() + ":" + this.port;
		}
		else
		{
			desc = "Disconnected";
		}
		return "Wifi Server " + desc + "";
	}

	@Override
	public void open( final Context context )
	{
		Log.d( "MAME", "WifiClient#open()" );
		new Thread( new Runnable()
		{
			@Override
			public void run()
			{
				// First, validate that the address string can in fact be converted to a valid IP address.
				InetAddress address;
				try
				{
					address = InetAddress.getByName( WifiClient.this.ipAddressString );
					Log.e( "MAME", "Opening wifi socket to '" + address + "'..." );
				}
				catch( IOException ioe )
				{
					Log.e( "MAME", "Could not parse IP address from '" + WifiClient.this.ipAddressString + "'" );
					Log.e( "MAME", ioe.getMessage() );
					WifiClient.this.notifyListeners( ConnectionEvent.createErrorEvent( WifiClient.this, "Invalid address '" + WifiClient.this.ipAddressString + "'", ioe ) );
					return;
				}

				// Local address, should have wifi enabled to correctly route to this address...
				if ( address.isSiteLocalAddress() )
				{
					WifiManager wifiManager = (WifiManager)context.getSystemService( Context.WIFI_SERVICE );
					if ( !wifiManager.isWifiEnabled() )
					{
						// TODO: Try to enable wifi ourselves...
						Log.e( "MAME", "Wifi disabled, and " + address.getHostName() + " looks like a local address..." );
						WifiClient.this.notifyListeners( ConnectionEvent.createErrorEvent( WifiClient.this, "Wifi is disabled, and it looks like you are trying to connect to a computer on your local network.\n\nPlease enable wifi." ) );
						return;
					}

					ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
					NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					if ( networkInfo == null || !networkInfo.isConnected() )
					{
						// TODO: Launch connection intent to let the user connect to wifi themselves...
						Log.e( "MAME", "Wifi not connected, and " + address.getHostName() + " looks like a local address..." );
						WifiClient.this.notifyListeners( ConnectionEvent.createErrorEvent( WifiClient.this, "Wifi is not connected, and it looks like you are trying to connect to a computer on your local network.\n\nAre you connected to the correct WIFI network?" ) );
						return;
					}

				}

				WifiClient.this.ipAddress = address;
				WifiClient.this.socket = new Socket();
				SocketAddress socketAddress = new InetSocketAddress( address.getHostAddress(), port );

				WifiClient.this.notifyListeners( ConnectionEvent.createStatusEvent( WifiClient.this, "Connecting..." ) );

				try
				{
					// TODO: Change the timeout to an (advanced) preference...
					Log.d( "MAME", "Socket#connect() - Timeout of 3 seconds." );
					WifiClient.this.socket.connect( socketAddress, 3000 );
					Log.d( "MAME", "Connected." );
					WifiClient.this.output = new PrintWriter( WifiClient.this.socket.getOutputStream(), true );
					WifiClient.this.input = new BufferedReader( new InputStreamReader( WifiClient.this.socket.getInputStream() ) );
					WifiClient.this.isConnected = true;
					Log.d( "MAME", "Setup writer to talk to server." );
					WifiClient.this.notifyListeners( ConnectionEvent.createConnectedEvent( WifiClient.this ) );
				}
				catch( IOException ioe )
				{
					Log.e( "MAME", ioe.getMessage() );
					WifiClient.this.notifyListeners( ConnectionEvent.createErrorEvent( WifiClient.this, "Could not connect to server (" + ioe.getMessage() + ")", ioe ) );
				}


			}

		}).start();
	}

	/**
	 * TODO: Check for errors (e.g. is the server still connected?)
	 */
	public void sendEvent( InputEvent event )
	{
		Log.d( "MAME", "Sending to server: '" + event.toString() + "'" );
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
				// TODO: I don't think this is even necessary, they will find out when I close anyway...
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
