package com.serwylo.mame.controller.client.android.net;

public class ConnectionEvent
{

	public static final int TYPE_CONNECTED = 1;
	public static final int TYPE_ERROR = 2;
	public static final int TYPE_STATUS = 4;

	private int type;
	private NetworkClient client;
	private String status;
	private Exception cause;

	/**
	 * The type of the event.
	 * @return One of the following: {@link ConnectionEvent#TYPE_CONNECTED}, {@link ConnectionEvent#TYPE_ERROR} or
	 * {@link ConnectionEvent#TYPE_STATUS}.
	 */
	public int getType()
	{
		return this.type;
	}

	/**
	 * The {@link NetworkClient} which triggered this event.
	 * @return
	 */
	public NetworkClient getClient()
	{
		return this.client;
	}

	public String getStatus()
	{
		return this.status;
	}

	/**
	 * The exception which caused this error event.
	 * Only available for {@link ConnectionEvent#TYPE_ERROR} {@link ConnectionEvent}s.
	 * @return
	 */
	public Exception getCause()
	{
		return this.cause;
	}

	public static ConnectionEvent createConnectedEvent( NetworkClient client )
	{
		ConnectionEvent event = new ConnectionEvent();
		event.client = client;
		event.type = ConnectionEvent.TYPE_CONNECTED;
		return event;
	}

	public static ConnectionEvent createErrorEvent( NetworkClient client, String errorDescription )
	{
		return ConnectionEvent.createErrorEvent( client, errorDescription, null );
	}

	public static ConnectionEvent createErrorEvent( NetworkClient client, String errorDescription, Exception cause )
	{
		ConnectionEvent event = new ConnectionEvent();
		event.client = client;
		event.status = errorDescription;
		event.cause = cause;
		event.type = ConnectionEvent.TYPE_ERROR;
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
