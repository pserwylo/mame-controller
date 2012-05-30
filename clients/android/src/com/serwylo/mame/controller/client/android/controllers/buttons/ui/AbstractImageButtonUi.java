package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;

public abstract class AbstractImageButtonUi<ButtonType extends AbstractButton> extends ImageButton implements IButtonUi<ButtonType>
{

	public AbstractImageButtonUi(Context context)
	{
		super( context );
	}
	
	@Override
	public abstract Drawable getDrawable();

}
