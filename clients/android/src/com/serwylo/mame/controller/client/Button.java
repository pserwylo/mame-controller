package com.serwylo.mame.controller.client;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Button extends Actor
{
	
	static class Type
	{
		public static final Type BTN_A = new Type( 'a' );
		public static final Type BTN_B = new Type( 'b' );
		
		private char type;
		
		private Type( char type )
		{
			this.type = type;
		}
		
		public char toChar() 
		{ 
			return this.type; 
		}
	}

	private int colour;
	private Sprite sprite;
	private Vector2 position;
	private boolean isPressed;
	private Type type;
	
	public Button( Type type, int colour, Vector2 position )
	{
		this.type = type;
		this.position = position;
		this.colour = colour;
		this.sprite = SpriteManager.getButtonSprites()[ this.colour ];
	}
	
	@Override
	public void draw( SpriteBatch batch, float parentAlpha )
	{
		float y = this.position.y - this.sprite.getHeight() / 2;
		if ( this.isPressed )
		{
			y -= 5;
		}
		this.sprite.setPosition( this.position.x - this.sprite.getWidth() / 2, y );
		this.sprite.draw( batch );
	}

	@Override
	public boolean touchDown( float x, float y, int pointer )
	{
		this.isPressed = true;
		Server.getInstance().sendButton( this.type );
		return true;
	}

	@Override
	public void touchUp( float x, float y, int pointer )
	{
		this.isPressed = false;
	}

	@Override
	public void touchDragged( float x, float y, int pointer )
	{
		
	}

	@Override
	public Actor hit( float x, float y )
	{
		Vector2 point = new Vector2( x, y );
		if( point.dst( this.position ) < this.sprite.getWidth() / 2 )
		{
			return this;
		}
		else
		{
			return null;
		}
	}

}
