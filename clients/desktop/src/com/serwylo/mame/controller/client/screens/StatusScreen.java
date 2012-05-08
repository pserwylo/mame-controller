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
import com.serwylo.mame.controller.client.net.bluetooth.BluetoothEvent;
import com.serwylo.mame.controller.client.platform.IBluetoothListener;
import com.serwylo.mame.controller.client.platform.IQrCodeListener;

public abstract class StatusScreen implements Screen, ClickListener
{

	protected Stage stage;
	
	/**
	 * Required so that we can ask for other screens to be displayed.
	 */
	protected MameControllerClient app;
	
	/**
	 * Keep references to the menu buttons so that we can use ourself as a click listener.
	 */
	protected TextButton cancelButton;
	
	/**
	 * Keeps the user informed as to what the sync is currently doing (e.g. searching
	 * for devices to connect to).
	 */
	protected Label statusLabel;

	public StatusScreen( MameControllerClient app )
	{        
		this.app = app;
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

        this.statusLabel = new Label( "", skin );
        layout.register( "statusLabel", this.statusLabel );
        
        this.cancelButton = new TextButton( "Cancel", skin.getStyle( TextButtonStyle.class ), "cancelButton" );
        this.cancelButton.setClickListener( this );
        layout.register( this.cancelButton );

        layout.parse( Gdx.files.internal( "ui/screens/sync.txt" ).readString() );
	}

	@Override
	public void show() 
	{
        Gdx.input.setInputProcessor( this.stage );
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

}
