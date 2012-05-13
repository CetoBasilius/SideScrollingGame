package com.gamefinal.engines;

import java.awt.event.KeyEvent;

import com.gamefinal.global.Global;

public class InputEngine {
	
	public enum RecorderState { OFF, RECORDING, PLAYBACK }
	
	private static final int UP_KEY_CODE = KeyEvent.VK_W;
	private static final int DOWN_KEY_CODE = KeyEvent.VK_S;
	private static final int LEFT_KEY_CODE = KeyEvent.VK_A;
	private static final int RIGHT_KEY_CODE = KeyEvent.VK_D;
	
	private static final int ACTION_KEY_CODE = KeyEvent.VK_E;
	private static final int JUMP_KEY_CODE = KeyEvent.VK_SPACE;
	private static final int FIRE_KEY_CODE = KeyEvent.VK_K;
	private static final int RELOAD_KEY_CODE = KeyEvent.VK_R;
	
	private static final int CHAT_KEY_CODE = KeyEvent.VK_ENTER;

	private InputEngineController inputController = new InputEngineController();
	public InputRecorder inputRecorder = new InputRecorder();

	
	public void update(){
		inputController = inputRecorder.update(inputController);
		if(!Global.getGlobals().gameConsole.isConsoleActive()) {

			/*if(inputController.holdingUpKey){Global.getGlobals().graphicsEngine.gameCamera.moveCameraVertical(16);}
			if(inputController.holdingDownKey){Global.getGlobals().graphicsEngine.gameCamera.moveCameraVertical(-16);}
			if(inputController.holdingRightKey){Global.getGlobals().graphicsEngine.gameCamera.moveCameraHorizontal(16);}
			if(inputController.holdingLeftKey){Global.getGlobals().graphicsEngine.gameCamera.moveCameraHorizontal(-16);}*/

			//-------------------------------THIS IS ENTIRELY EXPERIMENTAL--------------------------------------------------
			if(pressedActionKey()) {
				Global.getGlobals().worldMap.mapTiles[2][152][69].setObjectResting(false);
				Global.getGlobals().graphicsEngine.gameCamera.setCameraToFollow(Global.getGlobals().worldMap.mapTiles[2][152][69]);
			}
			
			if(isHoldingUpKey() && isHoldingJumpKey()){Global.getGlobals().worldMap.mapTiles[2][152][69].addVelocityY(1.0f);}
			if(isHoldingDownKey() && isHoldingJumpKey()){Global.getGlobals().worldMap.mapTiles[2][152][69].addVelocityY(-1.0f);}
			if(isHoldingRightKey() && isHoldingJumpKey()){Global.getGlobals().worldMap.mapTiles[2][152][69].addVelocityX(1.0f);}
			if(isHoldingLeftKey() && isHoldingJumpKey()){Global.getGlobals().worldMap.mapTiles[2][152][69].addVelocityX(-1.0f);}	
			if(pressedReloadKey()) {Global.getGlobals().graphicsEngine.gameCamera.freeCameraFollow();}
			
			//--------------------------------------------------------------------------------------------------------------

			if(isHoldingUpKey()){Global.getGlobals().graphicsEngine.gameCamera.addVelocityY(1.0f);}
			if(isHoldingDownKey()){Global.getGlobals().graphicsEngine.gameCamera.addVelocityY(-1.0f);}
			if(isHoldingRightKey()){Global.getGlobals().graphicsEngine.gameCamera.addVelocityX(1.0f);}
			if(isHoldingLeftKey()){Global.getGlobals().graphicsEngine.gameCamera.addVelocityX(-1.0f);}
		}

		if(pressedChatKey()){Global.getGlobals().gameConsole.toggleConsole();}

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
		case CHAT_KEY_CODE:{
			inputController.holdingChatKey=true;
			inputController.chatKeyTime++;
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
		case CHAT_KEY_CODE:{
			inputController.holdingChatKey=false;
			inputController.chatKeyTime=0;
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
	
	public boolean isHoldingChatKey() {
		return inputController.holdingChatKey;
	}
	
	//Pressed key
	public boolean pressedUpKey() {
		if(inputController.upKeyTime==1){
			inputController.upKeyTime++;
			return true;
		}
		return false;
	}

	public boolean pressedDownKey() {
		if(inputController.downKeyTime==1){
			inputController.downKeyTime++;
			return true;
		}
		return false;
	}
	
	public boolean pressedLeftKey() {
		if(inputController.leftKeyTime==1){
			inputController.leftKeyTime++;
			return true;
		}
		return false;
	}
	
	public boolean pressedRightKey() {
		if(inputController.rightKeyTime==1){
			inputController.rightKeyTime++;
			return true;
		}
		return false;
	}

	public boolean pressedActionKey() {
		if(inputController.actionKeyTime==1){
			inputController.actionKeyTime++;
			return true;
		}
		return false;
	}

	public boolean pressedJumpKey() {
		if(inputController.jumpKeyTime==1){
			inputController.jumpKeyTime++;
			return true;
		}
		return false;
	}

	public boolean pressedFireKey() {
		if(inputController.fireKeyTime==1){
			inputController.fireKeyTime++;
			return true;
		}
		return false;
	}

	public boolean pressedReloadKey() {
		if(inputController.reloadKeyTime==1){
			inputController.reloadKeyTime++;
			return true;
		}
		return false;	
	}
	
	public boolean pressedChatKey() {
		if(inputController.chatKeyTime==1){
			inputController.chatKeyTime++;
			return true;
		}
		return false;
	}
	
	public class InputRecorder{
		private boolean recorderIsFull = false;
		private int currentPlayBackPosition = 0;
		private int savedPlayBackPosition = 0;
		private static final int RECORDER_BUFFER_SIZE = 5000;
		private InputEngineController inputRecord[] = new InputEngineController[RECORDER_BUFFER_SIZE];
		public RecorderState recorderState = RecorderState.OFF;
		
		
		public InputRecorder(){
			for(int line=0;line<RECORDER_BUFFER_SIZE;line++){
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
			if(currentPlayBackPosition==RECORDER_BUFFER_SIZE){
				//TODO here we can load but i think i will remove this in the future
			}
			currentPlayBackPosition--;
			if(currentPlayBackPosition<1){
				recorderIsFull=false;
				recorderState = RecorderState.OFF;
			}
			return inputRecord[currentPlayBackPosition];
		}

		public boolean isFull() {
			return recorderIsFull;
		}

		private void addMessage(InputEngineController message){
			
			for(int a = RECORDER_BUFFER_SIZE-1;a>0;a--){
				inputRecord[a]=inputRecord[a-1];
			}
			inputRecord[0]=message.clone();
			if(currentPlayBackPosition==0) {
				Global.getGlobals().graphicsEngine.saveCameraState();
			}
			currentPlayBackPosition++;
			savedPlayBackPosition = currentPlayBackPosition;
			
			if(currentPlayBackPosition>=RECORDER_BUFFER_SIZE-1){
				recorderIsFull=true;
				recorderState = RecorderState.OFF;
				currentPlayBackPosition=RECORDER_BUFFER_SIZE-1;
			}
		}

		public int getCurrentSlot() {
			return currentPlayBackPosition;
		}
		
		public void startRecording() {
			recorderState = RecorderState.RECORDING;
		}
		
		public void stopRecording() {
			recorderState = RecorderState.OFF;
		}
		
		public void startPlayBack() {
			if(savedPlayBackPosition>0) {
				currentPlayBackPosition = savedPlayBackPosition;
				Global.getGlobals().graphicsEngine.reloadCameraState();
				recorderState = RecorderState.PLAYBACK;
			}
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
		
		public boolean holdingChatKey;
		public int chatKeyTime;
		
		
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
