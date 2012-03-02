package com.serwylo.mame.controller.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteManager 
{

	public final static int BUTTON_RED = 0;
	public final static int BUTTON_BLUE = 1;
	public final static int BUTTON_WHITE = 2;
	public final static int BUTTON_YELLOW = 3;

	private static Sprite[] buttonSprites;
	
	public static Sprite[] getButtonSprites()
	{
		if ( buttonSprites == null )
		{
			Texture buttonTexture = new Texture( Gdx.files.internal("assets/buttons.png" ) );
			Sprite[] buttons = new Sprite[4];
			
			Sprite buttonRed = new Sprite( buttonTexture, 0, 0, 128, 128 );
			buttonRed.setOrigin( 64, 64 );
			buttons[ BUTTON_RED ] = buttonRed;
			
			Sprite buttonBlue = new Sprite( buttonTexture, 128, 0, 128, 128 );
			buttonBlue.setOrigin( 64, 64 );
			buttons[ BUTTON_BLUE ] = buttonBlue;
			
			Sprite buttonWhite = new Sprite( buttonTexture, 0, 128, 128, 128 );
			buttonWhite.setOrigin( 64, 64 );
			buttons[ BUTTON_WHITE ] = buttonWhite;
			
			Sprite buttonYellow = new Sprite( buttonTexture, 128, 128, 128, 128 );
			buttonYellow.setOrigin( 64, 64 );
			buttons[ BUTTON_YELLOW ] = buttonYellow;
			
			buttonSprites = buttons;
		}
		
		return buttonSprites;
	}

}
