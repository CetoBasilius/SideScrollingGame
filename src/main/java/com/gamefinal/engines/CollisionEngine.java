package com.gamefinal.engines;

import com.gamefinal.app.GameObject;

public class CollisionEngine {
	
	private float lastCollisionDepthX;
	private float lastCollisionDepthY;

	public CollisionEngine(){
		
	}
	
	public float distanceYBetweenObjects(GameObject object1,GameObject object2){
		float absoluteDifferenceY = Math.abs((float)object1.getWorldPositionY()-(float)object2.getWorldPositionY());
		float objectHalfSizeY1=(float)object1.getSizeY()/2;
		float objectHalfSizeY2=(float)object2.getSizeY()/2;

		return absoluteDifferenceY-objectHalfSizeY1-objectHalfSizeY2;
	}
	
	public float distanceXBetweenObjects(GameObject object1,GameObject object2){
		float absoluteDifferenceX = Math.abs((float)object1.getWorldPositionX()-(float)object2.getWorldPositionX());
		float objectHalfSizeX1=(float)object1.getSizeX()/2;
		float objectHalfSizeX2=(float)object2.getSizeX()/2;

		return (absoluteDifferenceX-objectHalfSizeX1-objectHalfSizeX2);
	}
	
	public float distanceBetweenObjects(GameObject object1,GameObject object2){

		return (float)Math.sqrt(Math.pow(distanceXBetweenObjects(object1,object2),2)+Math.pow(distanceYBetweenObjects(object1,object2),2));
	}
	
	public boolean objectsCollideX(GameObject object1,GameObject object2){
		float differenceX = (float)object1.getWorldPositionX()-(float)object2.getWorldPositionX();
			
		float objectHalfSizeX1=(float)object1.getSizeX()/2;
		float objectHalfSizeX2=(float)object2.getSizeX()/2;
		
		boolean collisionOcurred = false;
		//Means object 1 is <
		if(differenceX<0)
		{
			float collisionCheckX = differenceX+objectHalfSizeX1+objectHalfSizeX2;
			if(collisionCheckX<0){
				collisionOcurred=false;
			}else{
				collisionOcurred=true;
				lastCollisionDepthX=collisionCheckX;
			}
		}
		//Means object 1 is >
		else
		{
			float collisionCheckX = differenceX-objectHalfSizeX1-objectHalfSizeX2;
			if(collisionCheckX>0){
				collisionOcurred=false;
			}else{
				collisionOcurred=true;
			}
		}
		
		return collisionOcurred;
	}
	
	public boolean objectsCollideY(GameObject object1,GameObject object2){
		float differenceY = (float)object1.getWorldPositionY()-(float)object2.getWorldPositionY();
			
		float objectHalfSizeY1=(float)object1.getSizeY()/2;
		float objectHalfSizeY2=(float)object2.getSizeY()/2;
		
		boolean collisionOcurred = false;
		//Means object 1 is <
		if(differenceY<0)
		{
			float collisionCheckX = differenceY+objectHalfSizeY1+objectHalfSizeY2;
			if(collisionCheckX<0){
				collisionOcurred=false;
			}else{
				collisionOcurred=true;
				lastCollisionDepthY=collisionCheckX;
			}
		}
		//Means object 1 is >
		else
		{
			float collisionCheckX = differenceY-objectHalfSizeY1-objectHalfSizeY2;
			if(collisionCheckX>0){
				collisionOcurred=false;
			}else{
				collisionOcurred=true;
			}
		}
		
		return collisionOcurred;
	}
	
	public boolean objectsCollide(GameObject object1,GameObject object2){
		boolean collisionOcurred = false;
		
		if(objectsCollideX(object1,object2) && objectsCollideY(object1,object2)){
			collisionOcurred=true;
		}else{
			collisionOcurred=false;
		}
		
		return collisionOcurred;
	}
	
	public float[] getLastCollisionDepth(){
		float[] collisionDepth = {lastCollisionDepthX,lastCollisionDepthY};
		return collisionDepth;
	}
	
}
