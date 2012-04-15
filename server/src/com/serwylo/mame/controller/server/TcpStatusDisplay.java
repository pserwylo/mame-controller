package com.serwylo.mame.controller.server;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;

/**
 * Displays the status of the server:
 *  - A QR Code which can be used to access the server.
 *  - IP address and port of the server
 *  - Number of connected devices
 *  - Status (is it listening for clients?)
 */
public class TcpStatusDisplay extends JFrame
{

	private BufferedImage qrConnectImage = null;

	private MameControllerServer server;

	private JLabel labelConnect;
	private JLabel labelAddress;
	private JLabel labelQrImage;

	public TcpStatusDisplay(MameControllerServer server)
	{
		this.server = server;

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
			height = 100;
		}

		this.setSize( 140, height );
	}

	private void generateQr()
	{
		try
		{
			String connectLink = "http://mc.serwylo.com/?s=" + this.server.getIpAddress().getHostAddress() + ":" + this.server.getPort();
			BitMatrix qrCode = new QRCodeWriter().encode( connectLink, BarcodeFormat.QR_CODE, 100, 100 );
			this.qrConnectImage = MatrixToImageWriter.toBufferedImage( qrCode );
		}
		catch ( WriterException we )
		{
			System.err.println( "Cannot generate QRCode:" );
			System.err.println( we.getMessage() );
		}
	}

	@Override
	public void paint( Graphics g )
	{
		super.paint( g );
		/*
		Font headingFont = new Font( "Monospaced", Font.BOLD, 16 );
		g.setFont( headingFont );
		g.setColor( Color.BLACK );
		g.drawString( "Connect", 22, 12 );

		Font infoFont = new Font( "Monospaced", Font.PLAIN, 12 );
		g.setFont( infoFont );
		// this.server.getIpAddress().getHostAddress() + this.server.getPort();
		g.drawString( "255.255.255.255", 3, this.getHeight() - 20 );
		g.drawString( "65000", 12, this.getHeight() - 5 );
		*/
	}

}
