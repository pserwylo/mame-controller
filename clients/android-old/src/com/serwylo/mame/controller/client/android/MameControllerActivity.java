package com.serwylo.mame.controller.client.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.widget.Toast;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.serwylo.mame.controller.client.MameControllerClient;
import com.serwylo.mame.controller.client.net.NetworkManager;
import com.serwylo.mame.controller.client.net.tcp.TcpClient;

import java.io.IOError;
import java.io.IOException;
import java.net.InetAddress;

public class MameControllerActivity extends AndroidApplication 
{

	public static final int REQUEST_CONNECT_DEVICE = 1;

	private AndroidQrCodeReader qrCodeReader;

	/**
	 * Inpsect the intent which may or may not have been used to launch this app.
	 * If it exists and seems to point us to a server, return the connection string.
	 * @return
	 */
	protected String checkConnectionIntent()
	{
		Intent intent = this.getIntent();

		// Instead of Intent.ACTION_MAIN which is the intent when opened from the launcher...
		if ( intent.getAction().equals( Intent.ACTION_VIEW ) )
		{
			Uri uri = intent.getData();
			if ( uri != null )
			{
				String connectionString = uri.getQueryParameter( "s" );
				if ( connectionString != null )
				{
					return connectionString;
				}
			}
		}

		return null;
	}

	/**
	 * This depends on whether the phone supports certain network clients or not.
	 * For example, if WIFI is not turned on, we can't use the TCP network client.
	 * If Bluetooth is not turned on, we can't use the bluetooth client.
	 */
	protected void setupNetworkClients()
	{

		WifiManager wifiManager = (WifiManager)this.getSystemService( Context.WIFI_SERVICE );
		if ( wifiManager.isWifiEnabled() )
		{
			NetworkManager.getInstance().addNetworkClient( TcpClient.create( this.checkConnectionIntent() ) );
		}
		else
		{
			Toast.makeText( this, "Wifi not connected", 2000 );
		}

	}

	/** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) 
    {
    	
        super.onCreate( savedInstanceState );

		this.setupNetworkClients();
		
		// MameControllerClient.bluetoothClient = new AndroidBluetoothClient( this );

		this.qrCodeReader = new AndroidQrCodeReader( this );
		MameControllerClient.qrCodeReader = this.qrCodeReader;

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize( new MameControllerClient(), config );
    }

	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent intent )
	{
		this.qrCodeReader.receiveResult( intent.getDataString() );
	}
    
}