package com.serwylo.mame.controller.client;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.OrderedMap;

public class ControllerLayout implements Serializable 
{

	public static final int LAYOUT_HORIZONTAL = 1;
	public static final int LAYOUT_VERTICAL = 2;
	
	private ArrayList<ArcadeButton> buttonList;
	private String label;
	private int layout;
	
	public ControllerLayout()
	{
		this.buttonList = new ArrayList<ArcadeButton>();
	}
	
	public String getLabel()
	{
		return this.label;
	}
	
	public ArrayList<ArcadeButton> getButtonList()
	{
		return this.buttonList;
	}
	
	@Override
	public void write( Json json ) 
	{
		json.writeValue( "label", this.label );
		json.writeValue( "layout", this.layout );
		json.writeArrayStart( "buttons" );
		for ( ArcadeButton button : this.buttonList )
		{
			json.writeObjectStart();
			json.writeValue( "colour", button.getColour() );
			json.writeValue( "keyCode", button.getKeyCode() );
			json.writeValue( "x", button.x );
			json.writeValue( "y", button.y );
			json.writeObjectEnd();
		}
		json.writeArrayEnd();
	}

	/**
	 * Looks for definition files which describe controller layouts and sprites.
	 * (files ending in .ctrl in the controller.layouts folder).
	 * @return List of {@link FileHandle}s pointing to different controller definitions.
	 */
	public static ArrayList<FileHandle> findControllers()
	{
		ArrayList<FileHandle> controllers = new ArrayList<FileHandle>();

		// TODO: Add an option for where else to look in the filesystem for advanced users.
		FileHandle dir = Gdx.files.internal( "controller.layouts" );
		for ( FileHandle controllerFile : dir.list( ".ctrl" ) )
		{
			Gdx.app.log( "Controller", "Found: " + controllerFile );
			controllers.add( controllerFile );
		}

		return controllers;
	}

	/**
	 * Given a name (e.g. "snes.controller") look for the definition of that controller and then try to load it.
	 * If there is any problems, then return null.
	 * @param controllerName
	 * @return Returns null if the controller couldn't be read.
	 * TODO: Let the person calling this function deal with the exception...
	 */
	public static ControllerLayout readController( String controllerName )
	{
		ControllerLayout controller = null;
		Json json = new Json( JsonWriter.OutputType.minimal );
		try
		{
			controller = json.fromJson( ControllerLayout.class, Gdx.files.internal( controllerName ) );
		}
		catch ( Exception e )
		{
			Gdx.app.error( "Controller", "Cannot read controller '" + controllerName + "'." );
			Gdx.app.error( "Controller", e.getMessage() );
		}
		return controller;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void read( Json json, OrderedMap<String, Object> jsonData ) 
	{
		this.label = json.readValue( "label", String.class, jsonData );
		//  this.layout = json.readValue( "layout", Integer.class, jsonData );
		
		// Iterate over all of the arcade button controls...
		Array<OrderedMap<String, Object>> buttons = json.readValue( "buttons", Array.class, jsonData );
		for ( OrderedMap<String, Object> button : buttons )
		{
			// ArcadeButton, Joystick, D-Pad, etc...
			// int type = json.readValue( "type", Integer.class, button );
			// if ( type == ArcadeButton )
			// {
			float x = json.readValue( "x", Float.class, button );
			float y = json.readValue( "y", Float.class, button );
			int keyCode = json.readValue( "keyCode", Integer.class, button );
			int colour = json.readValue( "colour", Integer.class, button );
			ArcadeButton arcadeButton = new ArcadeButton( keyCode, colour );
			arcadeButton.x = x;
			arcadeButton.y = y;
			this.buttonList.add( arcadeButton );
			// }
		}
	}

	public static boolean exists( String controllerPath )
	{
		return Gdx.files.internal( controllerPath ).exists();
	}
}
