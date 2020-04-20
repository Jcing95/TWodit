package de.jcing.engine.image;

import de.jcing.utillities.log.Log;

public class ImageData {
	
	private int width, height;
	
	private int[] data;
	
	public ImageData(int width, int height) {
		this.width = width;
		this.height = height;
		
		//TODO: maybe switch width & height
		data = new int[width*height];
	}
	
	public void copy(ImageData data, int xOffset, int yOffset) {
		for(int x = xOffset; x < xOffset + data.getWidth(); x++) {
			for(int y = yOffset; y < yOffset + data.getHeight(); y++) {
				this.data[x+width*y] = data.data[(x-xOffset)+data.width*(y-yOffset)];
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[] getData() {
		return data;
	}
	
	void setWidth(int width) {
		this.width = width;
	}
	
	void setHeight(int height) {
		this.height = height;
	}
	
	void setData(int[] data) {
		this.data = data;
	}
	
	
}
