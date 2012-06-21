package com.serwylo.mame.controller.client.android.controllers;

import com.serwylo.mame.controller.client.android.controllers.buttons.AbstractButton;
import com.serwylo.mame.controller.client.android.controllers.buttons.ArcadeButton;
import java.util.ArrayList;

public class ControllerDefinition
{

	public static final String ORIENTATION_LANDSCAPE = "landscape";
	public static final String ORIENTATION_PORTRAIT = "portrait";

	private ArrayList<AbstractButton> buttonList;
	private String label;
	private int layout;
	private String orientation;

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

	public String getOrientation()
	{
		return orientation;
	}

	public void setOrientation( String orientation )
	{
		this.orientation = orientation;
	}
}