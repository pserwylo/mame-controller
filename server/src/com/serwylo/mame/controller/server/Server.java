package com.serwylo.mame.controller.server;

import com.serwylo.mame.controller.server.events.IInputEventListener;
import com.serwylo.mame.controller.server.events.IServerEventListener;
import com.serwylo.mame.controller.server.events.ServerEvent;
import com.serwylo.mame.controller.shared.InputEvent;

import java.awt.event.KeyEvent;
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

	private ArrayList<IInputEventListener> inputEventListeners = new ArrayList<IInputEventListener>();
	private ArrayList<IServerEventListener> serverEventListeners = new ArrayList<IServerEventListener>();

	public void addInputEventListener( IInputEventListener listener )
	{
		if ( !this.inputEventListeners.contains( listener ) )
		{
			this.inputEventListeners.add( listener );
		}
	}

	public void removeInputEventListener( IInputEventListener listener )
	{
		if ( this.inputEventListeners.contains( listener ) )
		{
			this.inputEventListeners.remove( listener );
		}
	}

	protected void dispatchInputEvent( InputEvent event )
	{
		for ( IInputEventListener listener : this.inputEventListeners )
		{
			listener.onInputEvent( event );
		}
	}

	public void addServerEventListener( IServerEventListener listener )
	{
		if ( !this.serverEventListeners.contains( listener ) )
		{
			this.serverEventListeners.add( listener );
		}
	}

	public void removeServerEventListener( IServerEventListener listener )
	{
		if ( this.serverEventListeners.contains( listener ) )
		{
			this.serverEventListeners.remove( listener );
		}
	}

	protected void dispatchServerEvent( ServerEvent event )
	{
		for ( IServerEventListener listener : this.serverEventListeners )
		{
			listener.onServerEvent( event );
		}
	}

}
