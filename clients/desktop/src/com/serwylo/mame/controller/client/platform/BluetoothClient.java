package com.serwylo.mame.controller.client.platform;

import com.serwylo.mame.controller.client.bluetooth.BluetoothEvent;
import com.serwylo.mame.controller.shared.Event;

public abstract class BluetoothClient
{
	
	public abstract void init();
	
	public abstract void send( Event event );

	public abstract void discover();
	
	public abstract void close();
	
	public abstract void connect( String address );
	
	private IBluetoothListener listener;
	
	public void setBluetoothListener( IBluetoothListener listener )
	{
		this.listener = listener;
	}
	
	protected void sendBluetoothEvent( BluetoothEvent event )
	{
		if ( this.listener != null )
		{
			this.listener.onBluetoothEvent( event );
		}
	}
	
}
