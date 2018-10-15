package de.Jcing.engine.graphics;

import java.util.ArrayList;

import de.Jcing.image.Image;

public class TileSet {
	
	//TODO: Tileset features
	// save tileset to file
	// give usefull randomnes properties to make easy lvls.
	// easy creator integration
	
	public ArrayList<Image> images;
	
	public TileSet() {
		images = new ArrayList<>();
	}
	
	public void addImage(Image image) {
		images.add(image);
	}
	
	
	
	
	
}
