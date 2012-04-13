package com.serwylo.mame.controller.client.android;

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
    	
        super.onCreate(savedInstanceState);
        
		MameControllerClient.bluetoothClient = new AndroidBluetoothClient( this );
        
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize( new MameControllerClient(), config );
    }
    
}