package com.serwylo.mame.controller.server;

import com.serwylo.mame.controller.shared.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Server
{

	protected InetAddress address;

	protected int port;

	protected ServerSocket socket;

	public TcpServer( InetAddress addressToBind, int portToBind )
	{
		this.address = addressToBind;
		this.port = portToBind;
	}

	protected void start()
	{

	}

	protected void stop()
	{

	}

	@Override
	public void run()
	{

		try
		{
			Robot robot = new Robot();

			ServerSocket serverSocket = new ServerSocket( this.port, 50, this.address );
			System.out.println( "Started server at " + this.address.getHostAddress() + ":" + this.port );

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
		}
		catch ( AWTException awte )
		{
			System.err.println( "Error while trying to build robot to control server" );
			System.err.println( awte.getMessage() );
		}
		catch ( BindException be )
		{
			System.err.println( "Failed while binding to " + this.address + ":" + this.port );
			System.err.println( be.getMessage() );
		}
		catch ( IOException ioe )
		{
			System.err.println( ioe.getMessage() );
		}


	}

}
