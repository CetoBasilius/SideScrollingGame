package com.gamefinal.engines;

import static org.junit.Assert.*;
import org.junit.Test;

import com.gamefinal.app.GameObject;

public class CollisionEngineTest {

	@Test
	public void testObjectsCollide(){
		CollisionEngine collisionEngine = new CollisionEngine();
		
		GameObject object1 = new GameObject();
		GameObject object2 = new GameObject();
		
		object1.setSize(100,100);
		object2.setSize(100,100);
		
		object1.setWorldPosition(100, 100);
		object2.setWorldPosition(500, 500);

		//Objects are too far away
		assertFalse(collisionEngine.objectsCollide(object1, object2));
		
		object1.setWorldPosition(100, 100);
		object2.setWorldPosition(100, 500);
		
		//Objects are still too far away
		assertFalse(collisionEngine.objectsCollide(object1, object2));
		
		object1.setWorldPosition(100, 100);
		object2.setWorldPosition(120, 120);
		
		//Objects are colliding
		assertTrue(collisionEngine.objectsCollide(object1, object2));
		
		object1.setWorldPosition(-500, -500);
		object2.setWorldPosition(-450, -500);
		
		//Objects are colliding
		assertTrue(collisionEngine.objectsCollide(object1, object2));
		
		object1.setWorldPosition(-500, -500);
		object2.setWorldPosition(-1450, -1500);
		
		//Objects are too far away to collide
		assertFalse(collisionEngine.objectsCollide(object1, object2));

	}
	
	@Test
	public void testDistanceBetweenObjects(){
		CollisionEngine collisionEngine = new CollisionEngine();

		GameObject object1 = new GameObject();
		GameObject object2 = new GameObject();

		object1.setSize(100,100);
		object2.setSize(100,100);

		object1.setWorldPosition(100,100);
		object2.setWorldPosition(500,500);
		
		int delta = 1;
		
		assertEquals(300,collisionEngine.distanceXBetweenObjects(object1, object2),delta);
		assertEquals(300,collisionEngine.distanceYBetweenObjects(object1, object2),delta);
		assertEquals(424,collisionEngine.distanceBetweenObjects(object1, object2),delta);
		
		
	}
}
