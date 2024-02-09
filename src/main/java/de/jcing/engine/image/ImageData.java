package de.jcing.engine.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageData {

	public static final int BYTES_PER_PIXEL = 4;

	private final int[] data;
	private final int width;
	private final int height;

	public ImageData(int[] data, int width, int height) {
		this.data = data;
		this.width = width;
		this.height = height;
	}

	public ImageData(int width, int height) {
		this.width = width;
		this.height = height;
		this.data = new int[width * height * BYTES_PER_PIXEL];
	}

	public static ImageData from(String filePath) throws IOException {
		log.debug("loading image: {}", filePath);
		@Cleanup
		InputStream inputStream = ImageData.class.getResourceAsStream(filePath);
		BufferedImage img = ImageIO.read(inputStream);
		int width = img.getWidth();
		int height = img.getHeight();
		int[] data = img.getRGB(0, 0, width, height, null, 0, width);
		return new ImageData(data, width, height).flip();
	}

	public void writeTo(ImageData data, int xOffset, int yOffset) {
		for (int x = 0; x < data.getWidth(); x++) {
			for (int y = 0; y < data.getHeight(); y++) {
				this.data[xOffset + x + width * (y + yOffset)] = data.data[x + data.width * y];
			}
		}
	}

	public ImageData flip() {
		for (int y = 0; y < height / 2; y++) {
            int topRowIndex = y * width;
            int bottomRowIndex = (height - y - 1) * width;
            for (int x = 0; x < width; x++) {
                // Swap the top and bottom pixels
                int temp = data[topRowIndex + x];
                data[topRowIndex + x] = data[bottomRowIndex + x];
                data[bottomRowIndex + x] = temp;
            }
        }
		return this;
	}

	public ImageData[] split(int rows, int cols, int total) {
		int newWidth = width / rows;
		int newHeight = height / rows;
		int[][] data = new int[total][newWidth * newHeight];
		for (int i = 0; i < newWidth * newHeight * total; i++) {
			int currentX = i % width;
			int currentSubX = currentX / cols;
			int currentY = i / width;
			int currentSubY = currentY / rows;
			int subX = currentX - currentSubX * newWidth;
			int subY = currentY - currentSubY * newHeight;
			data[currentSubY * cols + currentSubX][subY * newWidth + subX] = this.data[i];
		}
		ImageData[] datas = new ImageData[total];
		for (int i = 0; i < total; i++) {
			datas[i] = new ImageData(data[i], newWidth, newHeight);
		}
		return datas;
	}

	public int[] getData() {
		return data;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
