package com.gamefinal.global;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConsoleTest {

	@Test
	public void testConsoleMessageAdd(){
		Console console = new Console(0,0,0,0);
		
		//Console messages must be empty
		assertEquals("",console.getLine(0));
		assertEquals("",console.getLine(1));
		assertEquals("",console.getLine(2));
		assertEquals("",console.getLine(3));
		assertEquals("",console.getLine(4));
		
		//Messages should be added and moved like a queue
		console.addMessage("First Message");
		
		assertEquals("First Message",console.getLine(0));
		assertEquals("",console.getLine(1));
		assertEquals("",console.getLine(2));
		assertEquals("",console.getLine(3));
		assertEquals("",console.getLine(4));
		
		console.addMessage("Second Message");
		
		assertEquals("Second Message",console.getLine(0));
		assertEquals("First Message",console.getLine(1));
		assertEquals("",console.getLine(2));
		assertEquals("",console.getLine(3));
		assertEquals("",console.getLine(4));
		
		console.addMessage("Third Message");
		
		assertEquals("Third Message",console.getLine(0));
		assertEquals("Second Message",console.getLine(1));
		assertEquals("First Message",console.getLine(2));
		assertEquals("",console.getLine(3));
		assertEquals("",console.getLine(4));
		
		console.addMessage("Fourth Message");
		
		assertEquals("Fourth Message",console.getLine(0));
		assertEquals("Third Message",console.getLine(1));
		assertEquals("Second Message",console.getLine(2));
		assertEquals("First Message",console.getLine(3));
		assertEquals("",console.getLine(4));
		
		console.addMessage("Fifth Message");
		
		assertEquals("Fifth Message",console.getLine(0));
		assertEquals("Fourth Message",console.getLine(1));
		assertEquals("Third Message",console.getLine(2));
		assertEquals("Second Message",console.getLine(3));
		assertEquals("First Message",console.getLine(4));
		
		//Adding one last message must erase the first message
		console.addMessage("Last Message");
		
		assertEquals("Last Message",console.getLine(0));
		assertEquals("Fifth Message",console.getLine(1));
		assertEquals("Fourth Message",console.getLine(2));
		assertEquals("Third Message",console.getLine(3));
		assertEquals("Second Message",console.getLine(4));

	}
	
	@Test
	public void testConsoleToggle() {
		Console console = new Console(0,0,0,0);
		
		//Console must be off from the beginning
		assertFalse(console.isConsoleActive());
		
		//if we toggle the console it must turn on
		console.toggleConsole();
		assertTrue(console.isConsoleActive());
		
		//if we toggle the console it must turn off
		console.toggleConsole();
		assertFalse(console.isConsoleActive());
		
	}
}
