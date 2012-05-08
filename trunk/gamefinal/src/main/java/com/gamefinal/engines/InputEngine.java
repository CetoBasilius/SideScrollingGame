package com.gamefinal.engines;

import com.gamefinal.global.Global;

public class InputEngine {
	
	public enum RecorderState { OFF, RECORDING, PLAYBACK }
	
	private static final int UP_KEY_CODE = 87;
	private static final int DOWN_KEY_CODE = 83;
	private static final int LEFT_KEY_CODE = 65;
	private static final int RIGHT_KEY_CODE = 68;
	
	private static final int ACTION_KEY_CODE = 69;
	private static final int JUMP_KEY_CODE = 32;
	private static final int FIRE_KEY_CODE = 96;
	private static final int RELOAD_KEY_CODE = 82;

	InputEngineController inputController = new InputEngineController();
	InputRecorder inputRecorder = new InputRecorder();

	
	public void update(){
		//TODO remove this and record with command line or menu
		if(inputRecorder.isFull()==false){inputRecorder.recorderState = RecorderState.RECORDING;}
		else{inputRecorder.recorderState = RecorderState.PLAYBACK;}
		
		inputController = inputRecorder.update(inputController);
		
		
		/*if(inputController.holdingUpKey){Global.getGlobals().graphicsEngine.gameCamera.moveCameraVertical(16);}
		if(inputController.holdingDownKey){Global.getGlobals().graphicsEngine.gameCamera.moveCameraVertical(-16);}
		if(inputController.holdingRightKey){Global.getGlobals().graphicsEngine.gameCamera.moveCameraHorizontal(16);}
		if(inputController.holdingLeftKey){Global.getGlobals().graphicsEngine.gameCamera.moveCameraHorizontal(-16);}*/
		
		if(inputController.holdingUpKey){Global.getGlobals().graphicsEngine.gameCamera.addVelocityY(1.0f);}
		if(inputController.holdingDownKey){Global.getGlobals().graphicsEngine.gameCamera.addVelocityY(-1.0f);}
		if(inputController.holdingRightKey){Global.getGlobals().graphicsEngine.gameCamera.addVelocityX(1.0f);}
		if(inputController.holdingLeftKey){Global.getGlobals().graphicsEngine.gameCamera.addVelocityX(-1.0f);}
	}
	
	public void pressKey(int keyCode){
		switch(keyCode){
		case UP_KEY_CODE:{
			inputController.holdingUpKey=true;
			inputController.upKeyTime++;
			break;
		}
		case DOWN_KEY_CODE:{
			inputController.holdingDownKey=true;
			inputController.downKeyTime++;
			break;
		}
		case LEFT_KEY_CODE:{
			inputController.holdingLeftKey=true;
			inputController.leftKeyTime++;
			break;
		}
		case RIGHT_KEY_CODE:{
			inputController.holdingRightKey=true;
			inputController.rightKeyTime++;
			break;
		}
		case ACTION_KEY_CODE:{
			inputController.holdingActionKey=true;
			inputController.actionKeyTime++;
			break;
		}
		case JUMP_KEY_CODE:{
			inputController.holdingJumpKey=true;
			inputController.jumpKeyTime++;
			break;
		}
		case FIRE_KEY_CODE:{
			inputController.holdingFireKey=true;
			inputController.fireKeyTime++;
			break;
		}
		case RELOAD_KEY_CODE:{
			inputController.holdingReloadKey=true;
			inputController.reloadKeyTime++;
			break;
		}
		
		default:
			break;
		}
	}
	
	public void releaseKey(int keyCode){
		switch(keyCode){
		case UP_KEY_CODE:{
			inputController.holdingUpKey=false;
			inputController.upKeyTime=0;
			break;
		}
		case DOWN_KEY_CODE:{
			inputController.holdingDownKey=false;
			inputController.downKeyTime=0;
			break;
		}
		case LEFT_KEY_CODE:{
			inputController.holdingLeftKey=false;
			inputController.leftKeyTime=0;
			break;
		}
		case RIGHT_KEY_CODE:{
			inputController.holdingRightKey=false;
			inputController.rightKeyTime=0;
			break;
		}
		case ACTION_KEY_CODE:{
			inputController.holdingActionKey=false;
			inputController.actionKeyTime=0;
			break;
		}
		case JUMP_KEY_CODE:{
			inputController.holdingJumpKey=false;
			inputController.jumpKeyTime=0;
			break;
		}
		case FIRE_KEY_CODE:{
			inputController.holdingFireKey=false;
			inputController.fireKeyTime=0;
			break;
		}
		case RELOAD_KEY_CODE:{
			inputController.holdingReloadKey=false;
			inputController.reloadKeyTime=0;
			break;
		}
		
		default:
			break;
		}
	}

	//Holding key
	public boolean isHoldingUpKey() {
		return inputController.holdingUpKey;
	}

	public boolean isHoldingDownKey() {
		return inputController.holdingDownKey;
	}
	
	public boolean isHoldingLeftKey() {
		return inputController.holdingLeftKey;
	}
	
	public boolean isHoldingRightKey() {
		return inputController.holdingRightKey;
	}

	public boolean isHoldingActionKey() {
		return inputController.holdingActionKey;
	}

	public boolean isHoldingJumpKey() {
		return inputController.holdingJumpKey;
	}

	public boolean isHoldingFireKey() {
		return inputController.holdingFireKey;
	}

	public boolean isHoldingReloadKey() {
		return inputController.holdingReloadKey;
	}
	
	//Pressed key
	public boolean pressedUpKey() {
		if(inputController.upKeyTime==1){
			return true;
		}
		return false;
	}

	public boolean pressedDownKey() {
		if(inputController.downKeyTime==1){
			return true;
		}
		return false;
	}
	
	public boolean pressedLeftKey() {
		if(inputController.leftKeyTime==1){
			return true;
		}
		return false;
	}
	
	public boolean pressedRightKey() {
		if(inputController.rightKeyTime==1){
			return true;
		}
		return false;
	}

	public boolean pressedActionKey() {
		if(inputController.actionKeyTime==1){
			return true;
		}
		return false;
	}

	public boolean pressedJumpKey() {
		if(inputController.jumpKeyTime==1){
			return true;
		}
		return false;
	}

	public boolean pressedFireKey() {
		if(inputController.fireKeyTime==1){
			return true;
		}
		return false;
	}

	public boolean pressedReloadKey() {
		if(inputController.reloadKeyTime==1){
			return true;
		}
		return false;
		
	}
	
	
	public class InputRecorder{
		private boolean recorderIsFull = false;
		private int currentPlayBackPosition = 0;
		private static final int MAX_RECORDED_COMMANDS = 400;
		private InputEngineController inputRecord[] = new InputEngineController[MAX_RECORDED_COMMANDS];
		public RecorderState recorderState = RecorderState.OFF;
		
		
		public InputRecorder(){
			for(int line=0;line<MAX_RECORDED_COMMANDS;line++){
				inputRecord[line] = new InputEngineController();
			}
		}

		public InputEngineController update(InputEngineController inputControllerIn) {

			if(recorderState==RecorderState.RECORDING){
				this.addMessage(inputControllerIn);
			}
			else
			{
				if(recorderState==RecorderState.PLAYBACK){
					return this.replay();
				}
			}

			return inputControllerIn;
		}

		private InputEngineController replay() {
			if(currentPlayBackPosition==MAX_RECORDED_COMMANDS){
				//TODO reload everything.
			}
			currentPlayBackPosition--;
			if(currentPlayBackPosition<=0){
				recorderIsFull=false;
				
			}
			return inputRecord[currentPlayBackPosition];
		}

		public boolean isFull() {
			return recorderIsFull;
		}

		private void addMessage(InputEngineController message){
			
			for(int a = MAX_RECORDED_COMMANDS-1;a>0;a--){
				inputRecord[a]=inputRecord[a-1];
			}
			inputRecord[0]=message.clone();
			currentPlayBackPosition++;
			if(currentPlayBackPosition>=MAX_RECORDED_COMMANDS){
				recorderIsFull=true;
				currentPlayBackPosition=MAX_RECORDED_COMMANDS;
			}
		}

		public int getCurrentSlot() {
			return currentPlayBackPosition;
		}
	}
	
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

}
