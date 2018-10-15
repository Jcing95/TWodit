package de.Jcing.util;

import java.awt.image.BufferedImage;

public class Util {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage fastScaleUp(BufferedImage img, int factor) {
		BufferedImage result = new BufferedImage(img.getWidth()*factor, img.getHeight()*factor, img.getType());
		int[] rgb = result.getRGB(0, 0, result.getWidth(), result.getHeight(), null, 0, result.getWidth());
		
		for (int y = 0; y < result.getHeight(); y++) {
			for (int x = 0; x < result.getWidth(); x++) {
//				result.setRGB(x, y, factor, factor, img.getRGB(x, y, factor, factor, null, offset, scansize), offset, scansize);
				result.setRGB(x, y, img.getRGB((int)(x/factor), (int)(y/factor)));
			}
		}
		
		return result;
	}
	
}
