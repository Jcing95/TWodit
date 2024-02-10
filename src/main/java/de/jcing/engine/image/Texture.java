package de.jcing.engine.image;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class Texture {

	protected final int id;
	public static final int BYTES_PER_PIXEL = 4;

	private ImageData imageData;

	public Texture(ImageData imageData) throws IOException {
		this.imageData = imageData;
		id = glGenTextures();
		loadImage();
	}

	private void loadImage() {
		ByteBuffer buffer = BufferUtils
				.createByteBuffer(imageData.getWidth() * imageData.getHeight() * BYTES_PER_PIXEL); // 4 for RGBA, 3 for
																									// RGB
		int[] pixels = imageData.getData();
		for (int y = 0; y < imageData.getHeight(); y++) {
			for (int x = 0; x < imageData.getWidth(); x++) {
				int pixel = pixels[y * imageData.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
				buffer.put((byte) (pixel & 0xFF)); // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
			}
		}

		buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS

		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, id);

		// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Upload the texture data
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, imageData.getWidth(), imageData.getHeight(), 0, GL_RGBA,
				GL_UNSIGNED_BYTE, buffer);
		// Generate Mip Map
		glGenerateMipmap(GL_TEXTURE_2D);
	}

	public float getWidth() {
		return 1;
	}

	public float getHeight() {
		return 1;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void cleanup() {
		glDeleteTextures(id);
	}

	public int getId() {
		return id;
	}

}
