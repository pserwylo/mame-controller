package com.serwylo.mame.controller.client.android.controllers.buttons;

public class ArcadeButton extends AbstractButton
{

	private int colour;
	private int keyCode;

	public int getColour()
	{
		return colour;
	}

	public void setColour( int colour )
	{
		this.colour = colour;
	}

	public int getKeyCode()
	{
		return keyCode;
	}

	public void setKeyCode( int keyCode )
	{
		this.keyCode = keyCode;
	}
}
