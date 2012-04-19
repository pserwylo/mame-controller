package com.serwylo.mame.controller.client.android;

import com.google.zxing.integration.android.IntentIntegrator;
import com.serwylo.mame.controller.client.platform.QrCodeReader;

public class AndroidQrCodeReader extends QrCodeReader
{

	private MameControllerActivity activity;

	public AndroidQrCodeReader( MameControllerActivity mameControllerActivity )
	{
		super();
		this.activity = mameControllerActivity;
	}

	@Override
	public void readBarcode()
	{
		IntentIntegrator integrator = new IntentIntegrator( this.activity );
		integrator.initiateScan();
	}

	public void receiveResult( String dataString )
	{
		this.notifyListeners( dataString );
	}
}
