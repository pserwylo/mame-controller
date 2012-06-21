package com.serwylo.mame.controller.client.android.controllers.buttons;

import com.serwylo.mame.controller.client.android.controllers.KeyCodes;

public class DPadButton extends AbstractButton
{

	private int keyCodeLeft = KeyCodes.VK_LEFT;
	private int keyCodeRight = KeyCodes.VK_RIGHT;
	private int keyCodeUp = KeyCodes.VK_UP;
	private int keyCodeDown = KeyCodes.VK_DOWN;

	private int keyCodeUpLeft;
	private int keyCodeUpRight;
	private int keyCodeDownLeft;
	private int keyCodeDownRight;

	private boolean enableDiagonal = true;

	public int getKeyCodeLeft()
	{
		return keyCodeLeft;
	}

	public void setKeyCodeLeft(int keyCodeLeft)
	{
		this.keyCodeLeft = keyCodeLeft;
	}

	public int getKeyCodeRight()
	{
		return keyCodeRight;
	}

	public void setKeyCodeRight(int keyCodeRight)
	{
		this.keyCodeRight = keyCodeRight;
	}

	public int getKeyCodeUp()
	{
		return keyCodeUp;
	}

	public void setKeyCodeUp(int keyCodeUp)
	{
		this.keyCodeUp = keyCodeUp;
	}

	public int getKeyCodeDown()
	{
		return keyCodeDown;
	}

	public void setKeyCodeDown(int keyCodeDown)
	{
		this.keyCodeDown = keyCodeDown;
	}

	public int getKeyCodeUpLeft()
	{
		return keyCodeUpLeft;
	}

	public void setKeyCodeUpLeft(int keyCodeUpLeft)
	{
		this.keyCodeUpLeft = keyCodeUpLeft;
	}

	public int getKeyCodeUpRight()
	{
		return keyCodeUpRight;
	}

	public void setKeyCodeUpRight(int keyCodeUpRight)
	{
		this.keyCodeUpRight = keyCodeUpRight;
	}

	public int getKeyCodeDownLeft()
	{
		return keyCodeDownLeft;
	}

	public void setKeyCodeDownLeft(int keyCodeDownLeft)
	{
		this.keyCodeDownLeft = keyCodeDownLeft;
	}

	public int getKeyCodeDownRight()
	{
		return keyCodeDownRight;
	}

	public void setKeyCodeDownRight(int keyCodeDownRight)
	{
		this.keyCodeDownRight = keyCodeDownRight;
	}

	public boolean isEnableDiagonal()
	{
		return enableDiagonal;
	}

	public void setEnableDiagonal(boolean enableDiagonal)
	{
		this.enableDiagonal = enableDiagonal;
	}
}
