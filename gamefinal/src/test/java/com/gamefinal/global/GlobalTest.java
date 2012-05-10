package com.gamefinal.global;

import static org.junit.Assert.*;
import org.junit.Test;
import com.gamefinal.global.Global;

public class GlobalTest {

	@Test
	public void testGlobalSingletonInstance(){
		Global global1 = Global.getGlobals();
		Global global2 = Global.getGlobals();
		
		assertEquals(global1,global2);
		
		global1.setGameResolutionX(900);
		global2.setGameResolutionX(500);
		
		//Resolution must be the same even though we set it differently
		assertEquals(global1.getGameResolutionX(),global2.getGameResolutionX());
	}
}
