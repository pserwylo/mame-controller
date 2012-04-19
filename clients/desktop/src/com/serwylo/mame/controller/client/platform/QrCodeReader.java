package com.serwylo.mame.controller.client.platform;

import java.util.ArrayList;

public abstract class QrCodeReader
{

	/**
	 * Will read the barcode, and then return it by notifying any IQrCodeListeners which have been
	 * added.
	 */
	public abstract void readBarcode();

	private ArrayList<IQrCodeListener> listeners = new ArrayList<IQrCodeListener>();

	public void addQrCodeListener( IQrCodeListener listener )
	{
		if ( !this.listeners.contains( listener ) )
		{
			this.listeners.add( listener );
		}
	}

	public void removeQrCodeListener( IQrCodeListener listener )
	{
		if ( this.listeners.contains( listener ) )
		{
			this.listeners.remove( listener );
		}
	}

	protected final void notifyListeners( String qrCodeContents )
	{
		for ( IQrCodeListener listener : this.listeners )
		{
			listener.receiveQrCodeContents( qrCodeContents );
		}
	}
}
