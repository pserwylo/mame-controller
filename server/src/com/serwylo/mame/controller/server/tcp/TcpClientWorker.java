package com.serwylo.mame.controller.server.tcp;

import com.serwylo.mame.controller.shared.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpClientWorker implements Runnable
{

	protected TcpServer server;
	protected Socket clientSocket;

	public TcpClientWorker( TcpServer server, Socket clientSocket )
	{
		this.server = server;
		this.clientSocket = clientSocket;
	}

	@Override
	public void run()
	{
		try
		{
			System.out.println( "Connected to client..." );

			BufferedReader input = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
			System.out.println( "Waiting for input from client..." );
			String message = input.readLine();

			while ( message != null )
			{
				System.out.println( "Server received: '" + message.trim() + "'" );
				Event event = Event.createFromString( message );

				this.server.receiveEvent( event );

				System.out.println( "Waiting for input from client..." );
				message = input.readLine();
			}

			System.out.println( "Stopping server" );
			input.close();
			this.clientSocket.close();
		}
		catch ( IOException ioe )
		{
			System.err.println( "Error with client socket:" );
			System.err.println( ioe.getMessage() );
		}
	}

}
