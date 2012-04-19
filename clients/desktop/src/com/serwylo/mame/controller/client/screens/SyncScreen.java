package com.serwylo.mame.controller.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.serwylo.mame.controller.client.MameControllerClient;
import com.serwylo.mame.controller.client.NetworkClient;
import com.serwylo.mame.controller.client.bluetooth.BluetoothEvent;
import com.serwylo.mame.controller.client.platform.IBluetoothListener;
import com.serwylo.mame.controller.client.platform.IQrCodeListener;
import com.serwylo.mame.controller.client.platform.QrCodeReader;

public class SyncScreen implements Screen, ClickListener, IBluetoothListener, IQrCodeListener
{

	private static SyncScreen singleton;

	public static SyncScreen getInstance( MameControllerClient app )
	{
		if ( singleton == null )
		{
			singleton = new SyncScreen( app );
		}
		return singleton;
	}

	private Stage stage;
	
	/**
	 * Required so that we can ask for other screens to be displayed.
	 */
	private MameControllerClient app;
	
	/**
	 * Keep references to the menu buttons so that we can use ourself as a click listener.
	 */
	private TextButton cancelButton, searchAgainButton;
	
	/**
	 * Keeps the user informed as to what the sync is currently doing (e.g. searching
	 * for devices to connect to).
	 */
	private Label statusLabel;
	
	/**
	 * Keep track of how many devices we've found for user input.
	 * TODO: Replace this with a dynamically populated list of devices, so you wont have
	 * to wait until the discovery is complete.
	 */
	private int numDevicesFound;
	
	/**
	 * TODO: Just for debugging, later need to let the user choose...
	 */
	private String deviceAddressToConnect;
	
	public SyncScreen( MameControllerClient app )
	{        
		this.app = app;
	}
	
	/**
	 * Begin the search for devices to connect to, and also update the UI.
	 */
	private void searchForDevices()
	{
		if ( this.statusLabel != null )
		{
			this.statusLabel.setText( "Searching for devices to connect to..." );
	        this.cancelButton.setText( "Cancel" );
		}

		this.numDevicesFound = 0;
		MameControllerClient.bluetoothClient.init();
		MameControllerClient.bluetoothClient.setBluetoothListener( this );
		MameControllerClient.bluetoothClient.discover();
	}
	
	@Override
	public void click( Actor actor, float x, float y )
	{
		if ( actor == this.cancelButton )
		{
			this.app.setScreen( MainMenu.getInstance( this.app ) );
		}
	}
	
	@Override
	public void render( float delta ) 
	{
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act( Gdx.graphics.getDeltaTime() );
        stage.draw();
        // Table.drawDebug( stage );
	}

	@Override
	public void resize( int width, int height ) 
	{
		if ( this.statusLabel == null )
		{
			this.createUi( width, height );
		}
	}
	
	private void createUi( int width, int height )
	{
        this.stage = new Stage( width, height, true );
        Gdx.input.setInputProcessor( this.stage );
        
        Skin skin = new Skin( Gdx.files.internal( "ui/uiskin.json" ), Gdx.files.internal( "ui/uiskin.png") );
        
        Table table = new Table( skin );
        table.width = width;
        table.height = height;
        this.stage.addActor(table);
        
        TableLayout layout = table.getTableLayout();

        this.statusLabel = new Label( "Searching for devices to connect to...", skin );
        layout.register( "statusLabel", this.statusLabel );
        
        this.cancelButton = new TextButton( "Cancel", skin.getStyle( TextButtonStyle.class ), "cancelButton" );
        this.cancelButton.setClickListener( this );
        layout.register( this.cancelButton );
        
        this.searchAgainButton = new TextButton( "Search again", skin.getStyle( TextButtonStyle.class ), "searchAgainButton" );
        this.searchAgainButton.setClickListener( this );
        this.searchAgainButton.visible = false;
        layout.register( this.searchAgainButton );

        layout.parse( Gdx.files.internal( "ui/screens/sync.txt" ).readString() );
	}

	@Override
	public void show() 
	{
        Gdx.input.setInputProcessor( this.stage );

		// Let the user scan a qrcode...
		MameControllerClient.qrCodeReader.addQrCodeListener( this );
		MameControllerClient.qrCodeReader.readBarcode();

        // If we ended up at this view, then we need to start looking straight away...
        // this.searchForDevices();
	}

	@Override
	public void receiveQrCodeContents( String contents )
	{
		NetworkClient.getInstance().open( contents );
		this.app.setScreen( MainMenu.getInstance( this.app ) );
	}

	@Override
	public void hide() 
	{

	}

	@Override
	public void pause() 
	{

	}

	@Override
	public void resume() 
	{

	}

	@Override
	public void dispose() 
	{
	}

	@Override
	public void onBluetoothEvent( BluetoothEvent e ) 
	{
		if ( e.getType() == BluetoothEvent.DEVICE_FOUND )
		{
			this.numDevicesFound ++;
			this.statusLabel.setText( "Searching for devices to connect to (" + this.numDevicesFound + " found)..." );
			this.deviceAddressToConnect = e.getDeviceAddress();
		}
		else if ( e.getType() == BluetoothEvent.END_DISCOVERY )
		{
	        this.searchAgainButton.visible = true;
	        this.cancelButton.setText( "Back" );
			if ( this.numDevicesFound == 0 )
			{
				this.statusLabel.setText( "No devices found" );
			}
			else 
			{
				this.statusLabel.setText( "Select the device to connect to:" );
				MameControllerClient.bluetoothClient.connect( this.deviceAddressToConnect );
			}
		}
	}

}
