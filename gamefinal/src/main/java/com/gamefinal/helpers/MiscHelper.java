package com.gamefinal.helpers;

public class MiscHelper {
	public static void wait(int milliseconds) {
		long t0,t1;
        t0=System.currentTimeMillis();
        do{
            t1=System.currentTimeMillis();
        }
        while (t1-t0<milliseconds);
	}
}
