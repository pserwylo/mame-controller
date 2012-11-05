package com.serwylo.mame.controller.client.android.views.buttons;

import android.content.Context;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.DPadButton;

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

		throw new IllegalArgumentException( "Button UI element does not exist for '" + button.getClass().getCanonicalName() + "'" );
	}

	public static ArcadeButtonView createArcadeButton( Context context, ArcadeButton button )
	{
		ArcadeButtonView btnView = new ArcadeButtonView( context, button );
		return btnView;
	}

	public static DPadButtonView createDPadButton( Context context, DPadButton.Directional button )
	{
		DPadButtonView btnView = new DPadButtonView( context, button );
		return btnView;
	}

}
