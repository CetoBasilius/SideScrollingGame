package com.gamefinal.app;

import java.applet.Applet;

public class GameApplet extends Applet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void init() {
		@SuppressWarnings("unused")
		GameFinal game = new GameFinal();
	}

	@Override //Executed when the applet finalizes
	public void destroy() {
		// TODO Auto-generated method stub
	}


	@Override //Executed whenever the user switches tabs
	public void stop() {
		//mainThreadSuspended = true;
	}

}
