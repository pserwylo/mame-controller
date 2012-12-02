package com.serwylo.mame.controller.client.android.views.buttons;


import android.content.Context;
import android.graphics.*;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.controllers.buttons.DPadButton;
import com.serwylo.mame.controller.client.android.views.ControllerView;

public class DPadButtonView extends ButtonView<DPadButton.Directional>
{

	/**
	 * Keep only one copy of the original bitmap, and each different directional component (e.g. up/down/etc) will draw
	 * the appropriate portion of this bitmap to the screen.
	 */
	protected static Bitmap dpadBitmap = null;

	/**
	 * The part of {@link DPadButtonView#dpadBitmap} which will be drawn to {@link DPadButtonView#screenDestRect}
	 */
	protected Rect bitmapSrcRect;

	/**
	 * The location on the screen to draw {@link DPadButtonView#bitmapSrcRect}
	 */
	protected Rect screenDestRect;

	public DPadButtonView( Context context, DPadButton.Directional button )
	{
		this.button = button;
		loadBitmap( context );
		this.calcBounds();
	}

	@Override
	public boolean contains( Point point )
	{
		boolean in;
		if ( this.button.getDirection() == DPadButton.Directional.DIR_MIDDLE )
		{
			in = false;
		}
		else
		{
			in = super.contains( point );
		}
		return in;
	}

	@Override
	protected void calcBounds()
	{
		this.bitmapSrcRect = new Rect();

		int dpadTop = this.getButton().getY() - dpadBitmap.getHeight() / 2;
		int dpadLeft = this.getButton().getX() - dpadBitmap.getWidth() / 2;

		float top = 0;
		float left = 0;
		float width = dpadBitmap.getWidth() / 3;
		float height = dpadBitmap.getHeight() / 3;

		if ( this.button.includesDirection( DPadButton.Directional.DIR_UP ) )
		{
			top = 0;
		}
		else if ( this.button.includesDirection(  DPadButton.Directional.DIR_DOWN ) )
		{
			top = height * 2;
		}
		else
		{
			top = height;
		}

		if ( this.button.includesDirection( DPadButton.Directional.DIR_LEFT ) )
		{
			left = 0;
		}
		else if ( this.button.includesDirection(  DPadButton.Directional.DIR_RIGHT ) )
		{
			left = width * 2;
		}
		else
		{
			left = width;
		}

		this.bitmapSrcRect.top = (int)top;
		this.bitmapSrcRect.left = (int)left;
		this.bitmapSrcRect.right = (int)( left + width );
		this.bitmapSrcRect.bottom = (int)( top + height );

		this.screenDestRect = new Rect( this.bitmapSrcRect );
		this.screenDestRect.offsetTo( (int)( dpadLeft + left ), (int)( dpadTop + top ) );

		this.bounds = this.screenDestRect;
	}

	@Override
	public void draw( Canvas canvas )
	{

		Rect screenLocation = new Rect( this.screenDestRect );

		if ( isPressed() )
		{
			screenLocation.offset( ControllerView.PRESS_OFFSET, ControllerView.PRESS_OFFSET );
		}

		canvas.drawBitmap( dpadBitmap, this.bitmapSrcRect, screenLocation, new Paint() );

	}

	private static void loadBitmap( Context context )
	{
		if ( dpadBitmap == null )
		{
			dpadBitmap = BitmapFactory.decodeResource( context.getResources(), R.drawable.dpad_button );
		}
	}

}
