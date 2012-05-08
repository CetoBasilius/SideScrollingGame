package com.gamefinal.global;

public class Console {
	
	private static final int MAX_MESSAGE_LENGHT = 24;
	private static final int MAX_CONSOLE_LINES = 5;
	private String consoleLines[] = new String[MAX_CONSOLE_LINES];
	
	private int positionX;
	private int positionY;
	
	public Console(int x,int y){
		for(int line=0;line<MAX_CONSOLE_LINES;line++){
			consoleLines[line]="";
		}
		positionX = x;
		positionY = y;
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

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

}
