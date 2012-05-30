package com.serwylo.mame.controller.client.android.controllers.buttons.ui;

import android.view.View;
import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;

public interface IButtonUi<ButtonType extends AbstractButton>
{

	public abstract void setButton( ButtonType button );

	public abstract ButtonType getButton();

}
