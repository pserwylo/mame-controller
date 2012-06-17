package com.serwylo.mame.controller.client.net;

import com.serwylo.mame.controller.shared.InputEvent;

import java.util.ArrayList;

public abstract class NetworkClient
{

	protected boolean isConnected = false;

	public abstract boolean open();

	public abstract void sendEvent( InputEvent event );

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
