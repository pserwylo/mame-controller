package com.serwylo.mame.controller.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.serwylo.mame.controller.client.io.IBluetoothClient;
import com.serwylo.mame.controller.client.screens.MainMenu;

public class MameControllerClient extends Game implements ApplicationListener 
{
	
	public static IBluetoothClient bluetoothClient;
	
	@Override
	public void create() 
	{
		this.setScreen( MainMenu.getInstance( this ) );
	}

}
