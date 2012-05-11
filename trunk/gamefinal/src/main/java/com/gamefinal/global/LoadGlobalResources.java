package com.gamefinal.global;

import java.awt.Component;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import com.gamefinal.helpers.ImageHelper;

public class LoadGlobalResources extends Thread{

	private MediaTracker mediaTracker;
	private int imageTrackerCount;
	private Toolkit toolkit;

	LoadGlobalResources(Component component) {
		toolkit = Toolkit.getDefaultToolkit();
		imageTrackerCount=0;
		mediaTracker = new MediaTracker(component);
	}
	
	public void run() {
		Global.getGlobals().setLoadingNotDone();
		//TODO load stuff

		loadAllTiles();
		loadAllAnimatedTiles();
		makeTriangleTileSet();
		loadMap();
		waitForResources();
		
	}

	private void loadMap() {
		Global.getGlobals().setGlobalStatus("Loading map");
		Global.getGlobals().worldMap.loadMap();
	}

	private void loadAllTiles() {
		Global.getGlobals().setGlobalStatus("Loading tiles");
		if(Global.getGlobals().tileImages!=null){
			for(int tileIndex=0;tileIndex<Global.getGlobals().tileImages.length;tileIndex++){
				loadTileImage(Global.getGlobals().tileImagesFileNames[tileIndex],tileIndex);
			}
		}
	}
	
	
	private void loadAllAnimatedTiles() {
		Global.getGlobals().setGlobalStatus("Loading animated tiles");
		if(Global.getGlobals().animatedTileImages!=null){
			for(int tileIndexA=0;tileIndexA<Global.getGlobals().animatedTileImages.length;tileIndexA++){
				for(int tileIndexB=0;tileIndexB<Global.getGlobals().animatedTileImages[tileIndexA].length;tileIndexB++){
					if(Global.getGlobals().animatedTileImagesFileNames[tileIndexA][tileIndexB]!=null){
						loadAnimatedTileImage(Global.getGlobals().animatedTileImagesFileNames[tileIndexA][tileIndexB],tileIndexA,tileIndexB);
					}
				}
			}
		}
	}


	private void makeTriangleTileSet() {
		Global.getGlobals().setGlobalStatus("Making triangle tiles");
		if(Global.getGlobals().tileImages!=null){
			for (int tileIndex = 0; tileIndex < Global.getGlobals().tileImages.length; tileIndex++) {
				if (Global.getGlobals().tileImages[tileIndex] != null) {
					Global.getGlobals().triangleTileImages[tileIndex] = ImageHelper.squareToTriangleImage(Global.getGlobals().tileImages[tileIndex]);
				}
			}
		}
	}

	private void waitForResources() {
		Global.getGlobals().setGlobalStatus("Waiting for resources to download.");
		try {
			mediaTracker.waitForAll();
		} catch (InterruptedException error) {
			error.printStackTrace();
			Global.getGlobals().setGlobalStatus("There was an error.");
		}
		Global.getGlobals().setLoadingDone();
	}
	
	public void loadAnimatedTileImage(String imageName,int tileIndexA,int tileIndexB) {
		try{
			Global.getGlobals().animatedTileImages[tileIndexA][tileIndexB] = toolkit.getImage(getClass().getResource(imageName));  
			mediaTracker.addImage(Global.getGlobals().animatedTileImages[tileIndexA][tileIndexB], imageTrackerCount);
			imageTrackerCount++;
		}
		catch(Exception e){
			System.out.println("Could not load "+imageName);
		}
	}

	public void loadTileImage(String imageName,int tileIndex) {
		try{
			Global.getGlobals().tileImages[tileIndex] = toolkit.getImage(getClass().getResource(imageName));  
			mediaTracker.addImage(Global.getGlobals().tileImages[tileIndex], imageTrackerCount);
			imageTrackerCount++;
		}
		catch(Exception e){
			System.out.println("Could not load "+imageName);
		}
	}

}
