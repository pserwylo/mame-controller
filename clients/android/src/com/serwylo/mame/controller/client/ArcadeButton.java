package com.serwylo.mame.controller.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ArcadeButton extends Button
{
	
	private int colour;
	private int keyCode;
	
	public ArcadeButton( int keyCode, int colour )
	{
		super( SpriteManager.getButtonSprites()[ colour ] );
		this.width = SpriteManager.getButtonSprites()[ colour ].getWidth();
		this.height = SpriteManager.getButtonSprites()[ colour ].getHeight();
		this.x = 0;
		this.y = 0;
		this.keyCode = keyCode;
		this.colour = colour;
	}
	
	public int getColour()
	{
		return this.colour;
	}
	
	public int getKeyCode()
	{
		return this.keyCode;
	}
	
	/**
	 * Seeing as these buttons are round, we can perform a simpler and more accurate hit
	 * detection for them.
	 */
	@Override
	public Actor hit( float x, float y )
	{
		float distance = new Vector2( x, y ).len();
		Gdx.app.log( "DISTANCE", "To " + this.colour + " - " + distance );
		return distance < this.width / 2 ? this : null;
	}
	
}
