package com.serwylo.mame.controller.client.io;

/**
 * The {@link IBluetoothClient} will send out {@link BluetoothEvent}s to this interface,
 * so that the GUI can be updated.
 */
public interface IBluetoothListener 
{
	
	public void onBluetoothEvent( BluetoothEvent e );
	
}
