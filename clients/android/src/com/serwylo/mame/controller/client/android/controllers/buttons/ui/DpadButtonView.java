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

	@Override
	public boolean onTouchEvent( MotionEvent event )
	{
		if ( event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP )
		{
			int x = (int)( event.getX() * 3 / this.getLayoutParams().width );
			int y = (int)( event.getY() * 3 / this.getLayoutParams().height );

			int keyCode = -1;
			if ( y == 0 )
			{
				if ( x == 0 )
				{
					keyCode = this.getButton().getKeyCodeUpLeft();
				}
				else if ( x == 1 )
				{
					keyCode = this.getButton().getKeyCodeUp();
				}
				else if ( x == 2 )
				{
					keyCode = this.getButton().getKeyCodeUpRight();
				}
			}
			else if ( y == 1 )
			{
				if ( x == 0 )
				{
					keyCode = this.getButton().getKeyCodeLeft();
				}
				else if ( x == 1 )
				{
					// do nothing...
				}
				else if ( x == 2 )
				{
					keyCode = this.getButton().getKeyCodeRight();
				}
			}
			else if ( y == 2 )
			{
				if ( x == 0 )
				{
					keyCode = this.getButton().getKeyCodeDownLeft();
				}
				else if ( x == 1 )
				{
					keyCode = this.getButton().getKeyCodeDown();
				}
				else if ( x == 2 )
				{
					keyCode = this.getButton().getKeyCodeDownRight();
				}
			}

			if ( keyCode >= 0 )
			{
				if ( event.getAction() == MotionEvent.ACTION_DOWN )
				{
					this.buttonDown( keyCode );
				}
				else if ( event.getAction() == MotionEvent.ACTION_UP )
				{
					this.buttonUp( keyCode );
				}
			}
		}

		return super.onTouchEvent( event );
	}

}
