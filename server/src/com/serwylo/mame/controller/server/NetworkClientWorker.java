package com.serwylo.mame.controller.server;

import com.serwylo.mame.controller.server.events.ClientEvent;
import com.serwylo.mame.controller.server.events.IClientEventListener;

import java.util.ArrayList;

public abstract class NetworkClientWorker implements Runnable
{

	private ArrayList<IClientEventListener> listeners = new ArrayList<IClientEventListener>();

	public void addClientEventListener( IClientEventListener listener )
	{
		if ( !this.listeners.contains( listener ) )
		{
			this.listeners.add( listener );
		}
	}

	public void removeClientEventListener( IClientEventListener listener )
	{
		if ( this.listeners.contains( listener ) )
		{
			this.listeners.remove(listener);
		}
	}

	protected void dispatchClientEvent( ClientEvent event )
	{
		for ( IClientEventListener listener : this.listeners )
		{
			listener.onClientEvent( event );
		}
	}
}
