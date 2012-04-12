package com.serwylo.mame.controller.server;

import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.intel.bluetooth.BluetoothConsts;
import com.serwylo.mame.controller.shared.Event;

public class MameControllerServer implements DiscoveryListener
{
	
	public static void main( String[] args )
	{
		new MameControllerServer().waitForConnection();
	}
	
	public MameControllerServer()
	{
	}

	/** Waiting for connection from devices */
	private void waitForConnection() {
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
			notifier = (StreamConnectionNotifier)Connector.open( url );
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
	
	public void run()
	{
		
		
		try
		{
			/*
			LocalDevice device = LocalDevice.getLocalDevice();
			DiscoveryAgent agent = device.getDiscoveryAgent();
			
			agent.startInquiry( DiscoveryAgent.LIAC, this );
			System.out.println( "Inquiry started" );
			
			RemoteDevice[] remoteDevices = agent.retrieveDevices( DiscoveryAgent.PREKNOWN );
			
			while( true )
			{
				Thread.sleep( 100 );
			}
			*/
			
			/*
			Robot robot = new Robot();
			
			ServerSocket serverSocket = new ServerSocket( 8282, 50, InetAddress.getByName( "192.168.1.7" ) );
			System.out.println( "Started server" );
			
			System.out.println( "Waiting for client..." );
			Socket clientSocket = serverSocket.accept();
			System.out.println( "Connected to client..." );
			
			BufferedReader input = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
			System.out.println( "Waiting for input from client..." );
			String message = input.readLine();
			
			while ( message != null )
			{
				System.out.println( "Server received: '" + message + "'" );
				Event event = Event.createFromString( message );
				System.out.println( "Event: '" + event + "'" );
				if ( event.getType() == Event.TYPE_CLOSE )
				{
					System.out.println( "Gracefully closing socket" );
					break;
				}
				else
				{
					switch( event.getType() )
					{
					case Event.TYPE_KEY_DOWN:
						robot.keyPress( event.getKeyCode() );
						break;
						
					case Event.TYPE_KEY_UP:
						robot.keyRelease( event.getKeyCode() );
						break;
					}
				}

				System.out.println( "Waiting for input from client..." );
				message = input.readLine();
			}

			System.out.println( "Stopping server" );
			input.close();
			clientSocket.close();
			serverSocket.close();
			*/
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			System.out.println( e.getMessage() );
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
	
}
