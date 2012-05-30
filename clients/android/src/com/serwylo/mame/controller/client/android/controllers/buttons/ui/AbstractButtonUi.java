package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.Button;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;

public abstract class AbstractButtonUi<ButtonType extends AbstractButton> extends Button implements IButtonUi<ButtonType>
{

	public AbstractButtonUi( Context context )
	{
		super( context );
	}

}
