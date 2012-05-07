package com.serwylo.mame.controller.client.net;

import com.serwylo.mame.controller.shared.Event;

public abstract class NetworkClient
{

	protected boolean isConnected = false;

	public abstract boolean open();

	public abstract void sendEvent( Event event );

	public abstract void close();

	public abstract String toString();

	public final boolean isConnected()
	{
		return this.isConnected;
	}

}
