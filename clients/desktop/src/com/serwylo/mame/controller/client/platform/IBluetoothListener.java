package com.serwylo.mame.controller.client.platform;

import com.serwylo.mame.controller.client.net.bluetooth.BluetoothEvent;

/**
 * The {@link BluetoothClient} will send out {@link com.serwylo.mame.controller.client.net.bluetooth.BluetoothEvent}s to this interface,
 * so that the GUI can be updated.
 */
public interface IBluetoothListener 
{
	
	public void onBluetoothEvent( BluetoothEvent e );
	
}
