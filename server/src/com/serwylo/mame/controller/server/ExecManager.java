package com.serwylo.mame.controller.server;

import java.io.IOException;

/**
 * The executable manager deals with running and maintaining the executable once the clients are connected.
 * This means starting it, as well as gracefully shutting it down if need be.
 */
public class ExecManager
{

	private String executable;
	private Process process;

	protected ExecManager()
	{

	}

	public static ExecManager create( String executable )
	{
		ExecManager manager = new ExecManager();
		manager.executable = executable;
		return manager;
	}

	public boolean start()
	{
		boolean success = true;
		try
		{
			System.out.println( "Starting executable: '" + this.executable + "'..." );
			this.process = Runtime.getRuntime().exec( this.executable );
			System.out.print( "Done!" );
		}
		catch ( IOException e )
		{
			System.err.println( "Error starting executable: '" + this.executable + "'" );
			System.err.println( e.getMessage() );
			success = false;
		}
		return success;
	}

	public boolean stop()
	{
		System.out.print( "Stopping executable: '" + this.executable + "'..." );
		this.process.destroy();
		System.out.println( "Done!" );
		this.process = null;
		return true;
	}

	public boolean isRunning()
	{
		return this.process != null;
	}

}
