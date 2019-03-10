package de.jcing.engine.gl;

public class TestShader extends Shader{

	public TestShader() {
		super("shaders/test/vertex", "shaders/test/fragment");
		try {
			link();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}

}
