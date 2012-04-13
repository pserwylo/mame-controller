package com.serwylo.mame.controller.client.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.serwylo.mame.controller.client.*;
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
        
        // this.buttons = new ControllerLayout();
        
        Json json = new Json( OutputType.minimal );
        this.buttons = json.fromJson( ControllerLayout.class, Gdx.files.internal( "rainbow.control" ) );
        
        for ( ArcadeButton button : this.buttons.getButtonList() )
        {
        	this.stage.addActor( button );
        	button.setClickListener( this );
        }
	}
	
	@Override
	public void click( Actor actor, float x, float y )
	{
		ArcadeButton button = (ArcadeButton)actor;
		// MameControllerClient.bluetoothClient.send( Event.createKeyUp( button.getKeyCode() ) );
		NetworkClient.getInstance().sendEvent( Event.createKeyUp( button.getKeyCode() ) );
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
