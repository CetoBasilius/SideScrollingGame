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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.gamefinal.global.Global;

public class GameFinal extends Canvas implements MouseMotionListener, KeyListener, MouseListener, MouseWheelListener, Runnable{

	/**
	 * this is the main class file and contains the games main loop
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int WINDOW_THICKNESS_X = 6;
	private static final int WINDOW_THICKNESS_Y = 28;
	private static final int DEFAULT_RESOLUTION_Y = 480;
	private static final int DEFAULT_RESOLUTION_X = 640;
	private static final int MAIN_THREAD_SLEEP = 33;
	public static final long PAINT_THREAD_SLEEP = 33;

	private Thread mainGameThread = null;
	private boolean mainThreadSuspended = false;

	private JFrame mainFrame;
	private JPanel mainPanel;
	
	GraphicsEnvironment graphicsEnvironment;
	GraphicsDevice graphicsDevice;
	
	public GameFinal(){
		init();
		start();
	}

	private void init() {
		getGraphicsDevice();
		createMainPanel();
		buildWindow();
		
		//Here all engines are initiated
		Global.getGlobals().init(this);
		Global.getGlobals().setDefaultImage(createImage(16,16));
		
		prepareWindow();
		addListeners();
	}



	private void getGraphicsDevice() {
		graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
	}

	private void addListeners() {
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this); 
		addMouseWheelListener(this);
	}


	private void prepareWindow() {
		int resolutionX=Global.getGlobals().getResolutionX();
		int resolutionY=Global.getGlobals().getResolutionY();
		
		mainFrame.setSize(new Dimension(resolutionX+WINDOW_THICKNESS_X,resolutionY+WINDOW_THICKNESS_Y));
		this.setSize(resolutionX, resolutionY);
		mainFrame.setResizable(false);

		requestFocus();
	}


	private void buildWindow() {
		
		mainFrame = new JFrame(graphicsDevice.getDefaultConfiguration());
		mainFrame.setSize(DEFAULT_RESOLUTION_X, DEFAULT_RESOLUTION_Y);
		this.setSize(DEFAULT_RESOLUTION_X, DEFAULT_RESOLUTION_Y);

		mainFrame.add(mainPanel);
		mainFrame.setIgnoreRepaint(true);
		this.setIgnoreRepaint(true);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		mainFrame.validate();
		mainFrame.setVisible(true);
	}

	private void createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.DARK_GRAY);
		mainPanel.setLayout(null);
		mainPanel.add(this);
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
		//TODO remove this
		if(arg0.getKeyChar()=='~'){
			Global.getGlobals().graphicsEngine.setFullScreen(mainFrame,mainPanel,this);
		}
	
		Global.getGlobals().gameConsole.update(arg0);
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {
		
	}

}
