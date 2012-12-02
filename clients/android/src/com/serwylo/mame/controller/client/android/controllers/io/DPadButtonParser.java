package com.serwylo.mame.controller.client.android.controllers.io;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.DPadButton;
import org.json.JSONException;

import java.util.ArrayList;

public class DPadButtonParser extends JsonButtonParser<DPadButton.Directional>
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
	public static String KEY_ENABLE_DIAGONAL = "enableDiagonal";

	@Override
	public ArrayList<DPadButton.Directional> parse() throws JSONException
	{
		ArrayList<DPadButton.Directional> buttons = new ArrayList<DPadButton.Directional>( 8 );

		int x = this.json.getInt( KEY_X );
		int y = this.json.getInt( KEY_Y );

		DPadButton.Middle middle = new DPadButton.Middle();
		DPadButton.Up up = new DPadButton.Up();
		DPadButton.Right right = new DPadButton.Right();
		DPadButton.Down down = new DPadButton.Down();
		DPadButton.Left left = new DPadButton.Left();

		this.parseBaseProperties( middle );
		this.parseBaseProperties( up );
		this.parseBaseProperties( right );
		this.parseBaseProperties( down );
		this.parseBaseProperties( left );

		buttons.add( middle );
		buttons.add( up );
		buttons.add( right );
		buttons.add( down );
		buttons.add( left );

		if ( this.json.has( KEY_CODE_LEFT ) )
		{
			left.setKeyCode( this.json.getInt( KEY_CODE_LEFT ) );
		}

		if ( this.json.has( KEY_CODE_RIGHT ) )
		{
			right.setKeyCode( this.json.getInt( KEY_CODE_RIGHT ) );
		}

		if ( this.json.has( KEY_CODE_UP ) )
		{
			up.setKeyCode( this.json.getInt( KEY_CODE_UP ) );
		}

		if ( this.json.has( KEY_CODE_DOWN ) )
		{
			down.setKeyCode( this.json.getInt( KEY_CODE_DOWN ) );
		}

		if ( !this.json.has( KEY_ENABLE_DIAGONAL ) || this.json.getBoolean( KEY_ENABLE_DIAGONAL ) )
		{
			DPadButton.UpLeft upLeft = new DPadButton.UpLeft();
			DPadButton.UpRight upRight = new DPadButton.UpRight();
			DPadButton.DownLeft downLeft = new DPadButton.DownLeft();
			DPadButton.DownRight downRight = new DPadButton.DownRight();

			this.parseBaseProperties( upLeft );
			this.parseBaseProperties( upRight );
			this.parseBaseProperties( downLeft );
			this.parseBaseProperties( downRight );

			buttons.add( upLeft );
			buttons.add( upRight );
			buttons.add( downLeft );
			buttons.add( downRight );

			if ( this.json.has( KEY_CODE_UP_LEFT ) )
			{
				upLeft.setKeyCode( this.json.getInt( KEY_CODE_UP_LEFT ) );
			}

			if ( this.json.has( KEY_CODE_UP_RIGHT ) )
			{
				upRight.setKeyCode( this.json.getInt( KEY_CODE_UP_RIGHT ) );
			}

			if ( this.json.has( KEY_CODE_DOWN_LEFT ) )
			{
				downLeft.setKeyCode( this.json.getInt( KEY_CODE_DOWN_LEFT ) );
			}

			if ( this.json.has( KEY_CODE_DOWN_RIGHT ) )
			{
				downRight.setKeyCode( this.json.getInt( KEY_CODE_DOWN_RIGHT ) );
			}

		}

		return buttons;
	}

}
