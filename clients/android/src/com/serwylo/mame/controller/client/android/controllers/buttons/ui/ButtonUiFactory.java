package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;

public class ButtonUiFactory
{

	public static View createButton( Context context, AbstractButton button )
	{
		if ( button.getClass() == ArcadeButton.class )
		{
			return ButtonUiFactory.createArcadeButton( context, (ArcadeButton)button );
		}

		throw new IllegalArgumentException( "Button UI element does not exist for '" + button.getClass().getCanonicalName() + "'" );
	}

	public static ArcadeButtonView createArcadeButton( Context context, ArcadeButton button )
	{
		ArcadeButtonView btnView = new ArcadeButtonView( context );
		btnView.setButton( button );

		Drawable image = context.getResources().getDrawable( R.drawable.arcade_button ).mutate();
		image.setColorFilter( new LightingColorFilter( button.getColour(), 1 ) );
		btnView.setImageDrawable( image );
		btnView.setBackgroundColor( Color.TRANSPARENT );

		btnView.setLayoutParams( new AbsoluteLayout.LayoutParams( image.getMinimumWidth(), image.getMinimumHeight(), button.getX(), button.getY() ) );

		return btnView;
	}

}
