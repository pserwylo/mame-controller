package com.serwylo.mame.controller.server;

import com.serwylo.mame.controller.shared.InputEvent;

import java.awt.Robot;
import java.awt.AWTException;

public class MameRobot
{

	protected Robot robot;

	public MameRobot()
	{
		try
		{
			robot = new Robot();
		}
		catch ( AWTException e )
		{
			System.err.println( "Could not get access to a java.awt.Robot" );
			System.err.println( "**Controller will not work!!!**" );
			System.err.println( e.getMessage() );
		}
	}

	public void processEvent( InputEvent event )
	{
		if ( robot != null )
		{
			switch( event.getType() )
			{
				case InputEvent.TYPE_KEY_DOWN:
					robot.keyPress( event.getKeyCode() );
					break;

				case InputEvent.TYPE_KEY_UP:
					robot.keyRelease( event.getKeyCode() );
					break;
			}
		}
		else
		{
			System.err.println( "Cannot process event, unable to get access to a java.awt.Robot" );
		}
	}

	private static MameRobot mameRobot;

	public static MameRobot getInstance()
	{
		if ( mameRobot == null )
		{
			mameRobot = new MameRobot();
		}

		return mameRobot;
	}

}
