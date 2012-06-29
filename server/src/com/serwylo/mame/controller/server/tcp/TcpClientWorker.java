package com.serwylo.mame.controller.server.tcp;

import com.serwylo.mame.controller.server.NetworkClientWorker;
import com.serwylo.mame.controller.server.events.ClientEvent;
import com.serwylo.mame.controller.shared.InputEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpClientWorker extends NetworkClientWorker implements Runnable
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
		this.dispatchClientEvent(ClientEvent.createClientConnected( this.clientSocket.getInetAddress().getHostName() ) );

		try
		{
			System.out.println( "Connected to client..." );

			BufferedReader input = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
			System.out.println( "Waiting for input from client..." );
			String message = input.readLine();

			while ( message != null )
			{
				System.out.println( "Server received: '" + message.trim() + "'" );
				InputEvent event = InputEvent.createFromString(message);

				this.server.receiveEvent( event );

				System.out.println( "Waiting for input from client..." );
				message = input.readLine();
			}

			System.out.println( "Stopping server" );
			input.close();
			this.clientSocket.close();

			this.dispatchClientEvent( ClientEvent.createClientDisconnected( this.clientSocket.getInetAddress().getHostName() ) );
		}
		catch ( IOException ioe )
		{
			System.err.println( "Error with client socket:" );
			System.err.println( ioe.getMessage() );

			this.dispatchClientEvent(ClientEvent.createClientDisconnected( this.clientSocket.getInetAddress().getHostName(), ioe ) );
		}
		finally
		{
			// TODO: Listen to all keyPress events, and if there is not a corresponding keyRelease event, then dispatch
			// that now...
		}

	}

}
