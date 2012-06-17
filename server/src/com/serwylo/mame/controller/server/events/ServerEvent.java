package com.serwylo.mame.controller.server.events;

import com.serwylo.mame.controller.server.NetworkClientWorker;

/**
 * This event contains information about server status changes such as it being ready to listen for clients...
 */
public class ServerEvent
{


	public static final int TYPE_SERVER_CONNECTED = 1;
	public static final int TYPE_NEW_CLIENT = 2;
	public static final int TYPE_SERVER_DISCONNECTED = 3;

	private int type;
	private String status;
	private Exception cause;
	private NetworkClientWorker client;

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

	public NetworkClientWorker getClient()
	{
		return client;
	}

	public static ServerEvent createServerConnected( String serverName )
	{
		ServerEvent event = new ServerEvent();
		event.type = TYPE_SERVER_CONNECTED;
		event.status = serverName;
		return event;
	}

	public static ServerEvent createNewClient( NetworkClientWorker client )
	{
		ServerEvent event = new ServerEvent();
		event.type = TYPE_NEW_CLIENT;
		event.client = client;
		return event;
	}

	public static ServerEvent createServerDisconnected( String serverName )
	{
		ServerEvent event = new ServerEvent();
		event.type = TYPE_SERVER_DISCONNECTED;
		event.status = serverName;
		return event;
	}

}
