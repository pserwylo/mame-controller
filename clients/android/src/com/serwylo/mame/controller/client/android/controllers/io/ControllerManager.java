package com.serwylo.mame.controller.client.android.controllers.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;
import com.serwylo.mame.controller.client.android.controllers.ControllerDefinition;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * TODO: Add an option for advanced users to get controllers from SD card.
 */
public class ControllerManager
{

	private static final String DIR_CONTROLLERS = "controllers";

	/**
	 * All preferences to do with controllers will be stored in here.
	 */
	private static final String PREFERENCE_FILE = "controllerManager";

	/**
	 * Preference key which stores the last controller the user used, so that we can try to bring it up for them
	 * next time they open the app.
	 */
	private static final String PREF_LAST_CONTROLLER = "controller";

	public static boolean exists( Context context, String controllerName )
	{
		File file = context.getFileStreamPath( DIR_CONTROLLERS + "/" + controllerName );
		return file.exists();
	}

	/**
	 * Looks for definition files which describe controller layouts and sprites.
	 * (files ending in .ctrl in the controller.layouts folder).
	 * @return List of {@link File}s pointing to different controller definitions.
	 */
	public static ArrayList<String> findControllers( Context context ) throws IOException
	{
		String[] fileNames = context.getAssets().list( DIR_CONTROLLERS );
		ArrayList<String> validControllerFileNames = new ArrayList<String>( fileNames.length );
		for ( String name : fileNames )
		{
			if ( ( name.length() > 5 ) && name.substring( name.length() - 5, name.length() ).toLowerCase().equals( ".ctrl" ) )
			{
				Log.d( "MAME", "Found controller '" + name + "'" );
				validControllerFileNames.add( name );
			}
		}
		return validControllerFileNames;
	}

	/**
	 * Given a name (e.g. "snes.controller") look for the definition of that controller and then try to load it.
	 * If there is any problems, then return null.
	 * @param controllerName
	 * @return Returns null if the controller couldn't be read.
	 */
	public static ControllerDefinition readController( Context context, String controllerName ) throws IOException, JSONException
	{

		// http://stackoverflow.com/questions/309424/in-java-how-do-i-read-convert-an-inputstream-to-a-string
		InputStream input = context.getAssets().open( DIR_CONTROLLERS + "/" + controllerName );
		String fileContents = null;
		try
		{
			fileContents = new Scanner( input ).useDelimiter( "\\A" ).next();
		}
		catch( NoSuchElementException e )
		{
			throw new IOException( "Error reading controller '" + controllerName + "'", e );
		}

		JSONObject json = new JSONObject( fileContents );

		String label = json.getString( "label" );
		String orientation = json.has( "orientation") ? json.getString( "orientation" ) : ControllerDefinition.ORIENTATION_PORTRAIT;
		JSONArray buttons = json.getJSONArray( "buttons" );
		ArrayList<AbstractButton> buttonList = new ArrayList<AbstractButton>( buttons.length() );
		for ( int i = 0; i < buttons.length(); i ++ )
		{
			buttonList.addAll( JsonButtonParser.getParser( buttons.getJSONObject( i ) ).parse() );
		}

		ControllerDefinition controller = new ControllerDefinition();
		controller.setLabel( label );
		controller.setButtonList( buttonList );
		controller.setOrientation( orientation );

		return controller;
	}

	/**
	 * Intelligently try to find an appropriate controller to show the user.
	 *
	 * First, attempt to return the last used controller (saved in a {@link SharedPreferences} file).
	 * If this fails, and there is only one controller anyway, just load that.
	 * Otherwise (e.g. if there are no controllers or more than one to choose from), return null.
	 * @param context Required so that we can resolve file paths.
	 * @return Will return null if no controllers exist.
	 */
	public static ControllerDefinition getDefaultController( Context context )
	{
		ControllerDefinition layout = null;
		try
		{
			// Try to get a controller to open without the user having to do anything...
			SharedPreferences preferences = context.getSharedPreferences( PREFERENCE_FILE, Context.MODE_PRIVATE );
			String lastController = preferences.getString( PREF_LAST_CONTROLLER, null );

			if ( lastController != null && lastController.length() > 0 && ControllerManager.exists( context, lastController ) )
			{
				// Try to find the appropriate controller...
				layout = ControllerManager.readController( context, lastController );
			}

			if ( layout == null )
			{
				// If there is only one controller type, then return that...
				ArrayList<String> availableControllers = ControllerManager.findControllers( context );
				if ( availableControllers.isEmpty() )
				{
					Log.e( "MAME", "No controllers found." );
				}
				else if ( availableControllers.size() == 1 )
				{
					layout = ControllerManager.readController( context, availableControllers.get( 0 ) );
				}
			}

		}
		catch ( IOException ioe )
		{
			Toast toast = Toast.makeText( context, "Error loading controller.", 3000 );
			toast.show();
		}
		catch ( JSONException je )
		{
			Toast toast = Toast.makeText( context, "Error reading controller.", 3000 );
			toast.show();
		}

		return layout;
	}


}
