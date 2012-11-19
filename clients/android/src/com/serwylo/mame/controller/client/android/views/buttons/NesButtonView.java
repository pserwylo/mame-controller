package com.serwylo.mame.controller.client.android.views.buttons;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.NesButton;

public class NesButtonView extends ButtonView<NesButton>
{

	private static Bitmap nesBitmap;

	public NesButtonView( Context context, NesButton button )
	{
		this.button = button;
		loadBitmap( context );
		this.bitmap = nesBitmap;
		this.calcBounds();
	}

	private static void loadBitmap( Context context )
	{
		if ( nesBitmap == null )
		{
			nesBitmap = BitmapFactory.decodeResource( context.getResources(), R.drawable.nes_button );
		}
	}
}
