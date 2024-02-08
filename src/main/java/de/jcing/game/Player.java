package de.jcing.game;

import org.lwjgl.glfw.GLFW;

import de.jcing.engine.entity.Creature;
import de.jcing.engine.image.JAnimation;
import de.jcing.engine.image.texture.AtlasCallback;
import de.jcing.engine.image.texture.TextureAssembler;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.opengl.Camera;
import de.jcing.engine.opengl.mesh.Mesh;
import de.jcing.engine.opengl.mesh.MeshFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends Creature {

	private static final Logger LOG = LoggerFactory.getLogger(Player.class);
	private static final float DEFAULT_SPEED = 0.1f;
	private static final float SHIFT_MULT = 2.5f;

	private final int anim_left;
	private final int anim_right;
	private final int anim_up;
	private final int anim_down;

	public Player(TextureAssembler assembler) {
		super();
		anim_left = assembler.addFrames(new JAnimation("gfx/player/left/"));
		anim_right = assembler.addFrames(new JAnimation("gfx/player/right"));
		anim_up = assembler.addFrames(new JAnimation("gfx/player/up/"));
		anim_down = assembler.addFrames(new JAnimation("gfx/player/down/"));
		//		position.z = 1;
		setRotation(0, 0, 0);
		AtlasCallback initTex = (assemb) -> {
			this.setAnim(ANIM.WALK_LEFT, assemb.getAnimation(anim_left));
			this.setAnim(ANIM.WALK_RIGHT, assemb.getAnimation(anim_right));
			this.setAnim(ANIM.WALK_UP, assemb.getAnimation(anim_up));
			this.setAnim(ANIM.WALK_DOWN, assemb.getAnimation(anim_down));
			setAnim(ANIM.WALK_UP);
			setMesh(new Mesh(currAnim, MeshFactory.createRectData(0, 0, 0.1f, 1, 1, currAnim, false)));
			LOG.info("player set up");
		};
		assembler.addCallback(initTex);
	}

	public void tick(Camera cam) {
		if (initialized) {
			float speed = DEFAULT_SPEED;
			float speedX = 0, speedY = 0;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
				speed *= SHIFT_MULT;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_W))
				speedY += speed;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_A))
				speedX -= speed;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_S))
				speedY -= speed;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_D))
				speedX += speed;

			accelerate(speedX, speedY);

			super.tick();

			cam.getPosition().x = position.x;
			cam.getPosition().y = position.y;
//			Log.debug(cam.getPosition().toString());
		}
	}

	@Override
	public Mesh getMesh() {
		return mesh;
	}

}
