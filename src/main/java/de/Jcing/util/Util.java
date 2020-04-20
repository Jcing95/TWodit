package de.jcing.util;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Util {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int seededRandom(long seed) {
		Random r = new Random(seed);
		return r.nextInt();
	}

	public static int fastABS(int value) {
		return value < 0 ? -value : value;
	}

	public static double fastABS(double value) {
		return value < 0 ? -value : value;
	}

	public static float fastABS(float value) {
		return value < 0 ? -value : value;
	}
}
