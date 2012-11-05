package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.os.Bundle;

/**
 * Show the user a list of connections they have previously connected to or created themselves.
 * For each connection profile, we will attempt to ping that server and see if we get a response.
 * This will all be done in parallel, and we will show feedback to display whether they are successful or not.
 * Also allow them to create new settings manually.
 * If none exist, display prominently somewhere a button to create a new connection.
 */
public class ViewConnectionsActivity extends Activity
{

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );
	}

}