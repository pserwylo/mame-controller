package com.serwylo.mame.controller.client.android;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.badlogic.gdx.Gdx;
import com.serwylo.mame.controller.client.io.BluetoothEvent;
import com.serwylo.mame.controller.client.io.IBluetoothClient;
import com.serwylo.mame.controller.shared.Event;

public class AndroidBluetoothClient extends IBluetoothClient
{

	private BluetoothAdapter bluetoothAdapter;
	
	private BluetoothCommandService service;
	
	private Activity activity;
	
	private ArrayList<BluetoothDevice> deviceList;
	
	public AndroidBluetoothClient( Activity app )
	{
		this.activity = app;
		this.deviceList = new ArrayList<BluetoothDevice>();
	}
	
	@Override
	public void init()
	{
		Gdx.app.log( "BLUETOOTH", "Attempting to connect to bluetooth server." );
		
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if ( this.bluetoothAdapter == null )
		{
			Gdx.app.log( "BLUETOOTH", "Could not find bluetooth adapter." );
			return;
		}
		
		if ( !this.bluetoothAdapter.isEnabled() )
		{
			Gdx.app.log( "BLUETOOTH", "Enabling bluetooth..." );
			this.bluetoothAdapter.enable();
			Gdx.app.log( "BLUETOOTH", "Done." );
			if ( !this.bluetoothAdapter.isEnabled() )
			{
				Gdx.app.log( "BLUETOOTH", "Could not enable bluetooth adapter." );
			}
		}

		Gdx.app.log( "BLUETOOTH", "Bluetooth ready for discovery." );
	}
	
	@Override
	public void connect( String address )
	{
		this.service = new BluetoothCommandService();
		this.service.connect( this.bluetoothAdapter.getRemoteDevice( address ) );
	}
	
	public void discover()
	{
		Gdx.app.log( "BLUETOOTH", "Starting discovery..." );
		
		this.deviceList.clear();
		
		IntentFilter filter = new IntentFilter( BluetoothDevice.ACTION_FOUND );
		this.activity.registerReceiver( this.receiver, filter );
		
		filter = new IntentFilter( BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
		this.activity.registerReceiver( this.receiver, filter );
		
		if ( this.bluetoothAdapter.isDiscovering() )
		{
			Gdx.app.log( "BLUETOOTH", "Cancelling previous discovery." );
			this.bluetoothAdapter.cancelDiscovery();
		}
		
		this.bluetoothAdapter.startDiscovery();
	}

	@Override
	public void send( Event event )
	{
		
	}

	@Override
	public void close()
	{
		
	}

	private final BroadcastReceiver receiver = new BroadcastReceiver()
	{
		
		@Override
		public void onReceive( Context context, Intent intent ) 
		{
			if ( intent.getAction().equals( BluetoothDevice.ACTION_FOUND ) )
			{
	            BluetoothDevice device = intent.getParcelableExtra( BluetoothDevice.EXTRA_DEVICE );
	            deviceList.add( device );
	            Gdx.app.log( "BLUETOOTH", "Found device '" + device.getName() + "'" );
	            
	            sendBluetoothEvent( BluetoothEvent.createDeviceFoundEvent( device.getName(), device.getAddress() ) );
			}
			else if ( intent.getAction().equals( BluetoothAdapter.ACTION_DISCOVERY_FINISHED ) )
			{
				Gdx.app.log( "BLUETOOTH", "Discovery complete." );
				sendBluetoothEvent( BluetoothEvent.createEndDiscoveryEvent() );
				
				Gdx.app.log( "BLUETOOTH", "Cancelling discovery." );
				activity.unregisterReceiver( this );
				bluetoothAdapter.cancelDiscovery();
			}
		}
		
	};
	
}
