package com.serwylo.mame.controller.server;

import com.serwylo.mame.controller.server.utils.PropertiesParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.util.ArrayList;

public abstract class ServerAppBridge
{

	/**
	 * This can be turned on with a specific command line switch, specified by the sub-class.
	 */
	protected boolean isActive = false;

	public final boolean isActive()
	{
		return this.isActive;
	}

	public abstract Server getServer();

	public abstract String getName();

	public abstract StatusDisplay getStatusDisplay();

	public abstract ArrayList<Option> getCliOptions();

	public abstract void parseConfig( PropertiesParser parser );

}
