package com.gamefinal.app;

import java.awt.Image;

public class Player extends GameObject{

	Image headImage;
	Image extendedArmImage;
	Image contractedArmImage;
	Image torsoImage;
	Image legsIdleImage;
	Image legsIdleCrouchingImage;
	Image legsJumpingImage;
	Image legsFallingImage;
	
	Image legsWalking[];
	Image legsWalkingCrouched[];
	
	public Player() {
		super();
	}
}
