package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import com.serwylo.mame.controller.client.android.controllers.ControllerDefinition;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.ButtonLayout;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.ButtonUiFactory;
import com.serwylo.mame.controller.client.android.controllers.io.ControllerManager;

public class ControllerActivity extends Activity
{

	public static final String ACTION_LAUNCH_DEFAULT_CONTROLLER = "com.serwylo.mc.launchDefaultController";

	public static void showDefaultController( Context context )
	{
		Intent intent = new Intent( context, ControllerActivity.class );
		intent.setAction( ACTION_LAUNCH_DEFAULT_CONTROLLER );
		context.startActivity( intent );
	}

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
			ControllerDefinition controller = ControllerManager.getDefaultController( this.getApplicationContext() );
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
		ButtonLayout layout = new ButtonLayout( this );
		for ( AbstractButton button : this.controller.getButtonList() )
		{
			View buttonView = ButtonUiFactory.createButtonUiElement( this, button );
			layout.addView( buttonView );
		}
		this.setContentView( layout );
	}

}