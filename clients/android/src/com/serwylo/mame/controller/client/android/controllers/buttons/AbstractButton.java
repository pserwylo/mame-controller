package com.serwylo.mame.controller.client.android.controllers.buttons;

public abstract class AbstractButton
{

	private int x;
	private int y;
	private int keyCode;

	public int getX()
	{
		return x;
	}

	public void setX( int x )
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY( int y )
	{
		this.y = y;
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
