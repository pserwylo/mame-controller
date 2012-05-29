package com.serwylo.mame.controller.client.android.controllers.io;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import org.json.JSONException;

public class ArcadeButtonParser extends JsonButtonParser
{

	public static String LABEL = "arcade";

	public static String KEY_COLOUR = "colour";

	@Override
	public AbstractButton parse() throws JSONException
	{
		ArcadeButton button = new ArcadeButton();
		this.parseBaseProperties( button );
		button.setColour( this.json.getInt( KEY_COLOUR ) );
		return button;
	}

}
