package com.gamefinal.app;

import java.awt.Image;

public class Tile extends GameObject {

	private boolean tileHasImage = false;
	private Image tileImages[];
	private int currentStepsToNextFrame = 0;
	private int frameStepsToNextFrame = 2;
	private int currentFrame = 0;
	
	private int offsetX = 0;
	private int offsetY = 0;
	
	private int maxFrames=0;
	
	private int invertedXFlag = 1;//1 not inverted,-1 inverted
	private int invertedYFlag = 1;
	
	public Tile(Image setImage[]){
		super();
		tileImages = new Image[setImage.length];
		tileImages = setImage.clone();
		checkImage();
	}
	
	public Tile(Image setImage){
		super();
		tileImages = new Image[1];
		tileImages[0] = setImage;
		checkImage();	
	}

	private void checkImage() {
		if(tileImages!=null){
			if(tileImages[0]!=null){
				setSize(tileImages[0].getWidth(null),tileImages[0].getHeight(null));
				tileHasImage=true;
				for(int a =0;a<tileImages.length;a++){
					if(tileImages[a]!=null){maxFrames++;}
				}
			}
			else
			{
				tileHasImage=false;
			}
			
		}
		else
		{
			tileHasImage=false;
		}
	}
	
	public Tile(){
		checkImage();
	}

	public void update(){
		super.update();
		if(tileHasImage){
			currentStepsToNextFrame++;
			if(currentStepsToNextFrame>=frameStepsToNextFrame){
				currentStepsToNextFrame=0;
				currentFrame++;
				if(currentFrame>=tileImages.length || tileImages[currentFrame]==null){
					currentFrame=0;
				}
			}
		}
	}
	
	public Image getTileImage(){
		return tileImages[currentFrame];
	}

	public boolean tileHasImage() {
		return tileHasImage;
	}

	public int isInvertedXFlag() {
		return invertedXFlag;
	}

	public void setInvertedXFlag(int invertedXFlag) {
		if(invertedXFlag>0){
			this.invertedXFlag=1;
		}else{
			this.invertedXFlag=-1;
			offsetX = this.getSizeX();
		}
	}
	
	public int isInvertedYFlag() {
		return invertedYFlag;
	}

	public void setInvertedYFlag(int invertedYFlag) {
		if(invertedYFlag>0){
			this.invertedYFlag=1;
		}else{
			this.invertedYFlag=-1;
			offsetY = this.getSizeY();
		}
	}

	public int getVisualY() {
		return getSizeY()*invertedYFlag;
	}
	
	public int getVisualX() {
		return getSizeX()*invertedXFlag;
	}

	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public int getMaxFrames(){
		return maxFrames;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

}
