package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.content.Context;
import android.view.View;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;

public class ButtonUiFactory
{

	public static View createButtonUiElement( Context context, AbstractButton button )
	{

		if ( button.getClass() == ArcadeButton.class )
		{
			ArcadeButtonUi view = new ArcadeButtonUi( context );
			view.setButton( (ArcadeButton)button );
			return view;
		}

		throw new IllegalArgumentException( "Button UI element does not exist for '" + button.getClass().getCanonicalName() + "'" );
	}

}
