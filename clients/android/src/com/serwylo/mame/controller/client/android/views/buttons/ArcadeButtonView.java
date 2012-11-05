package com.serwylo.mame.controller.client.android.views.buttons;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;

public class ArcadeButtonView extends ButtonView<ArcadeButton>
{

	public ArcadeButtonView( Context context, ArcadeButton button )
	{
		this.button = button;

		Bitmap bitmap = BitmapFactory.decodeResource( context.getResources(), R.drawable.arcade_button );
		BitmapDrawable bitmapDrawable = new BitmapDrawable( context.getResources(), bitmap );
		bitmapDrawable.setFilterBitmap( true );
		bitmapDrawable.setColorFilter( new LightingColorFilter( this.button.getColour(), 1 ) );

		this.bitmap = bitmapDrawable.getBitmap();
		this.calcBounds();
	}

}
