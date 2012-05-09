package com.gamefinal.engines;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

import com.gamefinal.app.GameObject;
import com.gamefinal.app.Tile;
import com.gamefinal.global.Console;
import com.gamefinal.global.Global;


public class GraphicsEngine {
	
	private Boolean graphicsDebug = false;

	private Graphics bufferGraphics;
	private Image offScreen;
	private ImageObserver observer;
	
	private int resolutionX;
	private int resolutionY;
	
	private int lastFrameRate = 30;
	public int frames = 0;
	public int oldFrames = 0;
	public Long frameRateMeasure;
	public Long frameRateNextSecond;
	
	private Font guiFont = new Font("Arial", Font.BOLD, 12);
	private Font debugFont = new Font("Arial", Font.BOLD, 10);
	public Camera gameCamera;
	
	
    public GraphicsEngine(Component imageObserver){
    	this.resolutionX = Global.getGlobals().getResolutionX();
    	this.resolutionY = Global.getGlobals().getResolutionY();
    	
    	gameCamera = new Camera();
    	
		observer = imageObserver;

		imageObserver.setBackground(Color.black);
		offScreen = imageObserver.createImage(resolutionX, resolutionY);
		offScreen.setAccelerationPriority(1);
		
		bufferGraphics = offScreen.getGraphics();
		
		bufferGraphics.setFont(guiFont);
		bufferGraphics.setColor(Color.white);

		frameRateMeasure = System.currentTimeMillis();
		frameRateNextSecond = frameRateMeasure + 1000;
	}

	public void measureFrames() {
		frameRateMeasure = System.currentTimeMillis();
		if (frameRateMeasure >= frameRateNextSecond) {
			frameRateNextSecond = frameRateMeasure +1000;//1000 milliseconds
        	setLastFrameRate(frames);
        	frames=0;
		}
	}

	public void renderAll(Graphics graphicsObject) {
		resetScreen();
		
		//Graphics2D g2d = (Graphics2D)bufferGraphics;
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
		
		if (Global.getGlobals().isLoadingDone()) {
			gameCamera.update();
		} else {
			drawLoadingScreen();
		}
		drawBackBuffer(graphicsObject);
		frames++;
		
		
	}

	private void drawLoadingScreen() {
		bufferGraphics.drawString("Loading", Global.getGlobals()
				.getHalfResoulutionX(), Global.getGlobals()
				.getHalfResoulutionY());
		bufferGraphics.drawString(Global.getGlobals().getGlobalStatus(), Global
				.getGlobals().getHalfResoulutionX(), Global.getGlobals()
				.getHalfResoulutionY() - 20);
	}

	private void drawBackBuffer(Graphics graphicsObject) {
		graphicsObject.drawImage(offScreen, 0, 0, observer);
	}

	private void resetScreen() {
		bufferGraphics.clearRect(0, 0, resolutionX, resolutionY);
		bufferGraphics.setColor(Color.white);
	}

	public int getFPS() {
		return lastFrameRate;
	}

	private void setLastFrameRate(int lastFrameRate) {
		this.lastFrameRate = lastFrameRate;
	}
	
	
	
	public void drawImageAlpha(Image alphaImage,int posX,int posY,Image offscreenImage,ImageObserver caller,float Alpha){

		Graphics2D g2d = (Graphics2D) offscreenImage.getGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Alpha));
		g2d.drawImage(alphaImage,posX,posY, caller);
		
		//return things to normal
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
	}
	
	public void drawImageAlphaRotated(Image alphaImage,int posX,int posY,float angle,Image offscreenImage,ImageObserver caller,float Alpha){

		Graphics2D g2d = (Graphics2D) offscreenImage.getGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Alpha));
	
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.translate (posX,posY);
		//g2d.fillRect((int)affineTransform.getTranslateX(), (int)affineTransform.getTranslateY(), 2, 2);
		affineTransform.translate (-alphaImage.getWidth(caller)/2,-alphaImage.getHeight(caller)/2);
		
		affineTransform.rotate(Math.toRadians(angle), alphaImage.getWidth(caller)/2, alphaImage.getHeight(caller)/2); 
		g2d.drawImage(alphaImage, affineTransform, caller);
		
		//return things to normal
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
				
	}
	
	public void drawImageAlphaRotatedScaled(Image alphaImage,int posX,int posY,float angle,float scale,Image offscreenImage,ImageObserver caller,float Alpha,Graphics destGraphics){

		//Graphics2D g2d = (Graphics2D) offscreenImage.getGraphics();
		Graphics2D g2d = (Graphics2D)destGraphics;
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Alpha));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Alpha));
		
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.translate (posX,posY);
		affineTransform.scale(scale, scale);
		////g2d.fillRect((int)affineTransform.getTranslateX(), (int)affineTransform.getTranslateY(), 2, 2);
		
		affineTransform.translate (-alphaImage.getWidth(caller)/2,-alphaImage.getHeight(caller)/2);
		affineTransform.rotate(Math.toRadians(angle), alphaImage.getWidth(caller)/2, alphaImage.getHeight(caller)/2); 

		g2d.drawImage(alphaImage, affineTransform, caller);
		
		//return things to normal
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
		
	}
	

	public class Camera{
		
		private static final int CAMERA_CENTER_CROSSHAIR_SIZE = 20;
		private static final int Y_MAP_STRING_OFFSET = 18;
		private static final int X_MAP_STRING_OFFSET = 12;
		public int TILE_SPACING = 32;
		
		private static final float MAX_CAMERA_VELOCITY = 64;
		private static final float CAMERA_FRICTION = 0.2f;
		
		private static final int CAMERA_TILE_TOLERANCE=4;
		
		private int cameraToleranceX;
		private int cameraToleranceY;
		
		private float velocityX=0.0f;
		private float velocityY=0.0f;
		
		private int nearestTileX;
		private int farthestTileX;
		
		private int nearestTileY;
		private int farthestTileY;
		
		
		private float cameraPositionX;
		private float cameraPositionY;
		
		private float finalCameraPositionX;
		private float finalCameraPositionY;
		
		private Camera(){
			cameraPositionX=0;
			cameraPositionY=0;
			
			cameraToleranceX=CAMERA_TILE_TOLERANCE;
			cameraToleranceY=CAMERA_TILE_TOLERANCE;
		}

		public void update(){
			setCameraFinalPosition();
			//TODO here we can draw anything we want

			renderMap();
			
			if(graphicsDebug){showGraphicsDebugInfo();}
			showFramesPerSecond();
			renderConsole();
		}

		private void showGraphicsDebugInfo() {
			renderMapDebug();
			showCameraPosition();
			showCameraCenter();	
			
			//TODO remove this, its just a test
			bufferGraphics.drawString("recording: "+Global.getGlobals().inputEngine.inputRecorder.getCurrentSlot(), resolutionX-100,resolutionY-40);
			
		}

		private void renderConsole() {
			Console gameConsole = Global.getGlobals().gameConsole;
			if(gameConsole.isConsoleActive()) {
				bufferGraphics.drawString(gameConsole.getInputLinePrefix()+gameConsole.getConsoleInputLine(),gameConsole.getInputPositionX(),gameConsole.getInputPositionY());
			}
			for(int consoleLogIndex = 0;consoleLogIndex<Console.getMaxConsoleLines();consoleLogIndex++) {
				bufferGraphics.drawString(gameConsole.getLine(consoleLogIndex),gameConsole.getLogPositionX(),gameConsole.getLogLinePositionY(consoleLogIndex));
			}
		}

		private void showCameraCenter() {
			bufferGraphics.setColor(Color.white);
			bufferGraphics.drawLine(resolutionX/2 -CAMERA_CENTER_CROSSHAIR_SIZE, resolutionY/2, resolutionX/2 +CAMERA_CENTER_CROSSHAIR_SIZE, resolutionY/2);
			bufferGraphics.drawLine(resolutionX/2, resolutionY/2 +CAMERA_CENTER_CROSSHAIR_SIZE, resolutionX/2, resolutionY/2 -CAMERA_CENTER_CROSSHAIR_SIZE);
		}

		private void showCameraPosition() {
			bufferGraphics.setColor(Color.white);
			bufferGraphics.drawString("Camera Position: "+this.getPositionX()+","+this.getPositionY(), 10,resolutionY-20);
		}

		private void showFramesPerSecond() {
			bufferGraphics.setColor(Color.white);
			bufferGraphics.drawString("FPS:"+getFPS(), resolutionX-60,resolutionY-20);
		}
		
		public void optimizeMapDrawing()
		{
			optimizeMapDrawingX();
			optimizeMapDrawingY();
		}
		
		
		public void drawGameObjectBounds(GameObject object){
			bufferGraphics.fillRect((int)(object.getWorldPositionX() - finalCameraPositionX),(int)(object.getWorldPositionY() + finalCameraPositionY),3,3);
			bufferGraphics.drawRect((int)(object.getWorldPositionX() - finalCameraPositionX),(int)(object.getWorldPositionY() + finalCameraPositionY), object.getSizeX(), object.getSizeY());
		}

		private void optimizeMapDrawingX() {
			//View Optimization X
			nearestTileX = (int)(finalCameraPositionX)/TILE_SPACING;
			nearestTileX-=cameraToleranceX;
			if(nearestTileX<0){nearestTileX=0;}
			if(nearestTileX>Global.getGlobals().worldMap.getMapLenght()){nearestTileX=Global.getGlobals().worldMap.getMapLenght()-1;}
			farthestTileX = (int)(finalCameraPositionX+resolutionX)/TILE_SPACING;
			farthestTileX+=cameraToleranceX;
			if(farthestTileX<0){farthestTileX=0;}
			if(farthestTileX>Global.getGlobals().worldMap.getMapLenght()){farthestTileX=Global.getGlobals().worldMap.getMapLenght();}
		}

		private void optimizeMapDrawingY() {
			//View Optimization Y
			nearestTileY = (int)-(finalCameraPositionY)/TILE_SPACING;
			nearestTileY-=cameraToleranceY;
			if(nearestTileY<0){nearestTileY=0;}
			if(nearestTileY>Global.getGlobals().worldMap.getMapHeight()){nearestTileY=Global.getGlobals().worldMap.getMapHeight()-1;}
			farthestTileY = (int)(-finalCameraPositionY+resolutionY)/TILE_SPACING;
			farthestTileY+=cameraToleranceY;
			if(farthestTileY<0){farthestTileY=0;}
			if(farthestTileY>Global.getGlobals().worldMap.getMapHeight()){farthestTileY=Global.getGlobals().worldMap.getMapHeight();}
		}
		
		public void drawTile(int positionX, int positionY, Tile tile) {
			tile.update();
			if(tile.tileHasImage()){
				bufferGraphics.drawImage(tile.getTileImage(), positionX+tile.getOffsetX(), positionY+tile.getOffsetY(),tile.getVisualX(),tile.getVisualY(), observer);
			}
			
	    }
		
		private void renderMap(){
			optimizeMapDrawing();
			bufferGraphics.setFont(debugFont);
			for(int mapLevel=0;mapLevel<Global.getGlobals().worldMap.mapTiles.length;mapLevel++){
				for (int mapYIndex = nearestTileY; mapYIndex < farthestTileY; mapYIndex++) {
					for (int mapXIndex = nearestTileX; mapXIndex < farthestTileX; mapXIndex++) {
						drawTile((int)((mapXIndex*TILE_SPACING) - finalCameraPositionX), (int)((mapYIndex*TILE_SPACING) + finalCameraPositionY),Global.getGlobals().worldMap.mapTiles[mapLevel][mapYIndex][mapXIndex]);
					}
				}
			}
			bufferGraphics.setFont(guiFont);
		}
		
		private void renderMapDebug(){
			bufferGraphics.setFont(debugFont);
			for(int mapLevel=0;mapLevel<Global.getGlobals().worldMap.mapString.length;mapLevel++){
				for (int mapYIndex = nearestTileY; mapYIndex < farthestTileY; mapYIndex++) {
					for (int mapXIndex = nearestTileX; mapXIndex < farthestTileX; mapXIndex++) {
						drawGameObjectBounds(Global.getGlobals().worldMap.mapTiles[mapLevel][mapYIndex][mapXIndex]);
						bufferGraphics.setColor(Global.getGlobals().worldMap.mapLevelColor[mapLevel]);
						if (!Global.getGlobals().worldMap.mapString[mapLevel][mapYIndex][mapXIndex].equals("")) {

							bufferGraphics.drawString(Global.getGlobals().worldMap.mapString[mapLevel][mapYIndex][mapXIndex]+","+
									Global.getGlobals().worldMap.mapTiles[mapLevel][mapYIndex][mapXIndex].getMaxFrames()+","+
									Global.getGlobals().worldMap.mapTiles[mapLevel][mapYIndex][mapXIndex].getCurrentFrame(), X_MAP_STRING_OFFSET + (int)((mapXIndex * TILE_SPACING) - finalCameraPositionX), Y_MAP_STRING_OFFSET + (int)((mapYIndex * TILE_SPACING) + finalCameraPositionY));

						}
					}
				}
			}
			bufferGraphics.setFont(guiFont);
		}


		public void setCameraFinalPosition() {
			updateCameraVelocity();
			finalCameraPositionX=(cameraPositionX-Global.getGlobals().getHalfResoulutionX());
			finalCameraPositionY=(cameraPositionY+Global.getGlobals().getHalfResoulutionY());
		}

		private void updateCameraVelocity() {
			updateCameraFriction();
			cameraPositionX+=velocityX;
			cameraPositionY+=velocityY;

		}

		private void updateCameraFriction() {
			updateCameraFrictionX();
			updateCameraFrictionY();
		}

		private void updateCameraFrictionX() {
			if(velocityX>0){
				velocityX-=CAMERA_FRICTION;
				if(velocityX<0){velocityX=0;}
			}
			
			if(velocityX<0){
				velocityX+=CAMERA_FRICTION;
				if(velocityX>0){velocityX=0;}
			}
		}
		
		private void updateCameraFrictionY() {
			if(velocityY>0){
				velocityY-=CAMERA_FRICTION;
				if(velocityY<0){velocityY=0;}
			}
			
			if(velocityY<0){
				velocityY+=CAMERA_FRICTION;
				if(velocityY>0){velocityY=0;}
			}
		}

		public float getPositionX() {
			return cameraPositionX;
		}

		public void setPositionX(int positionX) {
			this.cameraPositionX = positionX;
		}
		
		public void moveCameraHorizontal(int distance) {
			this.cameraPositionX+=distance;
		}
		
		public void moveCameraVertical(int distance) {
			this.cameraPositionY+=distance;
		}

		public float getPositionY() {
			return cameraPositionY;
		}

		public void setPositionY(int positionY) {
			this.cameraPositionY = positionY;
		}

		public void addVelocityX(float velocity){
			if(Math.abs(this.velocityX)<MAX_CAMERA_VELOCITY){
				this.velocityX+=velocity;
			}
		}

		public void addVelocityY(float velocity){
			if(Math.abs(this.velocityY)<MAX_CAMERA_VELOCITY){
				this.velocityY+=velocity;
			}
		}
	}

}
