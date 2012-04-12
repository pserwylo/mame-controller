package com.serwylo.mame.controller.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.serwylo.mame.controller.shared.Event;

public class Network
{

	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;
	
	private static Network instance = new Network();
	
	private Network()
	{
		
	}

	public static Network getInstance()
	{
		return instance;
	}
	
	public boolean open()
	{
		try
		{
			this.socket = new Socket( "192.168.1.7", 8282 );
			this.output = new PrintWriter( this.socket.getOutputStream(), true );
			this.input = new BufferedReader( new InputStreamReader( this.socket.getInputStream() ) );
			return true;
		}
		catch( IOException ioe )
		{
			System.err.println( ioe.getMessage() );
			ioe.printStackTrace();
			return false;
		}
	}
	
	public void sendEvent( Event event )
	{
		System.err.println( "Sending to server: '" + event.toString() + "'" );
		this.output.write( event.toString() );
		this.output.flush();
	}
	
	public void close()
	{
		try
		{
			if ( this.output != null )
			{
				this.sendEvent( Event.createCloseEvent() );
				this.output.close();
			}
			
			if ( this.input != null )
			{
				this.input.close();
			}
			
			if ( this.socket != null )
			{
				this.socket.close();
			}
		}
		catch( IOException ioe )
		{
			System.err.println( ioe.getMessage() );
			ioe.printStackTrace();
		}
	}
	
}