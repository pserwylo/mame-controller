package com.serwylo.mame.controller.server.events;

/**
 * This event contains information about clients connecting and disconnecting.
 */
public class ClientEvent
{

	public static final int TYPE_CLIENT_CONNECTED = 1;
	public static final int TYPE_CLIENT_DISCONNECTED = 2;

	private int connectedClients;
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

	/**
	 * The number of connected clients, AFTER this client has been connected/disconnected.
	 * @return
	 */
	public int getConnectedClients()
	{
		return connectedClients;
	}

	public static ClientEvent createClientConnected( String clientName, int connectedClients )
	{
		ClientEvent event = new ClientEvent();
		event.type = TYPE_CLIENT_CONNECTED;
		event.status = clientName;
		event.connectedClients = connectedClients;
		return event;
	}

	public static ClientEvent createClientDisconnected( String clientName, int connectedClients )
	{
		return ClientEvent.createClientDisconnected( clientName, null, connectedClients );
	}

	public static ClientEvent createClientDisconnected( String clientName, Exception cause, int connectedClients )
	{
		ClientEvent event = new ClientEvent();
		event.type = TYPE_CLIENT_DISCONNECTED;
		event.status = clientName;
		event.cause = cause;
		event.connectedClients = connectedClients;
		return event;
	}

}
