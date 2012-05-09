package com.gamefinal.app;


import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
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

	private static final int WINDOW_THICKNESS_X = 16;
	private static final int WINDOW_THICKNESS_Y = 38;
	private static final int DEFAULT_RESOLUTION_Y = 480;
	private static final int DEFAULT_RESOLUTION_X = 640;
	private static final int MAIN_THREAD_SLEEP = 33;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final long PAINT_THREAD_SLEEP = 33;
	private Thread mainGameThread = null;
	private boolean mainThreadSuspended = false;
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	
	//TODO actually use this if using page flipping
	//@SuppressWarnings("unused")
	//private Graphics2D gameGraphics;
	//private BufferStrategy gameBufferStrategy;

	public GameFinal(){
		init();
		start();
	}

	
	private void init() {
		buildWindow();
		
		Global.getGlobals().init(this);
		int resolutionX=Global.getGlobals().getResolutionX();
		int resolutionY=Global.getGlobals().getResolutionY();
		Global.getGlobals().setDefaultImage(createImage(16,16));
		
		prepareWindow(resolutionX, resolutionY);
		addListeners();

	}


	private void addListeners() {
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this); 
		addMouseWheelListener(this);
	}


	private void prepareWindow(int resolutionX, int resolutionY) {
		mainFrame.setSize(new Dimension(resolutionX+WINDOW_THICKNESS_X,resolutionY+WINDOW_THICKNESS_Y));
		mainPanel.setSize(new Dimension(resolutionX,resolutionY));
		this.setSize(resolutionX, resolutionY);
		mainFrame.setResizable(false);

		requestFocus();
		createBufferStrategy(2);
		//gameBufferStrategy = getBufferStrategy();
		//TODO use this if necessary (Page flipping)
		//gameGraphics = (Graphics2D) this.getBufferStrategy().getDrawGraphics();
	}


	private void buildWindow() {
		mainFrame = new JFrame(""+serialVersionUID);
		
		mainPanel = new JPanel();
		mainPanel.setSize(new Dimension(DEFAULT_RESOLUTION_X,DEFAULT_RESOLUTION_Y));
		mainPanel.setLayout(new FlowLayout());
		
		setSize(DEFAULT_RESOLUTION_X, DEFAULT_RESOLUTION_Y);
		
		mainPanel.add(this);	
		mainFrame.add(mainPanel);
		this.setIgnoreRepaint(true);
		mainFrame.pack();
		mainFrame.setVisible(true);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
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
				//repaint();
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
		// TODO Auto-generated method stub
		
	}


	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
