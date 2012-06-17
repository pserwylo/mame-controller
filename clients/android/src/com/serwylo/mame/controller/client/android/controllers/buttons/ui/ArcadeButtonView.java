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

	@Override
	public void setButton( ArcadeButton button )
	{
		super.setButton( button );

		ImageButton btnView = new ImageButton( this.getContext() );
		Drawable image = this.getResources().getDrawable( R.drawable.arcade_button ).mutate();
		image.setColorFilter( new LightingColorFilter( button.getColour(), 1 ) );
		btnView.setImageDrawable( image );
		btnView.setBackgroundColor( Color.TRANSPARENT );
		btnView.setOnTouchListener( new OnTouchListener()
		{
			@Override
			public boolean onTouch( View v, MotionEvent event )
			{
				if ( event.getAction() == MotionEvent.ACTION_DOWN )
				{
					buttonDown( ArcadeButtonView.this.getButton().getKeyCode() );
				}
				else if ( event.getAction() == MotionEvent.ACTION_UP )
				{
					buttonUp( ArcadeButtonView.this.getButton().getKeyCode() );
				}
				return false;
			}
		});

		this.addView( btnView );

		this.setLayoutParams( new AbsoluteLayout.LayoutParams( image.getMinimumWidth(), image.getMinimumHeight(), button.getX(), button.getY() ) );
	}

}
