package com.serwylo.mame.controller.client.android.controllers.buttons;

public class ArcadeButton extends AbstractButton
{

	private int colour;

	public String toString()
	{
		return "Arcade button";
	}

	public int getColour()
	{
		return colour;
	}

	public void setColour( int colour )
	{
		this.colour = colour;
	}

}
