package com.serwylo.mame.controller.server.utils;

import java.util.Properties;

public class MameProperties
{

	private static MameProperties instance = null;

	public static MameProperties getInstance()
	{
		if ( instance == null )
		{
			throw new AssertionError( "Cannot access properties before it has been setup() by the main class." );
		}
		return instance;
	}

	public static void setup(
		boolean showGuiStatus,
		String statusOutputPath,
		String executable,
		int waitTime
	)
	{
		instance = new MameProperties();
		instance.showGuiStatus = showGuiStatus;
		instance.statusOutputPath = statusOutputPath;
		instance.executable = executable;
		instance.waitTime = waitTime;
	}

	private boolean showGuiStatus = true;
	private String statusOutputPath;
	private String executable;
	private int waitTime;

	/**
	 * True if we want to open a window in the bottom right corner (always on top) which shows the status.
	 * This will not work for fulscreen apps, because we are no longer under the control of the window manager.
	 * In these cases, you may want to investigate using statusOutputPath
	 * @return
	 */
	public boolean getShowGuiStatus()
	{
		return showGuiStatus;
	}

	/**
	 * A path to a png file where we can dump the status window to.
	 * This is useful, e.g. if we want to embed the status inside another app.
	 * @return
	 */
	public String getStatusOutputPath()
	{
		return statusOutputPath;
	}

	/**
	 * The executable (including args) to run when a client connects (after {@link MameProperties#getWaitTime()}
	 * @return
	 */
	public String getExecutable()
	{
		return executable;
	}

	/**
	 * The amount of time to wait after the first client is connected before running executable.
	 * We will also allow clients to override this behaviour by hitting a 'continue' button.
	 * @return
	 */
	public int getWaitTime()
	{
		return waitTime;
	}
}
