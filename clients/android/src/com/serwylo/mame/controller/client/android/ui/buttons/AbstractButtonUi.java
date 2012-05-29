package com.serwylo.mame.controller.client.android.ui.buttons;

import android.content.Context;
import android.widget.Button;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;

public abstract class AbstractButtonUi<ButtonType extends AbstractButton> extends Button
{

	public AbstractButtonUi( Context context )
	{
		super( context );
	}

	public abstract ButtonType getButton();

}
