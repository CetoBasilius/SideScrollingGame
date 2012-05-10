package com.gamefinal.app;

public class GameObject {
	private float friction = 0;
	
	private int worldPositionX;
	private int worldPositionY;
	
	private float velocityX;
	private float velocityY;
	
	private int sizeX;
	private int sizeY;
	
	private boolean objectResting;
	
	public GameObject(){
		setWorldPositionX(0);
		setWorldPositionY(0);
		
		velocityX=0;
		velocityY=0;
		
		objectResting = true;
		
		setSize(1,1);
	}
	
	public void update() {
		if(!objectResting) {
			updateVelocity();
		}
	}

	private void updateVelocity() {
		updateFriction();
		worldPositionX+=velocityX;
		worldPositionY+=velocityY;
	}
	
	private void updateFriction() {
		updateFrictionX();
		updateFrictionY();
	}

	private void updateFrictionX() {
		if(velocityX>0){
			velocityX-=getFriction();
			if(velocityX<0){velocityX=0;}
		}
		
		if(velocityX<0){
			velocityX+=getFriction();
			if(velocityX>0){velocityX=0;}
		}
	}
	
	private void updateFrictionY() {
		if(velocityY>0){
			velocityY-=getFriction();
			if(velocityY<0){velocityY=0;}
		}
		
		if(velocityY<0){
			velocityY+=getFriction();
			if(velocityY>0){velocityY=0;}
		}
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


	public float getVelocityX() {
		return velocityX;
	}


	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}
	
	public void addVelocityX(float addX) {
		this.velocityX+=addX;
	}
	
	public void addVelocityY(float addY) {
		this.velocityY+=addY;
	}

	public float getVelocityY() {
		return velocityY;
	}


	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public boolean isObjectResting() {
		return objectResting;
	}

	public void setObjectResting(boolean objectResting) {
		this.objectResting = objectResting;
	}

}
