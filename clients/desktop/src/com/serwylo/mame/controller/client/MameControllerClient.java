package com.serwylo.mame.controller.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.serwylo.mame.controller.client.platform.BluetoothClient;
import com.serwylo.mame.controller.client.platform.QrCodeReader;
import com.serwylo.mame.controller.client.screens.MainMenu;

public class MameControllerClient extends Game implements ApplicationListener 
{
	
	public static BluetoothClient bluetoothClient;
	public static QrCodeReader qrCodeReader;

	@Override
	public void create() 
	{
		this.setScreen( MainMenu.getInstance( this ) );
	}

}
