package com.serwylo.mame.controller.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MameControllerClient implements ApplicationListener 
{
	
	private Stage stage;
	
	@Override
	public void create() 
	{
		this.stage = new Stage( 800, 480, true );
		Gdx.input.setInputProcessor( this.stage );

		Button redButton = new Button( Button.Type.BTN_A, SpriteManager.BUTTON_RED, new Vector2( 250, 250 ) );
		this.stage.addActor( redButton );
		
		Server.getInstance().open();
	}

	@Override
	public void dispose() 
	{
		Server.getInstance().close();
	}

	@Override
	public void pause() 
	{
	}

	@Override
	public void render() 
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
		this.stage.act( Gdx.graphics.getDeltaTime() );
		this.stage.draw();
	}

	@Override
	public void resize( int width, int height ) 
	{
		this.stage.setViewport( width, height, true );
	}

	@Override
	public void resume() 
	{
	}

}
