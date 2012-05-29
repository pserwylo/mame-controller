package com.serwylo.mame.controller.client.android.net;

/**
 * Opening (and perhaps closing) connections usually will need to be done asynchronosly so that we can
 * provide feedback to the user as it happens. This facilitates it by sending out {@link ConnectionEvent}'s
 * when we connect.
 */
public interface IConnectionListener
{

	/**
	 *
	 * @param event
	 */
	public void onConnectionEvent(ConnectionEvent event);

}
