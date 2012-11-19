package com.serwylo.mame.controller.client.android.views.buttons;

import android.content.Context;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.DPadButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.NesButton;

public class ButtonViewFactory
{

	public static ButtonView createButton( Context context, AbstractButton button )
	{
		if ( button instanceof ArcadeButton )
		{
			return ButtonViewFactory.createArcadeButton( context, (ArcadeButton) button );
		}
		else if ( button instanceof DPadButton.Directional )
		{
			return ButtonViewFactory.createDPadButton( context, (DPadButton.Directional)button );
		}
		else if ( button instanceof NesButton )
		{
			return ButtonViewFactory.createNesButton( context, (NesButton)button );
		}

		throw new IllegalArgumentException( "Button UI element does not exist for '" + button.getClass().getCanonicalName() + "'" );
	}

	public static ArcadeButtonView createArcadeButton( Context context, ArcadeButton button )
	{
		return new ArcadeButtonView( context, button );
	}

	public static DPadButtonView createDPadButton( Context context, DPadButton.Directional button )
	{
		return new DPadButtonView( context, button );
	}

	public static NesButtonView createNesButton( Context context, NesButton button )
	{
		return new NesButtonView( context, button );
	}

}
