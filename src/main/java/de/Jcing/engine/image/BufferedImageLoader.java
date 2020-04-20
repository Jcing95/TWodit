package de.jcing.engine.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.jcing.engine.image.exceptions.ImageLoadingException;
import de.jcing.utillities.log.Log;

public class BufferedImageLoader extends ImageLoader {

	public ImageData load(String path, boolean flip) {
		try {
			BufferedImage img = ImageIO.read(new File(path));
			if (flip)
				flip(img);
			ImageData data = new ImageData(img.getWidth(), img.getHeight());
			img.getRGB(0, 0, img.getWidth(), img.getHeight(), data.getData(), 0, img.getWidth());
			return data;
		} catch (IOException e) {
			Log.error("could not load: " + path);
			throw new ImageLoadingException("could not load: " + path);
		}
	}

	protected static void flip(BufferedImage img) {
		int[] lineBuffer = new int[img.getWidth()];
		int[] lineBuffer2 = new int[img.getWidth()];
		for (int y = 0; y < img.getHeight() / 2; y++) {
			img.getRGB(0, y, img.getWidth(), 1, lineBuffer, 0, 1);
			img.getRGB(0, img.getHeight() - y - 1, img.getWidth(), 1, lineBuffer2, 0, 1);
			img.setRGB(0, y, img.getWidth(), 1, lineBuffer2, 0, 1);
			img.setRGB(0, img.getHeight() - y - 1, img.getWidth(), 1, lineBuffer, 0, 1);
		}
	}

}
