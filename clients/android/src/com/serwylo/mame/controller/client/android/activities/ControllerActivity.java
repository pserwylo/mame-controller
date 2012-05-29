package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.serwylo.mame.controller.client.android.controllers.ControllerDefinition;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.io.ControllerManager;

public class ControllerActivity extends Activity
{

	public static final String ACTION_LAUNCH_DEFAULT_CONTROLLER = "com.serwylo.mc.launchDefaultController";

	protected ControllerDefinition controller;

	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
	}

	public void onStart()
	{
		super.onStart();

		Intent intent = this.getIntent();


		if ( intent.getAction().equals( ACTION_LAUNCH_DEFAULT_CONTROLLER ) )
		{
			ControllerDefinition controller = ControllerManager.getDefaultController( this );
			if ( controller == null )
			{
				Intent viewControllersIntent = new Intent( this, ViewControllersActivity.class );
				viewControllersIntent.setAction( ViewControllersActivity.ACTION_VIEW_CONTROLLERS );
				this.startActivity( viewControllersIntent );
			}
			else
			{
				this.controller = controller;
				this.initView();
			}

		}

	}

	protected void initView()
	{
		for ( AbstractButton button : this.controller.getButtonList() )
		{

		}
	}

}