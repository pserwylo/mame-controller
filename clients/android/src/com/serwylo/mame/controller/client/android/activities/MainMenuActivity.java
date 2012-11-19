package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.io.connectionProfiles.WifiProfileIo;
import com.serwylo.mame.controller.client.android.net.wifi.WifiProfile;

import java.util.ArrayList;

public class MainMenuActivity extends Activity implements View.OnClickListener
{

	private boolean hasStartedOnce = false;

	private boolean triedAutoConnect = false;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );

		this.setContentView( R.layout.main_menu );

		this.findViewById( R.id.btnConnectScan ).setOnClickListener( this );
		this.findViewById( R.id.btnSettings ).setOnClickListener( this );
		this.findViewById( R.id.btnQuit ).setOnClickListener( this );

	}

	public void onResume()
	{
		super.onResume();

		if ( !this.hasStartedOnce )
		{
			this.attemptAutoConnect();
			this.hasStartedOnce = true;
		}
		else
		{

		}
	}

	public static void show( Context context )
	{
		Intent intent = new Intent( context, MainMenuActivity.class );
		context.startActivity( intent );
	}

	/**
	 * Inspects the auto_connect preference to decide whether to attempt to connect to the last server.
	 * @return True if we went to a connect activity trying to connect, false if we didn't have the chance. Note that
	 * this has nothing to do with the success, we wont know that for a while.
	 */
	protected void attemptAutoConnect()
	{
		this.triedAutoConnect = false;

		if ( PreferenceManager.getDefaultSharedPreferences( this ).getBoolean( this.getString( R.string.pref_key_auto_connect ), true ) )
		{
			this.triedAutoConnect = WifiConnectActivity.connectAutomatically( this );
		}
	}

	@Override
	public void onClick( View view )
	{
		switch( view.getId() )
		{
			case R.id.btnConnectScan:
				this.connectByScanning();
				break;

			case R.id.btnSettings:
				SettingsActivity.show( this );
				break;

			case R.id.btnQuit:
				this.quit();
				break;
		}
	}

	/**
	 * Send user to list of saved connections so that they can open one.
	 * If none exist, they will have the opportunity to create one there.
	 */
	private void connectManually()
	{
		Intent intent = new Intent( this, ViewConnectionsActivity.class );
		this.startActivity( intent );
	}

	private void connectByScanning()
	{
		IntentIntegrator integrator = new IntentIntegrator( this );
		integrator.initiateScan( IntentIntegrator.QR_CODE_TYPES );
	}

	/**
	 * Parse the result of the barcode scan done by the user.
	 * @param scanResult If they haven't scanned anything (e.g. they hit the back button) then scanResult#getContents()
	 *  will be null. Otherwise, scanResult.getContents() will be the string from the decoded barcode.
	 */
	private void onScanComplete( IntentResult scanResult )
	{
		if ( scanResult != null )
		{
			if ( scanResult.getContents() != null )
			{
				Log.d( "MAME", "Scan results: '" + scanResult.getContents() + "'" );
				WifiConnectActivity.connect( scanResult.getContents(), this );
			}
			else
			{
				Log.d( "MAME", "Scan results empty" );
				Toast toast = Toast.makeText( this.getApplicationContext(), "Error scanning barcode.", 5000 );
				toast.show();
			}
		}
	}

	public void onActivityResult( int requestCode, int resultCode, Intent intent )
	{
		if ( requestCode == IntentIntegrator.REQUEST_CODE )
		{
			IntentResult scanResult = IntentIntegrator.parseActivityResult( requestCode, resultCode, intent );
			this.onScanComplete( scanResult );
		}
	}

	/**
	 * TODO: Should we prompt first? I think its a little wankey to do it if its from the main menu.
	 */
	private void quit()
	{
		this.finish();
	}

}