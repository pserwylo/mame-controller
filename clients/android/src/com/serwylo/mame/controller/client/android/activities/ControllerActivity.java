package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.serwylo.mame.controller.client.android.controllers.ControllerDefinition;
import com.serwylo.mame.controller.client.android.controllers.io.ControllerManager;
import com.serwylo.mame.controller.client.android.net.NetworkClient;
import com.serwylo.mame.controller.client.android.views.ControllerView;
import com.serwylo.mame.controller.client.android.views.buttons.IButtonPressListener;
import com.serwylo.mame.controller.shared.InputEvent;

import java.util.ArrayList;

/**
 * Shows a controller with buttons on the screen, and when the buttons are pressed, will send events to the server...
 *
 * Unfortunately there is no support for multitouch in the Android {@link View} stuff, so we will instead just plonk
 * each button on the screen, listen for touch events only the content view, and simply bruteforce a search for which
 * button was pressed. I figure there should be no reason to have thousands of buttons on screen (famous last words).
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

	/**
	 * Constructs a {@link ControllerView} as the content view for this activity.
	 */
	protected void initView()
	{
		ControllerView view = new ControllerView( this, this.controller );
		view.setButtonPressListener( this );
		this.setContentView( view );
	}

	public void onButtonDown( int keyCode )
	{
		this.vibrate();
		NetworkClient.getCurrent().sendEvent( InputEvent.createKeyDown( keyCode ) );
	}

	public void onButtonUp( int keyCode )
	{
		this.vibrate();
		NetworkClient.getCurrent().sendEvent( InputEvent.createKeyUp( keyCode ) );
	}

	private void vibrate()
	{
		Vibrator vibrator = (Vibrator)this.getSystemService( VIBRATOR_SERVICE );
		if ( vibrator != null /* TODO: && preferences.getBoolean( "VIBRATE_ON_PRESS" ) */ )
		{
			vibrator.vibrate( 50 );
		}
	}

}