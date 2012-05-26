package com.serwylo.mame.controller.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.serwylo.mame.controller.client.ControllerLayout;
import com.serwylo.mame.controller.client.MameControllerClient;
import com.serwylo.mame.controller.client.net.ConnectionEvent;
import com.serwylo.mame.controller.client.net.IConnectionListener;
import com.serwylo.mame.controller.client.net.tcp.TcpClient;

import java.util.ArrayList;

public class TcpConnectScreen extends StatusScreen implements IConnectionListener
{

	private TcpClient client;

	public TcpConnectScreen( MameControllerClient app, TcpClient client )
	{
		super( app );
		this.client = client;
	}

	public void connect()
	{
		this.statusLabel.setText( "Connecting to: " + this.client + "..." );
		this.client.addConnectionListener( this );
		this.client.open();
		Gdx.app.log( "Network", "Returned from open()" );
	}

	public void onConnectionEvent( ConnectionEvent event )
	{
		switch( event.getType() )
		{
			case ConnectionEvent.TYPE_CONNECTED:
				Gdx.app.log( "Network", "Connected." );
				this.statusLabel.setText( "Connected!" );
				this.showController();
				break;

			case ConnectionEvent.TYPE_CONNECTION_FAILED:
				Gdx.app.log( "Network", "Connection failed." );
				this.statusLabel.setText( "Connection failed: " + event.getStatus() );
				break;
		}
	}

	public void showController()
	{
		Gdx.app.log( "Network", "Connected." );
		ControllerLayout defaultController = this.getDefaultController();
		if ( defaultController != null )
		{
			Gdx.app.log( "Controller", "Found default controller and showing it..." );
			this.app.setScreen( Controller.getInstance( this.app ).setLayout( defaultController ) );
		}
		else if ( ControllerLayout.findControllers().size() > 0 )
		{
			Gdx.app.log( "Controller", "Found multiple controllers, showing menu..." );
			this.app.setScreen( SelectControllerMenu.getInstance( this.app ) );
		}
	}


	protected ControllerLayout getDefaultController()
	{
		ControllerLayout layout = null;

		// Try to get a controller to open without the user having to do anything...
		Preferences preferences = Gdx.app.getPreferences( "controller" );
		String lastController = preferences.getString( "last-controller" );
		if ( lastController != null && lastController.length() > 0 && ControllerLayout.exists( lastController ) )
		{
			// Try to find the appropriate controller...
			layout = ControllerLayout.readController( lastController );
		}

		if ( layout == null )
		{
			// If there is only one controller type, then return that...
			ArrayList<FileHandle> availableControllers = ControllerLayout.findControllers();
			if ( availableControllers.size() == 0 )
			{
				Gdx.app.error( "Controller", "No controllers found." );
				return null;
			}
			else if ( availableControllers.size() == 1 )
			{
				return ControllerLayout.readController( availableControllers.get( 0 ).path() );
			}
		}

		return null;
	}


}
