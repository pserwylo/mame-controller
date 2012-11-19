package com.serwylo.mame.controller.client.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.serwylo.mame.controller.client.android.R;

public class SettingsActivity extends PreferenceActivity
{

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );
		addPreferencesFromResource( R.xml.preferences );
		findPreference( "view_server_list" ).setOnPreferenceClickListener( new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick( Preference preference )
			{
				startActivity( new Intent( SettingsActivity.this, ServerListActivity.class ) );
				return true;
			}
		} );
	}

	public static void show( Context context )
	{
		Intent intent = new Intent( context, SettingsActivity.class );
		context.startActivity( intent );
	}

}