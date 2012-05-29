package com.serwylo.mame.controller.server.tcp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.serwylo.mame.controller.server.StatusDisplay;
import com.serwylo.mame.controller.server.tcp.TcpServer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Displays the status of the server:
 *  - A QR Code which can be used to access the server.
 *  - IP address and port of the server
 *  - Number of connected devices
 *  - Status (is it listening for clients?)
 */
public class TcpStatusDisplay extends StatusDisplay
{

	private BufferedImage qrConnectImage = null;

	private TcpServer server;

	private JLabel labelConnect;
	private JLabel labelAddress;
	private JLabel labelQrImage;

	public TcpStatusDisplay( TcpServer server)
	{
		this.server = server;
	}

	/**
	 * We will initialize the status display after we know the address and the port of the server.
	 */
	public void init()
	{
		this.getContentPane().removeAll();

		this.generateQr();

		JPanel panel = new JPanel();
		this.add( panel );
		panel.setLayout( new BorderLayout() );
		panel.setBackground( Color.WHITE );
		panel.setBorder( new LineBorder( Color.LIGHT_GRAY, 3 ) );

		Font headingFont = new Font( "Monospaced", Font.BOLD, 14 );
		this.labelConnect = new JLabel( "Connect", JLabel.CENTER );
		this.labelConnect.setFont( headingFont );
		panel.add( this.labelConnect, BorderLayout.NORTH );

		Font infoFont = new Font( "Monospaced", Font.BOLD, 10 );
		this.labelAddress = new JLabel( this.server.getIpAddress().getHostAddress() + ":" + this.server.getPort(), JLabel.CENTER );
		this.labelAddress.setFont( infoFont );
		panel.add( this.labelAddress, BorderLayout.SOUTH );

		int height = 30;
		if ( this.qrConnectImage != null )
		{
			this.labelQrImage = new JLabel( new ImageIcon( this.qrConnectImage ) );
			panel.add( this.labelQrImage, BorderLayout.CENTER );
			height = 150;
		}

		this.setSize( 140, height );
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

}
