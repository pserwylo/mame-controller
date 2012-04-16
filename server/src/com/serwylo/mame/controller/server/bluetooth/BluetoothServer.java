package com.serwylo.mame.controller.server.bluetooth;


import com.intel.bluetooth.BluetoothConsts;
import com.serwylo.mame.controller.server.Server;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothServer extends Server implements DiscoveryListener
{



	@Override
	public void run()
	{
		// retrieve the local Bluetooth device object
		LocalDevice local = null;

		StreamConnectionNotifier notifier;
		StreamConnection connection = null;

		// setup the server to listen for connection
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable( DiscoveryAgent.GIAC );


			// Unique UUID for this application

			// UUID uuid = new UUID( 80087355 ); // "04c6093b-0000-1000-8000-00805f9b34fb"
			String url = "btspp://localhost:" + BluetoothConsts.SERIAL_PORT_UUID + ";name=MameBluetooth";
			System.out.println( "Opening service at - " + url );
			notifier = (StreamConnectionNotifier) Connector.open( url );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return;
		}

		// waiting for connection
		while( true )
		{
			try
			{
				System.out.println("waiting for connection...");
				connection = notifier.acceptAndOpen();
				System.out.println("Received connection...");

				Thread processThread = new Thread( new ProcessConnectionThread( connection ) );
				processThread.start();
			}
			catch ( Exception e )
			{
				e.printStackTrace();
				return;
			}
		}

	}

	@Override
	public void deviceDiscovered( RemoteDevice arg0, DeviceClass arg1 )
	{
		System.out.println( "Device discovered" );
	}

	@Override
	public void inquiryCompleted( int arg0 )
	{
		System.out.println( "Inquiry completed" );
	}

	@Override
	public void serviceSearchCompleted( int arg0, int arg1 )
	{
		System.out.println( "Service search completed" );
	}

	@Override
	public void servicesDiscovered( int arg0, ServiceRecord[] arg1 )
	{
		System.out.println( "Services discovered" );
	}


	@Override
	public boolean start()
	{
		return false;
	}

	@Override
	public void stop()
	{

	}
}
