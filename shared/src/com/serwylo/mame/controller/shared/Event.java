package com.serwylo.mame.controller.shared;

public class Event 
{

	public static final int TYPE_KEY_DOWN = 0;
	public static final int TYPE_KEY_UP = 1;
	public static final int TYPE_MOUSE_DOWN = 2;
	public static final int TYPE_MOUSE_UP = 3;
	public static final int TYPE_MOUSE_MOVE = 4;
	public static final int TYPE_CLOSE = 5;
	public static final int TYPE_HANDSHAKE = 6;
	
	public static Event createCloseEvent()
	{
		Event event = new Event();
		event.type = TYPE_CLOSE;
		return event;
	}
	
	/**
	 * @param key One of the VK_* constants from {@link java.awt.event.KeyEvent}
	 * @return
	 */
	public static Event createKeyDown( int keyCode )
	{
		Event event = new Event();
		event.keyCode = keyCode;
		event.type = TYPE_KEY_DOWN;
		return event;
	}

	/**
	 * @param key One of the VK_* constants from {@link java.awt.event.KeyEvent}
	 * @return
	 */
	public static Event createKeyUp( int keyCode )
	{
		Event event = new Event();
		event.keyCode = keyCode;
		event.type = TYPE_KEY_UP;
		return event;
	}
	
	public static Event createFromString( String string ) throws IllegalArgumentException
	{
		// Specifically interested in removing the newline character from the end...
		string = string.trim();
		String[] parts = string.split( ":" );
		if ( parts.length == 0 )
		{
			throw new IllegalArgumentException( "Expected event type. Cannot parse '" + string + "'." );
		}

		Event event = new Event();
		event.type = Integer.parseInt( parts[ 0 ] );
		
		if ( event.type == TYPE_KEY_DOWN || event.type == TYPE_KEY_UP )
		{
			if ( parts.length <= 1 )
			{
				throw new IllegalArgumentException( "Expected keycode after colon, not found. Cannot parse '" + string + "'." );
			}
			event.keyCode = Integer.parseInt( parts[ 1 ] );
		}
		
		return event;
	}
	
	public String toString()
	{
		String message;
		if ( type == TYPE_KEY_DOWN || type == TYPE_KEY_UP )
		{
			message = type + ":" + keyCode;
		}
		else 
		{
			message = "" + type;
		}
		
		return message + "\n";
	}
	
	private int keyCode;
	
	private int type;
	
	private Event()
	{
		
	}

	public int getType()
	{
		return this.type;
	}
	
	public int getKeyCode()
	{
		return this.keyCode;
	}
	
}