package com.serwylo.mame.controller.client.desktop;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.backends.jogl.JoglApplicationConfiguration;
import com.serwylo.mame.controller.client.MameControllerClient;

public class MameControllerClientDesktop 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		MameControllerClient.bluetoothClient = new DesktopBluetoothClient();
		MameControllerClient.qrCodeReader = new DesktopQrCodeReader();

		JoglApplicationConfiguration config = new JoglApplicationConfiguration();
		config.fullscreen = false;
		config.width = 800;
		config.height = 480;
		config.title = "Mame Controller";
		new JoglApplication( new MameControllerClient(), config );
	}

}
