package com.serwylo.mame.controller.client.android.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import com.serwylo.mame.controller.client.android.R;
import com.serwylo.mame.controller.client.android.io.connectionProfiles.WifiProfileIo;
import com.serwylo.mame.controller.client.android.net.wifi.WifiProfile;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerListActivity extends ListActivity
{

	private static final String KEY_TITLE = "title";
	private static final String KEY_DETAILS = "details";
	private static final String KEY_LAST_CONNECT_DATE = "lastConnectDate";
	private static final String KEY_PROFILE = "profile";

	private ArrayList<HashMap<String, Object>> dataProvider;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );

		this.setTitle( "Past servers" );
		this.setContentView( R.layout.server_list );

		this.dataProvider = this.getServerList();

		ListAdapter adapter = new SimpleAdapter(
			this,
			this.dataProvider,
			R.layout.server_list_item,
			new String[]{ KEY_TITLE, KEY_DETAILS, KEY_LAST_CONNECT_DATE },
			new int[]{ R.id.server_list_item_title, R.id.server_list_item_details, R.id.server_list_item_last_connect_date }
		);

		this.setListAdapter( adapter );
		this.registerForContextMenu( findViewById( android.R.id.list ) );
	}

	public void onCreateContextMenu( ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo )
	{
		super.onCreateContextMenu( menu, view, menuInfo );
		getMenuInflater().inflate( R.menu.server_list_item, menu );
	}

	public boolean onContextItemSelected( MenuItem item )
	{
		boolean handled;
		if ( item.getItemId() == R.id.server_list_item_delete )
		{
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			HashMap selectedItem = (HashMap)this.getListAdapter().getItem( info.position );

			this.dataProvider.remove( selectedItem );
			new WifiProfileIo( this ).forget( (WifiProfile)selectedItem.get( KEY_PROFILE ) );

			// Update UI in response to the dataProvider changing...
			getListView().invalidateViews();

			handled = true;
		}
		else
		{
			handled = super.onContextItemSelected( item );
		}
		return handled;
	}

	/**
	 * Pull up a list of servers from the preferences, and build a set of entries for the 'server_list' preference.
	 */
	protected ArrayList<HashMap<String, Object>> getServerList()
	{

		ArrayList<HashMap<String, Object>> serverListEntries = new ArrayList<HashMap<String, Object>>();

		ArrayList<WifiProfile> wifiProfiles = new WifiProfileIo( this ).loadList();
		for ( WifiProfile profile : wifiProfiles )
		{
			String lastConnectedString = profile.getLastConnectDate() == null
				? "Never connected"
				: "Last connected at " + profile.getLastConnectDate().toLocaleString();

			HashMap<String, Object> details = new HashMap<String, Object>();
			details.put( KEY_TITLE, "WiFi Server" );
			details.put( KEY_DETAILS, profile.getDetails() );
			details.put( KEY_LAST_CONNECT_DATE, lastConnectedString );
			details.put( KEY_PROFILE, profile );
			serverListEntries.add( details );
		}

		return serverListEntries;

	}

}