package com.serwylo.mame.controller.client.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.serwylo.mame.controller.client.MameControllerClient;

public class MameControllerActivity extends AndroidApplication 
{

	public static final int REQUEST_CONNECT_DEVICE = 1;
	
	/** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) 
    {
    	
        super.onCreate( savedInstanceState );

        Intent intent = this.getIntent();

        // Instead of Intent.ACTION_MAIN which is the intent when opened from the launcher...
        if ( intent.getAction().equals( Intent.ACTION_VIEW ) )
        {
            Uri uri = intent.getData();
            if ( uri != null )
            {
                String param = uri.getQueryParameter( "s" );
                if ( param != null )
                {
                    log( "MC", param );
                    String[] parts = param.split( ":" );
                    if ( parts.length == 2 )
                    {

                    }
                }
            }
        }

		MameControllerClient.bluetoothClient = new AndroidBluetoothClient( this );
        
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize( new MameControllerClient(), config );
    }
    
}