package com.serwylo.mame.controller.server.events;

import com.serwylo.mame.controller.shared.Event;

public interface IMameEventListener
{

	public void receiveEvent( Event event );

}
