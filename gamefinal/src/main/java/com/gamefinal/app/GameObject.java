package com.gamefinal.app;

public class GameObject {
	private int worldPositionX;
	private int worldPositionY;
	
	private int sizeX;
	private int sizeY;
	
	public GameObject(){
		setWorldPositionX(0);
		setWorldPositionY(0);
		
		setSize(1,1);
	}

	public void setSize(int sizeX,int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public int getWorldPositionX() {
		return worldPositionX;
	}

	public void setWorldPositionX(int worldPositionX) {
		this.worldPositionX = worldPositionX;
	}

	public int getWorldPositionY() {
		return worldPositionY;
	}

	public void setWorldPositionY(int worldPositionY) {
		this.worldPositionY = worldPositionY;
	}
	
	public void setWorldPosition(int worldPositionY,int worldPositionX) {
		this.worldPositionX = worldPositionX;
		this.worldPositionY = worldPositionY;

	}
}
