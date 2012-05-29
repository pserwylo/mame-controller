package com.serwylo.mame.controller.client.android.controllers;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import java.util.ArrayList;

public class ControllerDefinition
{

	public static final int LAYOUT_HORIZONTAL = 1;
	public static final int LAYOUT_VERTICAL = 2;

	private ArrayList<AbstractButton> buttonList;
	private String label;
	private int layout;

	public ControllerDefinition()
	{
		this.buttonList = new ArrayList<AbstractButton>();
	}

	public ControllerDefinition setLabel( String label )
	{
		this.label = label;
		return this;
	}

	public String getLabel()
	{
		return this.label;
	}
	
	public ArrayList<AbstractButton> getButtonList()
	{
		return this.buttonList;
	}

	public ControllerDefinition setButtonList( ArrayList<AbstractButton> buttonList )
	{
		this.buttonList = buttonList;
		return this;
	}

}