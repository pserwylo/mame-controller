package com.serwylo.mame.controller.client.android.net;

import com.serwylo.mame.controller.shared.Event;

import java.util.ArrayList;

public abstract class NetworkClient
{

	private static NetworkClient current;

	/**
	 * Returns the currently connected network client.
	 * @return Will return null if not connected.
	 */
	public static NetworkClient getCurrent()
	{
		return NetworkClient.current;
	}

	/**
	 * Change the currently available network client.
	 * @param current
	 */
	protected static void setCurrent( NetworkClient current )
	{
		NetworkClient.current = current;
	}

	protected boolean isConnected = false;

	public abstract void open();

	public abstract void sendEvent( Event event );

	public abstract void close();

	public abstract String toString();

	public final boolean isConnected()
	{
		return this.isConnected;
	}

	public final void addConnectionListener( IConnectionListener listener )
	{
		if ( !this.listeners.contains( listener ) )
		{
			this.listeners.add( listener );
		}
	}

	public final void removeConnectionListener( IConnectionListener listener )
	{
		if ( this.listeners.contains( listener ) )
		{
			this.listeners.remove( listener );
		}
	}

	protected void notifyListeners( ConnectionEvent event )
	{
		for ( IConnectionListener listener : this.listeners )
		{
			listener.onConnectionEvent( event );
		}
	}

	private ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();

}
