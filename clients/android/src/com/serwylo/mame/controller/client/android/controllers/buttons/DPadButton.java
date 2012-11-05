package com.serwylo.mame.controller.client.android.controllers.buttons;

import com.serwylo.mame.controller.client.android.controllers.KeyCodes;

abstract public class DPadButton extends AbstractButton
{

	public abstract static class Directional extends AbstractButton
	{

		public static final int DIR_UP = 1;
		public static final int DIR_RIGHT = 2;
		public static final int DIR_DOWN = 4;
		public static final int DIR_LEFT = 8;

		public static final int DIR_UP_LEFT = DIR_UP | DIR_LEFT;
		public static final int DIR_UP_RIGHT = DIR_UP | DIR_RIGHT;
		public static final int DIR_DOWN_LEFT = DIR_DOWN | DIR_LEFT;
		public static final int DIR_DOWN_RIGHT = DIR_DOWN | DIR_RIGHT;

		private int direction;

		public Directional( int direction )
		{
			this.direction = direction;
		}

		public int getDirection()
		{
			return this.direction;
		}

		/**
		 * Checks if this button points in the direction 'dir'.
		 * For example, the following three buttons all point in the direction DIR_UP:
		 *  - DIR_UP
		 *  - DIR_UP_LEFT
		 *  - DIR_UP_RIGHT
		 * @param dir
		 * @return
		 */
		public boolean includesDirection( int dir )
		{
			return ( ( this.direction & dir ) == dir );
		}

	}

	public static class Up extends Directional
	{
		public Up()
		{
			super( DIR_UP );
			this.setKeyCode( KeyCodes.VK_UP );
		}
	}

	public static class Right extends Directional
	{
		public Right()
		{
			super( DIR_RIGHT );
			this.setKeyCode( KeyCodes.VK_RIGHT );
		}
	}

	public static class Down extends Directional
	{
		public Down()
		{
			super( DIR_DOWN );
			this.setKeyCode( KeyCodes.VK_DOWN );
		}
	}

	public static class Left extends Directional
	{
		public Left()
		{
			super( DIR_LEFT );
			this.setKeyCode( KeyCodes.VK_LEFT );
		}
	}

	public static class UpLeft extends Directional
	{
		public UpLeft()
		{
			super( DIR_UP_LEFT );
			// this.setKeyCode( KeyCodes.VK_LEFT );
		}
	}

	public static class DownLeft extends Directional
	{
		public DownLeft()
		{
			super( DIR_DOWN_LEFT );
			// this.setKeyCode( KeyCodes.VK_LEFT );
		}
	}

	public static class UpRight extends Directional
	{
		public UpRight()
		{
			super( DIR_UP_RIGHT );
			// this.setKeyCode( KeyCodes.VK_LEFT );
		}
	}

	public static class DownRight extends Directional
	{
		public DownRight()
		{
			super( DIR_DOWN_RIGHT );
			// this.setKeyCode( KeyCodes.VK_LEFT );
		}
	}

}
