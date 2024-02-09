package de.jcing.engine.opengl.shaders;

public class EntityShader extends Shader {

	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String WORLD_MATRIX = "worldMatrix";
	public static final String TEXTURE_SAMPLER = "texture_sampler";

	public static final String TEXURE_OFFSET = "texOffset";
	public static final String ALPHA = "alpha";

	public EntityShader() {
		super("shaders/entity.vert", "shaders/entity.frag");
		try {
			link();
		} catch (Exception e) {
			e.printStackTrace();
		}
		createUniform(PROJECTION_MATRIX);
		createUniform(WORLD_MATRIX);
		createUniform(TEXTURE_SAMPLER);
		createUniform(TEXURE_OFFSET);
		createUniform(ALPHA);
	}

}
