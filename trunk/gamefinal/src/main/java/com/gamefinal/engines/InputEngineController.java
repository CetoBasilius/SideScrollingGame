package com.gamefinal.engines;

public class InputEngineController implements Cloneable{
	public boolean holdingUpKey;
	public boolean holdingDownKey;
	public boolean holdingLeftKey;
	public boolean holdingRightKey;
	public int upKeyTime;
	public int downKeyTime;
	public int leftKeyTime;
	public int rightKeyTime;
	public boolean holdingActionKey;
	public boolean holdingJumpKey;
	public boolean holdingFireKey;
	public boolean holdingReloadKey;
	public int actionKeyTime;
	public int jumpKeyTime;
	public int fireKeyTime;
	public int reloadKeyTime;
	
	
	@Override
	public InputEngineController clone()
	{
		InputEngineController returnedClone=null;
		try {
			returnedClone = (InputEngineController) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return returnedClone;	
	} 

	public InputEngineController() {
		this.holdingUpKey = false;
		this.holdingDownKey = false;
		this.holdingLeftKey = false;
		this.holdingRightKey = false;
		this.upKeyTime = 0;
		this.downKeyTime = 0;
		this.leftKeyTime = 0;
		this.rightKeyTime = 0;
		this.holdingActionKey = false;
		this.holdingJumpKey = false;
		this.holdingFireKey = false;
		this.holdingReloadKey = false;
		this.actionKeyTime = 0;
		this.jumpKeyTime = 0;
		this.fireKeyTime = 0;
		this.reloadKeyTime = 0;
	}
}