package de.jcing.engine.opengl.shaders;

public class TerrainShader extends Shader {

	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String WORLD_MATRIX = "worldMatrix";
	public static final String TEXTURE_SAMPLER = "texture_sampler";

	public TerrainShader() {
		super("shaders/terrain/vertex", "shaders/terrain/fragment");
		try {
			link();
		} catch (Exception e) {
			e.printStackTrace();
		}
		createUniform(PROJECTION_MATRIX);
		createUniform(WORLD_MATRIX);
		createUniform(TEXTURE_SAMPLER);
	}

}
