package com.serwylo.mame.controller.server;

import com.serwylo.mame.controller.server.events.IMameEventListener;
import com.serwylo.mame.controller.shared.Event;

import java.util.ArrayList;

public abstract class Server implements Runnable
{

	/**
	 * Does whatever is required to start, e.g. opening sockets.
	 * If it cannot start successfully, then return false.
	 * @return
	 */
	public abstract boolean start();

	/**
	 * Close down any sockets or other resources which are being used by the server.
	 * @return
	 */
	public abstract void stop();

	private ArrayList<IMameEventListener> eventListeners = new ArrayList<IMameEventListener>();

	public void addMameEventListener( IMameEventListener listener )
	{
		if ( !this.eventListeners.contains( listener ) )
		{
			this.eventListeners.add( listener );
		}
	}

	public void removeMameEventListener( IMameEventListener listener )
	{
		if ( this.eventListeners.contains( listener ) )
		{
			this.eventListeners.remove( listener );
		}
	}

	protected void dispatchEvent( Event event )
	{
		for ( IMameEventListener listener : this.eventListeners )
		{
			listener.receiveEvent( event );
		}
	}

}
