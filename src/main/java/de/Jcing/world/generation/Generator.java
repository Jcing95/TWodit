<<<<<<< Updated upstream:src/main/java/de/Jcing/engine/world/generation/Generator.java
package de.jcing.engine.world.generation;
=======
package de.jcing.world.generation;
>>>>>>> Stashed changes:src/main/java/de/Jcing/world/generation/Generator.java

import opensimplexnoise.OpenSimplexNoise;

public class Generator {
	
	private static OpenSimplexNoise n = new OpenSimplexNoise(1337l);
	
	private static final int spread = 10;
	
	public static int getValue(int x, int y) {
		return Math.abs((int)(n.eval(x/10.0, y/10.0)*spread));
		
	}
	
	
	
}
