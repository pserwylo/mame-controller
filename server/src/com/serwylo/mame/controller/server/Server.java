package com.serwylo.mame.controller.server;

import java.awt.*;
import java.io.IOException;

public abstract class Server implements Runnable
{

	protected static Robot robot;

	public static Robot getRobot()
	{
		if ( robot == null )
		{
			try
			{
				robot = new Robot();
			}
			catch ( AWTException e )
			{
				System.err.println( "Could not get access to a java.awt.Robot:" );
				System.err.println( e.getMessage() );
				System.exit( 1 ); // No point in using this example
			}
		}

		return robot;
	}

	public abstract StatusDisplay createStatusDisplay();

	protected void processEvent( Event event )
	{

	}

}
