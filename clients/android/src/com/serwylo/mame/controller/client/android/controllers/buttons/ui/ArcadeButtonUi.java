package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.graphics.PorterDuff;
import android.view.ViewGroup;
import android.widget.Button;
import com.serwylo.mame.controller.client.android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;

public class ArcadeButtonUi extends AbstractImageButtonUi<ArcadeButton>
{

	protected ArcadeButton button;

	public ArcadeButtonUi( Context context )
	{
		super( context );
	}

	public void setButton( ArcadeButton button )
	{
		this.button = button;
		this.invalidate();
	}

	@Override
	public ArcadeButton getButton()
	{
		return this.button;
	}

	/**
	 * @see android.view.View#measure(int, int)
	 */
	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
	{
		this.setMeasuredDimension( 100, 100 );
	}

	@Override
	public Drawable getDrawable()
	{
		Drawable image = this.getResources().getDrawable( R.drawable.arcade_button );
		image.setColorFilter( this.button.getColour(), PorterDuff.Mode.MULTIPLY );
		return image;
	}

}
