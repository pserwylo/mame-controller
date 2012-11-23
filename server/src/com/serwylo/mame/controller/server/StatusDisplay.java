package com.serwylo.mame.controller.server;

import com.serwylo.mame.controller.server.utils.MameProperties;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StatusDisplay extends JFrame
{

	private static ArrayList<StatusDisplay> displays = new ArrayList<StatusDisplay>();

	public StatusDisplay()
	{
		this.setUndecorated( true );
		this.setAlwaysOnTop( true );
	}

	protected void dumpImage()
	{
		if ( MameProperties.getInstance().getStatusOutputPath() != null )
		{
			String tempDir = System.getProperty( "java.io.tmpdir" );
			BufferedImage bi = new BufferedImage( this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB );
			Graphics g = bi.getGraphics();

			boolean v = this.isVisible();
			this.setVisible( true );
			this.getContentPane().paint( g );
			this.setVisible( v );

			try
			{
				System.out.println( "Dumping image to '" + MameProperties.getInstance().getStatusOutputPath() );
				ImageIO.write( bi, "png", new File( MameProperties.getInstance().getStatusOutputPath() ) );
			}
			catch ( IOException ioe )
			{
				System.err.println( ioe.getMessage() );
			}
		}
	}

	/**
	 * Keeps track of status displays, so that we can position them appropriately.
	 */
	public static void add( StatusDisplay display )
	{
		displays.add( display );
		layoutDisplays();
		display.dumpImage();
	}

	/**
	 * Currently, they will be stacked in the bottom right corner. As they are removed, they will be repositioned.
	 */
	public static void layoutDisplays()
	{
		if ( MameProperties.getInstance().getShowGuiStatus() )
		{
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int startY = (int)screenSize.getHeight();
			for ( StatusDisplay display : displays )
			{
				display.setVisible( true );
				display.setLocation( (int)( screenSize.getWidth() - display.getWidth() ), ( startY - display.getHeight() ) );
				startY += display.getHeight();
			}
		}
	}

}
