package de.jcing.engine.image.texture;

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

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import de.jcing.engine.image.ImageData;

public class Texture {

	protected final int id;
	protected final int width, height;
	public static final int BYTES_PER_PIXEL = 4;

	public Texture(String fileName) throws Exception {
		this(loadTexture(fileName));
	}

	public Texture(ImageData image) {
		this(loadTexture(image));
	}

	protected Texture(Texture texture) {
		this(texture.id, texture.width, texture.height);
	}

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public int getId() {
		return id;
	}

	public Vector2f getOffset() {
		return new Vector2f(0f, 0f);
	}

	public void cleanup() {
		glDeleteTextures(id);
	}

	// PROTECTED FACTORY FUNCTIONS

	protected static Texture loadTexture(String fileName) throws Exception {
		int id = glGenTextures();
		return loadTexture(id, fileName);
	}

	protected static Texture loadTexture(int textureId, String fileName) throws Exception {
		ByteBuffer buf;
		int width, height;
		// Load Texture file
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

			URL url = Texture.class.getResource(fileName);
			File file = Paths.get(url.toURI()).toFile();
			String filePath = file.getAbsolutePath();
			buf = STBImage.stbi_load(filePath, w, h, channels, 4);
			if (buf == null) {
				throw new Exception("Image file [" + filePath + "] not loaded: " + STBImage.stbi_failure_reason());
			}

			width = w.get();
			height = h.get();

			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, textureId);

			// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			// Upload the texture data
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
			// Generate Mip Map
			glGenerateMipmap(GL_TEXTURE_2D);

			STBImage.stbi_image_free(buf);
			return new Texture(textureId, width, height);
		}
	}

	protected static Texture loadTexture(ImageData image) {
		// Create a new OpenGL texture
		int textureId = glGenTextures();
		return loadTexture(textureId, image);
	}

	protected static Texture loadTexture(int textureId, ImageData data) {
		int[] pixels = data.getData();
//		data.getRGB(0, 0, data.getWidth(), data.getHeight(), pixels, 0, data.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(data.getWidth() * data.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

		for (int y = 0; y < data.getHeight(); y++) {
			for (int x = 0; x < data.getWidth(); x++) {
				int pixel = pixels[y * data.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
				buffer.put((byte) (pixel & 0xFF)); // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
			}
		}

		buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, textureId);

		// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Upload the texture data
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		// Generate Mip Map
		glGenerateMipmap(GL_TEXTURE_2D);

		return new Texture(textureId, data.getWidth(), data.getHeight());
	}

}
