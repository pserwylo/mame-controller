package com.serwylo.mame.controller.client.android.views.buttons;


import android.graphics.*;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.views.ControllerView;

/**
 * Wrapper around a button which keeps track of its drawing assets, its pressed state, and other misc stuff required
 * for the ControllerView.
 * @param <ButtonType>
 */
abstract public class ButtonView<ButtonType extends AbstractButton>
{
	protected Bitmap bitmap;
	protected ButtonType button;
	protected Rect bounds;
	private boolean pressed;

	/**
	 * Default implementation just dumps {@link ButtonView#bitmap} to the screen according to the position of
	 * {@link ButtonView#bounds} (perhaps with an offset).
	 * @param canvas
	 */
	public void draw( Canvas canvas )
	{
		Paint paint = new Paint();

		float left = this.getBounds().left;
		float top = this.getBounds().top;

		if ( this.isPressed() )
		{
			left += ControllerView.PRESS_OFFSET;
			top += ControllerView.PRESS_OFFSET;
		}

		canvas.drawBitmap( this.getBitmap(), left, top, paint );
	}

	protected final Bitmap getBitmap()
	{
		return this.bitmap;
	}

	public final ButtonType getButton()
	{
		return this.button;
	}

	/**
	 * Return the bounds with associated padding on all sides.
	 * This padded version of getBounds helps when invalidating the screen area around a pressed button.
	 * @param padding
	 * @return
	 */
	public final Rect getBounds( int padding )
	{
		Rect bounds = new Rect( this.bounds );
		bounds.top -= padding;
		bounds.right += padding;
		bounds.bottom += padding;
		bounds.left -= padding;
		return bounds;
	}

	public final Rect getBounds()
	{
		return this.bounds;
	}

	/**
	 * To be called from the base class, to initialise the bounds of the button.
	 * Both the default implementation of {@link com.serwylo.mame.controller.client.android.views.ControllerView.ButtonView#contains(android.graphics.Point)} and the invalidate
	 * code in tne {@link ControllerView} make use of this bounds.
	 */
	protected void calcBounds()
	{
		this.bounds = new Rect();
		this.bounds.left = this.button.getX() - this.bitmap.getWidth() / 2;
		this.bounds.right = this.button.getX() + this.bitmap.getWidth() / 2;
		this.bounds.top = this.button.getY() - this.bitmap.getHeight() / 2;
		this.bounds.bottom = this.button.getY() + this.bitmap.getHeight() / 2;
	}

	/**
	 * Used to see if a pointer is within the bounds of this button.
	 * Default implementation just checks {@link com.serwylo.mame.controller.client.android.views.ControllerView.ButtonView#bounds}, but you are encouraged to be more specific
	 * in the base classes.
	 * @param point
	 * @return
	 */
	public boolean contains( Point point )
	{
		return this.bounds.contains( point.x, point.y );
	}

	public void setPressed( boolean pressed )
	{
		this.pressed = pressed;
	}

	public final boolean isPressed()
	{
		return this.pressed;
	}
}
