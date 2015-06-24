package com.gamefinal.global;

import java.awt.event.KeyEvent;

public class Console {
	
	private static final int MAX_MESSAGE_LENGHT = 24;
	private static final int MAX_CONSOLE_LINES = 5;
	private static final int LOG_LINE_SPACING = 14;
	private static final String DEFAULT_INPUTLINE_PREFIX = "Console: ";
	private String inputLinePrefix = DEFAULT_INPUTLINE_PREFIX;
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
	
	public void update(KeyEvent keyEvent){

		if(isConsoleActive()){
			if(keyEvent.getKeyChar() == '\b') {
				if(consoleInputLine.length()>0) {
					consoleInputLine = deleteLastChar(consoleInputLine);
				}
			}
			else
			{
				if(consoleInputLine.length()<MAX_MESSAGE_LENGHT) {
					if(keyEvent.getKeyChar() != '\n') {
						consoleInputLine+=keyEvent.getKeyChar();
					}
				}
			}
		}
	}

	private String deleteLastChar(String inString) {
		return inString.substring(0, inString.length()-1);
	}

	public void addMessage(String message){
		if(message.length()>0){
			if(!message.equals("")) {
				for(int a = MAX_CONSOLE_LINES-1;a>0;a--){
					consoleLines[a]=consoleLines[a-1];
				}
				consoleLines[0]=message;
			}
		}
	}
	
	private void transferMessage() {
		//The following if checks if the message is a command.
		if(Global.getGlobals().processConsoleMessage(consoleInputLine)){
			addMessage(consoleInputLine);
		}
		consoleInputLine = "";
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

	public void setConsoleActive(boolean consoleSetActive) {
		this.consoleActive = consoleSetActive;
		if(consoleSetActive==false) {
			
			transferMessage();
		}
	}
	
	public void activateConsole() {
		this.consoleActive = true;
	}
	
	public void dectivateConsole() {
		transferMessage();
		this.consoleActive = false;
	}

	public String getConsoleInputLine() {
		return consoleInputLine;
	}

	public void setConsoleInputLine(String consoleInputLineSet) {
		this.consoleInputLine = consoleInputLineSet;
	}

	public void toggleConsole() {
		if(consoleActive==false) {
			consoleActive=true;
		}
		else
		{
			dectivateConsole();
		}
	}

	public static int getMaxConsoleLines() {
		return MAX_CONSOLE_LINES;
	}

	public int getLogLinePositionY(int line) {
		return messageLogPositionY-(LOG_LINE_SPACING*line);
	}

	public String getInputLinePrefix() {
		return inputLinePrefix;
	}

	public void setInputLinePrefix(String inputLinePrefix) {
		this.inputLinePrefix = inputLinePrefix;
	}

	public void reMessageAgain() {
		this.consoleInputLine = this.consoleLines[0];
	}

}
