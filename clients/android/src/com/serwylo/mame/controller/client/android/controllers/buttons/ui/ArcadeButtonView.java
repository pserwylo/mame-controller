package com.serwylo.mame.controller.client.android.controllers.buttons.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;

public class ArcadeButtonView extends ImageButton implements IButtonUi<ArcadeButton>
{

	private ArcadeButton button;

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
		this.button = button;
	}

	@Override
	public ArcadeButton getButton()
	{
		return this.button;
	}
}
