package com.serwylo.mame.controller.client.android.controllers.io;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import org.json.JSONException;

import java.util.ArrayList;

public class ArcadeButtonParser extends JsonButtonParser
{

	public static String LABEL = "arcade";

	public static String COLOUR = "colour";
	public static final String KEY_CODE = "keyCode";

	@Override
	public ArrayList<AbstractButton> parse() throws JSONException
	{
		ArcadeButton button = new ArcadeButton();
		this.parseBaseProperties( button );
		button.setColour( this.json.getInt( COLOUR ) );
		button.setKeyCode( this.json.getInt( KEY_CODE ) );

		ArrayList<AbstractButton> buttons = new ArrayList<AbstractButton>( 1 );
		buttons.add( button );
		return buttons;
	}

}
