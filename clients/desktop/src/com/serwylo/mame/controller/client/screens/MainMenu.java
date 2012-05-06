package com.serwylo.mame.controller.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.serwylo.mame.controller.client.MameControllerClient;

public class MainMenu implements Screen, ClickListener
{

	private static MainMenu singleton;

	public static MainMenu getInstance( MameControllerClient app )
	{
		if ( singleton == null )
		{
			singleton = new MainMenu( app );
		}
		return singleton;
	}

	private Stage stage;
	
	/**
	 * Required so that we can ask for other screens to be displayed.
	 */
	private MameControllerClient app;
	
	/**
	 * Keep references to the menu buttons so that we can use ourself as a 
	 * click listener.
	 */
	private TextButton openControllerButton, syncButton, optionsButton, quitButton;
	
	public MainMenu( MameControllerClient app )
	{        
		this.app = app;
	}
	
	@Override
	public void click( Actor actor, float x, float y )
	{
		if ( actor == this.openControllerButton )
		{
			this.app.setScreen( Controller.getInstance( this.app ) );
		}
		else if ( actor == this.syncButton )
		{
			// NetworkClient.getInstance().open();
			this.app.setScreen( SyncScreen.getInstance( this.app ) );
		}
		else if ( actor == this.quitButton )
		{
			Gdx.app.exit();
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
		if ( this.openControllerButton == null )
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

        this.openControllerButton = new TextButton( "Open Controller", skin.getStyle( TextButtonStyle.class ), "openControllerButton" );
        this.openControllerButton.setClickListener( this );
        layout.register( this.openControllerButton );

        this.syncButton = new TextButton( "Sync to Arcade Machine", skin.getStyle( TextButtonStyle.class ), "syncButton" );
        this.syncButton.setClickListener( this );
        layout.register( this.syncButton );

        this.optionsButton = new TextButton( "Options", skin.getStyle( TextButtonStyle.class ), "optionsButton" );
        this.optionsButton.setClickListener( this );
        layout.register( this.optionsButton );
         
        this.quitButton = new TextButton( "Quit", skin.getStyle( TextButtonStyle.class ), "quitButton" );
        this.quitButton.setClickListener( this );
        layout.register( this.quitButton );

        layout.parse( Gdx.files.internal( "ui/screens/main-menu.txt" ).readString() );
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
