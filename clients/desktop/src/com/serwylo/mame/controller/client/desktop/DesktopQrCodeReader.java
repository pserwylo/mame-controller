package com.serwylo.mame.controller.client.desktop;

import com.serwylo.mame.controller.client.platform.QrCodeReader;

public class DesktopQrCodeReader extends QrCodeReader
{

	@Override
	public void readBarcode()
	{
		this.notifyListeners( "192.168.1.7:57368" );
	}
}
