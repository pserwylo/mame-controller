package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.DPadButton;

/**
 * One colourful round button, ala the physical arcade machine buttons.
 */
public class DpadButtonView extends ButtonView<DPadButton>
{

	public DpadButtonView(Context context)
	{
		super( context );
	}

	public DpadButtonView(Context context, AttributeSet attrs)
	{
		super( context, attrs );
	}

	public DpadButtonView(Context context, AttributeSet attrs, int defStyle)
	{
		super( context, attrs, defStyle );
	}

	@Override
	public void setButton( final DPadButton button )
	{
		super.setButton( button );

		ImageButton btnView = new ImageButton( this.getContext() );
		final Drawable image = this.getResources().getDrawable( R.drawable.dpad_button ).mutate();
		btnView.setImageDrawable( image );
		btnView.setBackgroundColor( Color.TRANSPARENT );;

		this.addView( btnView );
		this.setLayoutParams( new AbsoluteLayout.LayoutParams( image.getMinimumWidth(), image.getMinimumHeight(), button.getX(), button.getY() ) );
	}

	public int getKeyCode( int x, int y )
	{
		int xIndex = x * 3 / this.getLayoutParams().width;
		int yIndex = y * 3 / this.getLayoutParams().height;

		int keyCode = NO_KEY_CODE;
		if ( yIndex == 0 )
		{
			if ( xIndex == 0 )
			{
				keyCode = this.getButton().getKeyCodeUpLeft();
			}
			else if ( xIndex == 1 )
			{
				keyCode = this.getButton().getKeyCodeUp();
			}
			else if ( xIndex == 2 )
			{
				keyCode = this.getButton().getKeyCodeUpRight();
			}
		}
		else if ( yIndex == 1 )
		{
			if ( xIndex == 0 )
			{
				keyCode = this.getButton().getKeyCodeLeft();
			}
			else if ( xIndex == 1 )
			{
				// do nothing...
			}
			else if ( xIndex == 2 )
			{
				keyCode = this.getButton().getKeyCodeRight();
			}
		}
		else if ( yIndex == 2 )
		{
			if ( xIndex == 0 )
			{
				keyCode = this.getButton().getKeyCodeDownLeft();
			}
			else if ( xIndex == 1 )
			{
				keyCode = this.getButton().getKeyCodeDown();
			}
			else if ( xIndex == 2 )
			{
				keyCode = this.getButton().getKeyCodeDownRight();
			}
		}

		return keyCode;
	}

}
