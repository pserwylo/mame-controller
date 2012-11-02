package com.serwylo.mame.controller.client.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.serwylo.mame.controller.client.android.controllers.ControllerDefinition;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.ButtonUiFactory;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.ButtonView;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.IButtonPressListener;
import com.serwylo.mame.controller.client.android.controllers.io.ControllerManager;
import com.serwylo.mame.controller.client.android.net.NetworkClient;
import com.serwylo.mame.controller.shared.InputEvent;
import de.passy.multitouch.MultiTouchActivity;

import java.util.ArrayList;

/**
 * Shows a controller with buttons on the screen, and when the buttons are pressed, will send events to the server...
 *
 * Unfortunately there is no support for multitouch in the Android {@link View} stuff, so we will instead just plonk
 * each button on the screen, listen for touch events only the content view, and simply bruteforce a search for which
 * button was pressed. I figure there should be no reason to have thousands of buttons on screen (famous last words).
 */
public class ControllerActivity extends Activity implements View.OnTouchListener
{

	public static final String ACTION_LAUNCH_DEFAULT_CONTROLLER = "com.serwylo.mc.launchDefaultController";

	private ArrayList<ButtonView> buttonViews = new ArrayList<ButtonView>();

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
		this.buttonViews.clear();
		for ( AbstractButton button : this.controller.getButtonList() )
		{
			ButtonView buttonView = ButtonUiFactory.createButton( this, button );
			this.buttonViews.add( buttonView );
			layout.addView( buttonView );
		}

		if ( this.controller.getOrientation() == null || this.controller.getOrientation().equals( ControllerDefinition.ORIENTATION_PORTRAIT ) )
		{
			this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
		}
		else
		{
			this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
		}

		ViewGroup touchCatcher = new FrameLayout( this );
		touchCatcher.setLayoutParams( new AbsoluteLayout.LayoutParams( 10000, 10000, 0, 0 ) );
		touchCatcher.setOnTouchListener( this );
		layout.addView( touchCatcher );

		this.setContentView( layout );
	}

	private void onButtonDown( int keyCode )
	{
		this.vibrate();
		NetworkClient.getCurrent().sendEvent( InputEvent.createKeyDown( keyCode ) );
	}

	private void onButtonUp( int keyCode )
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

	@Override
	public boolean onTouch( View v, MotionEvent event )
	{
		if ( event.getAction() != MotionEvent.ACTION_MOVE )
		{
			Log.d( "MAME", "Event: " + event );
		}
		else
		{
			return true;
		}

		int eventX = (int)event.getX( event.getActionIndex() );
		int eventY = (int)event.getY( event.getActionIndex() );

		for ( ButtonView button : this.buttonViews )
		{
			if ( button.getLeft() < eventX && button.getRight() > eventX &&
				button.getTop() < eventY && button.getBottom() > eventY )
			{
				int x = (int)( eventX - button.getLeft() );
				int y = (int)( eventY - button.getTop() );
				int keyCode = button.getKeyCode( x, y );
				Log.d( "MAME", "At: [" + x + ", " + y + "] KeyCode: " + keyCode + ", Button: " + button.getClass().getSimpleName() );

				if ( keyCode != ButtonView.NO_KEY_CODE )
				{
					switch ( event.getAction() & MotionEvent.ACTION_MASK )
					{
						case MotionEvent.ACTION_DOWN:
						case MotionEvent.ACTION_POINTER_DOWN:
							Log.d( "MAME", "Down" );
							this.onButtonDown( keyCode );
							break;

						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_POINTER_UP:
							Log.d( "MAME", "Up (" + x + ", " + y + ")" );
							this.onButtonUp( keyCode );
							break;
					}
				}
			}

		}
		return true;
	}
}