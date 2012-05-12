package com.gamefinal.app;

import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class GameApplet extends Applet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GameFinal game;
	private JButton launchGameButton;
	private JLabel launchGameStatus;

	@Override
	public void init() {

		launchGameStatus = new JLabel("Press the button to launch game.");
		launchGameButton = new JButton("Launch Game");
		launchGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(game==null) {
            		game = new GameFinal();
            		launchGameStatus.setText("Game launched.");
            	}
            	else
            	{
            		launchGameStatus.setText("Game is already running!");
            	}
            }
        });
		this.add(launchGameButton);
		this.add(launchGameStatus);

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
