package com.serwylo.mame.controller.client.android.controllers.io;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import org.json.JSONException;

import java.util.ArrayList;

public class ArcadeButtonParser extends JsonButtonParser<ArcadeButton>
{

	public static String LABEL = "arcade";
	public static String COLOUR = "colour";

	@Override
	public ArrayList<ArcadeButton> parse() throws JSONException
	{
		ArcadeButton button = new ArcadeButton();

		this.parseBaseProperties( button );

		button.setColour( this.json.getInt( COLOUR ) );

		ArrayList<ArcadeButton> buttons = new ArrayList<ArcadeButton>( 1 );
		buttons.add( button );

		return buttons;
	}

}
