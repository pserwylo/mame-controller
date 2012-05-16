package com.serwylo.mame.controller.client;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.serwylo.mame.controller.client.net.NetworkClient;
import com.serwylo.mame.controller.client.net.NetworkManager;
import com.serwylo.mame.controller.client.net.tcp.TcpClient;
import com.serwylo.mame.controller.client.platform.BluetoothClient;
import com.serwylo.mame.controller.client.platform.QrCodeReader;
import com.serwylo.mame.controller.client.screens.Controller;
import com.serwylo.mame.controller.client.screens.MainMenu;
import com.serwylo.mame.controller.client.screens.SelectControllerMenu;
import com.serwylo.mame.controller.client.screens.TcpConnectScreen;

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
			TcpConnectScreen connectScreen = new TcpConnectScreen( this, tcpClient );
			this.setScreen( connectScreen );
			connectScreen.connect();
		}
		else
		{
			Gdx.app.log( "Main", "No TcpClient found, just showing main menu..." );
			this.setScreen( MainMenu.getInstance( this ) );
		}
	}

}
