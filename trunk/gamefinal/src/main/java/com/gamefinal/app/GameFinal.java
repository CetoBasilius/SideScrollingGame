package com.gamefinal.app;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.gamefinal.global.Global;

public class GameFinal extends Canvas implements MouseMotionListener, KeyListener, MouseListener, MouseWheelListener, Runnable{

	/**
	 * this is the main class file and contains the games main loop
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int MAIN_THREAD_SLEEP = 33;
	private static final long PAINT_THREAD_SLEEP = 33;

	private Thread mainGameThread = null;
	private boolean mainThreadSuspended = false;

	GraphicsEnvironment graphicsEnvironment;
	GraphicsDevice graphicsDevice;
	
	public JFrame mainFrame;
	public JPanel mainPanel;
	public Canvas mySelf;
	
	public GameFinal(){
		init();
		start();
	}

	private void init() {
		setUpWindow();

		//Here all engines are initiated
		Global.getGlobals().init(mainFrame,mainPanel,mySelf);
		Global.getGlobals().setDefaultImage(createImage(16,16));
		
		adjustWindow();
		addListeners();
	}
	
	private void addListeners() {
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this); 
		addMouseWheelListener(this);
	}

	private void adjustWindow() {
		int resolutionX=Global.getGlobals().getGameResolutionX();
		int resolutionY=Global.getGlobals().getGameResolutionY();
		int windowPositionX = Global.getGlobals().getDefaultWindowPositionX();
		int windowPositionY = Global.getGlobals().getDefaultWindowPositionY();
		
		mainFrame.setSize(new Dimension(resolutionX+Global.WINDOW_THICKNESS_X,resolutionY+Global.WINDOW_THICKNESS_Y));
		mainFrame.setResizable(false);
		
		mainFrame.setLocation(windowPositionX, windowPositionY);
		
		mySelf.setSize(resolutionX, resolutionY);
		
		mainFrame.requestFocus();
		mySelf.requestFocus();
	}



	private void setUpWindow() {
		mySelf = this;
		
		graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
		
		mainFrame = new JFrame(graphicsDevice.getDefaultConfiguration());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(false);
		mainFrame.dispose();
		mainFrame.setSize(Global.DEFAULT_GAME_RESOLUTION_X, Global.DEFAULT_GAME_RESOLUTION_Y);
		
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.DARK_GRAY);
		mainPanel.setLayout(null);
		mainPanel.add(mySelf);
		
		mySelf.setSize(Global.DEFAULT_GAME_RESOLUTION_X, Global.DEFAULT_GAME_RESOLUTION_Y);
		mySelf.setIgnoreRepaint(true);

		mainFrame.add(mainPanel);
		mainFrame.validate();
		mainFrame.setVisible(true);
		mainFrame.requestFocus();
	}

	

	public void start() {
		createMainThread();
	}

	private void createMainThread() {
		if (mainGameThread == null) {
			mainGameThread = new Thread(this);
			//threadSuspended = false;
			mainGameThread.start();
		} else {
			if (mainThreadSuspended) {
				mainThreadSuspended = false;
				synchronized (this) {
					notify();
				}
			}
		}
	}


	public void run() {
		generalMainLoop();
	}
	
	
	//TODO ask sensei about this thread
	public class PaintThread extends Thread{
		public PaintThread(){
			
		}

		public void run(){
			while(true){

				repaint();

				try {
					Thread.sleep(PAINT_THREAD_SLEEP);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void generalMainLoop() {	
		try {
			PaintThread painter = new PaintThread();
			painter.start();
			while(true){
				
				Global.getGlobals().inputEngine.update();
				Global.getGlobals().graphicsEngine.measureFrames();
                
				if(Global.getGlobals().isLoadingDone()){
					doGameLogic();
				}

				if (mainThreadSuspended) {
					synchronized (this) {
						while (mainThreadSuspended) {
							wait();
						}
					}
				}
				Thread.sleep(MAIN_THREAD_SLEEP);
			}
		}
		catch(Exception e)
		{

		}
	}
	
	private void doGameLogic(){
		//TODO add game logic
	}

	@Override
	public void paint(Graphics graphicsObject) {
		Global.getGlobals().graphicsEngine.renderAll(graphicsObject);
	}
	
	@Override
	public void update(Graphics graphicsObject) {
        paint(graphicsObject);
    }

	public void mouseWheelMoved(MouseWheelEvent arg0) {
		
	}

	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

	public void keyPressed(KeyEvent key) {	
		Global.getGlobals().inputEngine.pressKey(key.getKeyCode());
	}

	public void keyReleased(KeyEvent key) {
		Global.getGlobals().inputEngine.releaseKey(key.getKeyCode());
	}

	public void keyTyped(KeyEvent arg0) {
		Global.getGlobals().gameConsole.update(arg0);
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {
		
	}

}
