package com.gamefinal.app;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.gamefinal.global.Global;

public class GameFinal extends Applet implements Runnable, MouseMotionListener, KeyListener, MouseListener, MouseWheelListener{

	private static final int MAIN_THREAD_SLEEP = 33;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread mainGameThread = null;
	private boolean mainThreadSuspended = false;
 


	@Override
	public void init() {
		Global.getGlobals().init(this);
		int resolutionX=Global.getGlobals().getResolutionX();
		int resolutionY=Global.getGlobals().getResolutionY();
		Global.getGlobals().setDefaultImage(createImage(16,16));
		
		setSize(resolutionX,resolutionY);
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this); 
		addMouseWheelListener(this);
	
	}

	@Override //Executed when the applet finalizes
	public void destroy() {
		// TODO Auto-generated method stub
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

	@Override //Executed whenever the user switches tabs
	public void stop() {
		//mainThreadSuspended = true;
	}

	@Override
	public void run() {
		generalMainLoop();
	}

	private void generalMainLoop() {
		
		try {
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
				
				
				this.repaint();
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


	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent key) {	
		
		Global.getGlobals().inputEngine.pressKey(key.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent key) {
		Global.getGlobals().inputEngine.releaseKey(key.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
