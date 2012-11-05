package com.serwylo.mame.controller.client.android.views;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.activities.ControllerActivity;
import com.serwylo.mame.controller.client.android.controllers.ControllerDefinition;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.DPadButton;
import com.serwylo.mame.controller.client.android.views.buttons.*;

import java.util.ArrayList;

/**
 * Renders a ControllerDefinition.
 */
public class ControllerView extends View implements View.OnTouchListener
{

	/**
	 * Number of pixels to offset a button when rendering to show that it is pressed.
	 * This will be used to move the button down and right this much. it will also be used as a buffer zone when telling
	 * the view which parts to invalidate (when we press a button).
	 */
	public static final int PRESS_OFFSET = 4;

	private ControllerDefinition controller;

	private ArrayList<ButtonView> buttonDetails;

	private ArrayList<Point> touchPositions = new ArrayList<Point>( 10 );

	private ArrayList<ButtonView> touchedButtons = new ArrayList<ButtonView>( 10 );

	private IButtonPressListener buttonPressListener;

	public ControllerView( Context context, ControllerDefinition controller )
	{
		super( context );

		this.init( controller );
	}

	/**
	 * Create {@link ButtonView}s for each {@link AbstractButton} in 'controller'.
	 * @param controller
	 */
	private void init( ControllerDefinition controller )
	{
		this.controller = controller;
		this.buttonDetails = new ArrayList<ButtonView>( this.controller.getButtonList().size() );
		for ( AbstractButton button : this.controller.getButtonList() )
		{
			this.buttonDetails.add( ButtonViewFactory.createButton( this.getContext(), button ) );
		}

		this.setOnTouchListener( this );
	}

	/**
	 * Delegate the drawing to the {@link ButtonView}s for each button.
	 * @param canvas
	 */
	@Override
	public void onDraw( Canvas canvas )
	{
		super.onDraw( canvas );

		for ( ButtonView details : this.buttonDetails )
		{
			details.draw( canvas );
		}
	}

	/**
	 * Will remember the touch positions from last time, compare them to the touch positions this time, and decide
	 * if any buttons have been pressed or released. We don't really care about the specific event which just occurred,
	 * we will still just count up all positions and compare them. This is not very well optimised, but I've heard the
	 * SDK is a bit buggy when dealing with pointer indices, so I don't really want to rely on keeping track of pointers
	 * across events by their pointer indices or ids.
	 * @param view
	 * @param motionEvent
	 * @return
	 */
	@Override
	public boolean onTouch( View view, MotionEvent motionEvent )
	{
		boolean handled = false;

		int action = motionEvent.getActionMasked();
		int actionIndex = motionEvent.getActionIndex();

		// I'm fairly sure these are the only actions we care about...
		if ( action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_MOVE )
		{
			handled = true;

			// Remember these from last time (or an empty array if the first time)...
			ArrayList<ButtonView> preTouchedButtons = new ArrayList<ButtonView>( this.touchedButtons );

			this.touchPositions.clear();
			for ( int i = 0; i < motionEvent.getPointerCount(); i ++ )
			{
				if ( !( actionIndex == i && action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP ) )
				{
					this.touchPositions.add( new Point( (int)motionEvent.getX( i ), (int)motionEvent.getY( i ) ) );
				}
			}

			this.touchedButtons = this.getTouchedButtons();

			this.checkPressed( preTouchedButtons, this.touchedButtons );
			this.checkReleased( preTouchedButtons, this.touchedButtons );

		}

		return handled;
	}

	/**
	 * Iterate over both arrays, and check if there are any in pre which are NOT in post.
	 * If so, tell the button it was released.
	 *
	 * @param pre
	 * @param post
	 */
	private void checkReleased( ArrayList<ButtonView> pre, ArrayList<ButtonView> post )
	{
		for ( ButtonView before : pre )
		{
			boolean stillPressed = false;
			for ( ButtonView after : post )
			{
				if ( before == after )
				{
					stillPressed = true;
					break;
				}
			}

			if ( !stillPressed )
			{
				before.setPressed( false );

				if ( this.buttonPressListener != null )
				{
					this.buttonPressListener.onButtonUp( before.getButton().getKeyCode() );
				}

				this.invalidate( before.getBounds( PRESS_OFFSET ) );
			}
		}
	}

	/**
	 * Iterate over both arrays, and check if there are any in post which are NOT in pre.
	 * If so, tell the button it was pressed.
	 * @param pre
	 * @param post
	 */
	private void checkPressed( ArrayList<ButtonView> pre, ArrayList<ButtonView> post )
	{
		for ( ButtonView after : post )
		{
			boolean wasPressed = false;
			for ( ButtonView before : pre )
			{
				if ( before == after )
				{
					wasPressed = true;
					break;
				}
			}

			if ( !wasPressed )
			{
				after.setPressed( true );

				if ( this.buttonPressListener != null )
				{
					this.buttonPressListener.onButtonDown( after.getButton().getKeyCode() );
				}

				this.invalidate( after.getBounds( PRESS_OFFSET ) );
			}
		}
	}

	private ArrayList<ButtonView> getTouchedButtons()
	{
		ArrayList<ButtonView> touchedButtons = new ArrayList<ButtonView>();

		for ( Point point : this.touchPositions )
		{
			if ( point != null )
			{
				for ( ButtonView details : this.buttonDetails )
				{
					if ( details.contains( point ) && !touchedButtons.contains( details ) )
					{
						touchedButtons.add( details );
					}
				}
			}
		}

		return touchedButtons;
	}

	public void setButtonPressListener( IButtonPressListener buttonPressListener )
	{
		this.buttonPressListener = buttonPressListener;
	}

}
