package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.net.AsyncConnector;
import com.serwylo.mame.controller.client.android.net.ConnectionEvent;
import com.serwylo.mame.controller.client.android.net.IConnectionListener;
import com.serwylo.mame.controller.client.android.net.NetworkClient;
import com.serwylo.mame.controller.client.android.net.wifi.WifiClient;
import com.serwylo.mame.controller.client.android.net.wifi.WifiProfile;
import com.serwylo.mame.controller.client.android.util.WifiBarcodeParser;

public class WifiConnectActivity extends Activity implements IConnectionListener
{

	public static final String DATA_CONNECTION_PROFILE = "connectionProfile";

	public static final String ACTION_CONNECT_WITH_PROFILE = "connectWithProfile";

	/**
	 * Show the view, and try to connect to this profile.
	 * @param profile
	 * @param context
	 */
	public static void connect( WifiProfile profile, Context context )
	{
		Intent intent = new Intent( context, WifiConnectActivity.class );
		intent.setAction( WifiConnectActivity.ACTION_CONNECT_WITH_PROFILE );
		intent.putExtra( DATA_CONNECTION_PROFILE, profile );
		context.startActivity( intent );
	}

	/**
	 * Show the view, and try to connect to this profile.
	 * @param connectionString Connection string provided by the server (e.g. http://mc.serwylo.com/wifi?host=192.168.1.7&port=57368)
	 * @param context
	 */
	public static void connect( String connectionString, Context context )
	{
		Intent intent = new Intent( context, WifiConnectActivity.class );
		intent.setAction( Intent.ACTION_VIEW );
		intent.setData( Uri.parse( connectionString ) );
		context.startActivity( intent );
	}

	/**
	 * Inspect the intent which launched this view.
	 * It could be either launched from an external barcode scanner (in which case it will be {@link Intent#ACTION_VIEW},
	 * or it could be launched from another view in this app, in which case we'd expect it to be a
	 * {@link WifiConnectActivity#ACTION_CONNECT_WITH_PROFILE}.
	 *
	 * In the case of {@link Intent#ACTION_VIEW}, we expect {@link android.content.Intent#getData()} to have a string
	 * with the connection string of the server.
	 *
	 * In the case of {@link WifiConnectActivity#ACTION_CONNECT_WITH_PROFILE}, we expect {@link android.content.Intent#getExtras()}
	 * to have a key {@link WifiConnectActivity#DATA_CONNECTION_PROFILE}.
	 * @return A profile which holds info about the host and port of the server to connect to.
	 */
	protected void onStart()
	{
		super.onStart();

		WifiProfile profile = null;
		Intent intent = this.getIntent();
		String error = null;

		// Instead of Intent.ACTION_MAIN which is the intent when opened from the launcher...
		if ( intent != null && intent.getAction() != null )
		{
			if ( intent.getAction().equals( Intent.ACTION_VIEW ) )
			{
				Uri uri = intent.getData();
				if ( uri != null )
				{
					try
					{
						profile = WifiBarcodeParser.create( uri ).createProfile();
					}
					catch( IllegalArgumentException e )
					{
						error = "Could not identify server at: " + uri + ".\nAre you sure you scanned the correct barcode?";
					}
				}
			}
			else if ( intent.getAction().equals( WifiConnectActivity.ACTION_CONNECT_WITH_PROFILE ) )
			{
				profile = (WifiProfile)intent.getExtras().get( WifiConnectActivity.DATA_CONNECTION_PROFILE );
			}
		}

		if ( profile != null )
		{
			NetworkClient client = profile.createClient();
			AsyncConnector connector = new AsyncConnector( client );
			connector.addConnectionListener( this );
			connector.execute();
		}
		else
		{
			if ( error == null )
			{
				error = "Not quite sure went wrong, sorry.";
			}

			((TextView)this.findViewById( R.id.txtStatus )).setText( "Uh oh..." );
			((TextView)this.findViewById( R.id.txtSubStatus )).setText( error );
		}
	}

	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.connecting );
	}

	@Override
	public void onConnectionEvent(ConnectionEvent event)
	{
		switch( event.getType() )
		{
			case ConnectionEvent.TYPE_CONNECTED:
				((TextView)this.findViewById( R.id.txtSubStatus )).setText( "Connected!" );
				break;

			case ConnectionEvent.TYPE_STATUS:
				((TextView)this.findViewById( R.id.txtStatus )).setText( event.getClient().toString() );
				((TextView)this.findViewById( R.id.txtSubStatus )).setText( event.getStatus() );

				ControllerActivity.showDefaultController( this );
				break;

			case ConnectionEvent.TYPE_ERROR:

				break;
		}
	}
}