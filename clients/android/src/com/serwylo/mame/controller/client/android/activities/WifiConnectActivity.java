package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.io.connectionProfiles.WifiProfileIo;
import com.serwylo.mame.controller.client.android.net.AsyncConnector;
import com.serwylo.mame.controller.client.android.net.ConnectionEvent;
import com.serwylo.mame.controller.client.android.net.IConnectionListener;
import com.serwylo.mame.controller.client.android.net.NetworkClient;
import com.serwylo.mame.controller.client.android.net.wifi.WifiClient;
import com.serwylo.mame.controller.client.android.net.wifi.WifiProfile;
import com.serwylo.mame.controller.client.android.util.WifiBarcodeParser;

import java.util.ArrayList;
import java.util.Date;

public class WifiConnectActivity extends Activity implements IConnectionListener
{

	public static final String DATA_CONNECTION_PROFILE = "connectionProfile";

	public static final String ACTION_CONNECT_WITH_PROFILE = "connectWithProfile";
	public static final String ACTION_AUTO_CONNECT_WITH_PROFILE = "autoConnectWithProfile";

	protected WifiProfile profile;

	/**
	 * If we connected through the {@link WifiConnectActivity#ACTION_AUTO_CONNECT_WITH_PROFILE} then instead of showing
	 * errors to the user via our activity, we just throw up a {@link android.widget.Toast} with an error about
	 * auto connect failing and redirect to the main screen.
	 */
	private boolean returnToMainScreenOnError = false;

	/**
	 * Show the view, and try to connect to this profile.
	 * "Automatically" is in reference to the fact the user didn't explicitly ask to connect, we're just trying our
	 * luck connecting to the last known connection. What this means is that if we fail when attempting to connect, then
	 * we will just redirect them back to the main menu, rather than showing the error. We'll show a toast to let them
	 * know the auto connect failed.
	 * @param context
	 */
	public static boolean connectAutomatically( Context context )
	{
		boolean attemptedConnection = false;
		ArrayList<WifiProfile> profiles = new WifiProfileIo( context ).loadList();

		if ( profiles.size() > 0 )
		{
			Intent intent = new Intent( context, WifiConnectActivity.class );
			intent.setAction( WifiConnectActivity.ACTION_AUTO_CONNECT_WITH_PROFILE );
			intent.putExtra( DATA_CONNECTION_PROFILE, profiles.get( 0 ) );
			context.startActivity( intent );
			attemptedConnection = true;
		}

		return attemptedConnection;
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
	 */
	protected void onStart()
	{
		super.onStart();

		WifiProfile profile = null;
		boolean isAutoConnect = false;
		Intent intent = this.getIntent();
		String error = null;

		if ( intent != null && intent.getAction() != null )
		{
			// Instead of Intent.ACTION_MAIN which is the intent when opened from the launcher...
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
			else if ( intent.getAction().equals( WifiConnectActivity.ACTION_AUTO_CONNECT_WITH_PROFILE ) )
			{
				profile = (WifiProfile)intent.getExtras().get( WifiConnectActivity.DATA_CONNECTION_PROFILE );
				isAutoConnect = true;
			}
		}

		if ( profile != null )
		{
			this.profile = profile;
			this.returnToMainScreenOnError = isAutoConnect;
			NetworkClient client = profile.createClient();
			AsyncConnector connector = new AsyncConnector( this, client );
			connector.addConnectionListener( this );
			connector.execute();
		}
		else
		{
			if ( error == null )
			{
				error = "Not quite sure went wrong, sorry.";
			}

			this.updateStatus( "Uh oh...", error );
		}
	}

	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.connecting );
	}

	/**
	 * When the server tells us it is connected, we will:
	 * 	- Save it as the last connected server.
	 * 	- Update the UI to say connected (in case there is a lag in showing the controller activity).
	 * 	- Sets up the global {@link NetworkClient} object.
	 * 	- Switches to the controller activity.
	 * @param event
	 */
	protected void onConnected( ConnectionEvent event )
	{

		try
		{
			this.profile.setLastConnectDate( new Date( System.currentTimeMillis() ) );
			new WifiProfileIo( this ).save( this.profile );
		}
		catch ( Exception e )
		{
			Log.d( "WIFI", "Error saving profile details: " + e.getMessage() );
		}

		this.updateStatus( "", "Connected!" );
		NetworkClient.setCurrent( event.getClient() );
		ControllerActivity.showDefaultController( this );

	}

	protected void onError( ConnectionEvent event )
	{
		if ( this.returnToMainScreenOnError )
		{
			Toast.makeText( this, "Auto connect to " + event.getClient().toString() + " failed.\n\n" + event.getStatus(), 6000 ).show();
			finish();
		}
		else
		{
			updateStatus( "Uh oh...", "Could not connect to " + event.getClient().toString() + "\n\n" + event.getStatus() );
		}
	}

	@Override
	public void onConnectionEvent( ConnectionEvent event )
	{
		switch( event.getType() )
		{
			case ConnectionEvent.TYPE_CONNECTED:
				onConnected( event );
				break;

			case ConnectionEvent.TYPE_STATUS:
				updateStatus( event.getClient().toString(), event.getStatus() );
				break;

			case ConnectionEvent.TYPE_ERROR:
				onError( event );
				break;
		}
	}

	public void updateStatus( String title, String message )
	{
		((TextView)this.findViewById( R.id.txtStatus )).setText( title );
		((TextView)this.findViewById( R.id.txtSubStatus )).setText( message );
	}

}