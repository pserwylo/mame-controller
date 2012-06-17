package com.serwylo.mame.controller.server.events;

/**
 * This event contains information about clients connecting and disconnecting.
 */
public class ClientEvent
{

	public static final int TYPE_CLIENT_CONNECTED = 1;
	public static final int TYPE_CLIENT_DISCONNECTED = 2;

	private int type;
	private String status;
	private Exception cause;

	public int getType()
	{
		return type;
	}

	public String getStatus()
	{
		return status;
	}

	public Exception getCause()
	{
		return cause;
	}

	public static ClientEvent createClientConnected( String clientName )
	{
		ClientEvent event = new ClientEvent();
		event.type = TYPE_CLIENT_CONNECTED;
		event.status = clientName;
		return event;
	}

	public static ClientEvent createClientDisconnected( String clientName )
	{
		return ClientEvent.createClientDisconnected( clientName, null );
	}

	public static ClientEvent createClientDisconnected( String clientName, Exception cause )
	{
		ClientEvent event = new ClientEvent();
		event.type = TYPE_CLIENT_DISCONNECTED;
		event.status = clientName;
		event.cause = cause;
		return event;
	}

}
