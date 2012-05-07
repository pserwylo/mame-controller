package com.serwylo.mame.controller.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.serwylo.mame.controller.client.*;
import com.serwylo.mame.controller.client.net.NetworkClient;
import com.serwylo.mame.controller.client.net.NetworkManager;
import com.serwylo.mame.controller.shared.Event;

public class Controller implements Screen, ClickListener
{

	private static Controller singleton;

	public static Controller getInstance( MameControllerClient app )
	{
		if ( singleton == null )
		{
			singleton = new Controller( app );
		}
		return singleton;
	}
	
	private MameControllerClient app;

	private Stage stage;
	
	private ControllerLayout buttons;
	
	public Controller( MameControllerClient app )
	{
		this.app = app;
        this.stage = new Stage( 800, 400, false );
        this.buttons = new ControllerLayout();

		// Json json = new Json( OutputType.minimal );
		// this.setLayout(  json.fromJson( ControllerLayout.class, Gdx.files.internal( "rainbow.ctrl" ) ) );
	}


	/**
	 * Clears any previous layout, and assigns the buttons from the new layout.
	 * @param layout
	 * @return Returns a reference to itself, so that it can be chained after calls to getInstance().
	 */
	public Controller setLayout( ControllerLayout layout )
	{
		this.stage.clear();

		this.buttons = layout;
		for ( ArcadeButton button : this.buttons.getButtonList() )
		{
			this.stage.addActor( button );
			button.setClickListener( this );
		}

		return this;
	}
	
	@Override
	public void click( Actor actor, float x, float y )
	{
		ArcadeButton button = (ArcadeButton)actor;
		NetworkManager.getInstance().sendEvent( Event.createKeyUp( button.getKeyCode() ) );
		// this.app.setScreen( MainMenu.getInstance( this.app ) );
	}
	
	@Override
	public void render( float delta ) 
	{
        Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
        stage.act( delta );
        stage.draw();
    }

	@Override
	public void resize( int width, int height ) 
	{
		
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
