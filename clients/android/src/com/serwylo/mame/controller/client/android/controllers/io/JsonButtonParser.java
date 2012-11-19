package com.serwylo.mame.controller.client.android.controllers.io;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class JsonButtonParser<ButtonType extends AbstractButton>
{

	public static final String KEY_TYPE = "type";
	public static final String KEY_X = "x";
	public static final String KEY_Y = "y";

	public static final String KEY_KEY_CODE = "keyCode";


	public static JsonButtonParser getParser( JSONObject json ) throws JSONException
	{
		String type = json.getString( KEY_TYPE );
		if ( type.equals( ArcadeButtonParser.LABEL ) )
		{
			return new ArcadeButtonParser().setJson( json );
		}
		else if ( type.equals( DPadButtonParser.LABEL ) )
		{
			return new DPadButtonParser().setJson( json );
		}
		else if ( type.equals( NesButtonParser.LABEL ) )
		{
			return new NesButtonParser().setJson( json );
		}
		else
		{
			throw new JSONException( "Expected a '" + KEY_TYPE + "' key." );
		}
	}

	protected JSONObject json;

	public JsonButtonParser setJson( JSONObject json )
	{
		this.json = json;
		return this;
	}

	/**
	 * Initially, this just parsed a single button an returned it, but it turns out that its much easier to model
	 * controllers if we incorporate composite buttons, which are just declared once in the ctrl file, but actually
	 * represent several buttons.As such, we return multiple buttons, even though this will often only be one button.
	 * @return
	 * @throws JSONException
	 */
	public abstract ArrayList<ButtonType> parse() throws JSONException;

	/**
	 * Parses all of the KEY_* properties declared in {@link JsonButtonParser} from the {@link JsonButtonParser#json}
	 * property.
	 * @param button The button to store the parsed properties in.
	 */
	protected void parseBaseProperties( ButtonType button ) throws JSONException
	{
		button.setX( json.getInt( KEY_X ) );
		button.setY( json.getInt( KEY_Y ) );

		if ( json.has( KEY_KEY_CODE ) )
		{
			// Things like the DPad button specify multiple key code items in the JSON, for up/down/left/right.
			// As such, not all buttons have a KEY_KEY_CODE item, but most do, so I put this here anyhow.
			button.setKeyCode( json.getInt( KEY_KEY_CODE ) );
		}
	}

}
