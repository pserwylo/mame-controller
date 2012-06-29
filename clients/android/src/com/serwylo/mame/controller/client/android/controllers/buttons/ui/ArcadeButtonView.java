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

/**
 * One colourful round button, ala the physical arcade machine buttons.
 */
public class ArcadeButtonView extends ButtonView<ArcadeButton>
{

	public ArcadeButtonView( Context context )
	{
		super( context );
	}

	public ArcadeButtonView( Context context, AttributeSet attrs )
	{
		super( context, attrs );
	}

	public ArcadeButtonView( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
	}

	/**
	 * Will do a check to see if the point is within the circle before returning the keyCode.
	 * @param x
	 * @param y
	 * @return
	 */
	public int getKeyCode( int x, int y )
	{
		int radius = this.getLayoutParams().width;

		if ( radius * radius > x * x + y * y )
		{
			return this.getButton().getKeyCode();
		}
		else
		{
			return NO_KEY_CODE;
		}
	}

	@Override
	public void setButton( ArcadeButton button )
	{
		super.setButton( button );

		ImageButton btnView = new ImageButton( this.getContext() );
		Drawable image = this.getResources().getDrawable( R.drawable.arcade_button ).mutate();
		image.setColorFilter( new LightingColorFilter( button.getColour(), 1 ) );
		btnView.setImageDrawable( image );
		btnView.setBackgroundColor( Color.TRANSPARENT );
		this.addView( btnView );

		this.setLayoutParams( new AbsoluteLayout.LayoutParams( image.getMinimumWidth(), image.getMinimumHeight(), button.getX(), button.getY() ) );
	}

}
