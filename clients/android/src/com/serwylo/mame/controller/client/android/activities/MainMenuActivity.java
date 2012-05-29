package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.util.WifiBarcodeParser;

import java.util.ArrayList;

public class MainMenuActivity extends Activity implements View.OnClickListener
{

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );

		this.setContentView( R.layout.main_menu );

		this.findViewById( R.id.btnConnectManual ).setOnClickListener( this );
		this.findViewById( R.id.btnConnectScan ).setOnClickListener( this );
		this.findViewById( R.id.btnSettings ).setOnClickListener( this );
		this.findViewById( R.id.btnQuit ).setOnClickListener( this );
	}

	@Override
	public void onClick( View view )
	{
		switch( view.getId() )
		{
			case R.id.btnConnectManual:
				this.connectManually();
				break;

			case R.id.btnConnectScan:
				this.connectByScanning();
				break;

			case R.id.btnSettings:
				this.showSettings();
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

	private void showSettings()
	{
		Intent intent = new Intent( this, SettingsActivity.class );
		this.startActivity( intent );
	}

	/**
	 * TODO: Should we prompt first? I think its a little wankey to do it if its from the main menu.
	 */
	private void quit()
	{
		this.finish();
	}

}