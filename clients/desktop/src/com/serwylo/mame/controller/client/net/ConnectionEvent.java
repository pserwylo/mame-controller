package com.serwylo.mame.controller.client.net;

public class ConnectionEvent
{

	public static final int TYPE_CONNECTED = 1;
	public static final int TYPE_CONNECTION_FAILED = 2;
	public static final int TYPE_STATUS = 4;

	private int type;

	private NetworkClient client;

	private boolean successful;

	private String status;

	public int getType()
	{
		return this.type;
	}

	public NetworkClient getClient()
	{
		return this.client;
	}

	public boolean wasSuccessful()
	{
		return this.successful;
	}

	public String getStatus()
	{
		return status;
	}

	public static ConnectionEvent createConnectedEvent( NetworkClient client )
	{
		ConnectionEvent event = new ConnectionEvent();
		event.client = client;
		event.successful = true;
		event.type = ConnectionEvent.TYPE_CONNECTED;
		return event;
	}

	public static ConnectionEvent createConnectionFailedEvent( NetworkClient client, Exception reason )
	{
		ConnectionEvent event = new ConnectionEvent();
		event.client = client;
		event.successful = false;
		event.status = reason.getMessage();
		event.type = ConnectionEvent.TYPE_CONNECTION_FAILED;
		return event;
	}

	public static ConnectionEvent createStatusEvent( NetworkClient client, String status )
	{
		ConnectionEvent event = new ConnectionEvent();
		event.client = client;
		event.status = status;
		event.type = ConnectionEvent.TYPE_STATUS;
		return event;
	}

}
