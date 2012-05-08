package com.serwylo.mame.controller.client.screens;

import com.serwylo.mame.controller.client.MameControllerClient;
import com.serwylo.mame.controller.client.net.tcp.TcpClient;

public class TcpConnectScreen extends StatusScreen
{

	private TcpClient client;

	public TcpConnectScreen( MameControllerClient app, TcpClient client )
	{
		super( app );
		this.client = client;
	}

	public boolean connect()
	{
		this.statusLabel.setText( "Connecting to: " + this.client + "..." );
		return this.client.open();
	}



}
