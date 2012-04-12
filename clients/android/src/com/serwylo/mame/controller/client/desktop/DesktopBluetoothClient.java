package com.serwylo.mame.controller.client.desktop;

import com.serwylo.mame.controller.client.io.IBluetoothClient;
import com.serwylo.mame.controller.shared.Event;

public class DesktopBluetoothClient extends IBluetoothClient 
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
	public void send( Event event )
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
