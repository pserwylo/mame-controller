package com.serwylo.mame.controller.client.android.net;

import java.io.Serializable;
import java.util.Date;

public abstract class ConnectionProfile implements Serializable
{

	private Date lastConnectDate;

	private String label;

	/**
	 * More nitty gritty details than just a human readable name.
	 * e.g. "192.168.1.3:8246"
	 * @return
	 */
	public abstract String getDetails();

	/**
	 * From the settings stored in this profile, produce a network client which is populated with the appropriate
	 * properties so that we can just call it's {@link NetworkClient#open()} method.
	 * @return
	 */
	public abstract NetworkClient createClient();

	public final Date getLastConnectDate()
	{
		return this.lastConnectDate;
	}

	public final void setLastConnectDate( Date lastConnectDate )
	{
		this.lastConnectDate = lastConnectDate;
	}

	/**
	 * Human readable name, which the user is free to change.
	 * e.g. "Home" or "Office".
	 * @return
	 */
	public final String getLabel()
	{
		return this.label;
	}

	/**
	 * @see {@link ConnectionProfile#setLabel(String)}
	 */
	public final void setLabel( String label )
	{
		this.label = label;
	}
}
