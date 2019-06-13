package de.jcing.engine.world.generation;

import opensimplexnoise.OpenSimplexNoise;

public class Generator {
	
	private static OpenSimplexNoise n = new OpenSimplexNoise(1337l);
	
	private static final int spread = 10;
	
	public static int getValue(int x, int y) {
		return Math.abs((int)(n.eval(x/10.0, y/10.0)*spread));
		
	}
	
	
	
}
