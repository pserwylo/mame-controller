package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;

public class ButtonLayout extends ViewGroup
{

	public ButtonLayout( Context context )
	{
		super( context );
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
	{
		this.setMeasuredDimension( MeasureSpec.getSize( widthMeasureSpec ), MeasureSpec.getSize( heightMeasureSpec ) );
		for ( int i = 0; i < this.getChildCount(); i ++ )
		{
			this.getChildAt( i ).measure( widthMeasureSpec, heightMeasureSpec );
		}
	}

	@Override
	protected void onLayout( boolean changed, int left, int top, int right, int bottom )
	{
		for ( int i = 0; i < this.getChildCount(); i ++ )
		{
			View child = this.getChildAt( i );
			if ( child instanceof IButtonUi )
			{
				IButtonUi button = (IButtonUi)child;
				child.layout( button.getButton().getX(), button.getButton().getY(), child.getMeasuredWidth(), child.getMeasuredHeight() );
			}
		}
	}
}
