package com.serwylo.mame.controller.client.android.ui.buttons;

import android.content.Context;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;

public class ArcadeButtonUi extends AbstractButtonUi<ArcadeButton>
{

	protected ArcadeButton button;

	public ArcadeButtonUi( Context context, ArcadeButton button )
	{
		super( context );
		this.button = button;
		this.setBackgroundColor( button.getColour() );
	}

	public ArcadeButton getButton()
	{
		return this.button;
	}

}
