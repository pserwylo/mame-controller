package com.serwylo.mame.controller.client.android.util;

import android.view.MotionEvent;
import android.view.View;
import com.serwylo.mame.controller.client.android.controllers.buttons.ui.ButtonView;

import java.util.ArrayList;

/**
 * Keeps track of which views are being touched by which pointer.
 * Without something like this, it is very messy to try and send keyup events when moving a finger from
 * one button to another, or moving a ("pressed") finger outside a button and then releasing.
 */
public class MultitouchTracker implements View.OnTouchListener
{

	/**
	 *
	 */
	private ArrayList<View> trackedViews = new ArrayList<View>( 10 );

    /**
     * Uses the index of the ArrayList to indicate which pointer/finger is pressing the View at that index.
     */
    private ArrayList<View> pressedViews = new ArrayList<View>( 10 );

	public MultitouchTracker()
	{
		for ( int i = 0; i < 10; i ++ )
		{
			this.pressedViews.set( i, null );
		}
	}

	public void trackView( View view )
	{
		view.setOnTouchListener( this );
		this.trackedViews.add( view );
	}

    protected void processTouchEvent( MotionEvent event )
    {
		View view = this.getViewUnder( event );
		int actionIndex = event.getActionIndex();

        if ( event.getAction() == MotionEvent.ACTION_DOWN )
        {
			this.touchDownView( event, view, actionIndex );
        }
		else if ( event.getAction() == MotionEvent.ACTION_UP )
		{
			this.touchUpView( event, view, actionIndex );
		}
        else if ( event.getAction() == MotionEvent.ACTION_MOVE )
        {
			this.processMoveEvent( event, view, actionIndex );
        }
    }

	private void processMoveEvent( MotionEvent event, View view, int actionIndex )
	{
		View oldView = this.pressedViews.get( actionIndex );

		// If we have left a button, then release it...
		if ( oldView != null )
		{
			// ...but only if there are no more remaining fingers pressing it...
			int numPresses = this.getNumberOfPressesForView( view );
			if ( numPresses == 1 )
			{
				oldView.dispatchTouchEvent( MotionEvent.obtain( 0, System.currentTimeMillis(), MotionEvent.ACTION_POINTER_UP, actionIndex, event.getX(), event.getY(), event.getPressure(), event.getSize(), event.getMetaState(), event.getXPrecision(), event.getYPrecision(), event.getDeviceId(), event.getEdgeFlags() ) );
			}
			this.pressedViews.set( actionIndex, null );
		}

		// If we have entered another view then we want to send a touch-down event to the view we just entered...
		if ( oldView == null && view != null || oldView != null && view != oldView )
		{
			this.pressedViews.set( actionIndex, view );
			int numPresses = this.getNumberOfPressesForView( view );
			// ...if it has not already been pressed by another finger...
			if ( numPresses == 1 )
			{
				view.dispatchTouchEvent(  MotionEvent.obtain( 0, System.currentTimeMillis(), MotionEvent.ACTION_POINTER_DOWN, actionIndex, event.getX(), event.getY(), event.getPressure(), event.getSize(), event.getMetaState(), event.getXPrecision(), event.getYPrecision(), event.getDeviceId(), event.getEdgeFlags() ) );
			}
		}
	}

	/**
	 * Iterates over {@link MultitouchTracker#pressedViews} and counts how many times {@param view} is present.
	 * This tells us how many fingers are currently pressing the button.
	 * @param view
	 * @return
	 */
	private int getNumberOfPressesForView( View view )
	{
		int count = 0;
		for ( View pressedView : this.pressedViews )
		{
			if ( pressedView == view )
			{
				count ++;
			}
		}
		return count;
	}

	/**
	 * Searches through {@link MultitouchTracker#trackedViews} for a view which is underneath the motion event.
	 * If no view is there, then it returns null.
	 * @param event
	 * @return
	 */
	private View getViewUnder( MotionEvent event )
	{
		int eventX = (int)event.getX( event.getActionIndex() );
		int eventY = (int)event.getY( event.getActionIndex() );

		for ( View view : this.trackedViews )
		{
			if ( view.getLeft() < eventX && view.getRight() > eventX &&
					view.getTop() < eventY && view.getBottom() > eventY )
			{
				return view;
			}
		}

		return null;
	}

	public void touchDownView( MotionEvent event, View view, int pointer )
    {
        if ( this.pressedViews.get( pointer ) != null )
        {
            // What to do now?
            // We have pressed two views with the same finger...
			// This seems a little problematic, unless they are overlapping. Should we allow for this?
		}

        this.pressedViews.add( pointer, view );
		if ( this.getNumberOfPressesForView(  view ) == 1 )
		{
			view.dispatchTouchEvent( MotionEvent.obtain( 0, System.currentTimeMillis(), MotionEvent.ACTION_POINTER_DOWN, pointer, event.getX(), event.getY(), event.getPressure(), event.getSize(), event.getMetaState(), event.getXPrecision(), event.getYPrecision(), event.getDeviceId(), event.getEdgeFlags() ) );
		}
    }

    public void touchUpView( MotionEvent event, View view, int pointer )
    {
        if ( this.pressedViews.get( pointer ) == null )
        {
            // Bugger, we shouldn't have gotten here...
        }
		else
		{
			this.pressedViews.set( pointer, null );
			if ( this.getNumberOfPressesForView( view ) == 0 )
			{
				view.dispatchTouchEvent( MotionEvent.obtain( 0, System.currentTimeMillis(), MotionEvent.ACTION_POINTER_UP, pointer, event.getX(), event.getY(), event.getPressure(), event.getSize(), event.getMetaState(), event.getXPrecision(), event.getYPrecision(), event.getDeviceId(), event.getEdgeFlags() ) );
			}
		}
    }

	@Override
	public boolean onTouch( View view, MotionEvent motionEvent )
	{
		this.processTouchEvent( motionEvent );
		return false;
	}
}
