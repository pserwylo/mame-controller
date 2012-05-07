package com.serwylo.mame.controller.client;

import com.badlogic.gdx.*;
import com.serwylo.mame.controller.client.net.NetworkClient;
import com.serwylo.mame.controller.client.net.NetworkManager;
import com.serwylo.mame.controller.client.net.tcp.TcpClient;
import com.serwylo.mame.controller.client.platform.BluetoothClient;
import com.serwylo.mame.controller.client.platform.QrCodeReader;
import com.serwylo.mame.controller.client.screens.Controller;
import com.serwylo.mame.controller.client.screens.MainMenu;

import java.util.ArrayList;

/**
 * If there is a network client ready to go, then we will skip the main menu and go straight to a controller.
 * When doing this, if there was a previously opened controller, then just reopen that.
 * Otherwise, bring up a menu which lets the user choose which controller to select.
 * TODO: Let the user specify in the options menu if they want to remember the last used controller.
 */
public class MameControllerClient extends Game implements ApplicationListener 
{
	
	public static BluetoothClient bluetoothClient;
	public static QrCodeReader qrCodeReader;

	@Override
	public void create() 
	{
		Screen screen = null;

		TcpClient tcpClient = NetworkManager.getInstance().getNetworkClient( TcpClient.class );
		if ( tcpClient != null )
		{
			Gdx.app.log( "Network", "Connecting to " + tcpClient + "..." );
			boolean result = tcpClient.open();
			if ( result )
			{
				Gdx.app.log( "Network", "Connected." );
				ControllerLayout defaultController = this.getDefaultController();
				if ( defaultController != null )
				{
					Gdx.app.log( "Controller", "Found default controller and showing it..." );
					screen = Controller.getInstance( this ).setLayout( defaultController );
				}
			}
		}

		if ( screen != null )
		{
			this.setScreen( screen );
		}
		else
		{
			this.setScreen( MainMenu.getInstance( this ) );
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
			ArrayList<String> availableControllers = ControllerLayout.findControllers();
			if ( availableControllers.size() == 0 )
			{
				Gdx.app.error( "Controller", "No controllers found." );
				return null;
			}
			else if ( availableControllers.size() == 1 )
			{
				return ControllerLayout.readController( availableControllers.get( 0 ) );
			}
		}

		return null;
	}

}
