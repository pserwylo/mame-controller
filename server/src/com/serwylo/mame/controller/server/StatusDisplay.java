package com.serwylo.mame.controller.server;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatusDisplay extends JFrame
{

	private static ArrayList<StatusDisplay> displays = new ArrayList<StatusDisplay>();

	public StatusDisplay()
	{
		this.setUndecorated( true );
		this.setAlwaysOnTop( true );
	}

	/**
	 * Keeps track of status displays, so that we can position them appropriately.
	 */
	public static void add( StatusDisplay display )
	{
		displays.add( display );
		layoutDisplays();
	}

	/**
	 * Currently, they will be stacked in the bottom right corner. As they are removed, they will be repositioned.
	 */
	public static void layoutDisplays()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int startY = (int)screenSize.getHeight();
		for ( StatusDisplay display : displays )
		{
			display.setLocation( (int)( screenSize.getWidth() - display.getWidth() ), ( startY - display.getHeight() ) );
			display.setVisible( true );
			startY += display.getHeight();
		}
	}

}
