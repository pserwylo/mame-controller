package com.serwylo.mame.controller.client.net;

import com.serwylo.mame.controller.shared.InputEvent;

import java.util.ArrayList;

/**
 * Keeps hold of references to various network clients which have been established (e.g. bluetooth and
 * TCP/IP).
 *
 * IS Responsible for:
 * Dispatching events to the appropriate network clients, so the UI elements which want to talk
 * to a server don't need to know which clients are available.
 *
 * NOT Responsible for:
 * Establishing connections and closing connections. This should be done before the client is passed
 * here to be looked after.
 */
public class NetworkManager
{

	private static NetworkManager instance = new NetworkManager();

	public static NetworkManager getInstance()
	{
		return instance;
	}

	protected ArrayList<NetworkClient> clients = new ArrayList<NetworkClient>();

	public void addNetworkClient( NetworkClient client )
	{
		if ( !this.clients.contains( client ) )
		{
			this.clients.add( client );
		}
	}

	public void removeNetworkClient( NetworkClient client )
	{
		if ( this.clients.contains( client ) )
		{
			this.clients.remove( client );
		}
	}

	public <C extends NetworkClient> C getNetworkClient( Class<C> classType )
	{
		for ( NetworkClient client : this.clients )
		{
			if ( client.getClass() == classType )
			{
				return (C)client;
			}
		}
		return null;
	}

	/**
	 * Forwards 'event' to the network clients which exist.
	 * @param event
	 */
	public void sendEvent( InputEvent event )
	{
		for ( NetworkClient client : this.clients )
		{
			client.sendEvent( event );
		}
	}

	public ArrayList<NetworkClient> getAllClients()
	{
		return this.clients;
	}

}
