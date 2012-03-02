package com.serwylo.mame.controller.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server
{

	public static final String MSG_CLOSE = "Close";
	public static final String MSG_BTN_DOWN = "BtnDown:%";
	public static final String MSG_BTN_UP = "BtnUp:%";

	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;
	
	private static Server instance = new Server();
	
	private Server()
	{
		
	}

	public static Server getInstance()
	{
		return instance;
	}
	
	public boolean open()
	{
		try
		{
			this.socket = new Socket( "localhost", 8282 );
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
	
	private String generateButtonMessage( String message, Button.Type type )
	{
		return message.replace( '%', type.toChar() );
	}
	
	public void sendButton( Button.Type type )
	{
		String message = generateButtonMessage( MSG_BTN_DOWN, type );
		this.output.write( message );
		this.output.flush();
		System.out.println( "Writing '" + message + "' to server" );
	}
	
	public void close()
	{
		try
		{
			if ( this.output != null )
			{
				this.output.write( MSG_CLOSE );
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
