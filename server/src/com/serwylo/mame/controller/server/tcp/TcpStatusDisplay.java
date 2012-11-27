package com.serwylo.mame.controller.server.tcp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.serwylo.mame.controller.server.StatusDisplay;
import com.serwylo.mame.controller.server.events.ClientEvent;
import com.serwylo.mame.controller.server.events.IClientEventListener;
import com.serwylo.mame.controller.server.events.IServerEventListener;
import com.serwylo.mame.controller.server.events.ServerEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Displays the status of the server:
 *  - A QR Code which can be used to access the server.
 *  - IP address and port of the server
 *  - Number of connected devices
 *  - Status (is it listening for clients?)
 */
public class TcpStatusDisplay extends StatusDisplay implements IServerEventListener, IClientEventListener
{

	private BufferedImage qrConnectImage = null;

	private TcpServer server;

	private JPanel qrPanel;
	private JLabel labelConnect;
	private JLabel labelAddress;
	private JLabel labelQrImage;

	private JPanel connectedPanel;
	private JLabel labelConnectedClients;

	public TcpStatusDisplay( TcpServer server)
	{
		this.server = server;
		this.server.addServerEventListener( this );
	}

	/**
	 * We will initialize the status display after we know the address and the port of the server.
	 */
	public void init()
	{
		this.getContentPane().removeAll();

		this.generateQr();

		this.setTitle( "MAME Controller Server");

		// The qrPanel displays information useful for connecting...
		this.qrPanel = new JPanel();
		this.qrPanel.setLayout(new BorderLayout());
		this.qrPanel.setBackground(Color.WHITE);
		this.qrPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 3));

		Font headingFont = new Font( "Monospaced", Font.BOLD, 14 );
		this.labelConnect = new JLabel( "Scan to Connect", JLabel.CENTER );
		this.labelConnect.setFont( headingFont );
		this.qrPanel.add(this.labelConnect, BorderLayout.NORTH);

		Font infoFont = new Font( "Monospaced", Font.BOLD, 10 );
		this.labelAddress = new JLabel( this.server.getIpAddress().getHostAddress() + ":" + this.server.getPort(), JLabel.CENTER );
		this.labelAddress.setFont( infoFont );
		this.qrPanel.add(this.labelAddress, BorderLayout.SOUTH);

		int height = 30;
		if ( this.qrConnectImage != null )
		{
			this.labelQrImage = new JLabel( new ImageIcon( this.qrConnectImage ) );
			this.qrPanel.add(this.labelQrImage, BorderLayout.CENTER);
			height = 150;
		}

		this.add( this.qrPanel );
		this.setSize( 140, height );

		// This panel shows how many clients are connected...
		this.connectedPanel = new JPanel();
		this.connectedPanel.setLayout( new FlowLayout() );

		this.labelConnectedClients = new JLabel();
		this.labelConnectedClients.setFont( headingFont );
		this.connectedPanel.add( this.labelConnectedClients );
	}

	private void generateQr()
	{
		try
		{
			System.out.println( this.server.getIpAddress() );
			System.out.println( this.server.getIpAddress().getHostAddress() );
			String connectLink = "http://mc.serwylo.com/wifi?host=" + this.server.getIpAddress().getHostAddress() + "&port=" + this.server.getPort();
			BitMatrix qrCode = new QRCodeWriter().encode( connectLink, BarcodeFormat.QR_CODE, 125, 125 );
			this.qrConnectImage = MatrixToImageWriter.toBufferedImage( qrCode );
		}
		catch ( WriterException we )
		{
			System.err.println( "Cannot generate QRCode:" );
			System.err.println( we.getMessage() );
		}
	}

	@Override
	public void onServerEvent( ServerEvent event )
	{
		if ( event.getType() == ServerEvent.TYPE_SERVER_CONNECTED )
		{
			System.out.println( "[TcpStatusDisplay] Server connected: " + event );
		}
		else if ( event.getType() == ServerEvent.TYPE_NEW_CLIENT )
		{
			System.out.println( "[TcpStatusDisplay] New client: " + event );
			event.getClient().addClientEventListener( this );
		}
	}

	@Override
	public void onClientEvent( ClientEvent event )
	{
		if ( event.getType() == ClientEvent.TYPE_CLIENT_CONNECTED )
		{
			System.out.println( "[TcpStatusDisplay] Client connected: " + event );
		}
		else if ( event.getType() == ClientEvent.TYPE_CLIENT_DISCONNECTED )
		{
			System.out.println( "[TcpStatusDisplay] Client disconnected: " + event );
		}

		// TODO: Based on settings, either remove completely, don't change anything, or show status when clients connected...
		boolean hasClients = event.getConnectedClients() > 0;
		if ( false /* remove completely */ )
		{
			this.setVisible( !hasClients );
		}
		else if ( false /* update status and still show connection info */ )
		{

		}
		else if ( true /* remove connection info, show connected clients */ )
		{
			if ( hasClients )
			{
				this.labelConnect.setText( event.getConnectedClients() + " Connected" );
			}
			else
			{
				this.labelConnect.setText( "Scan to Connect" );
			}
			this.dumpImage();
		}
	}
}
