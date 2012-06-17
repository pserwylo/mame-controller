package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;
import com.serwylo.mame.controller.client.android.controllers.ControllerDefinition;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.ButtonUiFactory;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.ButtonView;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.IButtonPressListener;
import com.serwylo.mame.controller.client.android.controllers.io.ControllerManager;
import com.serwylo.mame.controller.client.android.net.NetworkClient;
import com.serwylo.mame.controller.shared.InputEvent;

/**
 * Shows a controller with buttons on the screen, and when the buttons are pressed, will send events to the server...
 */
public class ControllerActivity extends Activity implements IButtonPressListener
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

		if ( true || intent.getAction().equals( ACTION_LAUNCH_DEFAULT_CONTROLLER ) )
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

	/**
	 * Creates an absolute layout, iterates over the buttons which belong to the {@link ControllerDefinition} we are
	 * using, and places them on the screen. Uses {@link ButtonUiFactory#createButton(android.content.Context, com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton)}
	 * to create the buttons.
	 */
	protected void initView()
	{
		ViewGroup layout = new AbsoluteLayout( this );
		for ( AbstractButton button : this.controller.getButtonList() )
		{
			ButtonView buttonView = ButtonUiFactory.createButton( this, button );
			buttonView.setButtonPressListener( this );
			layout.addView( buttonView );
		}
		this.setContentView( layout );
	}

	@Override
	public void onButtonDown( int keyCode )
	{
		NetworkClient.getCurrent().sendEvent( InputEvent.createKeyDown( keyCode ) );
	}

	@Override
	public void onButtonUp( int keyCode )
	{
		NetworkClient.getCurrent().sendEvent( InputEvent.createKeyUp( keyCode ) );
	}

}