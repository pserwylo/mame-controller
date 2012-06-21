package com.serwylo.mame.controller.client.android.controllers.io;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.DPadButton;
import org.json.JSONException;

public class DPadButtonParser extends JsonButtonParser
{

	public static String LABEL = "dpad";

	public static String KEY_CODE_UP = "keyCodeUp";
	public static String KEY_CODE_DOWN = "keyCodeDown";
	public static String KEY_CODE_LEFT = "keyCodeLeft";
	public static String KEY_CODE_RIGHT = "keyCodeRight";
	public static String KEY_CODE_UP_LEFT = "keyCodeUpLeft";
	public static String KEY_CODE_UP_RIGHT = "keyCodeUpRight";
	public static String KEY_CODE_DOWN_LEFT = "keyCodeDownLeft";
	public static String KEY_CODE_DOWN_RIGHT = "keyCodeDownRight";
	public static String ENABLE_DIAGONAL = "enableDiagonal";

	@Override
	public AbstractButton parse() throws JSONException
	{
		DPadButton button = new DPadButton();
		this.parseBaseProperties(button);

		if ( this.json.has( KEY_CODE_LEFT ) )
		{
			button.setKeyCodeLeft( this.json.getInt( KEY_CODE_LEFT ) );
		}

		if ( this.json.has( KEY_CODE_RIGHT ) )
		{
			button.setKeyCodeRight( this.json.getInt( KEY_CODE_RIGHT ) );
		}

		if ( this.json.has( KEY_CODE_UP ) )
		{
			button.setKeyCodeUp( this.json.getInt( KEY_CODE_UP ) );
		}

		if ( this.json.has( KEY_CODE_DOWN ) )
		{
			button.setKeyCodeDown( this.json.getInt( KEY_CODE_DOWN ) );
		}

		if ( this.json.has( KEY_CODE_UP_LEFT ) )
		{
			button.setKeyCodeUpLeft( this.json.getInt( KEY_CODE_UP_LEFT ) );
		}

		if ( this.json.has( KEY_CODE_UP_RIGHT ) )
		{
			button.setKeyCodeUpRight( this.json.getInt( KEY_CODE_UP_RIGHT ) );
		}

		if ( this.json.has( KEY_CODE_DOWN_LEFT ) )
		{
			button.setKeyCodeDownLeft( this.json.getInt( KEY_CODE_DOWN_LEFT ) );
		}

		if ( this.json.has( KEY_CODE_DOWN_RIGHT ) )
		{
			button.setKeyCodeDownRight( this.json.getInt( KEY_CODE_DOWN_RIGHT ) );
		}

		if ( this.json.has( ENABLE_DIAGONAL ) )
		{
			button.setEnableDiagonal( this.json.getBoolean( ENABLE_DIAGONAL ) );
		}

		return button;
	}

}
