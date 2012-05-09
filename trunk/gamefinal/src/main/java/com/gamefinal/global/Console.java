package com.gamefinal.global;

public class Console {
	
	//private static final int MAX_MESSAGE_LENGHT = 24;
	private static final int MAX_CONSOLE_LINES = 5;
	private String consoleLines[] = new String[MAX_CONSOLE_LINES];
	private String consoleInputLine = "";
	
	private int messageLogPositionX;
	private int messageLogPositionY;
	
	private int inputPositionX;
	private int inputPositionY;
	
	private boolean consoleActive = false;
	
	public Console(int logPositionX,int logPositionY,int inputX, int inputY){
		for(int line=0;line<MAX_CONSOLE_LINES;line++){
			consoleLines[line]="";
		}
		messageLogPositionX = logPositionX;
		messageLogPositionY = logPositionY;
		inputPositionX = inputX;
		inputPositionY = inputY;
	}
	
	public void update(char inputChar){
		if(isConsoleActive()){
			consoleInputLine+=inputChar;
		}
	}

	public void addMessage(String message){
		if(message.length()>0){
			
			/*if(message.length()>MAX_MESSAGE_LENGHT){
				message = message.substring(0, MAX_MESSAGE_LENGHT);
			}*/
			
			for(int a = MAX_CONSOLE_LINES-1;a>0;a--){
				consoleLines[a]=consoleLines[a-1];
			}
			consoleLines[0]=message;
		}
	}

	public String getLine(int line) {
		return consoleLines[line];
	}

	public int getLogPositionX() {
		return messageLogPositionX;
	}

	public void setLogPositionX(int positionX) {
		this.messageLogPositionX = positionX;
	}

	public int getLogPositionY() {
		return messageLogPositionY;
	}

	public void setLogPositionY(int positionY) {
		this.messageLogPositionY = positionY;
	}

	public int getInputPositionY() {
		return inputPositionY;
	}

	public void setInputPositionY(int inputPositionY) {
		this.inputPositionY = inputPositionY;
	}

	public int getInputPositionX() {
		return inputPositionX;
	}

	public void setInputPositionX(int inputPositionX) {
		this.inputPositionX = inputPositionX;
	}

	public boolean isConsoleActive() {
		return consoleActive;
	}

	public void setConsoleActive(boolean consoleActive) {
		this.consoleActive = consoleActive;
	}

	public String getConsoleInputLine() {
		return consoleInputLine;
	}

	public void setConsoleInputLine(String consoleInputLine) {
		this.consoleInputLine = consoleInputLine;
	}

}
