package com.gamefinal.global;

public class Console {
	
	private static final int MAX_CONSOLE_LINES = 5;
	private String consoleLines[] = new String[MAX_CONSOLE_LINES];
	
	public Console(){
		for(int line=0;line<MAX_CONSOLE_LINES;line++){
			consoleLines[line]="";
		}
	}

	public void addMessage(String message){
		if(message.length()>0){
			for(int a = MAX_CONSOLE_LINES-1;a>0;a--){
				consoleLines[a]=consoleLines[a-1];
			}
			consoleLines[0]=message;
		}
	}

	public String getLine(int line) {
		return consoleLines[line];
	}

}
