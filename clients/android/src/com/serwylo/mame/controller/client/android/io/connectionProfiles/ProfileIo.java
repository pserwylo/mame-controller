package com.serwylo.mame.controller.client.android.io.connectionProfiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.serwylo.mame.controller.client.android.net.ConnectionProfile;
import com.serwylo.mame.controller.client.android.net.wifi.WifiProfile;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
abstract public class ProfileIo<ProfileType extends ConnectionProfile>
{

	private SharedPreferences preferences;

	public ProfileIo( Context context )
	{
		this.preferences = PreferenceManager.getDefaultSharedPreferences( context );
	}

	protected final SharedPreferences getPreferences()
	{
		return this.preferences;
	}

	abstract public void save( ProfileType profile );

	abstract public ArrayList<ProfileType> loadList();

	abstract public void forget( ProfileType profile );

}
