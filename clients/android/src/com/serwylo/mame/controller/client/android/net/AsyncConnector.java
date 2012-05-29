package com.serwylo.mame.controller.client.android.net;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class AsyncConnector extends AsyncTask<Void, ConnectionEvent, Boolean> implements IConnectionListener
{

	private NetworkClient client;

	public AsyncConnector( NetworkClient client )
	{
		this.client = client;
	}

	/**
	 * Runs the {@link com.serwylo.mame.controller.client.android.net.NetworkClient#open()} method on this thread.
	 * Listens to the {@link AsyncConnector#client} for {@link ConnectionEvent}s, and then forwards them to anybody
	 * listening to this class for ConnectionEvents, via the UI thread.
	 * @param params
	 * @return
	 */
	@Override
	public Boolean doInBackground( Void... params )
	{
		this.client.addConnectionListener( this );
		this.client.open();
		return true;
	}

	/**
	 * Post the event to the progress queue, so that we can see it on the UI thread.
	 * @param event
	 */
	@Override
	public void onConnectionEvent( ConnectionEvent event )
	{
		this.publishProgress( event );
	}

	/**
	 * Receive the connection event from the connection thread, and now that we are back on the UI thread, forward
	 * the event to whoever is listening (hopefully the connection activity).
	 * @param events
	 */
	@Override
	protected void onProgressUpdate( ConnectionEvent... events )
	{
		this.notifyListeners( events[ 0 ] );
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
