package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;

/**
 * I'm really interested in making things like a D-PAD a single button. The only way I can think for this to work is if
 * the ButtonView base class is actually a ViewGroup. Then, if we have a single on-screen button represented by a single
 * keyCode, then we just create a button and add it to the layout. If, however, something like a D-PAD or a joystick
 * is required, then we can create these complex, compound buttons and place their components in this layout.
 * @param <ButtonType>
 */
public abstract class ButtonView<ButtonType extends AbstractButton> extends RelativeLayout
{

	private ButtonType button;

	protected IButtonPressListener buttonPressListener;

	public ButtonView( Context context )
	{
		super( context );
	}

	public ButtonView( Context context, AttributeSet attrs )
	{
		super( context, attrs );
	}

	public ButtonView( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
	}

	public void setButtonPressListener( IButtonPressListener listener )
	{
		this.buttonPressListener = listener;
	}

	protected void buttonDown( int keyCode )
	{
		if ( this.buttonPressListener != null )
		{
			this.buttonPressListener.onButtonDown( keyCode );
		}
	}

	protected void buttonUp( int keyCode )
	{
		if ( this.buttonPressListener != null )
		{
			this.buttonPressListener.onButtonUp( keyCode );
		}
	}

	public void setButton( ButtonType button )
	{
		this.button = button;
	}

	public ButtonType getButton()
	{
		return this.button;
	}

}
