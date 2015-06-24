package com.gamefinal.app;

import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameApplet extends Applet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GameFinal game;
	private JButton launchGameButton;
	private JLabel launchGameStatus;
	private JFrame gameFrame;

	@Override
	public void init() {

		launchGameStatus = new JLabel("Press the button to launch game.");
		launchGameButton = new JButton("Launch Game");
		launchGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(game==null) {
            		game = new GameFinal();
            		gameFrame = (JFrame) game.getParent().getParent().getParent().getParent().getParent();
            		gameFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            		
            		gameFrame.addWindowListener(new WindowAdapter() {
                         public void windowClosing(WindowEvent e) {
                                init();
                         }
            		});

            		
            		launchGameStatus.setText("Game launched.");
            	}
            	else
            	{
            		gameFrame.setVisible(true);
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
