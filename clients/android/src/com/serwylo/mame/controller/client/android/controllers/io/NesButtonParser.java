package com.serwylo.mame.controller.client.android.controllers.io;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.NesButton;
import org.json.JSONException;

import java.util.ArrayList;

public class NesButtonParser extends JsonButtonParser<NesButton>
{

	public static String LABEL = "nes";

	@Override
	public ArrayList<NesButton> parse() throws JSONException
	{
		NesButton button = new NesButton();

		this.parseBaseProperties( button );

		ArrayList<NesButton> buttons = new ArrayList<NesButton>( 1 );
		buttons.add( button );

		return buttons;
	}

}
