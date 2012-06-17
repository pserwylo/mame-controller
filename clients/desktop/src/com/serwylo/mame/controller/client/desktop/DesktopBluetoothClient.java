package com.serwylo.mame.controller.client.desktop;

import com.serwylo.mame.controller.client.platform.BluetoothClient;
import com.serwylo.mame.controller.shared.InputEvent;

public class DesktopBluetoothClient extends BluetoothClient
{

	@Override
	public void init()
	{
		System.out.println( "Pretending to connect to bluetooth server..." );
	}

	@Override
	public void connect( String address )
	{
		
	}
	
	@Override
	public void send( InputEvent event )
	{
		System.out.println( "Pretending to send event '" + event.toString() + "' to bluetooth server..." );
	}

	@Override
	public void close()
	{
		System.out.println( "Pretending to close connection to bluetooth server..." );
	}

	@Override
	public void discover() 
	{
		System.out.println( "Pretending to discover bluetooth devices..." );
	}

}
