package com.serwylo.mame.controller.client.android.io.connectionProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.content.Context;
import com.serwylo.mame.controller.client.android.net.wifi.WifiProfile;

/**
 * CRUD operations for Wifi profiles.
 */
public class WifiProfileIo extends ProfileIo<WifiProfile>
{

	private static final String PREF_WIFI_PROFILES = "WifiProfiles";

	public WifiProfileIo( Context context )
	{
		super( context );
	}

	@Override
	public void save( WifiProfile profile )
	{

		boolean found = false;
		ArrayList<WifiProfile> list = loadList();

		for ( WifiProfile p : list )
		{
			if ( p.equals( profile ) )
			{
				found = true;
			}
		}

		if ( !found )
		{
			list.add( 0, profile );
			persistList( list );
		}

	}

	protected void persistList( ArrayList<WifiProfile> profiles )
	{
		String serializedList = serialize( profiles );
		getPreferences().edit().putString( PREF_WIFI_PROFILES, serializedList ).apply();
	}

	protected static String serialize( ArrayList<WifiProfile> profiles )
	{
		StringBuilder sb = new StringBuilder( profiles.size() * 30 );

		for ( int i = 0; i < profiles.size(); i ++ )
		{
			WifiProfile profile = profiles.get( i );

			sb.append( profile.getIpAddress() );
			sb.append( ':' );
			sb.append( profile.getPort() );

			if ( profile.getLastConnectDate() != null )
			{
				sb.append( ":" );
				sb.append( profile.getLastConnectDate().getTime() );
			}

			if ( i < profiles.size() - 1 )
			{
				sb.append( ',' );
			}
		}

		return sb.toString();
	}

	protected static ArrayList<WifiProfile> unserialize( String string )
	{
		ArrayList<WifiProfile> profiles = null;

		if ( string == null )
		{
			profiles = new ArrayList<WifiProfile>();
		}
		else
		{
			String[] profileStrings = string.split( "," );
			profiles = new ArrayList<WifiProfile>( profileStrings.length );

			for ( String profileString : profileStrings )
			{
				String[] profileParts = profileString.split( ":" );

				if ( profileParts.length >= 2 && profileParts.length <= 3 )
				{
					String ipAddress = profileParts[ 0 ];
					int port = Integer.parseInt( profileParts[1] );

					if ( ipAddress.length() > 0 && port >= 0 && port <= 65535 )
					{
						WifiProfile profile = WifiProfile.create( profileParts[ 0 ], Integer.parseInt( profileParts[ 1 ] ) );

						if ( profileParts.length >= 3 )
						{
							long lastConnectDate = Long.parseLong( profileParts[2] );
							profile.setLastConnectDate( new Date( lastConnectDate ) );
						}

						profiles.add( profile );
					}
				}
			}

		}

		return profiles;
	}

	@Override
	public void forget( WifiProfile profile )
	{
		ArrayList<WifiProfile> profiles = loadList();
		for ( Iterator<WifiProfile> it = profiles.iterator(); it.hasNext(); )
		{
			WifiProfile p = it.next();

			if ( p.equals( profile ) )
			{
				it.remove();
			}
		}
		persistList( profiles );

	}

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<WifiProfile> loadList()
	{
		String value = getPreferences().getString( PREF_WIFI_PROFILES, null );
		return unserialize( value );
	}

}