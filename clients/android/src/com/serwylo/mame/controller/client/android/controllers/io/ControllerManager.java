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

/**
 * TODO: Add an option for advanced users to get controllers from SD card.
 */
public class ControllerManager
{

	/**
	 * Suffixed with a "/" to make concatenation earlier.
	 */
	private static final String DIR_CONTROLLERS = "controllers/";

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
		File file = context.getFileStreamPath( DIR_CONTROLLERS + controllerName );
		return file.exists();
	}

	/**
	 * Looks for definition files which describe controller layouts and sprites.
	 * (files ending in .ctrl in the controller.layouts folder).
	 * @return List of {@link File}s pointing to different controller definitions.
	 */
	public static String[] findControllers( Context context )
	{
		File dir = context.getDir( DIR_CONTROLLERS, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE );

		return dir.list( new FilenameFilter()
		{
			@Override
			public boolean accept( File dir, String name )
			{
				return ( name.length() > 5 ) && name.substring( name.length() - 6, name.length() - 1 ).toLowerCase().equals( ".ctrl" );
			}
		});
	}

	/**
	 * Given a name (e.g. "snes.controller") look for the definition of that controller and then try to load it.
	 * If there is any problems, then return null.
	 * @param controllerName
	 * @return Returns null if the controller couldn't be read.
	 */
	public static ControllerDefinition readController( String controllerName ) throws IOException, JSONException
	{
		BufferedReader reader = new BufferedReader( new FileReader( DIR_CONTROLLERS + controllerName ) );

		// Read contents of file into buffer...
		StringBuilder buffer = new StringBuilder();
		String line = reader.readLine();
		while ( line != null )
		{
			buffer.append( line );
			line = reader.readLine();
		}

		JSONObject json = new JSONObject( buffer.toString() );

		String label = json.getString( "label" );
		JSONArray buttons = json.getJSONArray( "buttons" );
		ArrayList<AbstractButton> buttonList = new ArrayList<AbstractButton>( buttons.length() );
		for ( int i = 0; i < buttons.length(); i ++ )
		{
			buttonList.add( JsonButtonParser.getParser( buttons.getJSONObject( i ) ).parse() );
		}

		ControllerDefinition controller = new ControllerDefinition();
		controller.setButtonList( buttonList );
		controller.setLabel( label );

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
				layout = ControllerManager.readController( lastController );
			}

			if ( layout == null )
			{
				// If there is only one controller type, then return that...
				String[] availableControllers = ControllerManager.findControllers( context );
				if ( availableControllers.length == 0 )
				{
					Log.e( "Controller", "No controllers found." );
				}
				else if ( availableControllers.length == 1 )
				{
					layout = ControllerManager.readController( availableControllers[ 0 ] );
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
