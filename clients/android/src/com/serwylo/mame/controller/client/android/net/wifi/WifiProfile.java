package com.serwylo.mame.controller.client.android.net.wifi;

import com.serwylo.mame.controller.client.android.net.*;
import com.serwylo.mame.controller.client.android.util.WifiBarcodeParser;

/**
 * Encapsulates the IP address and the port of a wifi connection profile.
 */
public class WifiProfile extends ConnectionProfile
{

	private String ipAddress;
	private int port;

	public final String getIpAddress()
	{
		return this.ipAddress;
	}

	public final int getPort()
	{
		return this.port;
	}

	public boolean equals( WifiProfile profile )
	{
		return profile.getPort() == this.getPort() && profile.getIpAddress().equals( this.getIpAddress() );
	}

	/**
	 * Creates a {@link com.serwylo.mame.controller.client.android.util.WifiBarcodeParser} and passes it to
	 * {@link WifiProfile#createFromBarcode(com.serwylo.mame.controller.client.android.util.WifiBarcodeParser)}.
	 * @param barcodeContents A string which has a Uri obtained from the barcode displayed on the server we wish to
	 *                        connect to.
	 * @return Profile containing the ipAddress and port of the server we wish to connect to.
	 * @throws IllegalArgumentException If the string is malformed (e.g. scanned an unrelated QR code).
	 */
	public static WifiProfile createFromBarcode( String barcodeContents ) throws IllegalArgumentException
	{
		return WifiProfile.createFromBarcode( WifiBarcodeParser.create( barcodeContents ) );
	}

	public static WifiProfile createFromBarcode( WifiBarcodeParser parser )
	{
		WifiProfile profile = new WifiProfile();
		profile.port = parser.getPort();
		profile.ipAddress = parser.getIpAddress();
		return profile;
	}

	public static WifiProfile create( String ipAddress, int port )
	{
		WifiProfile profile = new WifiProfile();
		profile.port = port;
		profile.ipAddress = ipAddress;
		return profile;
	}

	@Override
	public String getDetails()
	{
		return this.ipAddress + ":" + this.port;
	}

	@Override
	public NetworkClient createClient()
	{
		return WifiClient.create( this.ipAddress, this.port );
	}
}
