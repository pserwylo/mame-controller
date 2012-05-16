package com.serwylo.mame.controller.client.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.backends.jogl.JoglApplicationConfiguration;
import com.serwylo.mame.controller.client.MameControllerClient;
import com.serwylo.mame.controller.client.net.NetworkManager;
import com.serwylo.mame.controller.client.net.tcp.TcpClient;
import org.apache.commons.cli.*;

public class MameControllerClientDesktop 
{

	private String connectionString = null;

	public MameControllerClientDesktop( String[] args )
	{
		MameControllerClient.bluetoothClient = new DesktopBluetoothClient();
		MameControllerClient.qrCodeReader = new DesktopQrCodeReader();

		this.processCliArgs( args );

		NetworkManager.getInstance().addNetworkClient( TcpClient.create( this.connectionString ) );

		JoglApplicationConfiguration config = new JoglApplicationConfiguration();
		config.fullscreen = false;
		config.width = 800;
		config.height = 480;
		config.title = "Mame Controller";
		new JoglApplication( new MameControllerClient(), config );
	}

	private void processCliArgs( String[] args )
	{
		Options options = new Options();
		options.addOption( "s", "connection-string", true, "Connection string" );

		CommandLine line;
		try
		{
			CommandLineParser parser = new PosixParser();
			line = parser.parse( options, args );
			this.connectionString = line.hasOption( 's' ) ? line.getOptionValue( 's' ) : null;
		}
		catch ( ParseException pe )
		{
			System.err.println( "Error while parsing CLI options:" );
			System.err.println( pe.getMessage() );
			System.exit( 1 );
		}
	}

	public static void main(String[] args)
	{
		new MameControllerClientDesktop( args );
	}

}
