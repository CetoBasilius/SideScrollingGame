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
	
	//this are just references from global. we just declare them here to make reading faster
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
	
	private boolean fullScreen = false;
	
	private Camera savedCamera;
	
    public GraphicsEngine(Component imageObserver){
    	resetWindow();
    	
    	this.resolutionX = Global.getGlobals().getGameResolutionX();
    	this.resolutionY = Global.getGlobals().getGameResolutionY();
    	
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
				.getGameHalfResoulutionX(), Global.getGlobals()
				.getGameHalfResoulutionY());
		bufferGraphics.drawString(Global.getGlobals().getGlobalStatus(), Global
				.getGlobals().getGameHalfResoulutionX(), Global.getGlobals()
				.getGameHalfResoulutionY() - 20);
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
	
	public void saveCameraState() {
		savedCamera = gameCamera.clone();
	}
	
	public void reloadCameraState() {
		this.gameCamera = savedCamera.clone();
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
	
	public void toggleFullScreen() {
		int defaultWindowPositionX = Global.getGlobals().getDefaultWindowPositionX();
		int defaultWindowPositionY = Global.getGlobals().getDefaultWindowPositionY();
		
		if(fullScreen==false) {

			//Set Fullscreen On
			Global.getGlobals().frameReference.setVisible(false);
			Global.getGlobals().frameReference.dispose();
			Global.getGlobals().frameReference.setUndecorated(true);
			Global.getGlobals().frameReference.setLocation(0, 0);
			Global.getGlobals().frameReference.setSize(Global.DESKTOP_RESOLUTION);
			Global.getGlobals().frameReference.setVisible(true);
			Global.getGlobals().frameReference.validate();
			Global.getGlobals().frameReference.requestFocus();
			Global.getGlobals().canvasReference.setLocation(defaultWindowPositionX,defaultWindowPositionY);
			Global.getGlobals().canvasReference.requestFocus();
			fullScreen=true;
		}
		else
		{
			resetWindow();
		}
	}
	
	public void resetWindow() {
		int gameResolutionX = Global.getGlobals().getGameResolutionX();
		int gameResolutionY = Global.getGlobals().getGameResolutionY();
		int defaultWindowPositionX = Global.getGlobals().getDefaultWindowPositionX();
		int defaultWindowPositionY = Global.getGlobals().getDefaultWindowPositionY();
		
		//Set Window on
		Global.getGlobals().frameReference.setVisible(false);
		Global.getGlobals().frameReference.dispose();
		Global.getGlobals().frameReference.setUndecorated(false);
		Global.getGlobals().frameReference.setLocation(defaultWindowPositionX,defaultWindowPositionY);
		Global.getGlobals().frameReference.setSize(gameResolutionX+Global.WINDOW_THICKNESS_X,gameResolutionY+Global.WINDOW_THICKNESS_Y);
		Global.getGlobals().frameReference.setVisible(true);
		Global.getGlobals().frameReference.validate();
		Global.getGlobals().frameReference.requestFocus();
		Global.getGlobals().canvasReference.setLocation(0,0);
		Global.getGlobals().canvasReference.requestFocus();
		fullScreen=false;
	}

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void toggleGraphicsDebug() {
		if(graphicsDebug) {
			graphicsDebug=false;
		}
		else
		{
			graphicsDebug=true;
		}
	}
	public Boolean isGraphicsDebugging() {
		return graphicsDebug;
	}

	public void setGraphicsDebug(Boolean graphicsDebug) {
		this.graphicsDebug = graphicsDebug;
	}

	public class Camera implements Cloneable{
		
		private static final int CAMERA_CENTER_CROSSHAIR_SIZE = 20;
		private static final int Y_MAP_STRING_OFFSET = 10;
		private static final int X_MAP_STRING_OFFSET = 6;
		
		public int TILE_SPACING = 32;
		
		private static final float MAX_CAMERA_VELOCITY = 64;
		private static final float CAMERA_FRICTION = 0.2f;
		
		private static final int CAMERA_TILE_TOLERANCE=4;
		
		private int cameraToleranceX;
		private int cameraToleranceY;
		
		private float velocityX;
		private float velocityY;
		
		private int nearestTileX;
		private int farthestTileX;
		
		private int nearestTileY;
		private int farthestTileY;
		
		private float cameraPositionX;
		private float cameraPositionY;
		
		private float finalCameraPositionX;
		private float finalCameraPositionY;
		
		private boolean mapRenderOptimized = true;
		
		private GameObject cameraFollow;
		
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

		private void renderMap() {
			if(isMapRenderOptimized()) {
				renderMapOptimizedByArray();
			}else {
				renderMapByPosition();
			}
			
		}

		private void showGraphicsDebugInfo() {
			renderMapDebug();
			showCameraPosition();
			showCameraCenter();	
			showRecorderState();
		}
		
		public Camera clone() {
			Camera returnedClone = null;
			try {
				returnedClone = (Camera) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return returnedClone;
		}

		private void showRecorderState() {
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
			bufferGraphics.drawString("Camera Position: "+this.getPositionX()+","+this.getPositionY(), 10,resolutionY-40);
			
			int aproximateCameraOverTileX = (int)(this.getPositionX()/TILE_SPACING);
			int aproximateCameraOverTileY = -(int)(this.getPositionY()/TILE_SPACING);
			bufferGraphics.drawString("Camera over tile: "+aproximateCameraOverTileX+","+aproximateCameraOverTileY,10,resolutionY-60);
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
			bufferGraphics.fillRect((int)(object.getWorldPositionX() - finalCameraPositionX),(int)(finalCameraPositionY - object.getWorldPositionY()),3,3);
			bufferGraphics.drawRect((int)(object.getWorldPositionX() - finalCameraPositionX),(int)(finalCameraPositionY - object.getWorldPositionY()), object.getSizeX(), object.getSizeY());
		}
		
		public boolean isGameObjectOnCameraView(GameObject object){
			//Check if object position x is inside camera range x
			if((this.cameraPositionX-Global.getGlobals().getGameHalfResoulutionX())<object.getWorldPositionX()){
				if(object.getWorldPositionX()<(this.cameraPositionX+Global.getGlobals().getGameHalfResoulutionX())){
					//now check object position y
					if((this.cameraPositionY-Global.getGlobals().getGameHalfResoulutionY())<object.getWorldPositionY()){
						if(object.getWorldPositionY()<(this.cameraPositionY+Global.getGlobals().getGameHalfResoulutionY())){
							return true;
						}
					}
				}
			}
			return false;
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
				bufferGraphics.drawImage(tile.getTileImage(), positionX+tile.getOffsetX(), positionY+tile.getOffsetY(),tile.getVisualSizeX(),tile.getVisualSizeY(), observer);
			}
			
	    }
		
		private void renderMapByPosition() {
			bufferGraphics.setFont(debugFont);
			for(int mapLevel=0;mapLevel<Global.getGlobals().worldMap.maxMapVisualLevels;mapLevel++){
				for (int mapXIndex = 0; mapXIndex < Global.getGlobals().worldMap.getMapLenght(); mapXIndex++) {
					for (int mapYIndex = 0; mapYIndex < Global.getGlobals().worldMap.getMapHeight(); mapYIndex++) {
						Tile tile = Global.getGlobals().worldMap.getMapTile(mapLevel,mapXIndex,mapYIndex);
						if(tile.tileHasImage()){
							if(this.isGameObjectOnCameraView(tile)) {
								drawTile((int)((tile.getWorldPositionX()) - finalCameraPositionX), (int)(finalCameraPositionY - (tile.getWorldPositionY())),tile);
							}
						}
					}
				}
			}
			bufferGraphics.setFont(guiFont);
		}
		
		private void renderMapOptimizedByArray(){
			optimizeMapDrawing();
			bufferGraphics.setFont(debugFont);
			for(int mapLevel=0;mapLevel<Global.getGlobals().worldMap.maxMapVisualLevels;mapLevel++){
				for (int mapXIndex = nearestTileX; mapXIndex < farthestTileX; mapXIndex++) {
					for (int mapYIndex = nearestTileY; mapYIndex < farthestTileY; mapYIndex++) {
						Tile tile = Global.getGlobals().worldMap.getMapTile(mapLevel,mapXIndex,mapYIndex);
						drawTile((int)((tile.getWorldPositionX()) - finalCameraPositionX), (int)(finalCameraPositionY - (tile.getWorldPositionY())),tile);
					}
				}
			}
			bufferGraphics.setFont(guiFont);
		}
		
		private void renderMapDebug(){
			bufferGraphics.setFont(debugFont);
			for(int mapLevel=0;mapLevel<Global.getGlobals().worldMap.maxMapVisualLevels;mapLevel++){
				for (int mapXIndex = nearestTileX; mapXIndex < farthestTileX; mapXIndex++) {
					for (int mapYIndex = nearestTileY; mapYIndex < farthestTileY; mapYIndex++) {

						drawGameObjectBounds(Global.getGlobals().worldMap.getMapTile(mapLevel,mapXIndex,mapYIndex));
						bufferGraphics.setColor(Global.getGlobals().worldMap.mapLevelColor[mapLevel]);
						
						Tile tile = Global.getGlobals().worldMap.getMapTile(mapLevel,mapXIndex,mapYIndex);
						
						if (!Global.getGlobals().worldMap.getMapString(mapLevel,mapXIndex,mapYIndex).equals("")) {

							bufferGraphics.drawString(Global.getGlobals().worldMap.getMapString(mapLevel,mapXIndex,mapYIndex).equals("")+","+
									tile.getMaxFrames()+","+
									tile.getCurrentFrame(), X_MAP_STRING_OFFSET + (int)((tile.getWorldPositionX()) - finalCameraPositionX), Y_MAP_STRING_OFFSET + (int)(finalCameraPositionY - (tile.getWorldPositionY())));

							bufferGraphics.drawString(
									tile.getWorldPositionX()+","+
									tile.getWorldPositionY(), X_MAP_STRING_OFFSET + (int)((tile.getWorldPositionX()) - finalCameraPositionX), Y_MAP_STRING_OFFSET*2 + (int)(finalCameraPositionY - (tile.getWorldPositionY()) ));

						}
					}
				}
			}
			bufferGraphics.setFont(guiFont);
		}


		public void setCameraFinalPosition() {
			updateCameraVelocity();
			finalCameraPositionX=(cameraPositionX-Global.getGlobals().getGameHalfResoulutionX());
			finalCameraPositionY=(cameraPositionY+Global.getGlobals().getGameHalfResoulutionY());
		}

		private void updateCameraVelocity() {
			if(cameraFollow==null) {
				updateCameraFriction();
				cameraPositionX+=velocityX;
				cameraPositionY+=velocityY;
			}
			else
			{
				cameraPositionX=cameraFollow.getWorldPositionX();
				cameraPositionY=cameraFollow.getWorldPositionY();
			}
		}
		
		public void setCameraToFollow(GameObject object) {
			cameraFollow = object;
			velocityX=0;
			velocityY=0;
		}
		
		public void freeCameraFollow() {
			cameraFollow = null;
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
			if(this.cameraFollow==null) {
				if(Math.abs(this.velocityX)<MAX_CAMERA_VELOCITY){
					this.velocityX+=velocity;
				}
			}
		}

		public void addVelocityY(float velocity){
			if(this.cameraFollow==null) {
				if(Math.abs(this.velocityY)<MAX_CAMERA_VELOCITY){
					this.velocityY+=velocity;
				}
			}
		}

		public boolean isMapRenderOptimized() {
			return mapRenderOptimized;
		}

		public void toggleMapRender() {
			if(mapRenderOptimized) {
				mapRenderOptimized=false;
			}else {
				mapRenderOptimized=true;
			}
		}

		
	}

	

}
