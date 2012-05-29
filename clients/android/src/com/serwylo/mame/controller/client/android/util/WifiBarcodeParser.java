package com.serwylo.mame.controller.client.android.util;

import android.net.Uri;
import android.util.Log;
import com.serwylo.mame.controller.client.android.net.wifi.WifiProfile;

/**
 * Utility class to help parse the barcode contents for a wifi server connection string.
 */
public class WifiBarcodeParser
{

	private int port;
	private String ipAddress;

	private WifiBarcodeParser( Uri uri ) throws IllegalArgumentException
	{

		String host = uri.getQueryParameter( "host" );
		String portString = uri.getQueryParameter( "port" );
		if ( host == null || portString == null )
		{
			throw new IllegalArgumentException( "Uri must have a query parameters 'host' and 'port'." );
		}

		int port = 0;
		try
		{
			port = Integer.parseInt( portString );
		}
		catch( NumberFormatException nfe )
		{
			throw new IllegalArgumentException( "Port must be a valid integer ('" + portString + "' given)." );
		}

		this.ipAddress = host;
		this.port = port;
	}

	/**
	 * Wraps {@link WifiProfile#createFromBarcode(WifiBarcodeParser)}.
	 * @return
	 */
	public WifiProfile createProfile()
	{
		return WifiProfile.createFromBarcode( this );
	}

	/**
	 * Port the server is listening on.
	 * This has not yet been validated to ensure that it is within the valid range.
	 * @return Unvalidated port number that the server is listening on.
	 */
	public int getPort()
	{
		return this.port;
	}

	/**
	 * IP address of the server.
	 * This has not yet been confirmed as a valid IP, so you should do this before using it (e.g. with
	 * {@link java.net.InetAddress#getByName(String)}.
	 * @return Unvalidated string representing the IP address of the server.
	 */
	public String getIpAddress()
	{
		return this.ipAddress;
	}

	/**
	 * Creates a {@link Uri} and passes it to {@link WifiBarcodeParser#create(android.net.Uri)}.
	 * @param barcodeContents Should be a Uri which identifies the connection string for the server.
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static WifiBarcodeParser create( String barcodeContents ) throws IllegalArgumentException
	{
		Uri uri = Uri.parse( barcodeContents );
		return WifiBarcodeParser.create( uri );
	}

	/**
	 * Looks for a query parameter "s" in the {@param uri} and passes it to the barcode parser to figure out.
	 * @param uri Connection string for the server (should have a query parameter "s").
	 * @return
	 * @throws IllegalArgumentException If there is no query parameter "s" in the uri.
	 */
	public static WifiBarcodeParser create( Uri uri ) throws IllegalArgumentException
	{
		return new WifiBarcodeParser( uri );
	}

}
